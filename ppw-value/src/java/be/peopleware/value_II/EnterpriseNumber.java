/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.value_II;


import java.util.regex.Pattern;

import be.peopleware.bean_V.CompoundPropertyException;
import be.peopleware.bean_V.PropertyException;


/**
 * <p><dfn>Enterprise numbers</dfn> (<dfn xml:lang="nl-BE">ondernemningsnummers</dfn>,
 *   <dfn xml:lang="fr-BE">num&eacute;ro d'entreprise</dfn>) uniquely identify enterprises for
 *   the Belgian Federal Government. Other names for <dfn>Enterprise numbers</dfn> are
 *   <dfn xml:lang="nl-BE">KBO-nummers</dfn> or <dfn xml:lang="fr-BE">num&eacute;ro BCE</dfn>.</p>
 * <p><dfn>Enterprise numbers</dfn> always are a sequence of 10 digits, grouped 4-3-3. The last 2
 *   numbers are a checksum (modulo 97 of the number made up of the first 8 digits).</p>
 * <p><dfn>Enterprise numbers</dfn> are in use since 1 januari 2005. For new numbers, the first
 *   8 digits have no special meaning. For existing enterprises, the <dfn>enterprise number</dfn>
 *   uniquely identifying the enterprise is the VAT-number (9 digits), pre-pended with the digit 0
 *   (zero).</p>
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 *
 * @invar  getLeftNumber() != null;
 * @invar  Pattern.matches(LEFT_PATTERN, getLeftNumber());
 * @invar  getMiddleNumber() != null;
 * @invar  Pattern.matches(MIDDLE_PATTERN, getMiddleNumber());
 * @invar  getRightNumber() != null;
 * @invar  Pattern.matches(RIGHT_PATTERN, getRightNumber());
 * @invar  checkEnterpriseNumber(getLeftNumber(), getMiddleNumber(), getRightNumber());
 */
public final class EnterpriseNumber extends ImmutableValue {

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

//  private static final Log LOG = LogFactory.getLog(EnterpriseNumber.class);

  /**
   * The pattern identifying the left part of an enterprise number.
   */
  public static final String LEFT_PATTERN = "[0-9][0-9][0-9][0-9]";

  /**
   * The pattern identifying the middle part of an enterprise number.
   */
  public static final String MIDDLE_PATTERN = "[0-9][0-9][0-9]";

  /**
   * The pattern identifying the right part of an enterprise number.
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
   *                        EnterpriseNumber.class, "leftNumber",
   *                        "LEFT_NUMBER_IS_NULL", null
   *                 );
   * @throws  PropertyException pExc
   *          (leftNumber != null && !Pattern.matches(LEFT_PATTERN, leftNumber))
   *              && pExc.reportsOn(
   *                        EnterpriseNumber.class, "leftNumber",
   *                        "LEFT_NUMBER_INVALID_PATTERN", null
   *                 );
   * @throws  PropertyException pExc
   *          (middleNumber == null)
   *              && pExc.reportsOn(
   *                        EnterpriseNumber.class, "middleNumber",
   *                        "MIDDLE_NUMBER_IS_NULL", null
   *                 );
   * @throws  PropertyException pExc
   *          (middleNumber != null && !Pattern.matches(MIDDLE_PATTERN, middleNumber))
   *              && pExc.reportsOn(
   *                        EnterpriseNumber.class, "middleNumber",
   *                        "MIDDLE_NUMBER_INVALID_PATTERN", null
   *                 );
   * @throws  PropertyException pExc
   *          (rightNumber == null)
   *              && pExc.reportsOn(
   *                        EnterpriseNumber.class, "rightNumber",
   *                        "RIGHT_NUMBER_IS_NULL", null
   *                 );
   * @throws  PropertyException pExc
   *          (rightNumber != null && !Pattern.matches(RIGHT_PATTERN, rightNumber))
   *              && pExc.reportsOn(
   *                        EnterpriseNumber.class, "rightNumber",
   *                        "RIGHT_NUMBER_INVALID_PATTERN", null
   *                 );
   * @throws  PropertyException pExc
   *          (  leftNumber != null &&
   *             middleNumber != null &&
   *             rightNumber != null &&
   *             !checkEnterpriseNumber(leftNumber, middleNumber, rightNumber)
   *          )
   *              && pExc.reportsOn(
   *                        EnterpriseNumber.class, null,
   *                        "INVALID_CHECK", null
   *                 );
   */
  public EnterpriseNumber(final String leftNumber,
                          final String middleNumber,
                          final String rightNumber) throws PropertyException {
    initialize(leftNumber, middleNumber, rightNumber);
  }

