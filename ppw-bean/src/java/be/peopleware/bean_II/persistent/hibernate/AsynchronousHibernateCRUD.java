package be.peopleware.bean_II.persistent.hibernate;


import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.QueryException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.bean_II.CompoundPropertyException;
import be.peopleware.bean_II.ConstraintException;
import be.peopleware.bean_II.DuplicateKeyException;
import be.peopleware.bean_II.persistent.AbstractAsynchronousCRUD;
import be.peopleware.bean_II.persistent.IdNotFoundException;
import be.peopleware.bean_II.persistent.PersistentBean;
import be.peopleware.exception_I.Exceptions;
import be.peopleware.exception_I.TechnicalException;


/**
 * <p>Asynchronous CRUD functionality with Hibernate. There are no extra
 *   requirements for {@link PersistentBean}s to be used with Hibernate,
 *   apart from the definition of <kbd>hbm</kbd> files.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 * @invar     getRequest() != null;
 * @invar     getSession() != null;
 */
public class AsynchronousHibernateCRUD extends AbstractAsynchronousCRUD {

  /* <section name="Meta Information"> */
  //------------------------------------------------------------------

  /** {@value} */
  public static final String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_TAG = "$Name$"; //$NON-NLS-1$

  /* </section> */



  /* <construction> */
  //------------------------------------------------------------------

  // default constructor

  /* </construction> */

  private static final Log LOG =
    LogFactory.getLog(AsynchronousHibernateCRUD.class);



  private static final String NULL_SESSION = "Session is null"; //$NON-NLS-1$
  private static final String NO_PENDING_TRANSACTION
      = "No transaction pending"; //$NON-NLS-1$
  private static final String PENDING_TRANSACTION
      = "There is a transaction still pending"; //$NON-NLS-1$
  private static final String NO_PERSISTENT_OBJECT
      = "No persistent object"; //$NON-NLS-1$
  private static final String WRONG_SUBTYPE
      = " not a subtype of PersistentBean"; //$NON-NLS-1$
  /**
   * @invar     isInTransaction() == (tx != null);
   */
  private Transaction $tx;

  /**
   * @basic
   * @init      null;
   */
  public final Session getSession() {
    return $session;
  }

  /**
   * @param     session
   *            The hibernate session to use for database manipulations.
   * @post      isInTransaction();
   * @post      new.getSession() == session;
   * @throws    IllegalStateException
   *            isInTransAction();
   */
  public final void setSession(final Session session) {
    LOG.trace("setting session (" + session + ")"); //$NON-NLS-1$ //$NON-NLS-2$
    if (isInTransaction()) {
      throw new IllegalStateException("Cannot set session now, "  //$NON-NLS-1$
                                      + "transaction still in use"); //$NON-NLS-1$
    }
    $session = session;
  }

  /**
   * @invar     $session != null;
   */
  private Session $session;

