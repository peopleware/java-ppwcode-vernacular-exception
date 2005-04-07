/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.value_I;


import java.util.regex.Pattern;

import be.peopleware.bean_IV.CompoundPropertyException;
import be.peopleware.bean_IV.PropertyException;


/**
 * A class of national numbers.
 *
 * @author nsmeets
 *
 * @invar  getLeftNumber() != null;
 * @invar  Pattern.matches(LEFT_PATTERN, getLeftNumber());
 * @invar  getMiddleNumber() != null;
 * @invar  Pattern.matches(MIDDLE_PATTERN, getMiddleNumber());
 * @invar  getRightNumber() != null;
 * @invar  Pattern.matches(RIGHT_PATTERN, getRightNumber());
 * @invar  checkNationalNumber(getLeftNumber(), getMiddleNumber(), getRightNumber());
 */
public final class NationalNumber extends ImmutableValue {

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
   * The pattern identifying the left part of a national number.
   */
  public static final String LEFT_PATTERN = "[0-9][0-9][0-9][0-9][0-9][0-9]";

  /**
   * The pattern identifying the middle part of a national number.
   */
  public static final String MIDDLE_PATTERN = "[0-9][0-9][0-9]";

  /**
   * The pattern identifying the right part of a national number.
   */
  public static final String RIGHT_PATTERN = "[0-9][0-9]";


  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @param   leftNumber
   *          The given left number to set.
   * @param   middleNumber
   *          The given middle number to set.
   * @param   rightNumber
   *          The given right number to set.
   * @post    getLeftNumber().equals(leftNumber);
   * @post    getMiddleNumber().equals(middleNumber);
   * @post    getRightNumber().equals(rightNumber);
   *
   * @throws  PropertyException pExc
   *          (leftNumber == null)
   *              && pExc.reportsOn(
   *                        NationalNumber.class, "leftNumber",
   *                        "LEFT_NUMBER_IS_NULL", null
   *                 );
   * @throws  PropertyException pExc
   *          (leftNumber != null && !Pattern.matches(LEFT_PATTERN, leftNumber))
   *              && pExc.reportsOn(
   *                        NationalNumber.class, "leftNumber",
   *                        "LEFT_NUMBER_INVALID_PATTERN", null
   *                 );
   * @throws  PropertyException pExc
   *          (middleNumber == null)
   *              && pExc.reportsOn(
   *                        NationalNumber.class, "middleNumber",
   *                        "MIDDLE_NUMBER_IS_NULL", null
   *                 );
   * @throws  PropertyException pExc
   *          (middleNumber != null && !Pattern.matches(MIDDLE_PATTERN, middleNumber))
   *              && pExc.reportsOn(
   *                        NationalNumber.class, "middleNumber",
   *                        "MIDDLE_NUMBER_INVALID_PATTERN", null
   *                 );
   * @throws  PropertyException pExc
   *          (rightNumber == null)
   *              && pExc.reportsOn(
   *                        NationalNumber.class, "rightNumber",
   *                        "RIGHT_NUMBER_IS_NULL", null
   *                 );
   * @throws  PropertyException pExc
   *          (rightNumber != null && !Pattern.matches(RIGHT_PATTERN, rightNumber))
   *              && pExc.reportsOn(
   *                        NationalNumber.class, "rightNumber",
   *                        "RIGHT_NUMBER_INVALID_PATTERN", null
   *                 );
   * @throws  PropertyException pExc
   *          (  leftNumber != null &&
   *             middleNumber != null &&
   *             rightNumber != null &&
   *             !checkNationalNumber(leftNumber, middleNumber, rightNumber)
   *          )
   *              && pExc.reportsOn(
   *                        NationalNumber.class, null,
   *                        "INVALID_CHECK", null
   *                 );
   */
  public NationalNumber(String leftNumber, String middleNumber, String rightNumber) throws PropertyException {
    initialize(leftNumber, middleNumber, rightNumber);
  }

  /**
   * @post    getLeftNumber().equals("000000");
   * @post    getMiddleNumber().equals("000");
   * @post    getRightNumber().equals("97");
   */
  public NationalNumber() {
    try {
      initialize("000000", "000", "97");
    }
    catch(PropertyException exc) {
      // shouldn't happen, because valid numbers are given
      assert false: "Shouldn't happen";
    }
  }

  /**
   * @post    getLeftNumber().equals(nationalNumber.getLeftNumber());
   * @post    getMiddleNumber().equals(nationalNumber.getMiddleNumber());
   * @post    getRightNumber().equals(nationalNumber.getRightNumber());
   */
  public NationalNumber(NationalNumber nationalNumber) throws PropertyException {
    initialize(
        nationalNumber.getLeftNumber(), nationalNumber.getMiddleNumber(),
        nationalNumber.getRightNumber()
    );
  }

  /*</construction */

