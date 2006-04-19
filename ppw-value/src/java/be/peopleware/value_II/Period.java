/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.value_II;

import java.util.Date;


/**
 * A period has a start date and an end date.
 * The start date of a period is strictly before the end date (so a period
 * should not be empty).
 * The start date is included in the interval, the end date is not
 * (right half-open interval).
 *
 * The {@link #compareTo(Object) compare method} compares the
 * {@link #getStartDate()}.
 *
 * @author    nsmeets
 * @author    Peopleware NV
 *
 * @invar     (getStartDate() != null && getEndDate() != null)
 *                ? getStartDate().before(getEndDate())
 *                : true;
 *
 * @mudo (jand) must be a value, and move to ppw-value; suggest mutable; (add
 *       normalize method and getWildExceptions ?)
 * @mudo (nsmeets) Normalization does not seem to be a good idea.
 */
public class Period extends MutableValue implements Comparable {

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
  public Period() {
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
      return (Date) $startDate.clone();
    }
  }

  /**
   * @param     startDate
   *            The start date to set for this Period.
   * @post      (startDate == null)
   *               ? new.getStartDate() == null
   *               : new.getStartDate().equals(startDate);
   * @throws    InvalidPeriodException pExc
   *            ( startDate != null
   *                && getEndDate() != null
   *                && !startDate.before(getEndDate())
   *            )
   *              && (startDate == null)
   *                    ? (pExc.getStartDate() == null)
   *                    : (pExc.getStartDate().equals(startDate))
   *              && (getEndDate() == null)
   *                    ? (pExc.getEndDate() == null)
   *                    : (pExc.getEndDate().equals(getEndDate()))
   *              && pExc.getMessage()
   *                    .equals("The given start date is not before the current end date.");
   */
  public void setStartDate(final Date startDate) throws InvalidPeriodException {
    if (startDate != null
            && getEndDate() != null
            && !startDate.before(getEndDate())
    ) {
      InvalidPeriodException ipe = new InvalidPeriodException(
          startDate, getEndDate(),
          "The given start date is not before the current end date."
      );
      throw ipe;
    }
    $startDate = startDate;
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
      return (Date) $endDate.clone();
    }
  }

  /**
   * @param     endDate
   *            The end date to set for this Period.
   * @post      (endDate == null)
   *               ? new.getEndDate() == null
   *               : new.getEndDate().equals(endDate);
   * @throws    InvalidPeriodException pExc
   *            ( getStartDate() != null
   *                && endDate != null
   *                && !getStartDate().before(endDate)
   *            )
   *              && (getStartDate() == null)
   *                    ? (pExc.getStartDate() == null)
   *                    : (pExc.getStartDate().equals(getStartDate()))
   *              && (endDate == null)
   *                    ? (pExc.getEndDate() == null)
   *                    : (pExc.getEndDate().equals(endDate))
   *              && pExc.getMessage()
   *                    .equals("The current start date is not before the given end date.");
   */
  public void setEndDate(final Date endDate) throws InvalidPeriodException {
    if (getStartDate() != null
          && endDate != null
          && !getStartDate().before(endDate)
    ) {
      throw new InvalidPeriodException(
          getStartDate(), endDate,
          "The current start date is not before the given end date."
      );
    }
    $endDate = endDate;
  }

  private Date $endDate;

  /*</property>*/

  /**
   * @return  result instanceof Period
   *          &&
   *          (getStartDate() == null)
   *              ? result.getStartDate() == null
   *              : result.getStartDate().equals(getStartDate())
   *          &&
   *          (getEndDate() == null)
   *              ? result.getEndDate() == null
   *              : result.getEndDate().equals(getEndDate());
   */
  public Object clone() {
    Period result = new Period();
    try {
      result.setStartDate(getStartDate());
      result.setEndDate(getEndDate());
    }
    catch (InvalidPeriodException PExc) {
      assert false : "InvalidPeriodException on clone cannot happen.";
    }
    return result;
  }

  /**
   * @return  o instanceof Period &&
   *          (getStartDate() == null)
   *             ? o.getStartDate() == null
   *             : getStartDate().equals(o.getStartDate())
   *          &&
   *          (getEndDate() == null)
   *             ? o.getEndDate() == null
   *             : getEndDate().equals(o.getEndDate());
   */
  public boolean equals(final Object o) {
    if (!(o instanceof Period)) {
      return false;
    }
    Period other = (Period) o;
    return
      ((getStartDate() == null)
          ? (other.getStartDate() == null)
          : (getStartDate().equals(other.getStartDate()))
      )
      &&
      ((getEndDate() == null)
          ? (other.getEndDate() == null)
          : (getEndDate().equals(other.getEndDate()))
      );
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
    return
      ((getStartDate() == null)
         ? 0
         : getStartDate().hashCode()
      )
      +
      ((getEndDate() == null)
         ? 0
         : getEndDate().hashCode()
      );
  }

  /**
   * @return  ((getStartDate() == null) ? "???" : getStartDate().toString())
   *          +
   *          " - "
   *          +
   *          ((getEndDate() == null) ? "???" : getEndDate().toString());
   */
  public String toString() {
    String start = (getStartDate() == null) ? "???" : getStartDate().toString();
    String end = (getEndDate() == null) ? "???" : getEndDate().toString();
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
   * @return  ((getStartDate() != null) && (getEndDate() != null)) ? -1 : getNbDaysInPeriod() + 1;
   */
  public long getNbDaysInPeriodInclusive() {
    long ndip = getNbDaysInPeriod();
    return (ndip == -1) ? -1 : ndip + 1;
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
    Period p = (Period)o; // ClassCastException ok
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
   * <code>date</code> is in this period, half-inclusive:
   * <code>date in [getStartDate(), getEndDate()]</code>.
   *
   * @return (date == null) && (getStartDate() == null) && (getEndDate() == null) &&
   *            (! date.before(getStartDate())) && (! date.after(getEndDate()));
   */
  public final boolean containsInclusive(final Date date) {
    if ((date == null) || (getStartDate() == null) || (getEndDate() == null)) {
      return false;
    }
    else {
      return (!date.before(getStartDate())) && (!date.after(getEndDate()));
    }
  }


  /**
   * <code>date</code> is in this period, half-inclusive:
   * <code>date in [getStartDate(), getEndDate()[</code>.
   *
   * @return (date == null) && (getStartDate() == null) && (getEndDate() == null) &&
   *            (! date.before(getStartDate())) && date.before(getEndDate());
   */
  public final boolean containsLeftInclusive(final Date date) {
    if ((date == null) || (getStartDate() == null) || (getEndDate() == null)) {
      return false;
    }
    else {
      return (!date.before(getStartDate())) && date.before(getEndDate());
    }
  }

}
