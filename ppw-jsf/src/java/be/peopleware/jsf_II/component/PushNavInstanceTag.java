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

 
  public String getComponentType() {
    return UIPushNavInstance.class.getName();
  }

  public String getRendererType() {
    return null;
  }

  /*<property name="handler">*/
  //------------------------------------------------------------------

  
  public final void setHandler(String handler) {
    $handler = handler;
    LOG.debug("handler expression set: " + $handler);
  }
  
  private String $handler;
  
  /*</property>*/

  /*<property name="navigationStack">*/
  //------------------------------------------------------------------

  
  public final void setNavigationStack(String navigationStack) {
    $navigationStack = navigationStack;
    LOG.debug("navigationStack expression set: " + $navigationStack);
  }
  
  private String $navigationStack;
  
  /*</property>*/

  /**
   * @throws FatalFacesException
   *         {@link RobustCurrent#creatValueBinding(UIComponent, String, String)};
   */
  protected final void setProperties(UIComponent component) throws FatalFacesException {
    super.setProperties(component);
    RobustCurrent.creatValueBinding(component,
                                    UIPushNavInstance.HANDLER_VALUE_BINDING_NAME,
                                    $handler);
    RobustCurrent.creatValueBinding(component,
                                    UIPushNavInstance.NAVIGATION_STACK_VALUE_BINDING_NAME,
                                    $navigationStack);
  }
  
  public final void release() {
    super.release();
    $handler = null;
    $navigationStack = null;
  }
  
}
