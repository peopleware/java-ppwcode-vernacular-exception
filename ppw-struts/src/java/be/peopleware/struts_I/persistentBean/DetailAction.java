package be.peopleware.struts_I.persistentBean;


import java.util.Arrays;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import be.peopleware.bean_I.BeanInstantiationException;
import be.peopleware.bean_I.CompoundPropertyException;
import be.peopleware.bean_I.persistent.AsynchronousCRUD;
import be.peopleware.bean_I.persistent.IdException;
import be.peopleware.bean_I.persistent.PersistentBean;
import be.peopleware.exception_I.TechnicalException;
import be.peopleware.struts_I.IllegalAccessException;
import be.peopleware.struts_I.persistentBean.event.CommittedEvent;
import be.peopleware.struts_I.persistentBean.event.CommittedEventListener;
import be.peopleware.struts_I.persistentBean.event.CreatedEvent;
import be.peopleware.struts_I.persistentBean.event.DeletedEvent;
import be.peopleware.struts_I.persistentBean.event.UpdatedEvent;


/**
 * Base class for CRUD Action functionality.
 *
 * @author    David Van Keer
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 */
public abstract class DetailAction extends CrudAction {

  /*<section name="Meta Information">*/
  //------------------------------------------------------------------

  /** {@value} */
  public static final String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_TAG = "$Name$"; //$NON-NLS-1$

  /*</section>*/

  private static final Log LOG
      = LogFactory.getLog(DetailAction.class);


  /*<property name="requestMode">*/
  //------------------------------------------------------------------

  /**
   * A prefix for standard attributes, defined in this type,
   * to be stored in request scope, to make it unlikely
   * that the name is used in a form by naive JSP developers.
   *
   * <p><strong>=</strong> {@value}</p>
   */
  public static final String REQUEST_ATTRIBUTE_PREFIX
      = "be.peopleware.struts_I.DetailAction."; //$NON-NLS-1$

  /**
   * A prefix for request mode parameter names to make it unlikely
   * that the name is used in a form by naive JSP developers.
   *
   * <p><strong>=</strong> {@value}</p>
   */
  public static final String REQUEST_MODE_PREFIX
      = REQUEST_ATTRIBUTE_PREFIX + "REQUEST_MODE."; //$NON-NLS-1$

  /**
   * The parameter value for the mode parameter that asks
   * <samp>display request mode</samp> of this detail action.
   * Display request mode means that the user requested the
   * application to display an object.
   * In display request mode, data is retrieved from persistent
   * storage and simply displayed in a non-editable way.
   * The request should provide an {@link PersistentBean#getId()}.
   *
   * <p><strong>=</strong> {@value}</p>
   */
  public static final String REQUEST_MODE_DISPLAY
      = REQUEST_MODE_PREFIX + "display"; //$NON-NLS-1$

  /**
   * The parameter value for the mode parameter that asks
   * <samp>retrieve/edit request mode</samp> of this detail action.
   * Edit request mode means that the user request the application
   * to display an existing object for editing.
   * In edit request mode, data is retrieved from persistent
   * storage and displayed in an HTML form, so that it can be edited.
   * The request should provide an {@link PersistentBean#getId()}.
   *
   * <p><strong>=</strong> {@value}</p>
   */
  public static final String REQUEST_MODE_EDIT
      = REQUEST_MODE_PREFIX + "edit"; //$NON-NLS-1$

  /**
   * The parameter value for the mode parameter that asks
   * <samp>update request mode</samp> of this detail action.
   * Update request mode means that the user requests the
   * application to update existing information with supplied
   * information.
   * In update request mode, data is stored in persistent
   * storage. The request should provide data for all
   * properties of the {@link PersistentBean}.
   * If the update operation succeeds, the data is
   * imply displayed in a non-editable way.
   * If the update operation fails, the data is
   * displayed in an HTML form, so that it can be corrected.
   *
   * <p><strong>=</strong> {@value}</p>
   */
  public static final String REQUEST_MODE_UPDATE
      = REQUEST_MODE_PREFIX + "update"; //$NON-NLS-1$

