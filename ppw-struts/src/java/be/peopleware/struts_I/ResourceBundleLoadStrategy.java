package be.peopleware.struts_I;


import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts.Globals;


/**
 * <p>Strategy pattern to load i18n resource bundles for Struts.</p>
 *
 * @author    David Van Keer
 * @author    PeopleWare n.v.
 */
public class ResourceBundleLoadStrategy
    implements be.peopleware.i18n_I.ResourceBundleLoadStrategy {

  /* <section name="Meta Information"> */
  //------------------------------------------------------------------

  /** {@value} */
  public static final String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_TAG = "$Name$"; //$NON-NLS-1$

  /* </section> */



  /* <construction> */
  //------------------------------------------------------------------

  public ResourceBundleLoadStrategy() {
    // NOP
  }

  /* </construction> */



  /* <property name="request"> */
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final HttpServletRequest getRequest() {
    return $request;
  }

  /**
   * @param     request
   *            The new value for the pageContext.
   * @post      new.getRequest() == request;
   */
  public final void setRequest(final HttpServletRequest request) {
    $request = request;
  }

  private HttpServletRequest $request;

  /* </property> */


  private static final String EMPTY = ""; //$NON-NLS-1$

  /**
   * @see   be.peopleware.i18n_I.ResourceBundleLoadStrategy#loadResourceBundle(String)
   */
  public ResourceBundle loadResourceBundle(final String basename) {
    ResourceBundle bundle = null;
    if ((basename != null) && !(basename.equals(EMPTY))) {
      // Copied from SetBundleSupport tag (JSTL 1.x)
      Locale locale = null;
      HttpSession session = $request.getSession();
      if (session != null) {
        locale = (Locale) session.getAttribute(Globals.LOCALE_KEY);
      }
      if (locale == null) {
        locale = Locale.getDefault();
      }

      try {
        bundle = ResourceBundle.getBundle(basename, locale);
        // throws MissingResourceException
      }
      catch (MissingResourceException mrExc) {
        // NOP we will return null
      }
    }
    return bundle;
  }

}
