package be.peopleware.value_I;


import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * <p>An enumeration type editor for {@link Locale}s.</p>
 * <p>For i18n, the features provided by {@link Locale}
 *   are used. The returned label is either in the
 *   requested language, if the {@link #getDisplayLocale()}
 *   is set, or in the language of the {@link #getValue()}
 *   displayed locale itself, if the {@link #getDisplayLocale()}
 *   is not set.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public class LocaleEditor
    extends DisplayLocaleBasedEnumerationValueEditor {

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


  /**
   * @return Locale.class;
   */
  public final Class getEnumerationValueType() {
    return Locale.class;
  }

  private static final Map LOCALE_MAP = new HashMap();

  static {
    Locale[] locales = Locale.getAvailableLocales();
    for (int i = 0; i < locales.length; i++) {
      Locale l = locales[i];
      LOCALE_MAP.put(l.toString(), l);
    }
    LOCALE_MAP.put(" ", new Locale(" "));  //$NON-NLS-1$ //$NON-NLS-2$
  }

  public final Map getValuesMap() {
    return LOCALE_MAP;
  }

  /**
   * If this is <code>true</code>, the label will only return
   * a label for the language part of the locale. If this is
   * <code>false</code>, the label will return a full label
   * that also displays the language variants.
   *
   * @basic
   * @init      false;
   */
  public final boolean isShortMode() {
    return $shortMode;
  }

  /**
   * @post      new.isShortMode() == shortMode;
   */
  public final void setShortMode(final boolean shortMode) {
    $shortMode = shortMode;
  }

  private boolean $shortMode;

  /**
   * @return    getValue().X((getDisplayLocale() == null)
   *                            ? getValue()
   *                            : getDisplayLocale());
   *            X is getDisplayName if !getShortMode(),
   *            getDisplayLanguage if getShortMode()
   *
   * @todo (jand): clean up contract
   */
  public final String getLabel() {
    String result = ""; //$NON-NLS-1$
    if ((getValue() != null) && (getValue() instanceof Locale)) {
      Locale localeToShow = (Locale)getValue();
      Locale localeInWhichToDisplay = (getDisplayLocale() == null)
                                          ? localeToShow
                                          : getDisplayLocale();
      if ($shortMode) {
        result = localeToShow.getDisplayLanguage(localeInWhichToDisplay);
      }
      else {
        result = localeToShow.getDisplayName(localeInWhichToDisplay);
      }
    }
    return result;
  }

}
