/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.value_II;


import java.util.regex.Pattern;

import be.peopleware.bean_V.CompoundPropertyException;
import be.peopleware.bean_V.PropertyException;


/**
 * A class of VAT numbers.
 *
 * @author nsmeets
 *
 * @invar  getLeftNumber() != null;
 * @invar  Pattern.matches(LEFT_PATTERN, getLeftNumber());
 * @invar  getMiddleNumber() != null;
 * @invar  Pattern.matches(MIDDLE_PATTERN, getMiddleNumber());
 * @invar  getRightNumber() != null;
 * @invar  Pattern.matches(RIGHT_PATTERN, getRightNumber());
 * @invar  checkVATNumber(getLeftNumber(), getMiddleNumber(), getRightNumber());
 */
public final class VATNumber extends ImmutableValue {

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
   * The pattern identifying the left part of a national number.
   */
  public static final String LEFT_PATTERN = "[0-9][0-9][0-9]";

  /**
   * The pattern identifying the middle part of a national number.
   */
  public static final String MIDDLE_PATTERN = "[0-9][0-9][0-9]";

  /**
   * The pattern identifying the right part of a national number.
   */
  public static final String RIGHT_PATTERN = "[0-9][0-9][0-9]";


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
   *                        VATNumber.class, "leftNumber",
   *                        "LEFT_NUMBER_IS_NULL", null
   *                 );
   * @throws  PropertyException pExc
   *          (leftNumber != null && !Pattern.matches(LEFT_PATTERN, leftNumber))
   *              && pExc.reportsOn(
   *                        VATNumber.class, "leftNumber",
   *                        "LEFT_NUMBER_INVALID_PATTERN", null
   *                 );
   * @throws  PropertyException pExc
   *          (middleNumber == null)
   *              && pExc.reportsOn(
   *                        VATNumber.class, "middleNumber",
   *                        "MIDDLE_NUMBER_IS_NULL", null
   *                 );
   * @throws  PropertyException pExc
   *          (middleNumber != null && !Pattern.matches(MIDDLE_PATTERN, middleNumber))
   *              && pExc.reportsOn(
   *                        VATNumber.class, "middleNumber",
   *                        "MIDDLE_NUMBER_INVALID_PATTERN", null
   *                 );
   * @throws  PropertyException pExc
   *          (rightNumber == null)
   *              && pExc.reportsOn(
   *                        VATNumber.class, "rightNumber",
   *                        "RIGHT_NUMBER_IS_NULL", null
   *                 );
   * @throws  PropertyException pExc
   *          (rightNumber != null && !Pattern.matches(RIGHT_PATTERN, rightNumber))
   *              && pExc.reportsOn(
   *                        VATNumber.class, "rightNumber",
   *                        "RIGHT_NUMBER_INVALID_PATTERN", null
   *                 );
   * @throws  PropertyException pExc
   *          (  leftNumber != null &&
   *             middleNumber != null &&
   *             rightNumber != null &&
   *             !checkVATNumber(leftNumber, middleNumber, rightNumber)
   *          )
   *              && pExc.reportsOn(
   *                        VATNumber.class, null,
   *                        "INVALID_CHECK", null
   *                 );
   */
  public VATNumber(String leftNumber, String middleNumber, String rightNumber) throws PropertyException {
    initialize(leftNumber, middleNumber, rightNumber);
  }

  /**
   * Construct a new instance from a String, which we interpret
   * leniently.
   *
   * @todo contract & Toryt
   */
  public VATNumber(String pattern) throws PropertyException {
    if (pattern == null) {
      throw new PropertyException(VATNumber.class, null, "NULL_PATTERN", null);
    }
    String[] array = pattern.split("[ -/|*:]+");
        // PatternSyntaxException: cannot happen
    StringBuffer buffer = new StringBuffer("");
    for (int i = 0; i < array.length; i++) {
      buffer.append(array[i]);
    }
    String nnString = buffer.toString();
    try {
      String leftNumber = nnString.substring(0,3);
      String middleNumber = nnString.substring(3,6);
      String rightNumber = nnString.substring(6,9);
          // IndexOutOfBoundsException
      initialize(leftNumber, middleNumber, rightNumber);
    }
    catch (IndexOutOfBoundsException ioobExc) {
      throw new PropertyException(VATNumber.class, null, "PATTERN_TOO_SHORT", ioobExc);
    }
  }

  /**
   * @post    getLeftNumber().equals("123");
   * @post    getMiddleNumber().equals("456");
   * @post    getRightNumber().equals("749");
   */
  public VATNumber() {
    try {
      initialize("123", "456", "749");
    }
    catch(PropertyException exc) {
      // shouldn't happen, because valid numbers are given
      assert false: "Shouldn't happen";
    }
  }

  /**
   * @post    getLeftNumber().equals(vatNumber.getLeftNumber());
   * @post    getMiddleNumber().equals(vatNumber.getMiddleNumber());
   * @post    getRightNumber().equals(vatNumber.getRightNumber());
   */
  public VATNumber(VATNumber vatNumber) throws PropertyException {
    initialize(
        vatNumber.getLeftNumber(), vatNumber.getMiddleNumber(),
        vatNumber.getRightNumber()
    );
  }

  /*</construction */

  /**
   * For contract: see first constructor.
   */
  private void initialize(String leftNumber, String middleNumber, String rightNumber)
     throws PropertyException {
    CompoundPropertyException cpe =
      new CompoundPropertyException(VATNumber.class, null, null, null);
    if (leftNumber == null) {
      cpe.addElementException(
          new PropertyException(VATNumber.class, "leftNumber", "LEFT_NUMBER_IS_NULL", null)
      );
    }
    if (leftNumber != null && !Pattern.matches(LEFT_PATTERN, leftNumber)) {
      cpe.addElementException(
          new PropertyException(VATNumber.class, "leftNumber", "LEFT_NUMBER_INVALID_PATTERN", null)
      );
    }
    if (middleNumber == null) {
      cpe.addElementException(
          new PropertyException(VATNumber.class, "middleNumber", "MIDDLE_NUMBER_IS_NULL", null)
      );
    }
    if (middleNumber != null && !Pattern.matches(MIDDLE_PATTERN, middleNumber)) {
      cpe.addElementException(
          new PropertyException(VATNumber.class, "middleNumber", "MIDDLE_NUMBER_INVALID_PATTERN", null)
      );
    }
    if (rightNumber == null) {
      cpe.addElementException(
          new PropertyException(VATNumber.class, "rightNumber", "RIGHT_NUMBER_IS_NULL", null)
      );
    }
    if (rightNumber != null && !Pattern.matches(RIGHT_PATTERN, rightNumber)) {
      cpe.addElementException(
          new PropertyException(VATNumber.class, "rightNumber", "RIGHT_NUMBER_INVALID_PATTERN", null)
      );
    }
    if (leftNumber != null &&
        middleNumber != null &&
        rightNumber != null &&
        !VATNumber.checkVATNumber(leftNumber, middleNumber, rightNumber)
    ) {
      cpe.addElementException(
          new PropertyException(VATNumber.class, null, "INVALID_CHECK", null)
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
   * @return  (  (  Integer.parseInt(leftNumber + middleNumber + rightNumber.substring(0,1))
   *                +
   *                Integer.parseInt(rightNumber.substring(1))
   *             ) % 97
   *             == 0
   *          );
   */
  public static final boolean checkVATNumber(String leftNumber, String middleNumber, String rightNumber) {
    String first7String = leftNumber + middleNumber + rightNumber.substring(0,1);
    String last2String = rightNumber.substring(1);
    int first7Int = Integer.parseInt(first7String);
    int last2Int = Integer.parseInt(last2String);
    return ((first7Int + last2Int) % 97) == 0;
  }

  /**
   * @return  o instanceof VATNumber &&
   *          ((VATNumber) o).getLeftNumber().equals(getLeftNumber()) &&
   *          ((VATNumber) o).getMiddleNumber().equals(getMiddleNumber()) &&
   *          ((VATNumber) o).getRightNumber().equals(getRightNumber());
   */
  public boolean equals(Object o) {
    return
      o instanceof VATNumber &&
      ((VATNumber) o).getLeftNumber().equals(getLeftNumber()) &&
      ((VATNumber) o).getMiddleNumber().equals(getMiddleNumber()) &&
      ((VATNumber) o).getRightNumber().equals(getRightNumber());
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

  public static void main(String args[])  {
    System.out.println("in main");
    String left, middle, right;
    left = "123"; middle = "456"; right = "749";
    try {
      new VATNumber(left, middle, right);
      System.out.println("Created VAT number ("+left+", "+middle+", "+right+")");
    }
    catch(PropertyException exc) {
      System.out.println("Error when creating VAT number ("+left+", "+middle+", "+right+"):"+exc);
    }
    left = "222"; middle = "333"; right = "403";
    try {
      new VATNumber(left, middle, right);
      System.out.println("Created VAT number ("+left+", "+middle+", "+right+")");
    }
    catch(PropertyException exc) {
      System.out.println("Error when creating VAT number ("+left+", "+middle+", "+right+"):"+exc);
    }
    left = "999"; middle = "888"; right = "767";
    try {
      new VATNumber(left, middle, right);
      System.out.println("Created VAT number ("+left+", "+middle+", "+right+")");
    }
    catch(PropertyException exc) {
      System.out.println("Error when creating VAT number ("+left+", "+middle+", "+right+"):"+exc);
    }
    left = "123"; middle = "456"; right = "750";
    try {
      new VATNumber(left, middle, right);
      System.out.println("Created VAT number ("+left+", "+middle+", "+right+")");
    }
    catch(PropertyException exc) {
      System.out.println("Error when creating VAT number ("+left+", "+middle+", "+right+"):"+exc);
    }
    left = "1234"; middle = "56"; right = "789";
    try {
      new VATNumber(left, middle, right);
      System.out.println("Created VAT number ("+left+", "+middle+", "+right+")");
    }
    catch(PropertyException exc) {
      System.out.println("Error when creating VAT number ("+left+", "+middle+", "+right+"):"+exc);
    }
  }
}