  /**
   * The parameter value for the mode parameter that asks
   * <samp>new request mode</samp> of this detail action.
   * New request mode means that the user requests the
   * application to present a form with default information,
   * to be able to request the application to create a
   * new object in the next step.
   * In new request mode, a new persistent bean is
   * created with default data and displayed in an HTML
   * form, so that it can be edited.
   *
   * <p><strong>=</strong> {@value}</p>
   */
  public static final String REQUEST_MODE_NEW
      = REQUEST_MODE_PREFIX + "new"; //$NON-NLS-1$

  /**
   * The parameter value for the mode parameter that asks
   * <samp>cancel new request mode</samp> of this detail action.
   * This is intended to bring the user back to where he
   * requested the <samp>new request mode</samp> in the
   * first place.
   * In cancel new request mode, we do nothing but forward
   * the request.
   *
   * <p><strong>=</strong> {@value}</p>
   */
  public static final String REQUEST_MODE_CANCEL_NEW
      = REQUEST_MODE_PREFIX + "cancelNew"; //$NON-NLS-1$

  /**
   * The parameter value for the mode parameter that asks
   * <samp>create request mode</samp> of this detail action.
   * Create request mode means that the user requests the
   * application to create a new persistent bean
   * with supplied information..
   * In create request mode, new data is stored in
   * persistent storage. The request should provide data
   * for all properties of the {@link PersistentBean},
   * except the {@link PersistentBean#getId()}.
   * If the create operation succeeds, the data is
   * simply displayed in a non-editable way.
   * If the create operation fails, the data is
   * displayed in an HTML form, so that it can be corrected.
   *
   * <p><strong>=</strong> {@value}</p>
   */
  public static final String REQUEST_MODE_CREATE
      = REQUEST_MODE_PREFIX + "create"; //$NON-NLS-1$

  /**
   * The parameter value for the mode parameter that asks
   * <samp>delete request mode</samp> of this detail action.
   * Delete request mode means that the user requests the
   * application to delete an existing object.
   * In delete request mode, existing data is deleted
   * from persistent storage. The request should provide
   * data for the {@link PersistentBean#getId()}.
   * If the delete operation succeeds, the data is displayed
   * in a non-editable way, with a notice that signifies
   * successful deletion.
   * If the delete operation fails, the data is displayed
   * in a non-editable way, with an error notice.
   *
   * <p><strong>=</strong> {@value}</p>
   */
  public static final String REQUEST_MODE_DELETE
      = REQUEST_MODE_PREFIX + "delete"; //$NON-NLS-1$

  // @idea (jand): Add MODE_SEARCH when we grow up

  /**
   * The valid mode values.
   *
   * @return    {REQUEST_MODE_CREATE,
   *             REQUEST_MODE_NEW,
   *             REQUEST_MODE_CANCEL_NEW,
   *             REQUEST_MODE_DISPLAY,
   *             REQUEST_MODE_EDIT,
   *             REQUEST_MODE_UPDATE,
   *             REQUEST_MODE_DELETE};
   */
  public static String[] getValidModes() {
    return MODES;
  }

  private static final String[] MODES = {REQUEST_MODE_CREATE,
                                         REQUEST_MODE_NEW,
                                         REQUEST_MODE_CANCEL_NEW,
                                         REQUEST_MODE_DISPLAY,
                                         REQUEST_MODE_EDIT,
                                         REQUEST_MODE_UPDATE,
                                         REQUEST_MODE_DELETE, };

  /**
   * Mode is a valid value to express a form mode.
   *
   * @param     mode
   *            The value to test.
   * @return    boolean
   *            <code>getValidModes().contains(mode);</code>
   */
  public static boolean isValidMode(final String mode) {
    return Arrays.asList(getValidModes()).contains(mode);
  }

  /**
   * Check if a parameter exists in requestScope and is not a empty string.
   *
   * @param     paramName
   *            The name of the parameter to check.
   * @param     request
   *            The HTTPServletRequest object to look into.
   * @return    boolean
   *            True if the parameter with name <code>paramName</code> is not
   *            null and empty string, false otherwise.
   */
  private boolean requestParamExistsAndIsNotEmpty(
      final String paramName,
      final HttpServletRequest request) {
    String param = request.getParameter(paramName);
    return ((param != null) && (!param.equals(""))); //$NON-NLS-1$
  }

