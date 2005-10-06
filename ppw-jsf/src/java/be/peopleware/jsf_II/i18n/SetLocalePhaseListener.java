/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.jsf_II.i18n;


import java.io.Serializable;
import java.util.Locale;

import javax.faces.FacesException;
import javax.faces.component.UIViewRoot;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.jsf_II.RobustCurrent;



/**
 * With every faces request, get the locale from the JSTL settings,
 * and put it in the view root. This needs to be done immediately
 * before the render response phase, so that the handling of the request parameters
 * (e.g., conversion and validation) still uses the locale of the request with
 * which the page in which the user entered values.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
public final class SetLocalePhaseListener implements PhaseListener, Serializable {

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



  private static final Log LOG = LogFactory.getLog(SetLocalePhaseListener.class);



  /*<construction>*/
  //------------------------------------------------------------------

  // Default constructor is needed

  /*</construction */

  /**
   * <p>Where JSTL stores it's locale setting. This is
   *   <code>javax.servlet.jsp.jstl.core.Config.FMT_LOCATE</code>.</p>
   *
   * @note For some reason <code>.session</code> is added in the effective
   *       variable name to <code>javax.servlet.jsp.jstl.core.Config.FMT_LOCATE</code>.
   *
   * <p><strong>{@value}</strong></p>
   */
  public static final String FMT_LOCALE = "javax.servlet.jsp.jstl.fmt.locale.session";

  /**
   * Set the {@link UIViewRoot#getLocale()}
   */
  public void beforePhase(PhaseEvent event) throws FacesException {
    LOG.debug("before RENDER_RESPONSE: setting locale");
    // get JSTL locale
    Locale locale = (Locale)RobustCurrent.sessionMap().get(FMT_LOCALE);
    LOG.debug("JSTL locale retrieved: " + locale);
    if (locale != null) {
      RobustCurrent.uiViewRoot().setLocale(locale);
      LOG.debug("locale of UIViewRoot set to " + locale);
    }
  }

  public final void afterPhase(PhaseEvent event) {
    // NOP
  }

  public final PhaseId getPhaseId() {
    return PhaseId.RENDER_RESPONSE;
  }

}