  /**
   * Construct a new instance from a String, which we interpret
   * leniently.
   *
   * @todo contract & Toryt
   */
  public EnterpriseNumber(final String pattern) throws PropertyException {
    if (pattern == null) {
      throw new PropertyException(EnterpriseNumber.class, null, "NULL_PATTERN", null);
    }
    String[] array = pattern.split("[ -/|*:]+");
        // PatternSyntaxException: cannot happen
    StringBuffer buffer = new StringBuffer("");
    for (int i = 0; i < array.length; i++) {
      buffer.append(array[i]);
    }
    String nnString = buffer.toString();
    try {
      String leftNumber = nnString.substring(0, 4);
      String middleNumber = nnString.substring(4, 7);
      String rightNumber = nnString.substring(7, 10);
          // IndexOutOfBoundsException
      initialize(leftNumber, middleNumber, rightNumber);
    }
    catch (IndexOutOfBoundsException ioobExc) {
      throw new PropertyException(EnterpriseNumber.class, null, "PATTERN_TOO_SHORT", ioobExc);
    }
  }

  /**
   * @post    getLeftNumber().equals("0123");
   * @post    getMiddleNumber().equals("456");
   * @post    getRightNumber().equals("749");
   */
  public EnterpriseNumber() {
    try {
      initialize("0123", "456", "749");
    }
    catch (PropertyException exc) {
      // shouldn't happen, because valid numbers are given
      assert false : "Shouldn't happen";
    }
  }

  /**
   * @post    getLeftNumber().equals(enterpriseNumber.getLeftNumber());
   * @post    getMiddleNumber().equals(enterpriseNumber.getMiddleNumber());
   * @post    getRightNumber().equals(enterpriseNumber.getRightNumber());
   */
  public EnterpriseNumber(final EnterpriseNumber enterpriseNumber) {
    try {
      initialize(enterpriseNumber.getLeftNumber(),
                 enterpriseNumber.getMiddleNumber(),
                 enterpriseNumber.getRightNumber());
    }
    catch (PropertyException exc) {
      assert false : "Shouldn't happen.";
    }
  }

  /*</construction */

