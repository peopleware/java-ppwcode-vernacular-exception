package be.peopleware.persistence_I.hibernate;


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

import be.peopleware.bean_IV.CompoundPropertyException;
import be.peopleware.bean_IV.ConstraintException;
import be.peopleware.bean_IV.DuplicateKeyException;
import be.peopleware.exception_I.Exceptions;
import be.peopleware.exception_I.TechnicalException;
import be.peopleware.persistence_I.IdNotFoundException;
import be.peopleware.persistence_I.PersistentBean;
import be.peopleware.persistence_I.dao.AsyncCrudDao;


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
  private static final String NO_PENDING_TRANSACTION
      = "No transaction pending";
  private static final String PENDING_TRANSACTION
      = "There is a transaction still pending";
  private static final String NO_PERSISTENT_OBJECT
      = "No persistent object";
  private static final String WRONG_SUBTYPE
      = " not a subtype of PersistentBean";

  
  /*<property name="session">*/
  //------------------------------------------------------------------
  
  public Session getSession() {
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
  public final void setSession(final Session session) throws IllegalStateException {
    if (isInTransaction()) {
      throw new IllegalStateException("Cannot set session now, " 
                                      + "transaction still in use");
    }
    $session = session;
  }

  protected Session $session;

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
      LOG.trace("Normalizing  \"" + pb + "\" ..."); //$NON-NLS-2$
      pb.normalize();
      pb.checkCivility(); // CompoundPropertyException
      LOG.trace("Normalization of \"" + pb + "\" done."); //$NON-NLS-2$
      getSession().save(pb);
      LOG.debug("Creating succesfull. Id = " + pb.getId());
    }
    catch (HibernateException hExc) {
      LOG.debug("Creation of new record failed.");
      handleHibernateException(hExc,
                               "Creating",
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
                .indexOf("Duplicate key or integrity constraint violation,  "
                         + "message from server: \"Duplicate entry") >= 0 
          || sqlExc.getMessage().indexOf("Duplicate entry") >= 0 ) {
        // WATCH OUT: SQL Error message contains 'dual space' after ','.
        assert pb != null;
        CompoundPropertyException cpExc = new CompoundPropertyException(pb,
                                                                        null,
                                                                        null,
                                                                        null);
        DuplicateKeyException dkExc = new DuplicateKeyException(pb,
                                                                null,
                                                                "VALUE_NOT_UNIQUE",
                                                                sqlExc);
        cpExc.addElementException(dkExc);
        cpExc.close();
        throw cpExc;
      }
      else if (sqlExc.getMessage()
          .indexOf("Duplicate key or integrity constraint violation,  "
                   + "message from server: \"Cannot delete or update a "
                   + "parent row: a foreign key constraint fails\"") >= 0) {
        // WATCH OUT: SQL Error message contains 'dual space' after ','.
        assert pb != null;
        CompoundPropertyException cpExc = new CompoundPropertyException(pb,
                                                                        null,
                                                                        null,
                                                                        null);
        ConstraintException cExc = new ConstraintException(pb,
                                                           null,
                                                           "CONSTRAINT_FAILURE",
                                                           sqlExc);
        cpExc.addElementException(cExc);
        cpExc.close();
        throw cpExc;
      }
      else {
        // cannot be that the record is not found
        throw new TechnicalException("problem "
                                         + operationName
                                         + " record",
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
      throw new TechnicalException("problem "
                                       + operationName
                                       + " record",
                                   hExc);
    }
  }


  public final boolean isInTransaction() {
    return $isInTransaction;
  }

  protected final void setInTransaction(final boolean inTransaction) {
    $isInTransaction = inTransaction;
  }

  private boolean $isInTransaction;

}
