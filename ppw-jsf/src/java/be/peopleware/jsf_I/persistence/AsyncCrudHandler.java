package be.peopleware.jsf_I.persistence;


import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.bean_IV.CompoundPropertyException;
import be.peopleware.exception_I.TechnicalException;
import be.peopleware.persistence_I.IdException;
import be.peopleware.persistence_I.IdNotFoundException;
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
    LOG.debug("type of " + this + " set to Class " + type.getName()); 
  }
  
  /**
   * Set the type of the {@link PersistentBean} that will be handled
   * by the requests, as text.
   * 
   * @todo This method is here for the faces-config managed bean stuff. Apparently,
   *       Converters are not used during creation and property setting of managed
   *       beans. So, with the {@link #setType(Class)} method, we would be trying
   *       to push a String into a Class type parameter (actually, it seems, but
   *       we are not sure, that JSF attempts to <em>instantiate</em> the class
   *       for which the name is given, which is pretty weird). When this get
   *       solved, or when we no longer need this method there, this method should
   *       be removed.
   * 
   * @post getType() == Class.forName(type);
   */
  public final void setTypeAsString(String typeName) throws FacesException {
    Class type;
    try {
      type = Class.forName(typeName);
    }
    catch (LinkageError e) {
      throw new FacesException("cannot convert String to Class", e);
    }
    catch (ClassNotFoundException e) {
      throw new FacesException("cannot convert String to Class", e);
    }
    $type = type;
    LOG.debug("type of " + this + " set to Class " + type.getName()); 
  }
  
  /**
   * The type of the {@link PersistentBean} that will be handled
   * by the requests.
   */
  private Class $type;
  
  /*</property>*/

  
  
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
    LOG.debug("id of " + this + " set to " + id);
  }
  
  /**
   * Retrieve the {@link PersistentBean} instance with
   * {@link PersistentBean#getId() id} {@link #getId()} of type
   * {@link #getType()}.
   * 
   * @post   (new.getPersistentBean() != null) ?
   *              getType().isInstance(new.getPersistentBean());
   * @post   (new.getPersistentBean() != null) ?
   *              new.getPersistentBean().getId().equals(getId());
   * @throws TechnicalException
   *         ; Could not create an AsyncCrudDao
   * @throws    IdException
   *            id == null;
   * @throws    IdException
   *            type == null;
   * @throws    TechnicalException tExc
   *            ; something technical went wrong, but surely
   *            ! (tExc instanceof IdNotFoundException)
   */
  public void idChanged(ValueChangeEvent vcEv) throws IdException, TechnicalException {
    // MUDO (jand) security
    LOG.debug("AsyncCrudHandler received ValueChangeEvent"
              + " (Id: " + getId() + "; Type: " + getType() + "): "
              + vcEv
              + "event.oldValue = " + vcEv.getOldValue()
              + "; event.newValue = " + vcEv.getNewValue());
    AsyncCrudDao asyncCRUD = null;
    asyncCRUD = getAsyncCrudDao(); // throws TechnicalExceptions
    retrieveWithId(asyncCRUD, (Long)vcEv.getNewValue(), getType()); // IdException
    LOG.debug("retrieve action finished; $persistentBean = " + $persistentBean);
    /*
     * We are not doing this in a transaction, deliberately. This means data
     * that is shown could be out of sync; but chances are this will hardly
     * occur, and if it does, errors will be caught when something is really
     * done.
     * TODO (jand): think more about this IT MIGHT BE NECESSARY TO RECONSIDER THIS FOR JSF
     */
    assert (getPersistentBean() != null)
              ? getType().isInstance(getPersistentBean()) : true;
//    assert (getPersistentBean() != null)
//              ? getPersistentBean().getId().equals(getId()) : true;
  }
  
  /**
   * Retrieve the {@link PersistentBean} instance with
   * {@link PersistentBean#getId() id} {@link #getId()} of type
   * {@link #getType()}.
   * 
   * @post   (new.getPersistentBean() != null) ?
   *              getType().isInstance(new.getPersistentBean());
   * @post   (new.getPersistentBean() != null) ?
   *              new.getPersistentBean().getId().equals(getId());
   * @throws TechnicalException
   *         ; Could not create an AsyncCrudDao
   * @throws    IdException
   *            id == null;
   * @throws    IdException
   *            type == null;
   * @throws    TechnicalException tExc
   *            ; something technical went wrong, but surely
   *            ! (tExc instanceof IdNotFoundException)
   */
  public void idClicked(ActionEvent aEv) throws IdException, TechnicalException {
    // MUDO (jand) security
    
    UIComponent component = aEv.getComponent();
    FacesContext context = FacesContext.getCurrentInstance();
    String idString = (String)component.getValueBinding("value").getValue(context);
    
    LOG.debug("AsyncCrudHandler received ActionEvent"
              + " (Id: " + getId() + "; Type: " + getType() + "): "
              + aEv
              + "event.value = " + idString);
    AsyncCrudDao asyncCRUD = null;
    asyncCRUD = getAsyncCrudDao(); // throws TechnicalExceptions
    retrieveWithId(asyncCRUD, new Long(idString), getType()); // IdException
    LOG.debug("retrieve action finished; $persistentBean = " + $persistentBean);
    /*
     * We are not doing this in a transaction, deliberately. This means data
     * that is shown could be out of sync; but chances are this will hardly
     * occur, and if it does, errors will be caught when something is really
     * done.
     * TODO (jand): think more about this IT MIGHT BE NECESSARY TO RECONSIDER THIS FOR JSF
     */
    assert (getPersistentBean() != null)
              ? getType().isInstance(getPersistentBean()) : true;
//    assert (getPersistentBean() != null)
//              ? getPersistentBean().getId().equals(getId()) : true;
  }
  
  /**
   * Retrieve the persistent bean of type {@link #getType()} with id {@link #getId()}
   * from the persistent storage, and set it in {@link #getPersistentBean()}.
   * 
   * If the necessary arguments and utilities are not set, exceptions are thrown.
   * 
   * If no bean of type {@link #getType()} with id {@link #getId()} is found in
   * persistent storage, {@link #getPersistentBean()} is forced to <code>null</code>.
   * 
   * This method does not deal with transactions. A transaction needs to be started
   * and closed around a call to this method.
   *
   * @pre       asyncCRUD != null;
   * @post      (new.getPersistentBean() != null)
   *                ? getId().equals(new.getPersistentBean().getId())
   *                    && getType().isInstance(new.getPersistentBean());
   * @throws    IdException
   *            id == null;
   * @throws    IdException
   *            type == null;
   * @throws    TechnicalException tExc
   *            ; something technical went wrong, but surely
   *            ! (tExc instanceof IdNotFoundException)
   */
  private void retrieveWithId(final AsyncCrudDao asyncCRUD, Long id, Class type)
      throws IdException, TechnicalException {
    try {
      assert asyncCRUD != null;
      //id or type are not known so passing the id to the exception is useless
      if (id == null) {
        LOG.error("id == null");
        throw new IdException("ID_NULL", null, type);
      }
      if (type == null) {
        LOG.error("type == null");
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
//      assert getPersistentBean().getId().equals(getId());
    }
    catch (IdNotFoundException e) {
      // this will force $persistentBean null
      LOG.info("could not find instance of type "
               + getType().getName()
               + " with id " + getId(), e);
      $persistentBean = null;
    }
    catch (TechnicalException e) {
      LOG.error("exception during retrieveWithId", e);
      throw e;
    }
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
   * Set boolean parameters for view mode DISPLAY.
   * We presume that {@link #idChanged(ValueChangeEvent)} was called before
   * this method is called (i.e., that an id-field on a JSF form was bound
   * to that method with <code>immediate</code> set).
   * 
   * If {@link #getPersistentBean()} <code>!= null</code>, forward to
   * {@link #FORWARD_SUCCESS}. If {@link #getPersistentBean()} <code>== null</code>,
   * forward to {@link #FORWARD_NOT_FOUND}.
   * 
   * @return FORWARD_SUCCESS.equals(result) || FORWARD_NOT_FOUND.equals(result);
   * 
   * @mudo (jand) security
   */
  public final String display() {
    LOG.debug("display called; showing bean");
    LOG.debug("persistentBean: " + getPersistentBean());
    if (getPersistentBean() == null) {
        // persistent bean not set by idChanged
      return FORWARD_NOT_FOUND;
    }
    else {
      setViewModeEdit(false);
      setViewModeDeleted(false);
      setViewModeNew(false);
      return FORWARD_SUCCESS;
    }
  }
  
  /** 
   * @mudo (jand) security
   */
  public final String edit() throws TechnicalException {
    LOG.debug("display called; showing bean for edit");
    if (getPersistentBean() == null) {
      // persistent bean not set by idChanged
      return FORWARD_NOT_FOUND;
    }
    else {
      setViewModeEdit(true);
      setViewModeDeleted(false);
      setViewModeNew(false);
      return FORWARD_SUCCESS;
    }
  }
  
  public final String update() throws TechnicalException {
    LOG.debug("update called; new versions of values should have been written to the bean");
    LOG.debug("persistentBean: " + getPersistentBean());
    if (getPersistentBean() == null) {
      // persistent bean not set by idChanged
      return FORWARD_NOT_FOUND;
    }
    else {
      // store in DB

      AsyncCrudDao asyncCRUD = null;
      try {
        asyncCRUD = getAsyncCrudDao(); // throws TechnicalExceptions
        asyncCRUD.startTransaction();
        asyncCRUD.updatePersistentBean(getPersistentBean()); // PropertyException
        asyncCRUD.commitTransaction(getPersistentBean());
        return display();
      }
      catch (CompoundPropertyException cpExc) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("update action failed; cancelling ...", cpExc); //$NON-NLS-1$
        }
        asyncCRUD.cancelTransaction();
        LOG.debug("update action cancelled; using exception as faces message"); //$NON-NLS-1$
        // MUDO use cpExc for faces message
        return edit();
      }
    }
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

//  /**
//   * Retrieve the {@link PersistentBean} instance with
//   * {@link PersistentBean#getId() id} {@link #getId()} of type
//   * {@link #getType()}. If retrieval is successful, forward to
//   * {@link #FORWARD_SUCCESS}. If no instance of type {@link #getType()}
//   * with id {@link #getId()} was found, forward to {@link #FORWARD_NOT_FOUND}.
//   * 
//   * @return FORWARD_SUCCESS.equals(result) || FORWARD_NOT_FOUND.equals(result);
//   * @throws TechnicalException
//   *         ; Could not create an AsyncCrudDao
//   */
//  private String retrieve(final boolean viewModeEdit) throws TechnicalException {
//    // MUDO (jand) security
//    AsyncCrudDao asyncCRUD = null;
//    try {
//      asyncCRUD = getAsyncCrudDao();
//          // throws TechnicalExceptions
//      retrieveWithId(asyncCRUD); // IdException
//      LOG.debug("retrieve action succeeded");
//      /*
//       * We are not doing this in a transaction, deliberately. This means data
//       * that is shown could be out of sync; but chances are this will hardly
//       * occur, and if it does, errors will be caught when something is really
//       * done.
//       * TODO (jand): think more about this
//       */
//      setViewModeEdit(viewModeEdit);
//      setViewModeDeleted(false);
//      setViewModeNew(false);
//      return FORWARD_SUCCESS;
//    }
//    catch (IdException idExc) {
//      /*
//       * on IdException, we go to a different page that shows a not found
//       * message. For this page we do not need to set any information
//       */
//      return FORWARD_NOT_FOUND;
//    }
//  }

}
