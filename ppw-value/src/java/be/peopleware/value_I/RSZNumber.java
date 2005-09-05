/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.value_I;


import java.util.regex.Pattern;

import be.peopleware.bean_V.CompoundPropertyException;
import be.peopleware.bean_V.PropertyException;


/**
 * A class of RSZ numbers.
 *
 * @author nsmeets
 *
 * @invar  getLeftNumber() != null;
 * @invar  Pattern.matches(LEFT_PATTERN, getLeftNumber());
 * @invar  getMiddleNumber() != null;
 * @invar  Pattern.matches(MIDDLE_PATTERN, getMiddleNumber());
 * @invar  getRightNumber() != null;
 * @invar  Pattern.matches(RIGHT_PATTERN, getRightNumber());
 * @invar  checkRSZNumber(getLeftNumber(), getMiddleNumber(), getRightNumber());
 */
public final class RSZNumber extends ImmutableValue {

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

  // @mudo (nsmeets) Are these patterns correct? We cannot find the correct
  //       patterns in the FVB documentation.

  /**
   * The pattern identifying the left part of a rsz number.
   */
  public static final String LEFT_PATTERN = "[0-9][0-9][0-9]";

  /**
   * The pattern identifying the middle part of a rsz number.
   */
  public static final String MIDDLE_PATTERN = "[0-9][0-9][0-9][0-9][0-9][0-9][0-9]";

  /**
   * The pattern identifying the right part of a rsz number.
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
   *                        RSZNumber.class, "leftNumber",
   *                        "LEFT_NUMBER_IS_NULL", null
   *                 );
   * @throws  PropertyException pExc
   *          (leftNumber != null && !Pattern.matches(LEFT_PATTERN, leftNumber))
   *              && pExc.reportsOn(
   *                        RSZNumber.class, "leftNumber",
   *                        "LEFT_NUMBER_INVALID_PATTERN", null
   *                 );
   * @throws  PropertyException pExc
   *          (middleNumber == null)
   *              && pExc.reportsOn(
   *                        RSZNumber.class, "middleNumber",
   *                        "MIDDLE_NUMBER_IS_NULL", null
   *                 );
   * @throws  PropertyException pExc
   *          (middleNumber != null && !Pattern.matches(MIDDLE_PATTERN, middleNumber))
   *              && pExc.reportsOn(
   *                        RSZNumber.class, "middleNumber",
   *                        "MIDDLE_NUMBER_INVALID_PATTERN", null
   *                 );
   * @throws  PropertyException pExc
   *          (rightNumber == null)
   *              && pExc.reportsOn(
   *                        RSZNumber.class, "rightNumber",
   *                        "RIGHT_NUMBER_IS_NULL", null
   *                 );
   * @throws  PropertyException pExc
   *          (rightNumber != null && !Pattern.matches(RIGHT_PATTERN, rightNumber))
   *              && pExc.reportsOn(
   *                        RSZNumber.class, "rightNumber",
   *                        "RIGHT_NUMBER_INVALID_PATTERN", null
   *                 );
   * @throws  PropertyException pExc
   *          (  leftNumber != null &&
   *             middleNumber != null &&
   *             rightNumber != null &&
   *             !checkRSZNumber(leftNumber, middleNumber, rightNumber)
   *          )
   *              && pExc.reportsOn(
   *                        RSZNumber.class, null,
   *                        "INVALID_CHECK", null
   *                 );
   */
  public RSZNumber(String leftNumber, String middleNumber, String rightNumber) throws PropertyException {
    initialize(leftNumber, middleNumber, rightNumber);
  }

  /**
   * @post    getLeftNumber().equals("024");
   * @post    getMiddleNumber().equals("1234567");
   * @post    getRightNumber().equals("49");
   */
  public RSZNumber() {
    try {
      initialize("024", "1234567", "49");
    }
    catch(PropertyException exc) {
      // shouldn't happen, because valid numbers are given
      assert false: "Shouldn't happen";
    }
  }

  /**
   * @post    getLeftNumber().equals(rszNumber.getLeftNumber());
   * @post    getMiddleNumber().equals(rszNumber.getMiddleNumber());
   * @post    getRightNumber().equals(rszNumber.getRightNumber());
   */
  public RSZNumber(RSZNumber rszNumber) throws PropertyException {
    initialize(
        rszNumber.getLeftNumber(), rszNumber.getMiddleNumber(),
        rszNumber.getRightNumber()
    );
  }

  /*</construction */

