/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.value_II;

import java.util.Date;


/**
 * A period with start and end dates, which are only meaningful
 * to the level of the day. This is different from {@link Period}
 * in this respect, that human semantics differ between working
 * with a more accurate time description, or only on the level
 * of days. If you say &quot;we have meeting from 2 to 4&quot;,
 * people interpret this as the meeting ending at 4. If you say
 * &quot;we go on holiday from Monday to Wednesday&quot;, you
 * will be understood to be back at work Thursday, not Wednesday.
 * In the former case, the period is a right half-open interval.
 * In the latter case, the period is a closed interval. Also, a
 * period of 1 day is possible.
 *
 * The {@link #compareTo(Object) compare method} compares the
 * {@link #getStartDate()}.
 *
 * Normalization can happen at any time, and removes all time
 * information more accurate than the day from the encapsulated
 * {@link Date Dates}. This is not guaranteed. In any case,
 * you should depend on the time part that is more accurate than
 * the day level in dates returned from instances of this class.
 *
 * This class has the same interface as {@link Period}, except
 * for <code>Period.containsLeftInclusive(Date)}</code>.
 *
 * @author    Jan Dockx
 * @author    Peopleware NV
 *
 * @invar getStartDate() != null ? DateUtil.isDayDate(getStartDate());
 * @invar getEndDate() != null ? DateUtil.isDayDate(getEndDate());
 * @invar     (getStartDate() != null && getEndDate() != null)
 *                ? ! getStartDate().after(getEndDate());
 */
public class DayPeriod extends MutableValue implements Comparable {

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
   * Create a new empty period object.
   *
   * @post    getStartDate() == null;
   * @post    getEndDate() == null;
   */
  public DayPeriod() {
    // Since we demand of subtypes of MutableValue that they implement
    // {@link java.io.Serializable}, a default constructor is mandatory.
    // NOP
  }

  /*</construction>*/


  /*<property name="startDate">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public Date getStartDate() {
    if ($startDate == null) {
      return null;
    }
    else {
      return (Date)$startDate.clone();
    }
  }

  /**
   * @param     startDate
   *            The start date to set for this Period.
   * @post      startDate == null ? new.getStartDate() == null : new.getStartDate().equals(DateUtil.dayDate(startDate));
   * @throws    InvalidPeriodException pExc
   *            ( startDate != null
   *                && getEndDate() != null
   *                && startDate.after(getEndDate())
   *            )
   *              && DateUtil.sameDay(pExc.getStartDate(), startDate)
   *              && DateUtil.sameDay(pExc.getEndDate(), getEndDate())
   *              && pExc.getMessage()
   *                    .equals("The given start date is not before the current end date.");
   */
  public void setStartDate(final Date startDate) throws InvalidPeriodException {
    if (startDate != null
            && getEndDate() != null
            && startDate.after(getEndDate())
    ) {
      InvalidPeriodException ipe = new InvalidPeriodException(
          startDate, getEndDate(),
          "The given start date is not before the current end date."
      );
      throw ipe;
    }
    $startDate = DateUtil.dayDate(startDate);
  }

  private Date $startDate;

  /*</property>*/



  /*<property name="endDate">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public Date getEndDate() {
    if ($endDate == null) {
      return null;
    }
    else {
      return (Date)$endDate.clone();
    }
  }

  /**
   * @param     endDate
   *            The end date to set for this Period.
   * @post      endDate == null ? new.getEndDate() == null : new.getEndDate().equals(DateUtil.dayDate(endDate));
   * @throws    InvalidPeriodException pExc
   *            ( getStartDate() != null
   *                && endDate != null
   *                && getStartDate().after(endDate)
   *            )
   *              && sameDay(pExc.getStartDate(), getStartDate())
   *              && sameDay(pExc.getEndDate(), endDate)
   *              && pExc.getMessage()
   *                    .equals("The current start date is not before the given end date.");
   */
  public void setEndDate(final Date endDate) throws InvalidPeriodException {
    if (getStartDate() != null
          && endDate != null
          && getStartDate().after(endDate)
    ) {
      throw new InvalidPeriodException(
          getStartDate(), endDate,
          "The current start date is not before the given end date."
      );
    }
    $endDate = DateUtil.dayDate(endDate);
  }

  private Date $endDate;

  /*</property>*/