  /**
   * @throws    TechnicalException
   *            isInTransaction()
   *            || getSession() == null;
   */
  public final void startTransaction() throws TechnicalException {
    LOG.debug("Starting hibernate transaction ..."); //$NON-NLS-1$
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
      throw new TechnicalException("Could not create Hibernate transaction", //$NON-NLS-1$
                                   hExc);
    }
    LOG.debug("Hibernate transaction started."); //$NON-NLS-1$
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
    LOG.debug("Starting commit ..."); //$NON-NLS-1$
    if (!isInTransaction()) {
      throw new TechnicalException(NO_PENDING_TRANSACTION, null);
    }
    if (pb == null) {
      throw new TechnicalException(NO_PERSISTENT_OBJECT, null);
    }
    assert $tx != null;
    try {
      getSession().flush();
      $tx.commit();
      $tx = null;
      Iterator iter = $deleted.iterator();
      while (iter.hasNext()) {
        PersistentBean iterPo = (PersistentBean)iter.next();
        iterPo.setId(null);
      }
      setInTransaction(false);
      $deleted = new HashSet();
      LOG.debug("Commit completed."); //$NON-NLS-1$
    }
    catch (HibernateException hExc) {
      LOG.debug("Commit failed.", hExc); //$NON-NLS-1$
/* @idea (jand): it is stupid to have an argument pb for this method;
 *               it is needed for the exceptions if it is a hibernate exception;
 *               does the hibernate exception no contain the pb?
 */
      handleHibernateException(hExc, "Committing", pb); //$NON-NLS-1$
    }
  }

  /**
   * @throws    TechnicalException
   *            isInTransaction();
   */
  public final void cancelTransaction() throws TechnicalException {
    LOG.debug("Cancelling transaction."); //$NON-NLS-1$
    if (!isInTransaction()) {
      throw new TechnicalException(NO_PENDING_TRANSACTION, null);
    }
    assert $tx != null;
    try {
      $tx.rollback();
    }
    catch (HibernateException hExc) {
      throw new TechnicalException("could not rollback " //$NON-NLS-1$
                                       + "Hibernate transaction. " //$NON-NLS-1$
                                       + "this is serious.", //$NON-NLS-1$
                                   hExc);
    }
    finally {
      $tx = null;
      setInTransaction(false);
      $deleted = new HashSet();
    }
  }

  /**
   * @throws    TechnicalException
   *            !isInTransaction()
   *            || getSession() == null
   *            || pb == null
   *            || pb.getId() != null;
   */
  public final void createPersistentBean(final PersistentBean pb)
      throws CompoundPropertyException, TechnicalException {
    LOG.debug("Creating new record for bean \"" + pb + "\" ..."); //$NON-NLS-1$ //$NON-NLS-2$
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
      throw new TechnicalException("pb cannot have an id", //$NON-NLS-1$
                                   null);
    }
    try {
      LOG.trace("Normalizing  \"" + pb + "\" ..."); //$NON-NLS-1$ //$NON-NLS-2$
      pb.normalize();
      pb.checkCivility(); // CompoundPropertyException
      LOG.trace("Normalization of \"" + pb + "\" done."); //$NON-NLS-1$ //$NON-NLS-2$
      getSession().save(pb);
      LOG.debug("Creating succesfull. Id = " + pb.getId()); //$NON-NLS-1$
    }
    catch (HibernateException hExc) {
      LOG.debug("Creation of new record failed."); //$NON-NLS-1$
      handleHibernateException(hExc,
                               "Creating", //$NON-NLS-1$
                               pb);
    }
    assert pb.getId() != null;
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
    LOG.debug("Retrieving record with id = " + id + " ..."); //$NON-NLS-1$ //$NON-NLS-2$
    if (getSession() == null) {
      throw new TechnicalException(NULL_SESSION, null);
    }
    if (id == null) {
      throw new IdNotFoundException(id, "ID_IS_NULL", //$NON-NLS-1$
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
        LOG.debug("Record not found"); //$NON-NLS-1$
        throw new IdNotFoundException(id, null, null, persistentObjectType);
      }
      // When hibernate caching is active they can give back a object with
      // the correct ID but of the wrong type, so this extra check is
      // introduced as a workaround for it. A posting was done to the hibernate
      // forum to ask if it is a bug or if we are missing something.
      //
      // URL: http://forum.hibernate.org/viewtopic.php?t=938177
      if (!persistentObjectType.isInstance(result)) {
        LOG.debug("Incorrect record found (Wrong type"); //$NON-NLS-1$
        throw new IdNotFoundException(id, null, null, persistentObjectType);
      }
      
    }
    catch (ClassCastException ccExc) {
      throw new TechnicalException("retrieved object was not a PersistentBean", //$NON-NLS-1$
                                   ccExc);
    }
    catch (HibernateException hExc) {
      // this cannot be that we did not find an object with that id, since we
      // use get
      throw new TechnicalException("problem getting record from DB", hExc); //$NON-NLS-1$
    }
    assert result != null;
    assert result.getId().equals(id);
    assert persistentObjectType.isInstance(result);
    if (LOG.isDebugEnabled()) {
      LOG.debug("Retrieval succeeded (" + result + ")"); //$NON-NLS-1$ //$NON-NLS-2$
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
    LOG.debug("Retrieving all records of type \"" + persistentObjectType + "\" ..."); //$NON-NLS-1$ //$NON-NLS-2$
    if (getSession() == null) {
      throw new TechnicalException(NULL_SESSION, null);
    }
    if (persistentObjectType == null) {
      throw new TechnicalException(
                    "persistentObjectType cannot be null", null); //$NON-NLS-1$
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
          results.addAll(getSession().createQuery("FROM " //$NON-NLS-1$
              + persistentObjectType.getName()
              + " as persistentObject WHERE persistentObject.class = " //$NON-NLS-1$
              + persistentObjectType.getName()).list());
        }
        catch (QueryException qExc) {
          if (qExc.getMessage().matches(
                "could not resolve property: class of: .*")) { //$NON-NLS-1$
            results.addAll(getSession().createCriteria(persistentObjectType).list());
          }
        }
      }
    }
    catch (HibernateException hExc) {
      throw new TechnicalException("problem getting all instances of " //$NON-NLS-1$
                                       + persistentObjectType.getName(),
                                   hExc);
    }
    assert results != null;
    LOG.debug("Retrieval succeeded (" + results.size() + " objects retrieved)"); //$NON-NLS-1$ //$NON-NLS-2$
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
      LOG.debug("Updating bean \"" + pb + "\" ..."); //$NON-NLS-1$ //$NON-NLS-2$
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
      throw new TechnicalException("pb has no id", null); //$NON-NLS-1$
    }
    try {
      if (LOG.isTraceEnabled()) {
        LOG.trace("Normalizing  \"" + pb + "\" ..."); //$NON-NLS-1$ //$NON-NLS-2$
      }
      pb.normalize();
      pb.checkCivility(); // CompoundPropertyException
      if (LOG.isTraceEnabled()) {
        LOG.trace("Normalization of \"" + pb + "\" done."); //$NON-NLS-1$ //$NON-NLS-2$
      }
      getSession().update(pb);
      /*
       * If there is a persistent instance with the same identifier, different
       * from this pb, an exception is thrown. This cannot happen since pb is
       * fresh from the DB: we got it with retrieve or created it ourself.
       */
      LOG.debug("Update succeeded."); //$NON-NLS-1$
    }
    catch (HibernateException hExc) {
      LOG.debug("Update failed."); //$NON-NLS-1$
      handleHibernateException(hExc, "updating", pb); //$NON-NLS-1$
    }
  }

  /**
   * @throws    TechnicalException !
   *            isInTransaction() || pb == null ||
   *            pb.getId() == null || getSession() == null;
   */
  public void deletePersistentBean(final PersistentBean pb)
      throws TechnicalException {
    LOG.debug("Deleting persistent bean \"" + pb + "\" ...");  //$NON-NLS-1$//$NON-NLS-2$
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
      throw new TechnicalException("pb has no id", null); //$NON-NLS-1$
    }
    try {
      getSession().delete(pb);
      $deleted.add(pb);
    }
    catch (HibernateException hExc) {
      LOG.debug("Deletion failed."); //$NON-NLS-1$
      try {
        handleHibernateException(hExc, "Deleting", pb); //$NON-NLS-1$
      }
      catch (CompoundPropertyException cpExc) {
        assert false : "this should possibly become a non-modifiable exception"; //$NON-NLS-1$
      }
    }
    LOG.debug("Deletion succeeded."); //$NON-NLS-1$
  }

  public boolean isDeleted(final PersistentBean pb) {
    return $deleted.contains(pb);
  }

  /**
   * @invar $deleted != null;
   * @invar ! $deleted.contains(null);
   * @invar (forall Object o; $deleted.contains(o); o instanceof PersistentBean);
   */
  private Set $deleted = new HashSet();

  private static void handleHibernateException(final HibernateException hExc,
                                               final String operationName,
                                               final PersistentBean pb)
      throws TechnicalException, CompoundPropertyException {
    SQLException sqlExc = (SQLException)Exceptions.huntFor(hExc,
                                                           SQLException.class);
    if (sqlExc != null) {
      if (sqlExc.getMessage()
                .indexOf("Duplicate key or integrity constraint violation,  " //$NON-NLS-1$
                         + "message from server: \"Duplicate entry") >= 0) { //$NON-NLS-1$
        // WATCH OUT: SQL Error message contains 'dual space' after ','.
        assert pb != null;
        CompoundPropertyException cpExc = new CompoundPropertyException(pb,
                                                                        null,
                                                                        null,
                                                                        null);
        DuplicateKeyException dkExc = new DuplicateKeyException(pb,
                                                                null, //$NON-NLS-1$
                                                                "VALUE_NOT_UNIQUE", //$NON-NLS-1$
                                                                sqlExc);
        cpExc.addElementException(dkExc);
        cpExc.close();
        throw cpExc;
      }
      else if (sqlExc.getMessage()
          .indexOf("Duplicate key or integrity constraint violation,  " //$NON-NLS-1$
                   + "message from server: \"Cannot delete or update a " //$NON-NLS-1$
                   + "parent row: a foreign key constraint fails\"") >= 0) { //$NON-NLS-1$
        // WATCH OUT: SQL Error message contains 'dual space' after ','.
        assert pb != null;
        CompoundPropertyException cpExc = new CompoundPropertyException(pb,
                                                                        null,
                                                                        null,
                                                                        null);
        ConstraintException cExc = new ConstraintException(pb,
                                                           null, //$NON-NLS-1$
                                                           "CONSTRAINT_FAILURE", //$NON-NLS-1$
                                                           sqlExc);
        cpExc.addElementException(cExc);
        cpExc.close();
        throw cpExc;
      }
      else {
        // cannot be that the record is not found
        throw new TechnicalException("problem " //$NON-NLS-1$
                                         + operationName
                                         + " record", //$NON-NLS-1$
                                     hExc);
      }
    }
    CompoundPropertyException cp = (CompoundPropertyException)Exceptions
        .huntFor(hExc,
                 CompoundPropertyException.class);
    if (cp != null) {
      throw cp;
    }
    else {
      // cannot be that the record is not found
      throw new TechnicalException("problem " //$NON-NLS-1$
                                       + operationName
                                       + " record", //$NON-NLS-1$
                                   hExc);
    }
  }

}
