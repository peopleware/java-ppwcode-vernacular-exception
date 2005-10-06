/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.bean_V;


/**
 * Thrown when the user tried to create a circularity in the
 * object graph where this was not allowed.
 *
 * @author    Dimitri Smits
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 */
public class CircularityException extends PropertyException {

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
   * @param     message
   *            The message that describes the exceptional circumstance.
   * @param     cause
   *            The exception that occured, causing this exception to be
   *            thrown, if that is the case.
   * @param     parent
   *            The parent that we tried to add.
   *
   * @pre       origin != null;
   * @pre       (propertyName != null) ==> ! propertyName.equals("");
   * @pre       (message != null) ==> ! message.equals("");
   * @pre       parent != null;
   * @post      new.getOrigin() == origin;
   * @post      (propertyName != null)
   *                ==> new.getpropertyName().equals(propertyName);
   * @post      (propertyName == null) ==> new.getpropertyName() == null;
   * @post      (message != null) ==> new.getMessage().equals(message);
   * @post      (message == null) ==> new.getMessage() == null;
   * @post      new.getCause() == cause;
   * @post      new.getLocalizedMessageResourceBundleLoadStrategy().getClass()
   *                == DefaultResourceBundleLoadStrategy.class;
   * @post      new.getParent() == parent;
   */
  public CircularityException(final Object origin,
                              final String propertyName,
                              final String message,
                              final Throwable cause,
                              final Object parent) {
    super(origin, propertyName, message, cause);
    assert parent != null;
    $parent = parent;
  }

  /**
   * @param     originType
   *            The type bean that has thrown this exception during initialization.
   * @param     propertyName
   *            The name of the property of which the setter has thrown
   *            this exception because parameter validation failed.
   * @param     message
   *            The message that describes the exceptional circumstance.
   * @param     cause
   *            The exception that occured, causing this exception to be
   *            thrown, if that is the case.
   * @param     parent
   *            The parent that we tried to add.
   *
   * @pre       originType != null;
   * @pre       (propertyName != null) ==> ! propertyName.equals("");
   * @pre       (message != null) ==> ! message.equals("");
   * @pre       parent != null;
   * @post      new.getOriginType() == originType;
   * @post      new.getOrigin() == null;
   * @post      (propertyName != null)
   *                ==> new.getpropertyName().equals(propertyName);
   * @post      (propertyName == null) ==> new.getpropertyName() == null;
   * @post      (message != null) ==> new.getMessage().equals(message);
   * @post      (message == null) ==> new.getMessage() == null;
   * @post      new.getCause() == cause;
   * @post      new.getLocalizedMessageResourceBundleLoadStrategy().getClass()
   *                == DefaultResourceBundleLoadStrategy.class;
   * @post      new.getParent() == parent;
   * @since IV
   */
  public CircularityException(final Class originType,
                              final String propertyName,
                              final String message,
                              final Throwable cause,
                              final Object parent) {
    super(originType, propertyName, message, cause);
    assert parent != null;
    $parent = parent;
  }

  /*</construction;>*/


  /**
   * The parent.
   *
   * @basic
   */
  public final Object getParent() {
    return $parent;
  }

  /**
   * @invar     $parent != null;
   */
  private Object $parent;

  /**
   * @return    super.hasProperties(origin, propertyName, message, cause)
   *            && getParent() == parent;
   */
  public boolean hasProperties(final Object origin,
                               final String propertyName,
                               final String message,
                               final Throwable cause,
                               final Object parent) {
    return super.hasProperties(origin, propertyName, message, cause)
             && getParent() == parent;
  }

  /**
   * @return    super.hasProperties(originType, propertyName, message, cause)
   *            && getParent() == parent;
   * @since IV
   */
  public boolean hasProperties(final Class originType,
                               final String propertyName,
                               final String message,
                               final Throwable cause,
                               final Object parent) {
    return super.hasProperties(originType, propertyName, message, cause)
             && getParent() == parent;
  }

}
