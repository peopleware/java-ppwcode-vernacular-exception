package be.peopleware.test_I.java.util;


import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import be.peopleware.test_I.CaseProvider;


/**
 * @author    David Van Keer
 * @author    PeopleWare n.v.
 */
public class _Test_Date extends CaseProvider {

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

  /** Number of millisecongs in a day. */
  private static final long DAY     = 86400000L;
  /** Number of milliseconds in a week. */
  private static final long WEEK    = 604800000L;
  /** Number of milliseconds in a month. */
  private static final long MONTH   = 2629743830L;
  /** Number of milliseconds in a year. */
  private static final long YEAR    = 31556926000L;
  /** Number of milliseconds in a decade. */
  private static final long DECADE  = 3155692597470L;

  public Set getCases() {
    Set result = new HashSet();
    result.add(new Date(System.currentTimeMillis()));           // Today
    result.add(new Date(System.currentTimeMillis() - DAY));     // Yesterday
    result.add(new Date(System.currentTimeMillis() + DAY));     // Tommorow
    result.add(new Date(System.currentTimeMillis() - WEEK));    // Last Week
    result.add(new Date(System.currentTimeMillis() + WEEK));    // Next Week
    result.add(new Date(System.currentTimeMillis() - MONTH));   // Last Month
    result.add(new Date(System.currentTimeMillis() + MONTH));   // Next Month
    result.add(new Date(System.currentTimeMillis() - YEAR));    // Last year
    result.add(new Date(System.currentTimeMillis() + YEAR));    // Next year
    result.add(new Date(System.currentTimeMillis() - DECADE));  // Last decade
    result.add(new Date(System.currentTimeMillis() + DECADE));  // Next decade
    result.add(new Date(0));                                    // Unix Epoch
    result.add(new Date(Long.MAX_VALUE));
    return result;
  }

  public Set getSomeCases() {
    Set result = new HashSet();
    result.add(new Date(System.currentTimeMillis()));           // Today
    result.add(new Date(System.currentTimeMillis() - DAY));     // Yesterday
    result.add(new Date(System.currentTimeMillis() + DAY));     // Tommorow
    result.add(new Date(0));                                    // Unix Epoch
    return result;
  }
}
