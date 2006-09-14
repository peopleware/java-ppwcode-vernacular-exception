/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.value_II;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * Static methods for working with dates
 *
 * @author    Jan Dockx
 * @author    Peopleware NV
 */
public class DateUtil {

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


  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * Don't instantiate this class.
   */
  private DateUtil() {
    // NOP
  }

  /*</construction>*/



  /**
   * @return (date1 == null) ? (date2 == null) : dayDate(date1).equals(dayDate(date2));
   */
  public static boolean sameDay(Date date1, Date date2) {
    if ((date1 != null) && (date2 != null)) {
      GregorianCalendar cal1 = new GregorianCalendar();
      cal1.setTime(date1);
      GregorianCalendar cal2 = new GregorianCalendar();
      cal2.setTime(date2);
      return fieldEqual(cal1, cal2, Calendar.YEAR) &&
             fieldEqual(cal1, cal2, Calendar.MONTH) &&
             fieldEqual(cal1, cal2, Calendar.DAY_OF_MONTH);
    }
    else {
      return date1 == date2; // they both have to be null
    }
  }

  /**
   * Change <code>date</code> so that it has no time information
   * more accurate then the day level. More accurate information
   * is set to <code>0</code>.
   *
   * @result date != null ? result != date;
   * @result date == null ? result == null;
   * @result date != null ? result.getYear() == date.getYear();
   * @result date != null ? result.getMonth() == date.getMonth();
   * @result date != null ? result.getDay() == date.getDay();
   * @result date != null ? result.getHours() == 0;
   * @result date != null ? result.getMinutes() == 0;
   * @result date != null ? result.getSeconds() == 0;
   * @result date != null ? result.getMilliSeconds() == 0;
   *
   * @mudo don't use deprecated methods in contract
   */
  public static Date dayDate(Date date) {
    if (date == null) {
      return null;
    }
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(date);
    cal.clear(Calendar.HOUR);
    cal.clear(Calendar.MINUTE);
    cal.clear(Calendar.SECOND);
    cal.clear(Calendar.MILLISECOND);
    return cal.getTime();
  }

  /**
   * @return (date != null) && date.equals(dayDate(date));
   */
  public static boolean isDayDate(Date date) {
    if (date == null) {
      return false;
    }
    else {
      GregorianCalendar cal = new GregorianCalendar();
      cal.setTime(date);
      return notSetOr0(cal, Calendar.HOUR) &&
             notSetOr0(cal, Calendar.MINUTE) &&
             notSetOr0(cal, Calendar.SECOND) &&
             notSetOr0(cal, Calendar.MILLISECOND);
    }
  }

  private static boolean notSetOr0(Calendar cal, int field) {
    assert cal != null;
    return (! cal.isSet(field)) || (cal.get(field) == 0);
  }

  private static boolean fieldEqual(Calendar cal1, Calendar cal2, int field) {
    assert cal1 != null;
    assert cal2 != null;
    return (! cal1.isSet(field)) ?
             (! cal2.isSet(field)) :
             (cal2.isSet(field) && (cal1.get(field) == cal2.get(field)));
  }

}