  /**
   * For contract: see first constructor.
   */
  private void initialize(final String leftNumber,
                          final String middleNumber,
                          final String rightNumber) throws PropertyException {
    CompoundPropertyException cpe =
      new CompoundPropertyException(EnterpriseNumber.class, null, null, null);
    boolean allPatternsOk = true;
    if (leftNumber == null) {
      cpe.addElementException(
          new PropertyException(
              EnterpriseNumber.class, "leftNumber", "LEFT_NUMBER_IS_NULL", null)
      );
      allPatternsOk = false;
    }
    if (leftNumber != null && !Pattern.matches(LEFT_PATTERN, leftNumber)) {
      cpe.addElementException(
          new PropertyException(
              EnterpriseNumber.class, "leftNumber", "LEFT_NUMBER_INVALID_PATTERN", null)
      );
      allPatternsOk = false;
    }
    if (middleNumber == null) {
      cpe.addElementException(
          new PropertyException(
              EnterpriseNumber.class, "middleNumber", "MIDDLE_NUMBER_IS_NULL", null)
      );
      allPatternsOk = false;
    }
    if (middleNumber != null && !Pattern.matches(MIDDLE_PATTERN, middleNumber)) {
      cpe.addElementException(
          new PropertyException(
              EnterpriseNumber.class, "middleNumber", "MIDDLE_NUMBER_INVALID_PATTERN", null)
      );
      allPatternsOk = false;
    }
    if (rightNumber == null) {
      cpe.addElementException(
          new PropertyException(
              EnterpriseNumber.class, "rightNumber", "RIGHT_NUMBER_IS_NULL", null)
      );
      allPatternsOk = false;
    }
    if (rightNumber != null && !Pattern.matches(RIGHT_PATTERN, rightNumber)) {
      cpe.addElementException(
          new PropertyException(
              EnterpriseNumber.class, "rightNumber", "RIGHT_NUMBER_INVALID_PATTERN", null)
      );
      allPatternsOk = false;
    }
    if (allPatternsOk &&
        (! EnterpriseNumber.checkEnterpriseNumber(leftNumber, middleNumber, rightNumber))) {
      cpe.addElementException(
          new PropertyException(EnterpriseNumber.class, null, "INVALID_CHECK", null)
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
  public String getLeftNumber() {
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
  public String getMiddleNumber() {
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
  public String getRightNumber() {
    return $rightNumber;
  }

  /**
   * @invar  $rightNumber != null;
   * @invar  Pattern.matches(RIGHT_PATTERN, $middleNumber);
   */
  private String $rightNumber;

  /*</property>*/



  /**
   * The integer used for checking an enterprise number.
   *
   * <strong>= 97</strong>
   */
  public static final int CHECK_NUMBER = 97;

  /**
   * @param   leftNumber
   *          The left part of an enterprise number.
   * @param   middleNumber
   *          The middle part of an enterprise number.
   * @param   rightNumber
   *          The right part of an enterprise number.
   * @pre     leftNumber != null;
   * @pre     Pattern.matches(LEFT_PATTERN, leftNumber);
   * @pre     middleNumber != null;
   * @pre     Pattern.matches(MIDDLE_PATTERN, middleNumber);
   * @pre     rightNumber != null;
   * @pre     Pattern.matches(RIGHT_PATTERN, rightNumber);
   * @return  (  (  Integer.parseInt(leftNumber + middleNumber + rightNumber.substring(0,1))
   *                +
   *                Integer.parseInt(rightNumber.substring(1))
   *             ) % CHECK_NUMBER
   *             == 0
   *          );
   */
  public static boolean checkEnterpriseNumber(final String leftNumber,
                                              final String middleNumber,
                                              final String rightNumber) {
    assert leftNumber != null;
    assert Pattern.matches(LEFT_PATTERN, leftNumber);
    assert middleNumber != null;
    assert Pattern.matches(MIDDLE_PATTERN, middleNumber);
    assert rightNumber != null;
    assert Pattern.matches(RIGHT_PATTERN, rightNumber);
    String first8String = leftNumber + middleNumber + rightNumber.substring(0, 1);
    assert first8String.length() == 8;
    String last2String = rightNumber.substring(1);
    assert last2String.length() == 2;
    int first8Int = Integer.parseInt(first8String); // preconditions make parse errors impossible
    int last2Int = Integer.parseInt(last2String); // preconditions make parse errors impossible
    return ((first8Int + last2Int) % CHECK_NUMBER) == 0;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @return  (o != null) && (o instanceof EnterpriseNumber)
   *          && ((EnterpriseNumber) o).getLeftNumber().equals(getLeftNumber())
   *          && ((EnterpriseNumber) o).getMiddleNumber().equals(getMiddleNumber())
   *          && ((EnterpriseNumber) o).getRightNumber().equals(getRightNumber());
   */
  public boolean equals(final Object o) {
    return (o != null) &&
           (o instanceof EnterpriseNumber) &&
           ((EnterpriseNumber) o).getLeftNumber().equals(getLeftNumber()) &&
           ((EnterpriseNumber) o).getMiddleNumber().equals(getMiddleNumber()) &&
           ((EnterpriseNumber) o).getRightNumber().equals(getRightNumber());
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return  getLeftNumber().hashCode()
   *          + getMiddleNumber().hashCode()
   *          + getRightNumber().hashCode();
   */
  public int hashCode() {
    return
      getLeftNumber().hashCode()
      + getMiddleNumber().hashCode()
      + getRightNumber().hashCode();
  }

  /**
   * Returns a string representatino of the object.
   *
   * return  getLeftNumber() + " " + getMiddleNumber() + " " + getRightNumber();
   */
  public String toString() {
    return getLeftNumber() + " " + getMiddleNumber() + " " + getRightNumber();
  }

}
