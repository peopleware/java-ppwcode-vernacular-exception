/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.persistence_II.hibernate;


import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.QueryException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.bean_V.CompoundPropertyException;
import be.peopleware.bean_V.PropertyException;
import be.peopleware.exception_I.TechnicalException;
import be.peopleware.persistence_II.IdNotFoundException;
import be.peopleware.persistence_II.PersistentBean;
import be.peopleware.persistence_II.dao.AsyncCrudDao;


/**
 * <p>Asynchronous CRUD functionality with Hibernate. There are no extra
 *   requirements for {@link PersistentBean}s to be used with Hibernate,
 *   apart from the definition of <kbd>hbm</kbd> files.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 * @invar     getRequest() != null;
 * @invar     getSession() != null;
 *
 * @todo Exceptions thrown here should be {@link PropertyException PropertyExceptions}
 *       instead of the more strict {@link CompoundPropertyException}, and should
 *       allow null origin.
 */
public class HibernateAsyncCrudDao extends AbstractHibernateDao implements AsyncCrudDao {

  /* <section name="Meta Information"> */
  //------------------------------------------------------------------

  /** {@value} */
  public static final String CVS_REVISION = "$Revision$";
  /** {@value} */
  public static final String CVS_DATE = "$Date$";
  /** {@value} */
  public static final String CVS_STATE = "$State$";
  /** {@value} */
  public static final String CVS_TAG = "$Name$";

  /* </section> */



  /* <construction> */
  //------------------------------------------------------------------

  // default constructor

  /* </construction> */

  private static final Log LOG = LogFactory.getLog(HibernateAsyncCrudDao.class);



  private static final String NULL_SESSION = "Session is null";
  private static final String NO_PENDING_TRANSACTION = "No transaction pending";
  private static final String PENDING_TRANSACTION = "There is a transaction still pending";
  private static final String NO_PERSISTENT_OBJECT = "No persistent object";
  private static final String WRONG_SUBTYPE = " not a subtype of PersistentBean";


  /*<property name="session">*/
  //------------------------------------------------------------------

  /**
   * @param     session
   *            The hibernate session to use for database manipulations.
   * @post      isInTransaction();
   * @post      new.getSession() == session;
   * @throws    IllegalStateException
   *            isInTransAction();
   */
  public final void setSession(final Session session) throws TechnicalException {
    if (isInTransaction()) {
      throw new TechnicalException("Cannot set session now, "
                                      + "transaction still in use", null);
    }
    super.setSession(session);
  }
  
  /*</property>*/
  
  
  
  /**
   * @invar     isInTransaction() == (tx != null);
   */
  private Transaction $tx;

  /**
   * @throws    TechnicalException
   *            isInTransaction()
   *            || getSession() == null;
   */
  public final void startTransaction() throws TechnicalException {
    LOG.debug("Starting hibernate transaction ...");
    if (getSession() == null) {
      throw new TechnicalException(NULL_SESSION, null);
    }
    if (isInTransaction()) {
      throw new TechnicalException(PENDING_TRANSACTION, null);
    }
    assert $tx == null;
    try {
      $tx = getSession().beginTransaction();
      setInTransaction(true);
    }
    catch (HibernateException hExc) {
      throw new TechnicalException("Could not create Hibernate transaction",
                                   hExc);
    }
    LOG.debug("Hibernate transaction started.");
  }

  /**
   * @param     pb
   *            The persitentObject thats needs to be written to the db.
   * @throws    TechnicalException !
   *            isInTransaction()
   *            || pb == null;
   * @throws    CompoundPropertyException
   *            If there are some data consitencies in <param>pb</param> that
   *            are detected by the database, for example: unique constraints
   */
  public final void commitTransaction(final PersistentBean pb)
      throws CompoundPropertyException, TechnicalException {
    LOG.debug("Starting commit ...");
    if (!isInTransaction()) {
      throw new TechnicalException(NO_PENDING_TRANSACTION, null);
    }
    if (pb == null) {
      throw new TechnicalException(NO_PERSISTENT_OBJECT, null);
    }
    assert $tx != null;
    try {
      $tx.commit();
      $tx = null;
      resetId($deleted);
      $deleted = new HashSet();
      $created = new HashSet();
      setInTransaction(false);
      LOG.debug("Commit completed.");
    }
    catch (HibernateException hExc) {
      LOG.debug("Commit failed.", hExc);
/* @idea (jand): it is stupid to have an argument pb for this method;
 *               it is needed for the exceptions if it is a hibernate exception;
 *               does the hibernate exception no contain the pb?
 */
      handleHibernateException(hExc, "Committing", pb);
    }
  }

