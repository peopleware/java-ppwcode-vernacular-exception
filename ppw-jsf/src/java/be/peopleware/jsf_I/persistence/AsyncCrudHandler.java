package be.peopleware.jsf_I.persistence;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.exception_I.TechnicalException;
import be.peopleware.persistence_I.IdException;
import be.peopleware.persistence_I.PersistentBean;
import be.peopleware.persistence_I.dao.AsyncCrudDao;


/**
 * @mudo (jand) doc
 * This class requires the definition of 2 filters in <kbd>WEB-INF/web.xml</kbd>:
 * <pre>
 * <listener>
 *   <listener-class>be.peopleware.servlet_I.hibernate.SessionFactoryController</listener-class>
 *   <listener-class>be.peopleware.servlet_I.hibernate.SessionInView</listener-class>
 * </listener>
 * </pre>
 * 
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 * 
 * @invar getDao() instanceof AsyncCrudDao
 * 
 * @mudo (jand) gather viewmode in separate class
 */
public class AsyncCrudHandler extends AbstractHandler {

  /*<section name="Meta Information">*/
  //------------------------------------------------------------------
  /** {@value} */
  public static final String CVS_REVISION = "$Revision$";
  /** {@value} */
  public static final String CVS_DATE = "$Date$";
  /** {@value} */
  public static final String CVS_STATE = "$State$";
  /** {@value} */
  public static final String CVS_TAG = "$Name$";
  /*</section>*/

  
  
  private static final Log LOG = LogFactory.getLog(AsyncCrudHandler.class);

  
  public final AsyncCrudDao getAsyncCrudDao() {
    return (AsyncCrudDao)getDao();
  }
 
  
  
  /*<property name="type">*/
  //------------------------------------------------------------------

  /**
   * The type of the {@link PersistentBean} that will be handled
   * by the requests.
   * 
   * @basic
   * @init null;
   */
  public final Class getType() {
    return $type;
  }
  
  /**
   * Set the type of the {@link PersistentBean} that will be handled
   * by the requests.
   * 
   * @post getType() == type;
   */
  public final void setType(Class type) {
    $type = type;
  }
  
  /**
   * The type of the {@link PersistentBean} that will be handled
   * by the requests.
   */
  private Class $type;
  
  /*</property>*/

  public final void setTypeName(String typename) {
  	try {
  		setType (Class.forName(typename));
  	} catch (ClassNotFoundException e) {
  		// TODO Auto-generated catch block
  		e.printStackTrace();
  	}
  }
  
  /*<property name="id">*/
  //------------------------------------------------------------------

  /**
   * The id of the {@link PersistentBean} that will be handled
   * by the requests.
   * 
   * @basic
   * @init null;
   */
  public final Long getId() {
    return $id;
  }
  
  /**
   * Set the id of the {@link PersistentBean} that will be handled
   * by the requests.
   * 
   * @post getId().equals(id);
   */
  public final void setId(Long id) {
    $id = id;
  }
  
  /**
   * The id of the {@link PersistentBean} that will be handled
   * by the requests.
   */
  private Long $id;

  /*</property>*/


  
  /*<property name="persistentBean">*/
  //------------------------------------------------------------------

  /**
   * The {@link PersistentBean} that is the result of the request.
   * 
   * @basic
   * @init null;
   */
  public final PersistentBean getPersistentBean() {
    return $persistentBean;
  }
  
  /**
   * The {@link PersistentBean} that is the result of the request.
   */
  private PersistentBean $persistentBean;
  
  /*</property>*/

  
  
  /*<property name="viewModeEdit">*/
  //------------------------------------------------------------------

  /**
   * Show the form editable. If false, show the form
   * read-only.
   *
   * @basic
   * @init      false;
   */
  public final boolean isViewModeEdit() {
    return $viewModeEdit;
  }

