package be.peopleware.value_I;


import be.peopleware.bean_III.PropertyException;


/**
 * An exception type for working with a ConstrainedString. Signals that there
 * was an attempt to set a {@link ConstrainedString} to an value that is too
 * long. The string that was tried, the name of the property of the origin that
 * was tried to set, and the maximum length are given. The origin is the bean
 * that uses {@link ConstrainedString} for a property, and not the
 * {@link ConstrainedString} itself.
 *
 * @author    Dimitri Smits
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 *
 * @invar     getMaxLength() > 0;
 * @invar     getString() != null
 * @invar     getString().length() > getMaxLength();
 */
public class TooLongException extends PropertyException {

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
   * @param     origin
   *            The bean that has thrown this exception.
   * @param     propertyName
   *            The name of the property of which the setter has thrown
   *            this exception because parameter validation failed.
   * @param     string
   *            The string that was tried to set to <code>propertyName</code>.
   * @param     maxLength
   *            The actual maximum length that was exceeded.
   * @param     message
   *            The message that describes the exceptional circumstance.
   * @param     cause
   *            The exception that occured, causing this exception to be
   *            thrown, if that is the case.
   *
   * @pre       origin != null;
   * @pre       (propertyName != null) ==> ! propertyName.equals("");
   * @pre       string != null;
   * @pre       string.length() > maxLength;
   * @pre       maxLength > 0;
   * @pre       (message != null) ==> ! message.equals("");
   *
   * @post      new.getOrigin() == origin;
   * @post      (propertyName != null)
   *                ==> new.getpropertyName().equals(propertyName);
   * @post      (propertyName == null) ==> new.getpropertyName() == null;
   * @post      new.getString().equals(string);
   * @post      new.getMaxLength() == maxLength;
   * @post      (message != null) ==> new.getMessage().equals(message);
   * @post      (message == null) ==> new.getMessage() == null;
   * @post      new.getCause() == cause;
   * @post      new.getLocalizedMessageResourceBundleLoadStrategy().getClass()
   *                == DefaultResourceBundleLoadStrategy.class;
   */
  public TooLongException(final Object origin,
                          final String propertyName,
                          final String string,
                          final int maxLength,
                          final String message,
                          final Throwable cause) {
    super(origin, propertyName, message, cause);
    $string = string;
    $maxLength = maxLength;
  }

  /*</construction;>*/



  /*<property name="maxLength">*/
  //------------------------------------------------------------------

  /**
   * The maximum length that was exceeded.
   *
   * @basic
   */
  public final int getMaxLength() {
    return $maxLength;
  }

  /**
   * @invar     $maxLength >= 0;
   */
  private int $maxLength;

  /*</property>*/



  /*<property name="string">*/
  //------------------------------------------------------------------

  /**
   * The string value that was attempted to set.
   *
   * @basic
   */
  public final String getString() {
    return $string;
  }

  /**
   * @invar     $string != null;
   * @invar     $string.length() > $maxLength;
   */
  private String $string = ""; //$NON-NLS-1$

  /*</property>*/

}