  /**
   * Reset the id of the {@link PersistentBean PersistentBeans} in
   * <code>persistentBeans</code> to <code>null</code>.
   *
   * @pre persistentBeans != null;
   * @pre cC:instanceof(persistentBeans, PersistentBean);
   */
  private void resetId(Set persistentBeans) {
    assert persistentBeans != null;
    Iterator iter = persistentBeans.iterator();
    while (iter.hasNext()) {
      PersistentBean iterPo = (PersistentBean)iter.next();
      iterPo.setId(null);
    }
  }

  /**
   * For {@link #isCreated(PersistentBean) created} persistent beans, the
   * {@link PersistentBean#getId()} is reset to <code>null</code> (part of rollback).
   *
   * @throws    TechnicalException
   *            isInTransaction();
   */
  public final void cancelTransaction() throws TechnicalException {
    LOG.debug("Cancelling transaction.");
    if (!isInTransaction()) {
      throw new TechnicalException(NO_PENDING_TRANSACTION, null);
    }
    assert $tx != null;
    try {
      $tx.rollback();
      resetId($created);
      // $deleted objects get to keep there original id, as they are not really deleted
    }
    catch (HibernateException hExc) {
      throw new TechnicalException("could not rollback "
                                       + "Hibernate transaction. "
                                       + "this is serious.",
                                   hExc);
    }
    finally {
      $tx = null;
      setInTransaction(false);
      $deleted = new HashSet();
      $created = new HashSet();
    }
  }

  /**
   * After this method, <code>pb</code> will have an fresh id. Only during commit will
   * this <code>pb</code> actually be created in the DB, so if that fails, we need
   * to call {@link #cancelTransaction()}. This will reset the id to <code>null</code>.
   *
   * @post isCreated(pb);
   * @throws    TechnicalException
   *            !isInTransaction()
   *            || getSession() == null
   *            || pb == null
   *            || pb.getId() != null;
   */
  public final void createPersistentBean(final PersistentBean pb)
      throws CompoundPropertyException, TechnicalException {
    LOG.debug("Creating new record for bean \"" + pb + "\" ..."); //$NON-NLS-2$
    if (getSession() == null) {
      throw new TechnicalException(NULL_SESSION, null);
    }
    if (!isInTransaction()) {
      throw new TechnicalException(NO_PENDING_TRANSACTION, null);
    }
    if (pb == null) {
      throw new TechnicalException(NO_PERSISTENT_OBJECT, null);
    }
    if (pb.getId() != null) {
      throw new TechnicalException("pb cannot have an id",
                                   null);
    }
    try {
      LOG.trace("Gather all beans to be created, taking into account cascade");
      List allToBeCreated = relatedFreshPersistentBeans(pb);
      // we need to normalize and check all these beans
      Iterator iter = allToBeCreated.iterator();
      while (iter.hasNext()) {
        PersistentBean current = (PersistentBean)iter.next();
        LOG.trace("Normalizing  \"" + current + "\" and checking civility ...");
        current.normalize();
        current.checkCivility(); // CompoundPropertyException
// MUDO (jand) package all PropertyExceptions for all beans together; don't stop after one!!!
        LOG.trace("\"" + current + "\" checks out ok");
      }
      getSession().save(pb);
        // cascade done by Hibernate; all elements of allToBeCreated are created
// IDEA (jand) by doing the cascade ourselfs, we might be able to get better exceptions
      $created.addAll(allToBeCreated);
      if (LOG.isDebugEnabled()) {
        LOG.debug("Creating succesfull.");
        iter = allToBeCreated.iterator();
        while (iter.hasNext()) {
          PersistentBean current = (PersistentBean)iter.next();
          LOG.debug("    generated " + current.getId() + " as id for " + current);
        }
      }
    }
    catch (HibernateException hExc) {
      LOG.debug("Creation of new record failed.");
      handleHibernateException(hExc, "Creating", pb);
    }
    assert pb.getId() != null;
  }

