package be.peopleware.bean_I.persistent;


import java.util.Set;

import be.peopleware.bean_I.CompoundPropertyException;
import be.peopleware.bean_I.RousseauBean;
import be.peopleware.exception_I.TechnicalException;


/**
 * <p>This interface gathers the methods needed for CRUD
 *   functionality in an asynchronous context, e.g., a
 *   web application. Clients will call
 *   {@link #createPersistentBean(PersistentBean)},
 *   {@link #updatePersistentBean(PersistentBean)}
 *   and
 *   {@link #deletePersistentBean(PersistentBean)}
 *   always in the context of a transaction. A transaction
 *   is started by
 *   {@link #startTransaction()}, and completes
 *   with, either a call to
 *   {@link #commitTransaction(PersistentBean)}
 *   or {@link #cancelTransaction()}.
 *   {@link #retrievePersistentBean(Long, Class)}
 *   can be called outside a transaction.
 *   Objects that are deleted have their {@link PersistentBean#getId()}
 *   set to null on {@link #commitTransaction(PersistentBean)}.</p>
 * <p>Before a {@link PersistentBean} is written to the persistent
 *   storage (see {@link #createPersistentBean(PersistentBean)}
 *   and {@link #updatePersistentBean(PersistentBean)}, it is
 *   {@link RousseauBean#normalize() normalized} and
 *   checked for {@link RousseauBean#isCivilized() civility}.
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 */
public interface AsynchronousCRUD {

  /*<section name="Meta Information">*/
  //------------------------------------------------------------------

  /** {@value} */
  String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  String CVS_TAG = "$Name$"; //$NON-NLS-1$

  /*</section>*/


  /**
   * <p>Start a transaction. The transaction will be closed in
   *   {@link #commitTransaction(PersistentBean)}
   *   or {@link #cancelTransaction()}.</p>
   * <p>This instance should keep track of the transaction state
   *   until it is requested to close the transaction.</p>
   *
   * @post   ! isInTransaction();
   *         This cannot be made true by this method when it is
   *         false in the old state. So the only option for the
   *         implementer is to throw an exception when this occurs.
   * @post   new.isInTransaction();
   * @throws TechnicalException
   *         ; could not start a transaction.
   * @throws TechnicalException
   *         isInTransaction();
   */
  void startTransaction() throws TechnicalException;

  /**
   * <p>Commit a transaction. The transaction was started by
   *  {@link #startTransaction()}.</p>
   * <p>This instance should keep track of the transaction state
   *   until it is requested to close the transaction.</p>
   *
   * @param  pb
   *         The {@link PersistentBean} instance this transaction was mainly
   *         concerned with.
   *         This is used as {@link CompoundPropertyException#getOrigin()}
   *         in potential {@link CompoundPropertyException}s that are only
   *         discovered on commit.
   * @post   isInTransaction();
   *         This cannot be made true by this method when it is
   *         false in the old state. So the only option for the
   *         implementer is to throw an exception when this occurs.
   * @post   pb != null;
   *         This cannot be made true by this method when it is
   *         false in the old state. So the only option for the
   *         implementer is to throw an exception when this occurs.
   * @post   ! new.isInTransaction();
   * @post   (forall PersistentBean o; isDeleted(o); (new o).getId() == null);
   * @post   (forall PersistentBean o; ; ! new.isDeleted(o));
   * @throws CompoundPropertyException
   *         ; the commit was stopped for a semantic reason
   * @throws TechnicalException
   *         ; could not stop the transaction.
   * @throws TechnicalException
   *         ! isInTransaction() || pb == null;
   */
  void commitTransaction(PersistentBean pb) throws CompoundPropertyException,
                                  TechnicalException;

  /**
   * <p>Cancel a transaction. The transaction was started by
   *   {@link #startTransaction()}.</p>
   * <p>This instance should keep track of the transaction state
   *   until it is requested to close the transaction.</p>
   *
   * @post   isInTransaction();
   *         This cannot be made true by this method when it is
   *         false in the old state. So the only option for the
   *         implementer is to throw an exception when this occurs.
   * @post   ! new.isInTransaction();
   * @post   (forall PersistentBean o; ; ! new.isDeleted(o));
   * @throws TechnicalException
   *         ; could not cancel the transaction.
   * @throws TechnicalException
   *         ! isInTransaction();
   */
  void cancelTransaction() throws TechnicalException;

