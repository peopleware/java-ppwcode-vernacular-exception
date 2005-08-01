/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/


package be.peopleware.jsf_II.i18n;


import java.util.MissingResourceException;
import java.util.ResourceBundle;

import be.peopleware.i18n_I.ResourceBundleLoadStrategy;
import be.peopleware.jsf_II.FatalFacesException;
import be.peopleware.jsf_II.RobustCurrent;


/**
 * <p>Strategy pattern to load i18n resource bundles for JSF.</p>
 *
 * @author    René Clerckx
 * @author    PeopleWare n.v.
 */
public class JsfResourceBundleLoadStrategy implements ResourceBundleLoadStrategy {

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

  /**
   * The {@link ResourceBundle} with name <code>basename</code> is loaded
   * with the {@link RobustCurrent#locale() current locale from the UI view}
   * and the {@link ClassLoader} of the {@link Thread current thread}.
   * If no matching resource bundle can be found with this strategy,
   * <code>null</code> is returned.
   *
   * @see ResourceBundleLoadStrategy#loadResourceBundle(String)
   * @throws FatalFacesException
   *         RobustCurrent.locale();
   */
  public ResourceBundle loadResourceBundle(final String basename) throws FatalFacesException {
    ResourceBundle result = null;
    if ((basename != null) && !(basename.equals(""))) {
      try {
        result = ResourceBundle.getBundle(basename, RobustCurrent.locale(),
                                          Thread.currentThread().getContextClassLoader());
      }
      catch (MissingResourceException mrExc) {
        return null;
      }
    }
    return result;
  }

}
