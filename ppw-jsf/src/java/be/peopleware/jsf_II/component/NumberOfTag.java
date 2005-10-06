/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.jsf_II.component;


import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;

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


  /**
   * Return the component type for the component that is or will be bound to
   * this tag.
   *
   * @return   UINumberOf.class.getName();
   */
  public String getComponentType() {
    return UINumberOf.class.getName();
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
    return $collectionExpression;
  }

  /**
   * Set the given value.
   *
   * @param collectionExpression
   * @post  collectionExpression == null
   *          ? new.getValue() == null
   *          : collectionExpression.equals(new.getValue());
   */
  public void setValue(final String collectionExpression) {
    $collectionExpression = collectionExpression;
  }

  private String $collectionExpression;

  /*</property>*/

  /**
   * Override properties and attributes of the specified component, if the
   * corresponding properties of this tag handler instance were explicitly set.
   *
   * Create a value binding with the expression {@link #getValue()} and add
   * it to the given component using the name {@link UINumberOf#COLLECTION_VALUE_BINDING_NAME}.
   *
   * @see    UIComponentTag
   * @throws FatalFacesException
   *         {@link RobustCurrent#creatValueBinding(UIComponent, String, String)};
   */
  protected final void setProperties(final UIComponent component) throws FatalFacesException {
    super.setProperties(component);
    RobustCurrent.creatValueBinding(component,
                                    UINumberOf.COLLECTION_VALUE_BINDING_NAME,
                                    getValue());
  }

  /**
   * Release any resources allocated during the execution of this tag handler.
   *
   * @see   UIComponentTag
   * @post  new.getValue() == null;
   */
  public final void release() {
    super.release();
    setValue(null);
  }

}
