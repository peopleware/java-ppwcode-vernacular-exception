package be.peopleware.struts_I.persistentBean.tag;


import be.peopleware.struts_I.persistentBean.CrudDynaActionForm;


/**
 * A support class for JSP tags that deal with Struts
 * {@link CrudDynaActionForm} instances. Tags that inherit
 * from this class should have an attribute <code>actionForm</code>
 * that can be set to an action form, and an attribute
 * <code>property</code> that can be set to a property name
 * of the property that the tag should deal with.
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
 public class CrudDynaActionFormTagSupport extends javax.servlet.jsp.tagext.SimpleTagSupport {

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



  // default constructor



  /*<property name="actionForm">*/
  //------------------------------------------------------------------

  /**
   * The action form that will be used to look up values.
   *
   * @basic
   * @init      null;
   */
  public final CrudDynaActionForm getActionForm() {
    return $actionForm;
  }

  /**
   * @param     actionForm
   *            the new action form
   * @post      new.getActionForm() == actionForm;
   */
  public final void setActionForm(
      final CrudDynaActionForm actionForm) {
    $actionForm = actionForm;
  }

  private CrudDynaActionForm $actionForm;

  /*</property>*/



  /*<property name="property">*/
  //------------------------------------------------------------------

  /**
   * The action form that will be used to look up values.
   *
   * @basic
   * @init      null;
   */
  public final String getProperty() {
    return $property;
  }

  /**
   * @param     property
   *            the new property name
   * @post      (property != null)
   *                ? new.getProperty().equals(property)
   *                : new.getProperty() == null;
   */
  public final void setProperty(final String property) {
    $property = property;
  }

  private String $property;

  /*</property>*/



  /*<property name="other attributes">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final String getOtherAttributes() {
    return $otherAttributes;
  }

  /**
   * @param     otherAttributes
   *            the new otherAttributes
   * @post      (otherAttributes != null)
   *                ==> new.getOtherAttributes().equals(otherAttributes);
   * @post      (otherAttributes == null)
   *                ==> new.getOtherAttributes() == null;
   */
  public final void setOtherAttributes(final String otherAttributes) {
    $otherAttributes = otherAttributes;
  }

  private String $otherAttributes;

  /*</property>*/

}
