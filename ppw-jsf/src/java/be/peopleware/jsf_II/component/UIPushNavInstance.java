package be.peopleware.jsf_II.component;


import java.io.IOException;
import java.io.Serializable;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.jsf_II.FatalFacesException;
import be.peopleware.jsf_II.RobustCurrent;
import be.peopleware.jsf_II.persistence.InstanceHandler;
import be.peopleware.servlet.navigation.NavigationStack;


/**
 * This {@link UIPushNavInstance} is used in JSF pages to push a handler 
 * (an instance of {@link InstanceHandler}) to a navigation stack 
 * (an instance of {@link NavigationStack}) during the Render Response phase. 
 * In this mode of use,
 * the {@link InstanceHandler} is a managed bean in request scope and 
 * the {@link NavigationStack} is a managed bean in session scope. 
 *
 * @author    Jan Dockx
 * @author    Abdul Shoudouev
 * @author    Peopleware n.v.
 *
 */
public class UIPushNavInstance extends UIInput implements Serializable {

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


  private static final Log LOG = LogFactory.getLog(UIPushNavInstance.class);



  /*<property name="handler">*/
  //------------------------------------------------------------------

  /**
   * <strong>= {@value}</strong>
   */
  public final static String HANDLER_VALUE_BINDING_NAME = "handler";

  /**
   * Return the result of {@link #HANDLER_VALUE_BINDING_NAME} value binding.
   *
   * @return result != null;
   * @throws FatalFacesException
   *         ; could not locate a handler through the value binding
   */
  public InstanceHandler getHandler(FacesContext context)
      throws FatalFacesException {
    ValueBinding vb = getValueBinding(HANDLER_VALUE_BINDING_NAME);
    if (vb == null) {
      RobustCurrent.fatalProblem("Could not locate handler", LOG);
    }
    Object result = vb.getValue(context);
    if ((result == null) ||
        (! (result instanceof InstanceHandler))) {
      RobustCurrent.fatalProblem("Could not locate handler", LOG);
    }
    return (InstanceHandler)result;
  }

  /*</property>*/

  /*<property name="navigationStack">*/
  //------------------------------------------------------------------

  /**
   * <strong>= {@value}</strong>
   */
  public final static String NAVIGATION_STACK_VALUE_BINDING_NAME = "navigationStack";

  /**
   * Return the result of {@link #NAVIGATION_STACK_VALUE_BINDING_NAME} value binding.
   *
   * @return result != null;
   * @throws FatalFacesException
   *         ; could not locate a navigationStack through the value binding
   */
  public NavigationStack getNavigationStack(FacesContext context)
      throws FatalFacesException {
    ValueBinding vb = getValueBinding(NAVIGATION_STACK_VALUE_BINDING_NAME);
    if (vb == null) {
      RobustCurrent.fatalProblem("Could not locate navigationStack", LOG);
    }
    Object result = vb.getValue(context);
    if ((result == null) ||
        (! (result instanceof NavigationStack))) {
      RobustCurrent.fatalProblem("Could not locate navigationStack", LOG);
    }
    return (NavigationStack)result;
  }

  /*</property>*/


  /*<section name="rendering">*/
  //------------------------------------------------------------------

  /**
   * Pushes the handler to the navigationStack.
   *
   * @throws FatalFacesException
   *         getHandler();
   *         getNavigationStack();
   */
  public void encodeBegin(FacesContext context) throws IOException, FatalFacesException {
    getNavigationStack(context).push(getHandler(context));
  }
  public void encodeEnd(FacesContext context) throws IOException, FatalFacesException {
    // NOP
  }


  /*</rendering>*/



  /*<section name="decoding">*/
  //------------------------------------------------------------------
  
    //NOP
  
  /*</section>*/

}