  /**
   * @param     editMode
   *            Sets this form in edit mode if <code>editMode</code> is true.
   * @post      new.isViewModeEdit() == editMode;
   * @post      editMode ==> ! new.isViewModeDeleted();
   * @post      ! editMode ==> ! new.isViewModeNew();
   */
  private void setViewModeEdit(final boolean editMode) {
    $viewModeEdit = editMode;
    if (editMode) {
      $viewModeDeleted = false;
    }
    else {
      $viewModeNew = false;
    }
  }

  private boolean $viewModeEdit;

  /*</property>*/



  /*<property name="viewModeDeleted">*/
  //------------------------------------------------------------------

  /**
   * Show the form as deleted.
   *
   * @basic
   * @init      false;
   */
  public final boolean isViewModeDeleted() {
    return $viewModeDeleted;
  }

  /**
   * @param     deleted
   *            Marks te persistentBean as deleted if <code>deleted</code> is
   *            true.
   * @post      new.isViewModeDeleted() == deleted;
   * @post      deleted ==> ! new.isViewModeEdit();
   * @post      deleted ==> ! new.isViewModeNew();
   */
  private void setViewModeDeleted(final boolean deleted) {
    $viewModeDeleted = deleted;
    if (deleted) {
      $viewModeEdit = false;
      $viewModeNew = false;
    }
  }

  private boolean $viewModeDeleted;

  /*</property>*/



  /*<property name="viewModeNew">*/
  //------------------------------------------------------------------

  /**
   * Show the form as new, i.e., a form in which information can be
   * filled out to request an object creation.
   *
   * @basic
   * @init      false;
   */
  public final boolean isViewModeNew() {
    return $viewModeNew;
  }

  /**
   * @param     viewModeNew
   *            Marks the persistent bean as an object that still needs to
   *            be created if <code>viewModeNew</code> is true.
   * @post      new.isViewModeNew() == viewModeNew;
   * @post      viewModeNew ==> new.isViewModeEdit();
   * @post      viewModeNew ==> ! new.isViewModeDeleted();
   */
  private void setViewModeNew(final boolean viewModeNew) {
    $viewModeNew = viewModeNew;
    if (viewModeNew) {
      $viewModeEdit = true;
      $viewModeDeleted = false;
    }
  }

  private boolean $viewModeNew;

  /*</property>*/



  /*<property name="editable">*/
  //------------------------------------------------------------------

  /**
   * Can we edit the object represented by this form?
   *
   * @basic
   * @init      false;
   */
  public final boolean isEditable() {
    return $editable;
  }

  /**
   * @param     editable
   *            Marks the persistent bean as a object that can be edited.
   * @post      new.isEditable() == editable;
   */
  private void setEditable(final boolean editable) {
    $editable = editable;
  }

  private boolean $editable;

  /*</property>*/



  /*<property name="deleteable">*/
  //------------------------------------------------------------------

  /**
   * Can we delete the object represented by this form?.
   *
   * @basic
   * @init      false;
   */
  public final boolean isDeleteable() {
    return $deleteable;
  }

  /**
   * @param     editable
   *            Marks the persistent bean as a object that can be deleted.
   * @post      new.isDeleteable() == deleteable;
   */
  private void setDeleteable(final boolean deleteable) {
    $deleteable = deleteable;
  }

  private boolean $deleteable;

  /*</property>*/



  /**
   * Forward code for successful processing
   * <strong>= {@value}</strong>.
   */
  public final static String FORWARD_SUCCESS = "success"; 
  
  /**
   * Forward code when no instance of type {@link #getType()}
   * with {@link #getId()} was found
   * <strong>= {@value}</strong>.
   */
  public final static String FORWARD_NOT_FOUND = "id not found"; 
  
