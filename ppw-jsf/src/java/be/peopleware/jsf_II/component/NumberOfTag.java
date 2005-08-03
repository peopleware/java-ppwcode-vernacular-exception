package be.peopleware.jsf_II.component;


import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.jsf_II.FatalFacesException;
import be.peopleware.jsf_II.RobustCurrent;


/**
 * Tag for {@link UINumberOf}.
 *
 * @author    Jan Dockx
 * @author    Abdul Shoudouev
 * @author    Peopleware n.v.
 *
 */
public class NumberOfTag extends UIComponentTag {

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


  public String getComponentType() {
    return UINumberOf.class.getName();
  }

  public String getRendererType() {
    return null;
  }

  public void setValue(String collectionExpression) {
    $collectionExpression = collectionExpression;
  }

  private String $collectionExpression;

  /**
   * @throws FatalFacesException
   *         {@link RobustCurrent#creatValueBinding(UIComponent, String, String)};
   */
  protected final void setProperties(UIComponent component) throws FatalFacesException {
    super.setProperties(component);
    RobustCurrent.creatValueBinding(component,
                                    UINumberOf.COLLECTION_VALUE_BINDING_NAME,
                                    $collectionExpression);
  }

  public final void release() {
    super.release();
    $collectionExpression = null;
  }

}
