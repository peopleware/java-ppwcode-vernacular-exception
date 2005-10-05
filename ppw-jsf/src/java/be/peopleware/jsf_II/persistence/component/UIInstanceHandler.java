package be.peopleware.jsf_II.persistence.component;


import java.io.IOException;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import be.peopleware.jsf_II.FatalFacesException;
import be.peopleware.jsf_II.RobustCurrent;
import be.peopleware.jsf_II.persistence.InstanceHandler;


/**
 * This {@link UIComponent} is used in JSF pages to initialize {@link InstanceHandler}
 * instances early in the JSF request / response lifecycle. In this mode of use,
 * the {@link InstanceHandler} is a managed bean in request scope. The
 * {@link InstanceHandler#getPersistentBeanType()} is set in <kbd>faces-config.xml</kbd> as a managed
 * property. The required {@link InstanceHandler#getId()} and
 * {@link InstanceHandler#getViewMode()} comes with the request as request parameters.
 * They need to be set in the {@link InstanceHandler} early, to
 * be able to retrieve the instance from persistent storage before it is accessed by
 * value bindings. This {@link UIComponent} renders the {@link InstanceHandler#getId()} and
 * {@link InstanceHandler#getViewMode()} as hidden fields, and reads them from
 * the HTTP request, validates them, converts them, and sets them in the handler during
 * the Apply Request Values phase.
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 */
public class UIInstanceHandler extends UIViewModeHandler {

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


  private static final Log LOG = LogFactory.getLog(UIInstanceHandler.class);


  /*<section name="id tag name">*/
  //------------------------------------------------------------------

  /**
   * <strong>= {@value}</strong>
   */
  public final static String INPUT_TAG_NAME_ID_EXTENSION = ".id";

  /**
   * The name of the hidden input tag for the id.
   *
   * @return getClientId(context) + INPUT_TAG_NAME_ID_EXTENSION;
   */
  public final String idTagName(FacesContext context) {
    return getClientId(context) + INPUT_TAG_NAME_ID_EXTENSION;
  }

  /*</section>*/



  /*<section name="rendering">*/
  //------------------------------------------------------------------

  /**
   * Writes 2 hidden input tags, containing the view mode and id, to the
   * output stream.
   *
   * The following hidden input tags are written:
   * <pre>
   *   &lt;input type=&quot;hidden&quot; name=&quot;<var>clientId</var>.id&quot; value=&quot;<var>handler.id</var>&quot; /&gt;
   *   &lt;input type=&quot;hidden&quot; name=&quot;<var>clientId</var>.viewMode&quot; value=&quot;<var>handler.viewMode</var>&quot; /&gt;
   * </pre>
   *
   * @throws FatalFacesException
   *         getHandler();
   */
  public void encodeBegin(FacesContext context) throws IOException, FatalFacesException {
    super.encodeBegin(context);
    encodeHiddenInput(context.getResponseWriter(),
                      idTagName(context), ((InstanceHandler)getHandler(context)).getId());
  }

  public void encodeEnd(FacesContext context) throws IOException {
    // Empty encodeEnd so no input field is written out by default.
  }

  /*</rendering>*/



  /*<section name="decoding">*/
  //------------------------------------------------------------------

  /**
   * @pre context != null;
   * @throws FatalFacesException
   *         context.getExternalContext.getRequestParameterMap().get(idTagName(context));
   */
  public void decode(FacesContext context) throws FatalFacesException {
    super.decode(context);
    Map requestParameters = context.getExternalContext().getRequestParameterMap();
    InstanceHandler handler = (InstanceHandler)getHandler(context); // FatalFacesException
    String viewMode = handler.getViewMode();
    Long id = null;
    { // get parameters from request

      if (!viewMode.equals(InstanceHandler.VIEWMODE_EDITNEW) &&
          !viewMode.equals(InstanceHandler.VIEWMODE_DELETED)) {
        // id
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

    }
    // fill id in handler
    handler.setId(id);
  }

  /*</section>*/

}
