package be.peopleware.test_I.java.util;


import be.peopleware.test_I.CaseProvider;
import java.util.Locale;
import java.util.Set;
import java.util.HashSet;


/**
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public class _Test_Locale extends CaseProvider {

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


  public Set getCases() {
    Set result = new HashSet();
    result.add(Locale.CANADA);
    result.add(Locale.CANADA_FRENCH);
    result.add(Locale.CHINA);
    result.add(Locale.CHINESE);
    result.add(Locale.ENGLISH);
    result.add(Locale.FRANCE);
    result.add(Locale.FRENCH);
    result.add(Locale.GERMAN);
    result.add(Locale.GERMANY);
    result.add(Locale.ITALIAN);
    result.add(Locale.ITALY);
    result.add(Locale.JAPAN);
    result.add(Locale.JAPANESE);
    result.add(Locale.KOREA);
    result.add(Locale.KOREAN);
    result.add(Locale.PRC);
    result.add(Locale.SIMPLIFIED_CHINESE);
    result.add(Locale.TAIWAN);
    result.add(Locale.TRADITIONAL_CHINESE);
    result.add(Locale.UK);
    result.add(Locale.US);
    result.add(new Locale("nl")); //$NON-NLS-1$
    result.add(new Locale("nl", "BE"));  //$NON-NLS-1$//$NON-NLS-2$
    result.add(new Locale("nl", "NL"));  //$NON-NLS-1$//$NON-NLS-2$
    result.add(new Locale("fr")); //$NON-NLS-1$
    result.add(new Locale("fr", "BE"));  //$NON-NLS-1$//$NON-NLS-2$
    result.add(new Locale("fr", "FR"));  //$NON-NLS-1$//$NON-NLS-2$
    result.add(new Locale("de")); //$NON-NLS-1$
    result.add(new Locale("de", "BE"));  //$NON-NLS-1$//$NON-NLS-2$
    result.add(new Locale("de", "DE"));  //$NON-NLS-1$//$NON-NLS-2$
    result.add(new Locale("qq"));  //$NON-NLS-1$
    return result;
  }

}
