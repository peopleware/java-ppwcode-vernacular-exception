package be.peopleware.struts_II;


import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.tiles.TilesRequestProcessor;


/**
 * A customized RequestProcessor that checks the user's preferred Locale from
 * the request each time. If a Locale is not in the session or the one in the
 * session doesn't match the request, the Locale in the request is set to the
 * session.
 *
 * @author    David Van Keer
 * @author    Peopleware n.v.
 */
public class HttpLocaleRequestProcessor extends TilesRequestProcessor {

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

  private static final Log LOG
      = LogFactory.getLog(HttpLocaleRequestProcessor.class);

  /**
   * Keep the locale of JSTL and the Struts framework in synch by pre-processing
   * the request.
   *
   * @param     request
   *            HTTP servlet request information.
   * @param     response
   *            HTTP servlet response information.
   */
  protected void processLocale(final HttpServletRequest request,
                               final HttpServletResponse response) {
    // Are we configured to select the Locale automatically?
    if (!moduleConfig.getControllerConfig().getLocale()) {
      return;
    }
    // Get the Locale (if any) that is stored in the user's session
    HttpSession session = request.getSession();
    Locale sessionLocale = (Locale)session.getAttribute(Globals.LOCALE_KEY);
    // Get the user's preferred Locale from the request
    Locale requestLocale = request.getLocale();
    // If was never a Locale in the session or it has changed, set it
    if (sessionLocale == null || (sessionLocale != requestLocale)) {
      if (LOG.isDebugEnabled()) {
        LOG.debug(" Setting user locale to '" //$NON-NLS-1$
                   + requestLocale
                   + "'"); //$NON-NLS-1$
      }
      // Set the new Locale into the user's session (Struts)
      session.setAttribute(Globals.LOCALE_KEY,  Locale.UK);
      // Set the new Locale into the user's session (JSTL)
      Config.set(session, Config.FMT_LOCALE, Locale.UK);
    }
  }

}