  /**
   * <code>pb</code> is part of the result
   *
   * @todo move method static as utility method
   * @pre pb != null;
   * @pre pb.getId() == null;
   */
  private List relatedFreshPersistentBeans(PersistentBean pb) {
    assert pb != null;
    assert pb.getId() == null;
    List result = new LinkedList();
    result.add(pb);
    int current = 0;
    while (current < result.size()) {
      PersistentBean currentPb = (PersistentBean)result.get(current);
      current++;
      PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(currentPb);
      for (int i = 0; i < pds.length; i++) {
        PersistentBean related = relatedPeristentBean(currentPb, pds[i]);
        if ((related != null) && (related.getId() == null) &&  (! result.contains(related))) {
            /* if it is a fresh bean and it is the first time that we encounter it,
             * it is to be part of the result;
             * we also need to process it further: remember it on the agenda */
          result.add(related); // adds at the end of the list; size++
        }
      }
    }
    return Collections.unmodifiableList(result);
  }

  /**
   * The value if the property <code>pd</code> of <code>pb</code>, if
   * <ul>
   *    <li>it is readable</li>
   *    <li>it is a {@link PersistentBean}
   * </ul>
   * <code>null</code> otherwise 9also if there is an exception reading).
   *
   * @pre pb != null;
   * @pre pd != null;
   */
  private PersistentBean relatedPeristentBean(PersistentBean pb, PropertyDescriptor pd) {
    assert pb != null;
    assert pd != null;
    PersistentBean result = null;
    if (PersistentBean.class.isAssignableFrom(pd.getPropertyType())) {
      Method rm = pd.getReadMethod();
      if (rm != null) {
        // found a property that returns a related bean; get it
        try {
          result = (PersistentBean)rm.invoke(pb, null);
        }
        catch (IllegalArgumentException iaExc) {
          assert false : "Should not happen, since there are no " //$NON-NLS-1$
                         + "arguments, and the implicit argument is " //$NON-NLS-1$
                         + "not null and of the correct type"; //$NON-NLS-1$
        }
        catch (IllegalAccessException e) {
          assert false : "IllegalAccessException should not happen: " + e;
        }
        catch (InvocationTargetException e) {
          assert false : "InvocationTargetException should not happen: " + e;
        }
        catch (NullPointerException e) {
          assert false : "NullPointerException should not happen: " + e;
        }
        /* ExceptionInInitializerError can occur with invoke, but we do not
         take into account errors */
      }
    }
    return result;
  }

