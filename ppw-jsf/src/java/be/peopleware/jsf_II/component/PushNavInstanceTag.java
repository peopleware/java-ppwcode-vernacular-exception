/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.jsf_II.component;


import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.jsf_II.FatalFacesException;
import be.peopleware.jsf_II.RobustCurrent;


/**
 * Tag for {@link UIPushNavInstance}.
 *
 * @author    Jan Dockx
 * @author    Abdul Shoudouev
 * @author    Peopleware n.v.
 *
 */
public class PushNavInstanceTag extends UIComponentTag {

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


  private static final Log LOG = LogFactory.getLog(PushNavInstanceTag.class);

  /**
   * Return the component type for the component that is or will be bound to
   * this tag.
   *
   * @return   UIPushNavInstance.class.getName();
   */
  public String getComponentType() {
    return UIPushNavInstance.class.getName();
  }

  /**
   * Return the rendererType property that selects the Renderer to be used for
   * encoding this component, or null to ask the component to render itself
   * directly.
   *
   * @return  null;
   */
  public String getRendererType() {
    return null;
  }

  /*<property name="value">*/
  //------------------------------------------------------------------

  /**
   * The value of this tag.
   *
   * @basic
   */
  public String getValue() {
    return $navigationInstanceExpression;
  }

  /**
   * Set the given value.
   *
   * @param navigationInstanceExpression
   * @post  navigationInstanceExpression == null
   *          ? new.getValue() == null
   *          : navigationInstanceExpression.equals(new.getValue());
   */
  public final void setValue(String navigationInstanceExpression) {
    $navigationInstanceExpression = navigationInstanceExpression;
    LOG.debug("navigation instance expression set: " + $navigationInstanceExpression);
  }

  private String $navigationInstanceExpression;

  /*</property>*/

  /*<property name="navigationStack">*/
  //------------------------------------------------------------------

  /**
   * The navigation stack of this tag.
   *
   * @basic
   */
  public String getNavigationStack() {
    return $navigationStackExpression;
  }

  /**
   * Set the given navigation stack.
   *
   * @param navigationStackExpression
   * @post  navigationStackExpression == null
   *          ? new.getNavigationStack() == null
   *          : navigationStackExpression.equals(new.getNavigationStack());
   */
  public final void setNavigationStack(String navigationStackExpression) {
    $navigationStackExpression = navigationStackExpression;
    LOG.debug("navigationStack expression set: " + $navigationStackExpression);
  }

  private String $navigationStackExpression;

  /*</property>*/

  /**
   * Override properties and attributes of the specified component, if the
   * corresponding properties of this tag handler instance were explicitly set.
   *
   * Create a value binding with the expression {@link #getValue()} and add
   * it to the given component using the name
   * {@link UIPushNavInstance#NAVIGATION_INSTANCE_VALUE_BINDING_NAME}.
   *
   * Create a value binding with the expression {@link #getNavigationStack()} and add
   * it to the given component using the name
   * {@link UIPushNavInstance#NAVIGATION_STACK_VALUE_BINDING_NAME}.
   *
   * @see    UIComponentTag
   * @throws FatalFacesException
   *         {@link RobustCurrent#creatValueBinding(UIComponent, String, String)};
   */
  protected final void setProperties(UIComponent component) throws FatalFacesException {
    super.setProperties(component);
    RobustCurrent.creatValueBinding(component,
                                    UIPushNavInstance.NAVIGATION_INSTANCE_VALUE_BINDING_NAME,
                                    getValue());
    RobustCurrent.creatValueBinding(component,
                                    UIPushNavInstance.NAVIGATION_STACK_VALUE_BINDING_NAME,
                                    getNavigationStack());
  }

  /**
  * Release any resources allocated during the execution of this tag handler.
  *
  * @see   UIComponentTag
  * @post  new.getValue() == null;
  * @post  new.getNavigationStack() == null;
  */
  public final void release() {
    super.release();
    setValue(null);
    setNavigationStack(null);
  }

}
