package be.peopleware.jsf_II.persistence.component;


import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.jsf_II.FatalFacesException;
import be.peopleware.jsf_II.RobustCurrent;
import be.peopleware.jsf_II.persistence.PersistentBeanCrudHandler;


/**
 * Tag for {@link UIPersistentBeanCrudHandler}.
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 *
 */
public class PersistentBeanCrudHandlerTag extends UIComponentTag {

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


  private static final Log LOG = LogFactory.getLog(PersistentBeanCrudHandlerTag.class);

 
  public String getComponentType() {
    return PersistentBeanCrudHandler.class.getName();
  }

  public String getRendererType() {
    return null;
  }

  public final void setHandler(String handler) {
    $handler = handler;
    LOG.debug("handler expression set: " + $handler);
  }
  
  private String $handler;
  
  /**
   * @throws FatalFacesException
   *         {@link RobustCurrent#creatValueBinding(UIComponent, String, String)};
   */
  protected final void setProperties(UIComponent component) throws FatalFacesException {
    super.setProperties(component);
    RobustCurrent.creatValueBinding(component,
                                    UIPersistentBeanCrudHandler.HANDLER_VALUE_BINDING_NAME,
                                    $handler);
  }
  
  public final void release() {
    super.release();
    $handler = null;
  }
  
}
