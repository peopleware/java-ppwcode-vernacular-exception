package be.peopleware.jsf_II.persistence.component;


import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.jsf_II.FatalFacesException;
import be.peopleware.jsf_II.RobustCurrent;
import be.peopleware.jsf_II.persistence.InstanceHandler;
import be.peopleware.jsf_II.persistence.PersistentBeanHandler;


/**
 * This {@link UIComponent} is used in JSF pages to initialize {@link InstanceHandler}
 * instances early in the JSF request / response lifecycle. In this mode of use,
 * the {@link InstanceHandler} is a managed bean in request scope. The
 * {@link InstanceHandler#getPersistentBeanType()} is set in <kbd>faces-config.xml</kbd>
 * as a managed property. The required {@link InstanceHandler#getViewMode()} comes
 * with the request as request parameters.
 * They need to be set in the {@link InstanceHandler} early, to
 * be able to retrieve the instance from persistent storage before it is accessed by
 * value bindings. This {@link UIComponent} renders the {@link InstanceHandler#getViewMode()}
 * as hidden field, and reads them from the HTTP request, validates them, converts them,
 * and sets them in the handler duringthe Apply Request Values phase.
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 */
public class UIViewModeHandler extends UIInput implements Serializable {

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


  private static final Log LOG = LogFactory.getLog(UIViewModeHandler.class);



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
  public PersistentBeanHandler getHandler(FacesContext context)
      throws FatalFacesException {
    ValueBinding vb = getValueBinding(HANDLER_VALUE_BINDING_NAME);
    if (vb == null) {
      RobustCurrent.fatalProblem("Could not locate handler", LOG);
    }
    Object result = vb.getValue(context);
    if ((result == null) ||
        (! (result instanceof PersistentBeanHandler))) {
      RobustCurrent.fatalProblem("Could not locate handler", LOG);
    }
    return (PersistentBeanHandler)result;
  }

  /*</property>*/


  /*<section name="viewMode tag">*/
  //------------------------------------------------------------------

  /**
   * <strong>= {@value}</strong>
   */
  public final static String INPUT_TAG_NAME_VIEWMODE_EXTENSION = ".viewMode";

  /**
   * The name of the hidden input tag for the view mode.
   *
   * @return getClientId(context) + INPUT_TAG_NAME_VIEWMODE_EXTENSION;
   */
  public final String viewModeTagName(FacesContext context) {
    return getClientId(context) + INPUT_TAG_NAME_VIEWMODE_EXTENSION;
  }

  /*</section>*/



  /*<section name="rendering">*/
  //------------------------------------------------------------------

  /**
   * Writes to the output stream a hidden input tag:
   * <pre>
   *   &lt;input type=&quot;hidden&quot; name=&quot;<var>clientId</var>.viewMode&quot; value=&quot;<var>handler.viewMode</var>&quot; /&gt;
   * </pre>
   *
   * @throws FatalFacesException
   *         getHandler();
   */
  public void encodeBegin(FacesContext context) throws IOException, FatalFacesException {
    encodeHiddenInput(context.getResponseWriter(),
                      viewModeTagName(context), getHandler(context).getViewMode());
  }

  public void encodeEnd(FacesContext context) throws IOException {
    // Empty encodeEnd so no input field is written out by default.
  }

  private final static String INPUT_TAG = "input";
  private final static String INPUT_TAG_TYPE_ATTR = "type";
  private final static String INPUT_TAG_TYPE_HIDDEN = "hidden";
  private final static String INPUT_TAG_NAME_ATTR = "name";
  private final static String INPUT_TAG_VALUE_ATTR = "value";

  /**
   * @pre getHandler() != null;
   * @throws IOException
   */
  protected void encodeHiddenInput(ResponseWriter response, String componentId, Object value) throws IOException {
    response.startElement(INPUT_TAG, this);
    response.writeAttribute(INPUT_TAG_TYPE_ATTR, INPUT_TAG_TYPE_HIDDEN, null);
    response.writeAttribute(INPUT_TAG_NAME_ATTR, componentId, null);
    response.writeAttribute(INPUT_TAG_VALUE_ATTR, (value != null) ? value : "", null);
    response.endElement(INPUT_TAG);
  }

  /*</rendering>*/



  /*<section name="decoding">*/
  //------------------------------------------------------------------

  /**
   * @pre context != null;
   * @throws FatalFacesException
   *         getHandler();
   */
  public void decode(FacesContext context) throws FatalFacesException {
    assert context != null;
    PersistentBeanHandler handler = getHandler(context); // FatalFacesException
    setValid(true);
    Map requestParameters = context.getExternalContext().getRequestParameterMap();
    assert requestParameters != null;
    String viewMode = null;
    { // get parameters from request

      { // viewMode
        viewMode = (String)requestParameters.get(viewModeTagName(context));
        if (! handler.isValidViewMode(viewMode)) {
          // if there is no correct view mode in the request, treat it as a display mode
          viewMode = PersistentBeanHandler.VIEWMODE_DISPLAY;
        }
      }
    }
    assert handler.isValidViewMode(viewMode);
    // fill id and viewmode in handler
    handler.setViewMode(viewMode);
  }

  /*</section>*/

}
