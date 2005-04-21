package be.peopleware.jsf_I;

import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import be.peopleware.exception_I.TechnicalException;


/**
 * General Utility class for use with Java Server Faces.
 *
 * @author     David Van Keer
 * @author     Peopleware n.v.
 */
public class Util {
  
  /*<section name="Meta Information">*/
  //------------------------------------------------------------------
  /** {@value} */
  public static final String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_TAG = "$Name$"; //$NON-NLS-1$
  /*</section>*/


  private static final Log LOG = LogFactory.getLog(Util.class);

  
  /**
   * The faces context of the request/response cyclye this method is
   * called in.
   * @see FacesContext#getCurrentInstance()
   * 
   * @return FacesContext.getCurrentInstance();
   */
  public static FacesContext getFacesContext() {
    return FacesContext.getCurrentInstance();
  }
  
  /**
   * The {@link ServletRequest} object of ther request/response cycle this method
   * is called in. In a JSP context, this is an {@link HttpServletRequest}.
   * 
   * @throws TechnicalException
   *         getFacesContext() == null;
   */
  public static ServletRequest getServletRequest() throws TechnicalException {
    try {
      ServletRequest result
          = (HttpServletRequest)getFacesContext().getExternalContext().getRequest();
      LOG.debug("retrieved servlet request");
      return result;
    }
    catch (NullPointerException npExc) {
      throw new TechnicalException("Cannot get ServletRequest, "
                                   + "because there is no FacesContext", npExc);
    }
    catch (ClassCastException ccExc) {
      throw new TechnicalException("Cannot get ServletRequest, "
                                   + "because there is another type of ExternalContext ("
                                   + getFacesContext().getExternalContext()
                                       .getRequest().getClass().getName()
                                   + ")",
                                   ccExc);
    }
  }

  /**
   * The current session, which is the external context
   * of the current {@link FacesContext#getCurrentInstance()}.
   * Returns <code>null</code> if there is no such session
   *
   * @result (FacesContext.getCurrentInstance() == null)
   *         || (FacesContext.getCurrentInstance().getExternalContext() == null)
   *         || (! FacesContext.getCurrentInstance().getExternalContext()
   *                .getSession(false) instanceof HttpSession)
   *         ==> null;
   * @result FacesContext.getCurrentInstance()
   *            .getExternalContext().getSession(false);
   */
  public static HttpSession getHttpSession() {
    try {
      return (HttpSession)FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
    }
    catch (NullPointerException npExc) {
      LOG.fatal("called outside context of a JSF HTTP request", npExc);
      return null;
    }
  }

  
}