  /*</property>*/


  public static final String FORWARD_NOTFOUND = "notFound"; //$NON-NLS-1$

  public static final String ILLEGAL_ACCESS = "accessDenied"; //$NON-NLS-1$

  public static final String REQUEST_ATTRIBUTE_KEY_NOTFOUND
      = REQUEST_ATTRIBUTE_PREFIX + "EXCEPTION"; //$NON-NLS-1$


  /**
   * @see       Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
   * @param     actionMapping
   *            The ActionMapping used to select this instance
   * @param     actionForm
   *            The optional ActionForm bean for this request (if any)
   * @param     request
   *            The request we are processing
   * @param     response
   *            The response we are creating
   * @pre       actionForm instanceof CrudDynaActionForm;
   * @pre       ; actionMapping contains forward with name "success"
   * @throws    TechnicalException
   *            A fatal error from which recovery is not possible.
   */
  public final ActionForward execute(final ActionMapping actionMapping,
                                     final ActionForm actionForm,
                                     final HttpServletRequest request,
                                     final HttpServletResponse response)
      throws TechnicalException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("start action request processing ..."); //$NON-NLS-1$
    }
    assert actionForm instanceof CrudDynaActionForm;
    CrudDynaActionForm form
        = ((CrudDynaActionForm)actionForm);
    ActionForward forward;
    if (requestParamExistsAndIsNotEmpty(REQUEST_MODE_CANCEL_NEW, request)) {
      String referer = request.getParameter("referer"); //$NON-NLS-1$
      referer = referer.replaceAll("https?://.*/.*/", "/"); //$NON-NLS-1$ //$NON-NLS-2$
      forward = new ActionForward(referer);
    }
    else {
      try {
        if (requestParamExistsAndIsNotEmpty(REQUEST_MODE_DISPLAY,
                                            request)) {
          if (getSecurityStrategy() != null) {
            getSecurityStrategy().checkDisplayRigths(request,
                                                     actionMapping,
                                                     actionForm);
          }
          templateRetrieve(form, false, request);
        }
        if (requestParamExistsAndIsNotEmpty(REQUEST_MODE_EDIT,
                                            request)) {
          if (getSecurityStrategy() != null) {
            getSecurityStrategy().checkEditRights(request,
                                                  actionMapping,
                                                  actionForm);
          }
          templateRetrieve(form, true, request);
        }
        else if (requestParamExistsAndIsNotEmpty(REQUEST_MODE_NEW,
                                                 request)) {
          if (getSecurityStrategy() != null) {
            getSecurityStrategy().checkNewRights(request,
                                                 actionMapping,
                                                 actionForm);
          }
          templateNew(form, request);
        }
        else if (requestParamExistsAndIsNotEmpty(REQUEST_MODE_UPDATE,
                                                 request)) {
          if (getSecurityStrategy() != null) {
            getSecurityStrategy().checkUpdateRights(request,
                                                    actionMapping,
                                                    actionForm);
          }
          templateUpdate(form, request); // return to edit mode on errors
        }
        else if (requestParamExistsAndIsNotEmpty(REQUEST_MODE_CREATE,
                                                 request)) {
          if (getSecurityStrategy() != null) {
            getSecurityStrategy().checkCreateRights(request,
                                                    actionMapping,
                                                    actionForm);
          }
          templateCreate(form, request); // return to edit mode on errors
        }
        else if (requestParamExistsAndIsNotEmpty(REQUEST_MODE_DELETE,
                                                 request)) {
          if (getSecurityStrategy() != null) {
            getSecurityStrategy().checkDeleteRights(request,
                                                    actionMapping,
                                                    actionForm);
          }
          templateDelete(form, request);
        }
        else {
          if (LOG.isDebugEnabled()) {
            LOG.debug("no dispatch parameter match foud; " //$NON-NLS-1$
                      + "doing default display retrieve"); //$NON-NLS-1$
          }
          if (getSecurityStrategy() != null) {
            getSecurityStrategy().checkDisplayRigths(request,
                                                     actionMapping,
                                                     actionForm);
          }
          templateRetrieve(form, false, request);
        }
        forward = actionMapping.getInputForward();
        form.releaseBean();
      }
      catch (IllegalAccessException iaExc) {
        forward = actionMapping.findForward(ILLEGAL_ACCESS);
      }
      catch (IdException idExc) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("id exception", idExc); //$NON-NLS-1$
        }
        request.setAttribute(REQUEST_ATTRIBUTE_KEY_NOTFOUND, idExc);
        forward = actionMapping.findForward(FORWARD_NOTFOUND);
      }
      catch (CompoundPropertyException cpExc) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("property exception", cpExc); //$NON-NLS-1$
        }
        assert cpExc.isClosed();
        form.setCompoundPropertyException(cpExc);
        forward = actionMapping.getInputForward();
        form.releaseBean();
      }
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("action request processing completed; forward = " //$NON-NLS-1$
                + forward.toString());
    }
    return forward;
  }

  /**
   * Display request mode means that the user requested the
   * application to display an object.
   * In display request mode, data is retrieved from persistent
   * storage and simply displayed in a non-editable way.
   * The request should provide an {@link PersistentBean#getId()}.
   * Edit request mode means that the user request the application
   * to display an existing object for editing.
   * In edit request mode, data is retrieved from persistent
   * storage and displayed in an HTML form, so that it can be edited.
   * The request should provide an {@link PersistentBean#getId()}.
   *
   * @pre       form.getPersistentBean() != null;
   * @pre       request != null;
   */
   private void templateRetrieve(final CrudDynaActionForm form,
                                 final boolean viewModeEdit,
                                 final HttpServletRequest request)
       throws IdException, TechnicalException {
     assert request != null;
     if (LOG.isDebugEnabled()) {
       LOG.debug("retrieve for " //$NON-NLS-1$
                 + (viewModeEdit
                       ? "edit" //$NON-NLS-1$
                       : "display") //$NON-NLS-1$
                 + " ..."); //$NON-NLS-1$
     }
     AsynchronousCRUD asyncCRUD = createAsynchronousCRUD(request);
     try {
       // we are not doing this in a transaction, deliberately
       retrieveWithId(form, asyncCRUD); // IdException
       if (LOG.isDebugEnabled()) {
         LOG.debug("retrieve action succeeded"); //$NON-NLS-1$
       }
       form.beanToForm(asyncCRUD);
         /* We are not doing this in a transaction, deliberately.
          * This means data that is shown could be out of sync;
          * but chances are this will hardly occur, and if
          * it does, errors will be caught when something is
          * really done.
          * @todo (jand): think more about this
          */
         /* on IdException, we go to a different page
          * that shows a not found message. For this page we do not need to set
          * any information
          */
     }
     finally {
       releaseAsynchronousCRUD(asyncCRUD);
     }
     form.setViewModeEdit(viewModeEdit);
     form.setViewModeDeleted(false);
     form.setViewModeNew(false);
   }

   /**
    * Retrieve the persistent bean with id <code>form.getIdFromForm()</code>
    * from the persistent storage, and set it in the form.
    * This method does not deal with transactions. A transaction needs
    * to be started and closed around a call to this method.
    *
    * @pre      form != null;
    * @pre      asyncCRUD != null;
    */
   private void retrieveWithId(final CrudDynaActionForm form,
                               final AsynchronousCRUD asyncCRUD)
       throws IdException, TechnicalException {
     assert form != null;
     assert asyncCRUD != null;
     Long id = form.getIdFromForm(); // IdException
     Class type = form.getPersistentBeanType();
     if (id == null) {
       throw new IdException("ID_NULL", null, type); //$NON-NLS-1$
     }
     if (LOG.isDebugEnabled()) {
       LOG.debug("retrieving persistent bean with id " //$NON-NLS-1$
                 + id.toString()
                 + " and type " //$NON-NLS-1$
                 + type.getName()
                 + "..."); //$NON-NLS-1$
     }
     form.setPersistentBean(
        asyncCRUD.retrievePersistentBean(id, type));
               // pre id != null; IdNotFoundException
     if (LOG.isDebugEnabled()) {
      // if makes that there really is lazy loading if not in debug
       LOG.debug("retrieved persistent bean is " //$NON-NLS-1$
                  + form.getPersistentBean());
     }
     assert form.getPersistentBean() != null;
     assert form.getPersistentBean().getId().toString()
             .equals(form.get(CrudDynaActionForm.ID_PROPERTY_NAME));
   }

  /**
   * Update request mode means that the user requests the
   * application to update existing information with supplied
   * information.
   * In update request mode, data is stored in persistent
   * storage. The request should provide data for all
   * properties of the {@link PersistentBean}.
   * If the update operation succeeds, the data is
   * imply displayed in a non-editable way.
   * If the update operation fails, the data is
   * displayed in an HTML form, so that it can be corrected.
   * This method first sets the id of the persistent bean
   * from the request. Next, the persistent bean is retrieved
   * from the database. Only after that, the information
   * from the request is filled out into the bean. This is
   * done to have an early warning of errors in the key,
   * or access violations, but mainly to make it possible to
   * operate easily with a request that contains only partial
   * information, i.e., a request that only carries values
   * for a subset of the properties of the persistent bean.
   * With this protocol, the given information will overwrite
   * information that is currently in persistent storage,
   * but if there is no data in the request for a given property,
   * it's old value is retained. Once the data that is offered
   * in the request is copied successfully in the persistent
   * bean, it is stored.
   *
   * @pre       form != null;
   * @pre       form.getPersistentBean() != null;
   * @pre       request != null;
   */
  private void templateUpdate(final CrudDynaActionForm form,
                              final HttpServletRequest request)
      throws IdException,
             CompoundPropertyException,
             TechnicalException {
    assert form != null;
    assert request != null;
    if (LOG.isDebugEnabled()) {
      LOG.debug("update action ..."); //$NON-NLS-1$
    }
    AsynchronousCRUD asyncCRUD =  createAsynchronousCRUD(request);
    try {
      asyncCRUD.startTransaction();
      retrieveWithId(form, asyncCRUD); // IdException, TechnicalException
      assert form.getPersistentBean() != null;
// @mudo (dvankeer): toString() needs to be replaced by something more usefull.
      String oldBeanString = form.getPersistentBean().toString();
      form.formToBean(asyncCRUD); // PropertyException, inside transaction
      asyncCRUD.updatePersistentBean(form.getPersistentBean()); // PropertyException
      form.beanToForm(asyncCRUD); // inside transaction
      asyncCRUD.commitTransaction(form.getPersistentBean());
      releaseAsynchronousCRUD(asyncCRUD);
      // do not do this in a finally block; this can throw exceptions too
      form.setViewModeEdit(false);
// @todo (jand): try to move this delta stuff to AsynchronousCRUD instead of here
      fireCommittedEvent(new UpdatedEvent(form.getPersistentBean(), oldBeanString));
    }
    catch (CompoundPropertyException cpExc) {
      form.beanToForm(asyncCRUD);
      if (LOG.isDebugEnabled()) {
        LOG.debug("update action failed; cancelling ...", cpExc); //$NON-NLS-1$
      }
      asyncCRUD.cancelTransaction();
      releaseAsynchronousCRUD(asyncCRUD);
      // do not do this in a finally block; this can throw exceptions too
      form.setViewModeEdit(true);
      LOG.debug("update action cancelled; rethrowing exeption"); //$NON-NLS-1$
      throw cpExc;
    }
    /* on IdException, we go to a different page
     * that shows a not found message. For this page we do not need to set
     * any information
     */
    form.setViewModeDeleted(false);
    form.setViewModeNew(false);
  }

  /**
   * New request mode means that the user requests the
   * application to present a form with default information,
   * to be able to request the application to create a
   * new object in the next step.
   * In new request mode, a new persistent bean is
   * created with default data and displayed in an HTML
   * form, so that it can be edited.
   * All the data that is already in this request is used
   * to display the new entity the first time.
   *
   * @throws    BeanInstantiationException
   *            Could not create a new bean of the expected type.
   * @throws    TechnicalException
   *            Could evaluate data for a technical reason
   * @throws    CompoundPropertyException
   *            Some of the provided data is wrong.
   */
  private void templateNew(final CrudDynaActionForm form,
                           final HttpServletRequest request)
      throws BeanInstantiationException,
             TechnicalException,
             CompoundPropertyException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("new action ..."); //$NON-NLS-1$
    }
    form.initBean(); // bean contains programmatic defaults
    /* BeanInstantiationException; this means that the app is wrongly
     * configured; this will generate a general error page.
     */
    assert form.getPersistentBean() != null;
    assert form.getPersistentBean().getId() == null;
    form.beanToId();
    AsynchronousCRUD asyncCRUD =  createAsynchronousCRUD(request);
    CompoundPropertyException cpExcStore = null;
    try {
// @todo (dvankeer): Why re-use the data?
//      try {
//        form.formToBean(asyncCRUD);
//           get the data that is already in this request,
//           * overwriting programmatic defaults
//           
//      }
//      catch (CompoundPropertyException cpExc) {
//        cpExcStore = cpExc;
//        // remember this exception
//      }
      // we are not doing this in a transaction, deliberately
      // @todo (jand): is this a good idea?
      form.beanToForm(asyncCRUD);
        // copy default data and data already set into the form
    }
    finally {
     releaseAsynchronousCRUD(asyncCRUD);
    }
    form.setViewModeNew(true);
    assert form.isViewModeEdit();
    assert !form.isViewModeDeleted();
    if (LOG.isDebugEnabled()) {
      LOG.debug("new action succeeded"); //$NON-NLS-1$
    }
    if (cpExcStore != null) {
      throw cpExcStore;
    }
  }

  /**
   * Create request mode means that the user requests the
   * application to create a new persistent bean
   * with supplied information..
   * In create request mode, new data is stored in
   * persistent storage. The request should provide data
   * for all properties of the {@link PersistentBean},
   * except the {@link PersistentBean#getId()}.
   * If the create operation succeeds, the data is
   * simply displayed in a non-editable way.
   * If the create operation fails, the data is
   * displayed in an HTML form, so that it can be corrected.
   *
   * @pre       form != null;
   * @pre       form.getPersistentBean() != null;
   * @pre       request != null;
   */
  private void templateCreate(final CrudDynaActionForm form,
                              final HttpServletRequest request)
      throws CompoundPropertyException, TechnicalException {
    assert form != null;
    assert request != null;
    if (LOG.isDebugEnabled()) {
      LOG.debug("create action ..."); //$NON-NLS-1$
    }
    form.initBean();
    /* BeanInstantiationException; this means that the app is wrongly
     * configured; this will generate a general error page.
     */
    assert form.getPersistentBean() != null;
    assert form.getPersistentBean().getId() == null;
    form.setViewModeDeleted(false);
    AsynchronousCRUD asyncCRUD =  createAsynchronousCRUD(request);
    try {
      asyncCRUD.startTransaction();
      form.formToBean(asyncCRUD); // PropertyException, inside transaction
      form.getPersistentBean().setId(null);
      asyncCRUD.createPersistentBean(form.getPersistentBean());
              // PropertyException
      form.beanToId();
      form.beanToForm(asyncCRUD); // inside transaction
      asyncCRUD.commitTransaction(form.getPersistentBean());
      form.setViewModeEdit(false);
      CreatedEvent pbnEvent
          = new CreatedEvent(form.getPersistentBean());
      fireCommittedEvent(pbnEvent);
      assert !form.isViewModeNew();
      assert !form.isViewModeDeleted();
      if (LOG.isDebugEnabled()) {
        LOG.debug("create action succeeded"); //$NON-NLS-1$
      }
    }
    catch (CompoundPropertyException cpExc) {
      form.beanToForm(asyncCRUD);
      asyncCRUD.cancelTransaction();
      form.setViewModeNew(true);
      assert form.isViewModeEdit();
      assert !form.isViewModeDeleted();
      throw cpExc;
    }
    finally {
      releaseAsynchronousCRUD(asyncCRUD);
    }
  }

  /**
   * Delete request mode means that the user requests the
   * application to delete an existing bean.
   * In delete request mode, existing data is deleted
   * from persistent storage. The request should provide
   * data for the {@link PersistentBean#getId()}.
   * If the delete operation succeeds, the data is displayed
   * in a non-editable way, with a notice that signifies
   * successful deletion.
   * If the delete operation fails, the data is displayed
   * in a non-editable way, with an error notice.
   * This method first sets the id of the persistent bean
   * from the request. Next, the persistent bean is retrieved
   * from the database. Only after that, the persistent bean
   * will be deleted. This is
   * done to have an early warning of errors in the key,
   * or access violations.
   *
   * @idea (jand) This would also make it possible to check here whether
   *              all information in the request matches the information
   *              in persistent storage during this transaction.
   *
   * @pre       form != null;
   * @pre       form.getPersistentBean() != null;
   * @pre       request != null;
   */
  private void templateDelete(final CrudDynaActionForm form,
                              final HttpServletRequest request)
      throws CompoundPropertyException,
             IdException,
             TechnicalException {
    assert form != null;
    // assert form.getPersistentBean() != null;
    assert request != null;
    if (LOG.isDebugEnabled()) {
      LOG.debug("delete action ..."); //$NON-NLS-1$
    }
    form.setViewModeEdit(false);
    AsynchronousCRUD asyncCRUD =  createAsynchronousCRUD(request);
    DeletedEvent pbdEvent = null;
    try {
      asyncCRUD.startTransaction();
      retrieveWithId(form, asyncCRUD); // IdException
      assert form.getPersistentBean() != null;
      pbdEvent = new DeletedEvent(form.getPersistentBean());
      asyncCRUD.deletePersistentBean(form.getPersistentBean());
      asyncCRUD.commitTransaction(form.getPersistentBean());
      assert form.getPersistentBean().getId() == null;
      form.beanToId(); // must be null
      form.setViewModeDeleted(true); //rae.isEmpty());
      if (LOG.isDebugEnabled()) {
        LOG.debug("delete action succeeded"); //$NON-NLS-1$
      }
    }
    catch (CompoundPropertyException cpExc) {
      asyncCRUD.cancelTransaction();
      form.setViewModeDeleted(false);
      throw cpExc;
    }
    catch (IdException pkvExc) {
      asyncCRUD.cancelTransaction();
      form.setViewModeDeleted(false);
      throw pkvExc;
    }
    finally {
      form.beanToForm(asyncCRUD);
      releaseAsynchronousCRUD(asyncCRUD);
    }
    fireCommittedEvent(pbdEvent);
    assert !form.isViewModeNew();
    assert !form.isViewModeEdit();
  }

  /**
   * Remove a CommittedEventListener from the notification List.
   *
   * @param     listener
   *            The listeren to remove.
   */
  public synchronized void removePersistentBeanEventListener(
      final CommittedEventListener listener) {
    if ($persistentBeanChangeListeners.contains(listener)) {
      $persistentBeanChangeListeners.removeElement(listener);
    }
  }

  /**
   * Add a CommittedEventListener to the notification List.
   *
   * @param     listener
   *            The listeren to add.
   */
  public synchronized void addPersistentBeanEventListener(
      final CommittedEventListener listener) {
    if (!$persistentBeanChangeListeners.contains(listener)) {
      $persistentBeanChangeListeners.addElement(listener);
    }
  }

  /**
   * Notify the listeners of a change.
   * @param     pbEvent
   *            The CommittedEvent containing the date about the change.
   */
  private void fireCommittedEvent(final CommittedEvent pbEvent) {
    int count = $persistentBeanChangeListeners.size();
    if (count > 0) {
      for (int i = 0; i < count; i++) {
        ((CommittedEventListener)$persistentBeanChangeListeners
            .elementAt(i)).committed(pbEvent);
      }
    }
  }

  private Vector $persistentBeanChangeListeners = new Vector(5, 2);

}