  /**
   * For contract: see first constructor.
   */
  private void initialize(String leftNumber, String middleNumber, String rightNumber)
     throws PropertyException {
    CompoundPropertyException cpe =
      new CompoundPropertyException(NationalNumber.class, null, null, null);
    if (leftNumber == null) {
      cpe.addElementException(
          new PropertyException(NationalNumber.class, "leftNumber", "LEFT_NUMBER_IS_NULL", null)
      );
    }
    if (leftNumber != null && !Pattern.matches(LEFT_PATTERN, leftNumber)) {
      cpe.addElementException(
          new PropertyException(NationalNumber.class, "leftNumber", "LEFT_NUMBER_INVALID_PATTERN", null)
      );
    }
    if (middleNumber == null) {
      cpe.addElementException(
          new PropertyException(NationalNumber.class, "middleNumber", "MIDDLE_NUMBER_IS_NULL", null)
      );
    }
    if (middleNumber != null && !Pattern.matches(MIDDLE_PATTERN, middleNumber)) {
      cpe.addElementException(
          new PropertyException(NationalNumber.class, "middleNumber", "MIDDLE_NUMBER_INVALID_PATTERN", null)
      );
    }
    if (rightNumber == null) {
      cpe.addElementException(
          new PropertyException(NationalNumber.class, "rightNumber", "RIGHT_NUMBER_IS_NULL", null)
      );
    }
    if (rightNumber != null && !Pattern.matches(RIGHT_PATTERN, rightNumber)) {
      cpe.addElementException(
          new PropertyException(NationalNumber.class, "rightNumber", "RIGHT_NUMBER_INVALID_PATTERN", null)
      );
    }
    if (leftNumber != null &&
        middleNumber != null &&
        rightNumber != null &&
        !NationalNumber.checkNationalNumber(leftNumber, middleNumber, rightNumber)
    ) {
      cpe.addElementException(
          new PropertyException(NationalNumber.class, null, "INVALID_CHECK", null)
      );
    }
    cpe.throwIfNotEmpty();
    $leftNumber   = leftNumber;
    $middleNumber = middleNumber;
    $rightNumber  = rightNumber;
  }

  /*<property name="leftNumber">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final String getLeftNumber() {
    return $leftNumber;
  }

  /**
   * @invar  $leftNumber != null;
   * @invar  Pattern.matches(LEFT_PATTERN, $leftNumber);
   */
  private String $leftNumber;

  /*</property>*/

  /*<property name="middleNumber">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final String getMiddleNumber() {
    return $middleNumber;
  }

  /**
   * @invar  $middleNumber != null;
   * @invar  Pattern.matches(MIDDLE_PATTERN, $middleNumber);
   */
  private String $middleNumber;

  /*</property>*/

  /*<property name="rightNumber">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final String getRightNumber() {
    return $rightNumber;
  }

  /**
   * @invar  $rightNumber != null;
   * @invar  Pattern.matches(RIGHT_PATTERN, $middleNumber);
   */
  private String $rightNumber;

  /*</property>*/

  /**
   * @param   leftNumber
   *          The left part of a national number.
   * @param   middleNumber
   *          The middle part of a national number.
   * @param   rightNumber
   *          The right part of a national number.
   * @pre     leftNumber != null;
   * @pre     Pattern.matches(LEFT_PATTERN, leftNumber);
   * @pre     middleNumber != null;
   * @pre     Pattern.matches(MIDDLE_PATTERN, middleNumber);
   * @pre     rightNumber != null;
   * @pre     Pattern.matches(RIGHT_PATTERN, rightNumber);
   * @return  (97 - Integer.parseInt(leftNumber + middleNumber) % 97) == (Integer.parseInt(rightNumber));
   */
  public static final boolean checkNationalNumber(String leftNumber, String middleNumber, String rightNumber) {
    String left = leftNumber + middleNumber;
    int first = Integer.parseInt(left);
    int second = Integer.parseInt(rightNumber);
    return (97 - first % 97) == (second);
  }

  /**
   * @return  o instanceof NationalNumber &&
   *          ((NationalNumber) o).getLeftNumber().equals(getLeftNumber()) &&
   *          ((NationalNumber) o).getMiddleNumber().equals(getMiddleNumber()) &&
   *          ((NationalNumber) o).getRightNumber().equals(getRightNumber());
   */
  public boolean equals(Object o) {
    return
      o instanceof NationalNumber &&
      ((NationalNumber) o).getLeftNumber().equals(getLeftNumber()) &&
      ((NationalNumber) o).getMiddleNumber().equals(getMiddleNumber()) &&
      ((NationalNumber) o).getRightNumber().equals(getRightNumber());
  }

  /**
   * @return  getLeftNumber().hashCode() +
   *          getMiddleNumber().hashCode() +
   *          getRightNumber().hashCode();
   */
  public int hashCode() {
    return 
      getLeftNumber().hashCode() +
      getMiddleNumber().hashCode() +
      getRightNumber().hashCode();
  }

  /**
   * return  getLeftNumber() + " " + getMiddleNumber() + " " + getRightNumber();
   */
  public String toString() {
    return getLeftNumber() + " " + getMiddleNumber() + " " + getRightNumber();
  }

}
