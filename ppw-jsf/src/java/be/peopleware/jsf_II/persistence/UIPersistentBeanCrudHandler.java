package be.peopleware.jsf_II.persistence;


import java.io.IOException;
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


/**
 * This {@link UIComponent} is used in JSF pages to initialize {@link PersistentBeanCrudHandler}
 * instances early in the JSF request / response lifecycle. In this mode of use,
 * the {@link PersistentBeanCrudHandler} is a managed bean in request scope. The
 * {@link PersistentBeanCrudHandler#getType()} is set in <kbd>faces-config.xml</kbd> as a managed
 * property. The required {@link PersistentBeanCrudHandler#getId()} and
 * {@link PersistentBeanCrudHandler#getViewMode()} comes with the request as request parameters.
 * They need to be set in the {@link PersistentBeanCrudHandler} early, to
 * be able to retrieve the instance from persistent storage before it is accessed by
 * value bindings. This {@link UIComponent} renders the {@link PersistentBeanCrudHandler#getId()} and
 * {@link PersistentBeanCrudHandler#getViewMode()} as hidden fields, and reads them from
 * the HTTP request, validates them, converts them, and sets them in the handler during
 * the Apply Request Values phase.
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 *
 */
public class UIPersistentBeanCrudHandler extends UIInput {

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


  private static final Log LOG = LogFactory.getLog(UIPersistentBeanCrudHandler.class);



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
  public PersistentBeanCrudHandler getHandler(FacesContext context)
      throws FatalFacesException {
    ValueBinding vb = getValueBinding(HANDLER_VALUE_BINDING_NAME);
    if (vb == null) {
      RobustCurrent.fatalProblem("Could not locate handler", LOG);
    }
    Object result = vb.getValue(context);
    if ((result == null) ||
        (! (result instanceof PersistentBeanCrudHandler))) {
      RobustCurrent.fatalProblem("Could not locate handler", LOG);
    }
    return (PersistentBeanCrudHandler)result;
  }

  /*</property>*/


  /*<section name="tag names">*/
  //------------------------------------------------------------------

  /**
   * <strong>= {@value}</strong>
   */
  public final static String INPUT_TAG_NAME_ID_EXTENSION = ".id";

  /**
   * <strong>= {@value}</strong>
   */
  public final static String INPUT_TAG_NAME_VIEWMODE_EXTENSION = ".viewMode";

  /**
   * The name of the hidden input tag for the id.
   *
   * @return getClientId(context) + INPUT_TAG_NAME_ID_EXTENSION;
   */
  public final String idTagName(FacesContext context) {
    return getClientId(context) + INPUT_TAG_NAME_ID_EXTENSION;
  }

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
   * Writes to the output stream 2 hidden input tags:
   * <pre>
   *   &lt;input type=&quot;hidden&quot; name=&quot;<var>clientId</var>.id&quot; value=&quot;<var>handler.id</var>&quot; /&gt;
   *   &lt;input type=&quot;hidden&quot; name=&quot;<var>clientId</var>.viewMode&quot; value=&quot;<var>handler.viewMode</var>&quot; /&gt;
   * </pre>
   *
   * @throws FatalFacesException
   *         getHandler();
   */
  public void encodeBegin(FacesContext context) throws IOException, FatalFacesException {
    encodeHiddenInput(context.getResponseWriter(),
                      idTagName(context), getHandler(context).getId());
    encodeHiddenInput(context.getResponseWriter(),
                      viewModeTagName(context), getHandler(context).getViewMode());
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
  private void encodeHiddenInput(ResponseWriter response, String componentId, Object value) throws IOException {
    response.startElement(INPUT_TAG, this);
    response.writeAttribute(INPUT_TAG_TYPE_ATTR, INPUT_TAG_TYPE_HIDDEN, null);
    response.writeAttribute(INPUT_TAG_NAME_ATTR, componentId, null);
    response.writeAttribute(INPUT_TAG_VALUE_ATTR, value, null);
    response.endElement(INPUT_TAG);
  }

  /*</rendering>*/



  /*<section name="decoding">*/
  //------------------------------------------------------------------

  /**
   * @pre context != null;
   * @throws FatalFacesException
   *         context.getExternalContext.getRequestParameterMap().get(idTagName(context));
   * @throws FatalFacesException
   *         getHandler();
   */
  public void decode(FacesContext context) throws FatalFacesException {
    assert context != null;
    PersistentBeanCrudHandler handler = getHandler(context); // FatalFacesException
    setValid(true);
    Map requestParameters = context.getExternalContext().getRequestParameterMap();
    assert requestParameters != null;
    Long id = null;
    String viewMode = null;
    { // get parameters from request
      { // id
        String idString = (String)requestParameters.get(idTagName(context));
        if (idString == null) {
          setValid(false);
          RobustCurrent.fatalProblem("There was no id value in the request " +
                                     "(expected parameter name: " +
                                     idTagName(context) + ")",
                                     LOG);
          // IDEA (jand) this is not fatal; do goback()
        }
        assert idString != null;
        try {
          id = Long.valueOf(idString); // NumberFormatException
        }
        catch (NumberFormatException nfExc) {
          setValid(false);
          RobustCurrent.fatalProblem("The id value in the request is not a Long (" +
                                     idString +  ")",
                                     nfExc,
                                     LOG);
          // IDEA (jand) this is not fatal; do goback()
        }
      }
      { // viewMode
        viewMode = (String)requestParameters.get(viewModeTagName(context));
        if (! PersistentBeanCrudHandler.isViewMode(viewMode)) {
          // if there is no correct view mode in the request, treat it as a display mode
          viewMode = PersistentBeanCrudHandler.VIEWMODE_DISPLAY;
        }
      }
    }
    assert id != null;
    assert PersistentBeanCrudHandler.isViewMode(viewMode);
    // fill id and viewmode in handler
    handler.setInstance(null); // be sure
    handler.setId(id);
    handler.setViewMode(viewMode);
  }

  /*</section>*/

}
