package be.peopleware.i18n_I;


import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * A simple resource bundle load strategy. We use the default class
 * loader strategy with a given locale. For properties files, if there
 * is no match with the given locale * (<code>basename + &quot;_&quot;
 * + getLocale().toString() + &quot;.properties&quot;</code>) or
 * {@link #getLocale()}returns <code>null</code>, we try the system default
 * locale (<code>basename + &quot;_&quot;
 * + {@link java.util.Locale#getDefault()}.toString()
 * + &quot;.properties&quot;</code>).
 * If there is no match still, the default properties file is used
 * (<code>basename + &quot;.properties&quot;</code>).
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public class DefaultResourceBundleLoadStrategy
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



  /* <construction> */
  //------------------------------------------------------------------

  /**
   * @post      new.getLocale() == null;
   */
  public DefaultResourceBundleLoadStrategy() {
    // NOP
  }

  /* </construction> */



  /* <property name="locale"> */
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Locale getLocale() {
    return $locale;
  }

  /**
   * @param     locale
   *            The new value for the locale.
   * @post      new.getLocale() == locale;
   */
  public final void setLocale(final Locale locale) {
    $locale = locale;
  }

  private Locale $locale;

  /* </property> */

  private static final String EMPTY = ""; //$NON-NLS-1$

  /**
   * <p>Load a resource bundle with the given <code>basename</code>,
   * according to this strategy.</p>
   * <p>For properties files, if there is no match with the given locale (
   *  <code>basename + &quot;_&quot; + getLocale().toString()
   *  + &quot;.properties&quot;</code>) or
   *  {@link #getLocale()}returns <code>null</code>, we try the system default
   *  locale (<code>basename + &quot;_&quot;
   *  + Locale.{@link java.util.Locale#getDefault()}.toString()
   *  + &quot;.properties&quot;</code>).</p>
   * <p>If there is no match still, the default properties file is used (
   * <code>basename + &quot;.properties&quot;</code>).</p>
   * <p>The method returns <code>null</code> if no resource bundle was found.</p>
   */
  public ResourceBundle loadResourceBundle(final String basename) {
    ResourceBundle result = null;
    if ((basename != null) && (!basename.equals(EMPTY))) { //$NON-NLS-1$
      Locale locale = null;
      if (getLocale() != null) {
        locale = getLocale();
      }
      else {
        locale = Locale.getDefault();
      }

      try {
        result = ResourceBundle.getBundle(basename, locale);
        // throws MissingResourceException
      }
      catch (MissingResourceException mrExc) {
        // NOP we will return null
      }
    }
    return result;
  }

}