  /**
   * @return  result instanceof DayPeriod &&
   *          sameDay(getStartDate(), result.getStartDate()) &&
   *          sameDay(getEndDate(), result.getEndDate()) &&
   */
  public Object clone() {
    DayPeriod result = (DayPeriod)super.clone();
    result.$startDate = getStartDate();
    result.$endDate = getEndDate();
    return result;
  }

  /**
   * @return  o instanceof DayPeriod &&
   *          sameDay(getStartDate(), o.getStartDate()) &&
   *          sameDay(getEndDate(), o.getEndDate()) &&
   */
  public boolean equals(final Object o) {
    if (! (o instanceof DayPeriod)) {
      return false;
    }
    DayPeriod other = (DayPeriod) o;
    return DateUtil.sameDay($startDate, other.$startDate) &&
           DateUtil.sameDay($endDate, other.$endDate);
  }

  /**
   * @return  ( (getStartDate() == null)
   *               ? 0
   *               : getStartDate().hashCode()
   *          )
   *          +
   *          ( (getEndDate() == null)
   *               ? 0
   *               : getEndDate().hashCode()
   *          );
   */
  public int hashCode() {
    return (($startDate == null) ? 0 : $startDate.hashCode()) +
           (($endDate == null) ? 0 : $endDate.hashCode());
  }

  /**
   * @return  ((getStartDate() == null) ? "???" : getStartDate().toString())
   *          +
   *          " - "
   *          +
   *          ((getEndDate() == null) ? "???" : getEndDate().toString());
   */
  public String toString() {
    String start = ($startDate == null) ? "???" : $startDate.toString();
    String end = ($endDate == null) ? "???" : $endDate.toString();
    return start + " - " + end;
  }

  /**
   * @return  ( getStartDate() != null &&
   *            getEndDate() != null
   *          )
   *             ? (  (  getEndDate().getTime()
   *                     -
   *                     getStartDate().getTime()
   *                  )
   *                  /
   *                  (24*60*60*1000)
   *               )
   *             : -1;
   */
  public long getNbDaysInPeriod() {
    Date startDate = getStartDate();
    Date endDate = getEndDate();
    if (startDate != null && endDate != null) {
        long differenceInMillis =
          endDate.getTime() - startDate.getTime();
        return differenceInMillis / (24 * 60 * 60 * 1000);
    }
    return -1;
  }

  /**
   * @return  getNbDaysInPeriod();
   * @deprecated User {@link #getNbDaysInPeriod()} instead. It is the same method.
   */
  public long getNbDaysInPeriodInclusive() {
    return getNbDaysInPeriod();
  }

  /**
   * Compares this object with the specified object for order.
   *
   * @result  getStartDate() == null && ((Period)o).getStartDate() == null
   *            ==> result == 0;
   * @result  getStartDate() == null && ((Period)o).getStartDate() != null
   *            ==> result == -1;
   * @result  getStartDate() != null && ((Period)o).getStartDate() == null
   *            ==> result == 1;
   * @result  getStartDate() != null && ((Period)o).getStartDate() != null
   *            ==> getStartDate().compareTo(((Period)o).getStartDate());
   */
  public int compareTo(final Object o) {
    DayPeriod p = (DayPeriod)o; // ClassCastException ok
    if (getStartDate() == null) {
      if (p.getStartDate() == null) { // NullPointerException ok
        return 0;
      }
      else {
        return -1;
      }
    }
    else if (p.getStartDate() == null) {
      return 1;
    }
    else {
      return getStartDate().compareTo(p.getStartDate());
    }
  }

  /**
   * <code>date</code> is in this period, inclusive:
   * <code>date in [getStartDate(), getEndDate()]</code>.
   *
   * @return contains(date);
   * @deprecated User {@link #contains(Date)} instead.
   */
  public final boolean containsInclusive(final Date date) {
    return contains(date);
  }

  /**
   * <code>date</code> is in this period, inclusive:
   * <code>date in [getStartDate(), getEndDate()]</code>.
   *
   * @return (date != null) && (getStartDate() != null) && (getEndDate() != null) &&
   *            (!DateUtil.dayDate(date).before(getStartDate())) && (!DateUtil.dayDate(date).after(getEndDate()));
   */
  public boolean contains(final Date date) {
    if ((date == null) || ($startDate == null) || ($endDate == null)) {
      return false;
    }
    else {
      Date dayDate = DateUtil.dayDate(date);
      return (!dayDate.before($startDate)) && (!dayDate.after($endDate));
    }
  }

}