  /**
   * For contract: see first constructor.
   */
  private void initialize(String leftNumber, String middleNumber, String rightNumber)
     throws PropertyException {
    CompoundPropertyException cpe =
      new CompoundPropertyException(RSZNumber.class, null, null, null);
    if (leftNumber == null) {
      cpe.addElementException(
          new PropertyException(RSZNumber.class, "leftNumber", "LEFT_NUMBER_IS_NULL", null)
      );
    }
    if (leftNumber != null && !Pattern.matches(LEFT_PATTERN, leftNumber)) {
      cpe.addElementException(
          new PropertyException(RSZNumber.class, "leftNumber", "LEFT_NUMBER_INVALID_PATTERN", null)
      );
    }
    if (middleNumber == null) {
      cpe.addElementException(
          new PropertyException(RSZNumber.class, "middleNumber", "MIDDLE_NUMBER_IS_NULL", null)
      );
    }
    if (middleNumber != null && !Pattern.matches(MIDDLE_PATTERN, middleNumber)) {
      cpe.addElementException(
          new PropertyException(RSZNumber.class, "middleNumber", "MIDDLE_NUMBER_INVALID_PATTERN", null)
      );
    }
    if (rightNumber == null) {
      cpe.addElementException(
          new PropertyException(RSZNumber.class, "rightNumber", "RIGHT_NUMBER_IS_NULL", null)
      );
    }
    if (rightNumber != null && !Pattern.matches(RIGHT_PATTERN, rightNumber)) {
      cpe.addElementException(
          new PropertyException(RSZNumber.class, "rightNumber", "RIGHT_NUMBER_INVALID_PATTERN", null)
      );
    }
    if (leftNumber != null &&
        middleNumber != null &&
        rightNumber != null &&
        !RSZNumber.checkRSZNumber(leftNumber, middleNumber, rightNumber)
    ) {
      cpe.addElementException(
          new PropertyException(RSZNumber.class, null, "INVALID_CHECK", null)
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
   *          The left part of a rsz number.
   * @param   middleNumber
   *          The middle part of a rsz number.
   * @param   rightNumber
   *          The right part of a rsz number.
   * @pre     leftNumber != null;
   * @pre     Pattern.matches(LEFT_PATTERN, leftNumber);
   * @pre     middleNumber != null;
   * @pre     Pattern.matches(MIDDLE_PATTERN, middleNumber);
   * @pre     rightNumber != null;
   * @pre     Pattern.matches(RIGHT_PATTERN, rightNumber);
   * @return  (  Integer.parseInt(rightNumber)
   *             ==
   *             ( ( 96 - ( (Integer.parseInt(middleNumber) * 100 ) % 97 ) ) == 0)
   *                 ? (97)
   *                 : ( 96 - ( (Integer.parseInt(middleNumber) * 100 ) % 97 ) )
   *          );
   */
  public static final boolean checkRSZNumber(String leftNumber, String middleNumber, String rightNumber) {
    int middle = Integer.parseInt(middleNumber);
    int right = Integer.parseInt(rightNumber);
    int modulo = 96 - ( (middle * 100) % 97 );
    if (modulo == 0) {
      modulo = 97;
    }
    return (right == modulo);
  }

  /**
   * @return  o instanceof RSZNumber &&
   *          ((RSZNumber) o).getLeftNumber().equals(getLeftNumber()) &&
   *          ((RSZNumber) o).getMiddleNumber().equals(getMiddleNumber()) &&
   *          ((RSZNumber) o).getRightNumber().equals(getRightNumber());
   */
  public boolean equals(Object o) {
    return
      o instanceof RSZNumber &&
      ((RSZNumber) o).getLeftNumber().equals(getLeftNumber()) &&
      ((RSZNumber) o).getMiddleNumber().equals(getMiddleNumber()) &&
      ((RSZNumber) o).getRightNumber().equals(getRightNumber());
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

//  public static void main(String args[])  {
//    System.out.println("in main");
//    String left, middle, right;
//    left = "024"; middle = "1234567"; right = "49";
//    try {
//      new RSZNumber(left, middle, right);
//      System.out.println("Created RSZ number ("+left+", "+middle+", "+right+")");
//    }
//    catch(PropertyException exc) {
//      System.out.println("Error when creating RSZ number ("+left+", "+middle+", "+right+"):"+exc);
//    }
//    left = "024"; middle = "2223334"; right = "08";
//    try {
//      new RSZNumber(left, middle, right);
//      System.out.println("Created RSZ number ("+left+", "+middle+", "+right+")");
//    }
//    catch(PropertyException exc) {
//      System.out.println("Error when creating RSZ number ("+left+", "+middle+", "+right+"):"+exc);
//    }
//    left = "024"; middle = "9998887"; right = "06";
//    try {
//      new RSZNumber(left, middle, right);
//      System.out.println("Created RSZ number ("+left+", "+middle+", "+right+")");
//    }
//    catch(PropertyException exc) {
//      System.out.println("Error when creating RSZ number ("+left+", "+middle+", "+right+"):"+exc);
//    }
//    left = "024"; middle = "1234567"; right = "50";
//    try {
//      new RSZNumber(left, middle, right);
//      System.out.println("Created RSZ number ("+left+", "+middle+", "+right+")");
//    }
//    catch(PropertyException exc) {
//      System.out.println("Error when creating RSZ number ("+left+", "+middle+", "+right+"):"+exc);
//    }
//    left = "0246"; middle = "123456"; right = "789";
//    try {
//      new RSZNumber(left, middle, right);
//      System.out.println("Created RSZ number ("+left+", "+middle+", "+right+")");
//    }
//    catch(PropertyException exc) {
//      System.out.println("Error when creating RSZ number ("+left+", "+middle+", "+right+"):"+exc);
//    }
//  }
}
