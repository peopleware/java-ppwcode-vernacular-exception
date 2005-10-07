/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.jsf_II.persistence.component;


import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import be.peopleware.jsf_II.FatalFacesException;
import be.peopleware.jsf_II.RobustCurrent;


/**
 * Tag for {@link UIViewModeHandler}.
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 *
 */
public class ViewModeHandlerTag extends UIComponentTag {

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


  private static final Log LOG = LogFactory.getLog(ViewModeHandlerTag.class);


  /**
   * Return the component type for the component that is or will be bound
   * to this tag.
   *
   * @return  ViewModeHandlerTag.class.getName();
   */
  public String getComponentType() {
    return ViewModeHandlerTag.class.getName();
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

  /*<section name="handler">*/
  //------------------------------------------------------------------

  /**
   * Returns the handler expression.
   *
   * @basic
   */
  public final String getHandler() {
    return $handler;
  }

  /**
   * Set the handler to the given value.
   *
   * @post  new.getHandler() == handler;
   */
  public final void setHandler(final String handler) {
    $handler = handler;
    LOG.debug("handler expression set: " + $handler);
  }

  private String $handler;

  /*</section>*/

  /**
   * Override properties and attributes of the specified component, if the
   * corresponding properties of this tag handler instance were explicitly set.
   *
   * @see    UIComponentTag
   * @throws FatalFacesException
   *         {@link RobustCurrent#creatValueBinding(UIComponent, String, String)};
   */
  protected final void setProperties(final UIComponent component) throws FatalFacesException {
    super.setProperties(component);
    RobustCurrent.creatValueBinding(component,
                                    UIViewModeHandler.HANDLER_VALUE_BINDING_NAME,
                                    getHandler());
  }

  /**
   * Release any resources allocated during the execution of this tag handler.
   *
   * @see   UIComponentTag
   * @post  new.getHandler() == null;
   */
  public final void release() {
    super.release();
    setHandler(null);
  }
}