  /**
   * @param     id
   *            The ID of the PersistentBean to retrieve
   * @param     persistentObjectType
   *            The type of PersistentBean (subclass) to retrieve.
   * @throws    IdNotFoundException
   *            No PersistentBean with <param>id</param> of type
   *            <param>persistentObjectType</param>was found.
   * @throws    TechnicalException
   *            getSession() == null
   *            || id == null
   *            || persistentObjectType == null
   *            || !PersistentBean.class
   *                    .isAssignableFrom(persistentObjectType);
   */
  public PersistentBean retrievePersistentBean(
      final Long id,
      final Class persistentObjectType)
          throws IdNotFoundException, TechnicalException {
    LOG.debug("Retrieving record with id = " + id + " ..."); //$NON-NLS-2$
    if (getSession() == null) {
      throw new TechnicalException(NULL_SESSION, null);
    }
    if (id == null) {
      throw new IdNotFoundException(id, "ID_IS_NULL",
                                    null, persistentObjectType);
    }
    if (persistentObjectType == null) {
      throw new TechnicalException(NO_PERSISTENT_OBJECT, null);
    }
    if (!PersistentBean.class.isAssignableFrom(persistentObjectType)) {
      throw new TechnicalException(persistentObjectType.toString()
                                       + WRONG_SUBTYPE,
                                   null);
    }
    PersistentBean result = null;
    try {
      result = (PersistentBean)getSession().get(persistentObjectType, id);
      if (result == null) {
        LOG.debug("Record not found");
        throw new IdNotFoundException(id, null, null, persistentObjectType);
      }
      // When hibernate caching is active they can give back a object with
      // the correct ID but of the wrong type, so this extra check is
      // introduced as a workaround for it. A posting was done to the hibernate
      // forum to ask if it is a bug or if we are missing something.
      //
      // URL: http://forum.hibernate.org/viewtopic.php?t=938177
      if (!persistentObjectType.isInstance(result)) {
        LOG.debug("Incorrect record found (Wrong type");
        throw new IdNotFoundException(id, null, null, persistentObjectType);
      }

    }
    catch (ClassCastException ccExc) {
      throw new TechnicalException("retrieved object was not a PersistentBean",
                                   ccExc);
    }
    catch (HibernateException hExc) {
      // this cannot be that we did not find an object with that id, since we
      // use get
      throw new TechnicalException("problem getting record from DB", hExc);
    }
    assert result != null;
    assert result.getId().equals(id);
    assert persistentObjectType.isInstance(result);
    if (LOG.isDebugEnabled()) {
      LOG.debug("Retrieval succeeded (" + result + ")"); //$NON-NLS-2$
    }
    return result;
  }


  /**
   * @throws    TechnicalException
   *            getSession() == null
   *              || persistentObjectType == null
   *              || ! PersistentBean.class.isAssignableFrom(persistentObjectType);
   */
  public Set retrieveAllPersistentBeans(final Class persistentObjectType,
                                        final boolean retrieveSubClasses)
      throws TechnicalException {
    LOG.debug("Retrieving all records of type \"" + persistentObjectType + "\" ..."); //$NON-NLS-2$
    if (getSession() == null) {
      throw new TechnicalException(NULL_SESSION, null);
    }
    if (persistentObjectType == null) {
      throw new TechnicalException(
                    "persistentObjectType cannot be null", null);
    }
    if (!PersistentBean.class.isAssignableFrom(persistentObjectType)) {
      throw new TechnicalException(persistentObjectType.toString()
                                       + WRONG_SUBTYPE,
                                   null);
    }
    Set results = new HashSet();
    try {
      if (retrieveSubClasses) {
        results.addAll(getSession().createCriteria(persistentObjectType).list());
      }
      else {
        try {
          results.addAll(getSession().createQuery("FROM "
              + persistentObjectType.getName()
              + " as persistentObject WHERE persistentObject.class = "
              + persistentObjectType.getName()).list());
        }
        catch (QueryException qExc) {
          if (qExc.getMessage().matches(
                "could not resolve property: class of: .*")) {
            results.addAll(getSession().createCriteria(persistentObjectType).list());
          }
        }
      }
    }
    catch (HibernateException hExc) {
      throw new TechnicalException("problem getting all instances of "
                                       + persistentObjectType.getName(),
                                   hExc);
    }
    assert results != null;
    LOG.debug("Retrieval succeeded (" + results.size() + " objects retrieved)"); //$NON-NLS-2$
    return results;
  }