  /**
   * Retrieve the {@link PersistentBean} instance with
   * {@link PersistentBean#getId() id} {@link #getId()} of type
   * {@link #getType()}. If retrieval is successful, forward to
   * {@link #FORWARD_SUCCESS}. If no instance of type {@link #getType()}
   * with id {@link #getId()} was found, forward to {@link #FORWARD_NOT_FOUND}.
   * 
   * @return FORWARD_SUCCESS.equals(result) || FORWARD_NOT_FOUND.equals(result);
   * @throws TechnicalException
   *         ; Could not create an AsyncCrudDao
   * 
   * @mudo (jand) security
   */
  public final String display() throws TechnicalException {
    return retrieve(false);
  }
  
  /** 
   * @mudo (jand) security
   */
  public final String edit() throws TechnicalException {
    return retrieve(true);
  }
  
  public final String update() {
    // MUDO (jand) stub
    return FORWARD_SUCCESS;
  }
  
  public final String editNew() {
    // MUDO (jand) stub
    return FORWARD_SUCCESS;
  }
  
  public final String create() {
    // MUDO (jand) stub
    return FORWARD_SUCCESS;
  }
  
  public final String delete() {
    // MUDO (jand) stub
    return FORWARD_SUCCESS;
  }

  /**
   * Retrieve the {@link PersistentBean} instance with
   * {@link PersistentBean#getId() id} {@link #getId()} of type
   * {@link #getType()}. If retrieval is successful, forward to
   * {@link #FORWARD_SUCCESS}. If no instance of type {@link #getType()}
   * with id {@link #getId()} was found, forward to {@link #FORWARD_NOT_FOUND}.
   * 
   * @return FORWARD_SUCCESS.equals(result) || FORWARD_NOT_FOUND.equals(result);
   * @throws TechnicalException
   *         ; Could not create an AsyncCrudDao
   */
  private String retrieve(final boolean viewModeEdit) throws TechnicalException {
    // MUDO (jand) security
    AsyncCrudDao asyncCRUD = null;
    try {
      asyncCRUD = getAsyncCrudDao();
          // throws TechnicalExceptions
      retrieveWithId(asyncCRUD); // IdException
      LOG.debug("retrieve action succeeded");
      /*
       * We are not doing this in a transaction, deliberately. This means data
       * that is shown could be out of sync; but chances are this will hardly
       * occur, and if it does, errors will be caught when something is really
       * done.
       * TODO (jand): think more about this
       */
      setViewModeEdit(viewModeEdit);
      setViewModeDeleted(false);
      setViewModeNew(false);
      return FORWARD_SUCCESS;
    }
    catch (IdException idExc) {
      /*
       * on IdException, we go to a different page that shows a not found
       * message. For this page we do not need to set any information
       */
      return FORWARD_NOT_FOUND;
    }
  }

  /**
   * Retrieve the persistent bean with id <code>form.getIdFromForm()</code>
   * from the persistent storage, and set it in the form. This method does not
   * deal with transactions. A transaction needs to be started and closed around
   * a call to this method.
   *
   * @pre       asyncCRUD != null;
   */
  private void retrieveWithId(final AsyncCrudDao asyncCRUD)
      throws IdException, TechnicalException {
    try {
      assert asyncCRUD != null;
      Long id = getId();
      Class type = getType();
      //id or type are not known so passing the id to the exception is useless
      if (id == null) {
        throw new IdException("ID_NULL", null, type);
      }
      if (type == null) {
        throw new IdException("TYPE_NULL", null, type);
      }
      LOG.debug("retrieving persistent bean with id "
                  + id.toString() + " and type "
                  + type.getName() + "...");
      $persistentBean = asyncCRUD.retrievePersistentBean(id, type);
            // pre id != null; IdNotFoundException
      if (LOG.isDebugEnabled()) {
        // if makes that there really is lazy loading if not in debug
        LOG.debug("retrieved persistent bean is " + getPersistentBean());
      }
      assert getPersistentBean() != null;
      assert getPersistentBean().getId().equals(getId());
    }
    catch (IdException e) {
      LOG.info("exception during retrieveWithId", e);
      throw e;
    }
    catch (TechnicalException e) {
      LOG.info("exception during retrieveWithId", e);
      throw e;
    }
  }

}
