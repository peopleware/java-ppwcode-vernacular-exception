package be.peopleware.struts_I.persistentBean;


import java.beans.Beans;
import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import be.peopleware.bean_I.BeanInstantiationException;
import be.peopleware.bean_I.CompoundPropertyException;
import be.peopleware.bean_I.persistent.AsynchronousCRUD;
import be.peopleware.bean_I.persistent.IdException;
import be.peopleware.bean_I.persistent.IdFormatException;
import be.peopleware.bean_I.persistent.PersistentBean;
import be.peopleware.exception_I.TechnicalException;


/**
 * A Struts DynaActionForm that wraps around a {@link PersistentBean}.
 * All values stored in forms of this type are Strings.
 * Subtypes offer the functionality for a particular persistent bean
 * to fill the properties of the persistent bean from strings in the
 * form (parse), and to fill the form with strings from properties of
 * the persistent bean (format).
 * By the time the form reaches the JSP, the persistent bean is
 * already released.
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 *
 * @invar     getPersistentBeanType() != null;
 * @invar     getPersistentBeanType()
 *                .isAssignableFrom(PersistentBean.class);
 * @invar     (getPersistententBean() != null)
 *                ==> getPersistentBeanType()
 *                        .isInstance(getPersistententBean()));
 * @invar     (forall Object o; getMap().valueSet().contains(o);
 *               o instanceof String);
 * @invar     getMap().keySet().contains("id");
 * @invar     isViewModeDeleted() ==> ! isViewModeEdit() && ! isViewModeNew();
 * @invar     isViewModeNew() ==> isViewModeEdit() && ! isViewModeDeleted();
 * @invar     isViewModeEdit() ==> ! isViewModeDeleted();
 * @invar     ! isViewModeEdit() ==> ! isViewModeNew();
 */
public abstract class CrudDynaActionForm extends DynaActionForm {


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
      = LogFactory.getLog(CrudDynaActionForm.class);


  /*<construction>*/
  //------------------------------------------------------------------

  // default constructor

  /*</construction>*/



  /*<property name="persistentBean">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final PersistentBean getPersistentBean() {
    return $persistentBean;
  }

  /**
   * @param     persistentBean
   *            The new value for persistentBean.
   * @post      new.getPersistentBean() == persistentBean;
   * @throws    IllegalArgumentException
   *            !getPersistentBeanType().isInstance(persistentBean);
   */
  public final void setPersistentBean(final PersistentBean persistentBean)
      throws IllegalArgumentException {
    if (!getPersistentBeanType().isInstance(persistentBean)) {
      throw new IllegalArgumentException(persistentBean.toString()
                                         + " is not of type " //$NON-NLS-1$
                                         + getPersistentBeanType().getClass()
                                               .toString());
    }
    $persistentBean = persistentBean;
  }

  private PersistentBean $persistentBean;

  /*</property>*/