  /**
   * @basic
   * @init false;
   */
  boolean isInTransaction();

  /**
   * <p>Take a persistent bean instance <code>pb</code> that exists
   *   in memory, but not yet in the persistent storage, and
   *   create it in the persistent storage.</p>
   * <p>Before a record for <code>pb</code> is created in the persistent
   *   storage, first <code>pb.normalize()</code> is called,
   *   and we check whether <code>pb.isCivilized()</code>.
   *
   * @post   isInTransaction();
   *         This cannot be made true by this method when it is
   *         false in the old state. So the only option for the
   *         implementer is to throw an exception when this occurs.
   * @post   pb != null;
   *         This cannot be made true by this method when it is
   *         false in the old state. So the only option for the
   *         implementer is to throw an exception when this occurs.
   * @post   pb.isCivilized();
   *         This cannot be made true by this method when it is
   *         false in the old state. So the only option for the
   *         implementer is to throw an exception when this occurs.
   * @post   pb.getId() == null;
   *         This cannot be made true by this method when it is
   *         false in the old state. So the only option for the
   *         implementer is to throw an exception when this occurs.
   * @post   (new pb).getId() != null;
   * @post   new pb.hasSameValues(pb);
   * @throws CompoundPropertyException
   *         pb.getWildExceptions();
   *         The operation was stopped for a semantic reason (! pb.isCivilized()).
   *         The CompoundPropertyException thrown will be closed.
   * @throws TechnicalException
   *         ; could not perform the operation
   * @throws TechnicalException
   *         ! isInTransaction() || pb == null || pb.getId() != null;
   *
   * @idea (jand) security exceptions
   */
  void createPersistentBean(final PersistentBean pb)
      throws CompoundPropertyException, TechnicalException;

  /**
   * <p>Return a persistent bean instance that represents the
   *   data of the record with key <code>id</code> of type
   *   <code>persistentBeanType</code> in the
   *   persistent storage.</p>
   *
   * @post   id != null;
   *         This cannot be made true by this method when it is
   *         false in the old state. So the only option for the
   *         implementer is to throw an exception when this occurs.
   * @post   persistentBeanType != null;
   *         This cannot be made true by this method when it is
   *         false in the old state. So the only option for the
   *         implementer is to throw an exception when this occurs.
   * @post   PersistentBean.class.isAssignableFrom(persistentBeanType);
   *         This cannot be made true by this method when it is
   *         false in the old state. So the only option for the
   *         implementer is to throw an exception when this occurs.
   * @result result != null;
   * @result result.getId().equals(id);
   * @result persistentBeanType.isInstance(result);
   * @result result.isCivilized();
   * @throws IdNotFoundException
   *         ; no instance found in persistent storage with
   *           primary key <code>id</code> of type
   *           <code>persistentBeanType</code>; also thrown
   *           when <code>id</code>
   *           is <code>null</code>.
   * @throws TechnicalException
   *         ; could not perform the operation
   * @throws TechnicalException
   *         persistentBeanType == null
   *          || ! PersistentBean.class.isAssignableFrom(persistentBeanType);
   *
   * @idea (jand) security exceptions
   */
  PersistentBean retrievePersistentBean(final Long id,
                                        final Class persistentBeanType)
      throws IdNotFoundException, TechnicalException;

  /**
   * <p>Return the set of all persistent bean instances that represent the
   *   data of the records of type
   *   <code>persistentBeanType</code> in the
   *   persistent storage.</p>
   *
   * @param  retrieveSubClasses
   *         whether or not to also retrieve subclasses of persistentBeanType
   * @post   persistentBeanType != null;
   *         This cannot be made true by this method when it is
   *         false in the old state. So the only option for the
   *         implementer is to throw an exception when this occurs.
   * @post   PersistentBean.class.isAssignableFrom(persistentBeanType);
   *         This cannot be made true by this method when it is
   *         false in the old state. So the only option for the
   *         implementer is to throw an exception when this occurs.
   * @result result != null;
   * @result ! result.contains(null);
   * @result (forall Object o; result.contains(o);
   *            persistentBeanType.isInstance(o));
   * @result (forall PersistentBean pb; result.contains(pb);
   *            pb.getId() != null);
   * @result (forall PersistentBean pb; result.contains(pb); pb.isCivilized());
   * @throws TechnicalException
   *         ; could not perform the operation
   * @throws TechnicalException
   *         persistentBeanType == null
   *            || PersistentBean.class.isAssignableFrom(persistentBeanType);
   *
   * @idea (jand) security exceptions
   */
  Set retrieveAllPersistentBeans(final Class persistentBeanType,
                                 final boolean retrieveSubClasses)
      throws TechnicalException;

