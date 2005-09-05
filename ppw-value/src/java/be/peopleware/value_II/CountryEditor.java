package be.peopleware.value_II;


import java.io.Serializable;
import java.util.Locale;
import java.util.Map;


/**
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public class CountryEditor
    extends DisplayLocaleBasedEnumerationValueEditor implements Serializable {

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
   * @return    Locale.class;
   */
  public final Class getEnumerationValueType() {
    return Country.class;
  }

  public final Map getValuesMap() {
    return Country.VALUES;
  }

  /**
   * The name of the country in the language of {@link #getDisplayLocale()}.
   * If there is no display locale, the name of the country in English.
   *
   * @return    new Locale("en", toString())
   *                .getDisplayCountry((getDisplayLocale() == null)
   *                                      ? new Locale("en", toString())
   *                                      : getDisplayLocale());
   *
   * @idea (jand): Try to find a way to return, as default, the name of the
   *               country in its main language. although, that is political.
   */
  public final String getLabel() {
    String result = ""; //$NON-NLS-1$
    if ((getValue() != null) && (getValue() instanceof Country)) {
      Country country = (Country)getValue();
      Locale localeToShow = new Locale("en", country.toString()); //$NON-NLS-1$
            /* language is just a default (null is not accepted);
             * we are only interested in the country setting */
      result = localeToShow.getDisplayCountry((getDisplayLocale() == null)
                                                  ? localeToShow
                                                  : getDisplayLocale());
    }
    return result;
  }

}