  /*<property name="persistentBeanType">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public Class getPersistentBeanType() {
    return PersistentBean.class;
  }

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
  final void setViewModeEdit(final boolean editMode) {
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



  /*<property name="deleted">*/
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
  final void setViewModeDeleted(final boolean deleted) {
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
  final void setViewModeNew(final boolean viewModeNew) {
    $viewModeNew = viewModeNew;
    if (viewModeNew) {
      $viewModeEdit = true;
      $viewModeDeleted = false;
    }
  }

  private boolean $viewModeNew;

  /*</property>*/

  /*<property name="viewModeEditable">*/
  //------------------------------------------------------------------

  /**
   * Mark the form as editable.
   *
   * @basic
   * @init      false;
   */
  public final boolean isViewModeEditable() {
    return $viewModeEditable;
  }

  /**
   * @param     viewModeEditable
   *            Marks the persistent bean as a object that can be set in
   *            edit mode.
   * @post      new.isViewModeEditable() == viewModeEditable;
   */
  final void setViewModeEditable(final boolean viewModeEditable) {
    $viewModeEditable = viewModeEditable;
  }

  private boolean $viewModeEditable;

  /*</property>*/



  /*<property name="locale">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Locale getLocale() {
    // @IDEA (jand): If we set a locale an not use the browser supplied we have
    //               to work on this method.
    // @IDEA (dvankeer): Locale selection can be stored in a cookie. In the
    //                   custom request processor we can detect if this cookie
    //                   exists or not and set the locale in the request
    //                   accordingly.

    // Hack for demo
    // return $request.getLocale();
    return new Locale("en_UK"); //$NON-NLS-1$
  }


  /*</property>*/



  /*<property name="propertyException">*/
  //------------------------------------------------------------------

  /**
   * An exception that occured during processing of
   * this request. The JSP developer can use
   * this information to give feedbsck about
   * failed requests.
   *
   * @basic
   * @init      null;
   */
  public final CompoundPropertyException getCompoundPropertyException() {
    return $compoundPropertyException;
  }

  /**
   * Convenience method for JSP developers.
   *
   * @return    getCompoundPropertyException() == null;
   */
  public final boolean isSuccessful() {
    return (getCompoundPropertyException() == null);
  }

  /**
   * @param     compoundPropertyException
   *            The new property exception, to be used by the JSP
   *            developers.
   * @post      new.getCompoundPropertyException == compoundPropertyException;
   */
  final void setCompoundPropertyException(
      final CompoundPropertyException compoundPropertyException) {
    $compoundPropertyException = compoundPropertyException;
  }

  private CompoundPropertyException $compoundPropertyException;

  /*</property>*/



  /**
   * Create a new persistent bean of {@link #getPersistentBeanType()},
   * and store it in the {@link #getPersistentBean() persistent bean
   * property}.
   *
   * @post      new.getPersistentBean() != null;
   * @post      new.getPersistentBean != getPersistentBean();
   * @post      (forall PersistentBean po;; po != new.getPersistentBean());
   *              The new persistent bean is a new object.
   * @throws BeanInstantiationException
   *         ; could not create the persistent bean
   */
  protected final void initBean() throws BeanInstantiationException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("initializing new PersistentBean of type " //$NON-NLS-1$
               + getPersistentBeanType().getName()
               + " ..."); //$NON-NLS-1$
    }
    try {
      setPersistentBean(
          (PersistentBean)Beans.instantiate(getClass().getClassLoader(),
                                              getPersistentBeanType().getName()));
      if (LOG.isDebugEnabled()) {
        LOG.debug("initialized new PersistentBean of type " //$NON-NLS-1$
                   + getPersistentBeanType().getName()); //$NON-NLS-1$
      }
    }
    catch (IOException ioExc) {
      throw new BeanInstantiationException(null,
                                           ioExc,
                                           getPersistentBeanType());
    }
    catch (ClassNotFoundException cnfExc) {
      throw new BeanInstantiationException(null,
                                           cnfExc,
                                           getPersistentBeanType());
    }
    catch (IllegalArgumentException iaExc) {
      throw new BeanInstantiationException(null,
                                           iaExc,
                                           getPersistentBeanType());
    }
    catch (ClassCastException ccExc) {
      assert false : "Cannot happen due to Type Invariants."; //$NON-NLS-1$
    }
  }

  /**
   * Release the {@link #getPersistentBean()}.
   *
   * @pre       getPersistentBean() != null;
   * @post      new.getPersistentBean() == null;
   */
  final void releaseBean() {
    assert getPersistentBean() != null;
    if (LOG.isDebugEnabled()) {
      // if makes that there really is lazy loading if not in debug
      LOG.debug("releasing PersistentBean " //$NON-NLS-1$
                 + getPersistentBean().toString()); //$NON-NLS-1$
    }
    $persistentBean = null;
  }

  /**
   * Fill this {@link org.apache.struts.action.DynaActionForm} with strings,
   * ready for display,
   * for each property of the persistent bean.
   *
   * @see       #initBean()
   * @see       #formToBean(AsynchronousCRUD)
   * @see       #getIdFromForm()
   *
   * @pre       getPersistentBean() != null;
   */
  protected abstract void beanToForm(final AsynchronousCRUD asyncCRUD)
      throws TechnicalException;

  /**
   * Set the properties of the {@link #getPersistentBean()}
   * from the strings information in this form. The
   * {@link PersistentBean#getId()} property is <strong>not</strong>
   * changed. To set the {@link PersistentBean#getId()}
   * property of the persistent bean, see {@link #getIdFromForm()}.
   * A given property of the persistent bean should only be set
   * if there actually is a value for the property in the request.
   * If there is no value in the request for a given property,
   * the existing value of the property should not be changed.
   * To make this clear, there should be an explicit <samp>no selection</samp>
   * for properties, where possible, to distinguish the user's choice
   * of <samp>no selection</samp> from <samp>no change</samp>.
   * This is. e.g., possible, when selecting a related object
   * in a -to-one association.
   *
   * @see       #initBean()
   * @see       #beanToForm(AsynchronousCRUD)
   * @see       #getIdFromForm()
   *
   * @param     asyncCRUD
   *            An asynchronous CRUD instance, potentially in a transaction,
   *            to be used to retrieve data from the database for filling
   *            the bean
   * @pre       asyncCRUD != null;
   * @pre       getPersistentBean() != null;
   * @post      new.getPersistentBean().getId()
   *                .equals(getPersistentBean().getId());
   * @throws    CompoundPropertyException
   *            All collection of all exceptions which occured converstion from
   *            form to a persistent bean .
   * @throws    TechnicalException
   *            A fatal error which cannot be recovered from.
   */
  protected abstract void formToBean(final AsynchronousCRUD asyncCRUD)
      throws CompoundPropertyException, TechnicalException;

  /**
   * <strong>= {@value}</strong>
   */
  static final String ID_PROPERTY_NAME = "id"; //$NON-NLS-1$

  private static final String EMPTY = ""; //$NON-NLS-1$

  /**
   * Set the {@link PersistentBean#getId()} of the
   * {@link #getPersistentBean() persistent bean}
   * from the strings information in this form in the map entry
   * with key <code>&quot;id&quot;</code>. The other properties
   * of the {@link #getPersistentBean() persistent bean}
   * are <strong>not</strong> changed. To set the other properties
   * of the persistent bean, see
   * {@link #formToBean(AsynchronousCRUD)}.
   *
   * @see       #initBean()
   * @see       #beanToForm(AsynchronousCRUD)
   * @see       #formToBean(AsynchronousCRUD)
   *
   * @return    Long.valueOf((String)get(ID_PROPERTY_NAME));
   * @throws    IdException
   *            There is no id in the form, or it is not in an acceptable
   *            format.
   */
  protected final Long getIdFromForm() throws IdException {
    if (get(ID_PROPERTY_NAME) == null) {
      throw new IdException("ID_NULL", //$NON-NLS-1$
                            null,
                            getPersistentBeanType());
    }
    try {
      Long id = Long.valueOf((String)get(ID_PROPERTY_NAME));
      if (LOG.isDebugEnabled()) {
        LOG.debug("got id from form (type: " //$NON-NLS-1$
                   + getPersistentBeanType().getName()
                   + ") = " //$NON-NLS-1$
                   + id.toString());
      }
      return id;
    }
    catch (NumberFormatException nfExc) {
      throw new IdFormatException((String)get(ID_PROPERTY_NAME),
                                    null, //$NON-NLS-1$
                                    nfExc,
                                    getPersistentBeanType());
    }
  }

  /**
   * Set the <code>&quot;id&quot;</code> entry in this form
   * to a string representing the {@link PersistentBean#getId() id} of the
   * {@link #getPersistentBean() persistent bean}.
   *
   * @see       #initBean()
   * @see       #formToBean(AsynchronousCRUD)
   * @see       #getIdFromForm()
   *
   * @pre       getPersistentBean() != null;
   * @post      (getPersistentBean().getId() != null)
   *              ? Long.valueOf(new.get(ID_PROPERTY_NAME)).equals(
   *                    getPersistentBean().getId())
   *              : "";
   */
  public final void beanToId() {
    assert getPersistentBean() != null;
    set(ID_PROPERTY_NAME, (getPersistentBean().getId() != null)
                            ? getPersistentBean().getId().toString()
                            : EMPTY);
    if (LOG.isDebugEnabled()) {
      // if makes that there really is lazy loading if not in debug
      LOG.debug("set id in form (persistent bean: " //$NON-NLS-1$
                 + getPersistentBean().toString()
                 + ") = " //$NON-NLS-1$
                 + get(ID_PROPERTY_NAME));
    }
  }

  /**
   * @see       DynaActionForm#reset(ActionMapping, HttpServletRequest)
   */
  public void reset(final ActionMapping mapping,
                    final HttpServletRequest request) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("resetting form (type: " //$NON-NLS-1$
                 + getPersistentBeanType().getName()
                 + ")"); //$NON-NLS-1$
    }
    super.reset(mapping, request);
    setCompoundPropertyException(null);
    $request = request;
  }

  protected HttpServletRequest $request;

  /**
   * When fetching all instances of a certain class, we can also fetch it's
   * subclasses. This String represent a boolean value whether or not to
   * retrieve subclasses.
   */
  public String getRetrieveSubclasses() {
    return (String)get("retrieveSubclasses"); //$NON-NLS-1$
  }

}