  /**
   * @throws    TechnicalException !
   *            isInTransaction() || pb == null ||
   *            pb.getId() == null || getSession() == null;
   */
  public final void updatePersistentBean(final PersistentBean pb)
      throws CompoundPropertyException, TechnicalException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Updating bean \"" + pb + "\" ..."); //$NON-NLS-2$
    }
    if (getSession() == null) {
      throw new TechnicalException(NULL_SESSION, null);
    }
    if (!isInTransaction()) {
      throw new TechnicalException(NO_PENDING_TRANSACTION, null);
    }
    if (pb == null) {
      throw new TechnicalException(NO_PERSISTENT_OBJECT, null);
    }
    if (pb.getId() == null) {
      throw new TechnicalException("pb has no id", null);
    }
    try {
      if (LOG.isTraceEnabled()) {
        LOG.trace("Normalizing  \"" + pb + "\" ..."); //$NON-NLS-2$
      }
      pb.normalize();
      pb.checkCivility(); // CompoundPropertyException
// MUDO (jand) normalize and checkCivility off all reachable PB's (cascade)
      if (LOG.isTraceEnabled()) {
        LOG.trace("Normalization of \"" + pb + "\" done."); //$NON-NLS-2$
      }
      getSession().update(pb);
      /*
       * If there is a persistent instance with the same identifier, different
       * from this pb, an exception is thrown. This cannot happen since pb is
       * fresh from the DB: we got it with retrieve or created it ourself.
       */
      LOG.debug("Update succeeded.");
    }
    catch (HibernateException hExc) {
      LOG.debug("Update failed.");
      handleHibernateException(hExc, "updating", pb);
    }
  }

  /**
   * @throws    TechnicalException !
   *            isInTransaction() || pb == null ||
   *            pb.getId() == null || getSession() == null;
   */
  public void deletePersistentBean(final PersistentBean pb)
      throws TechnicalException {
    LOG.debug("Deleting persistent bean \"" + pb + "\" ..."); //$NON-NLS-2$
    if (getSession() == null) {
      throw new TechnicalException(NULL_SESSION, null);
    }
    if (!isInTransaction()) {
      throw new TechnicalException(NO_PENDING_TRANSACTION, null);
    }
    if (pb == null) {
      throw new TechnicalException(NO_PERSISTENT_OBJECT, null);
    }
    if (pb.getId() == null) {
      throw new TechnicalException("pb has no id", null);
    }
    try {
      getSession().delete(pb);
      $deleted.add(pb);
// MUDO (jand) take into account cascade delete
    }
    catch (HibernateException hExc) {
      LOG.debug("Deletion failed.");
      try {
        handleHibernateException(hExc, "Deleting", pb);
      }
      catch (CompoundPropertyException cpExc) {
        assert false : "this should possibly become a non-modifiable exception";
      }
    }
    LOG.debug("Deletion succeeded.");
  }



  /*<property name="created">*/
  //------------------------------------------------------------------

  /**
   * Returns true when the given persistent bean has been created (i.e.,
   * has been used as a parameter in {@link #createPersistentBean(PersistentBean)});
   * returns false otherwise.
   *
   * @param  pb
   * @basic
   */
  public boolean isCreated(final PersistentBean pb) {
    return $created.contains(pb);
  }

  /**
   * @invar $created != null;
   * @invar ! $created.contains(null);
   * @invar (forall Object o; $created.contains(o); o instanceof PersistentBean);
   */
  private Set $created = new HashSet();

  /*</property>*/



  /*<property name="deleted">*/
  //------------------------------------------------------------------

  /**
   * Returns true when the given persistent bean has been deleted; returns false
   * otherwise.
   *
   * @param  pb
   * @basic
   */
  public boolean isDeleted(final PersistentBean pb) {
    return $deleted.contains(pb);
  }

  /**
   * @invar $deleted != null;
   * @invar ! $deleted.contains(null);
   * @invar (forall Object o; $deleted.contains(o); o instanceof PersistentBean);
   */
  private Set $deleted = new HashSet();

  /*</property>*/



 /*<property name="inTransaction">*/
 //------------------------------------------------------------------

  /**
   * Returns true when a transaction is open; returns false otherwise.
   *
   * @basic
   */
  public final boolean isInTransaction() {
    return $isInTransaction;
  }

  /**
   * Set the given boolean value, reflecting whether a transaction is open
   * or not.
   *
   * @param inTransaction
   * @post  new.isInTransaction() == inTransaction;
   */
  protected final void setInTransaction(final boolean inTransaction) {
    $isInTransaction = inTransaction;
  }

  private boolean $isInTransaction;

  /*</property>*/

}
