/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.jsp_II;


import java.util.ResourceBundle;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import org.apache.taglibs.standard.tag.common.fmt.BundleSupport;

import be.peopleware.i18n_I.ResourceBundleLoadStrategy;


/**
 * <p>Strategy pattern to load i18n resource bundles for JSTL.</p>
 *
 * @author    David Van Keer
 * @author    PeopleWare n.v.
 */
public class JSTLResourceBundleLoadStrategy
    implements ResourceBundleLoadStrategy {

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



  // Default constructor



  /* <property name="pageContext"> */
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final PageContext getPageContext() {
    return $pageContext;
  }

  /**
   * @param     pageContext
   *            The new value for the pageContext.
   * @post      new.getPageContext() == pageContext;
   */
  public final void setPageContext(final PageContext pageContext) {
    $pageContext = pageContext;
  }

  private PageContext $pageContext;

  /* </property> */



  /**
   * @see   ResourceBundleLoadStrategy#loadResourceBundle(String)
   */
  public ResourceBundle loadResourceBundle(final String basename) {
    ResourceBundle result = null;
    if ((basename != null) && !(basename.equals(""))) { //$NON-NLS-1$
      // Copied from SetBundleSupport tag (JSTL 1.x)
      LocalizationContext locCtxt =
          BundleSupport.getLocalizationContext(getPageContext(), basename);
      result = locCtxt.getResourceBundle();
    }
    return result;
  }

}