  /**
   * <p>Take a persistent bean instance <code>pb</code> that exists
   *   in memory and represents an existing record in the persistent
   *   storage, and change the data in the persistent storage to
   *   reflect the current state of <code>pb</code>.</p>
   * <p>Before the state of <code>pb</code> is written to the persistent
   *   storage, first <code>pb.normalize()</code> is called,
   *   and we check whether <code>pb.isCivilized()</code>.
   * <p>The state of <code>pb</code> remains completely unchanged,
   *   apart from normalization
   *   (<code>pb.hasSameValues(new pb)</code>).</p>
   *
   * @post   isInTransaction();
   *         This cannot be made true by this method when it is
   *         false in the old state. So the only option for the
   *         implementer is to throw an exception when this occurs.
   * @post   pb != null;
   *         This cannot be made true by this method when it is
   *         false in the old state. So the only option for the
   *         implementer is to throw an exception when this occurs.
   * @post   pb.getId() != null;
   *         This cannot be made true by this method when it is
   *         false in the old state. So the only option for the
   *         implementer is to throw an exception when this occurs.
   * @post   pb.isCivilized();
   *         This cannot be made true by this method when it is
   *         false in the old state. So the only option for the
   *         implementer is to throw an exception when this occurs.
   * @post   new pb.hasSameValues(pb);
   * @post   new pb.hasSameId(pb);
   * @throws CompoundPropertyException
   *         pb.getWildExceptions();
   *         The operation was stopped for a semantic reason (! pb.isCivilized()).
   *         The CompoundPropertyException thrown will be closed.
   * @throws TechnicalException
   *         ; could not perform the operation
   * @throws TechnicalException
   *         ! isInTransaction()
   *            || pb == null
   *            || pb.getId() == null;
   *
   * @idea (jand) security, unmodifiable exceptions
   */
  void updatePersistentBean(final PersistentBean pb)
      throws CompoundPropertyException, TechnicalException;

  /**
   * <p>Take a persistent bean instance <code>pb</code> that exists
   *   in memory and represents an existing record in the persistent
   *   storage, and remove that record from persistent storage.</p>
   * <p>The state of <code>pb</code> remains completely unchanged,
   *   apart from normalization
   *   (<code>pb.hasSameValues(new pb)</code>).</p>
   * <p>The state of <code>pb</code> remains
   *   unchanged, including the <code>id</code> (the
   *   <code>id</code> cannot change during a transaction).
   *   The <code>id</code> will be set to <code>null</code>
   *   on {@link #commitTransaction(PersistentBean)}. Also, there
   *   is no normalization.</p>
   *
   * @post   isInTransaction();
   *         This cannot be made true by this method when it is
   *         false in the old state. So the only option for the
   *         implementer is to throw an exception when this occurs.
   * @post   pb != null;
   *         This cannot be made true by this method when it is
   *         false in the old state. So the only option for the
   *         implementer is to throw an exception when this occurs.
   * @post   pb.getId() != null;
   *         This cannot be made true by this method when it is
   *         false in the old state. So the only option for the
   *         implementer is to throw an exception when this occurs.
   * @post   new.isDeleted(pb);
   * @post   new pb.hasSameValues(pb);
   * @post   new pb.hasSameId(pb);
   * @throws TechnicalException
   *         ; could not perform the operation
   * @throws TechnicalException
   *         ! isInTransaction()
   *            || pb == null
   *            || pb.getId() == null;
   *
   * @idea (jand) security, unmodifiable exceptions
   */
  void deletePersistentBean(final PersistentBean pb) throws TechnicalException;


  /**
   * @basic
   */
  boolean isDeleted(final PersistentBean pb);

}

