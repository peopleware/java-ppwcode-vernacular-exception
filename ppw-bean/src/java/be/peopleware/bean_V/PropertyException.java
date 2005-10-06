/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.bean_V;


import be.peopleware.exception_I.LocalizedMessageException;


/**
 * <p>PropertyExceptions are exceptions that carry with them information
 *   about the property for which they occured. They are usually thrown
 *   by a property setter during validation. If the property name is
 *   <code>null</code>, it means that the exception could not be
 *   attributed to a specific property of {@link #getOrigin()}.
 *   <em>The <code>origin</code> should not be <code>null</code></em>,
 *   except when the exception is thrown during construction of an
 *   object, that could not be completed. In that case, the type
 *   should be filled out.</p>
 * <p>Localized messages are sougth
 *   in a <kbd>*.properties</kbd> file for the class of the origin. The
 *   properties files should be in the directory next to the bean class, with
 *   name <kbd><var>{@link #getOrigin()}<code>.getClass().getName()</code></var>
 *   <var>_locale_identification</var>.properties</kbd>. Alternatively,
 *   messages can be added to a properties file that comes with the
 *   exception.</p>
 * <p>The keys for the localized messages have to have the form
 *   <code><var>this.getClass().getName()</var>.
 *         <var>{@link #getPropertyName()}</var>.
 *         <var>{@link #getMessage()}</var></code>.
 *   See {@link #getLocalizedMessageKeys()}.</p>
 *
 * @invar     getOriginType() != null;
 * @invar     (getOrigin() != null)  ==> getOriginType() == getOrigin().getClass();
 * @invar     (getPropertyName() == null) || ! getPropertyName().equals("");
 * @invar     (getMessage() == null) || ! getMessage().equals("");
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 *
 * @idea (jand): Check that property name is truly a property of the origin
 */
public class PropertyException extends LocalizedMessageException {

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
   *
   * @pre       origin != null;
   * @pre       (propertyName != null) ==> ! propertyName.equals("");
   * @pre       (message != null) ==> ! message.equals("");
   *
   * @post      new.getOrigin() == origin;
   * @post      new.getOriginType() == origin.getClass();
   * @post      (propertyName != null)
   *                ==> new.getpropertyName().equals(propertyName);
   * @post      (propertyName == null) ==> new.getpropertyName() == null;
   * @post      (message != null) ==> new.getMessage().equals(message);
   * @post      (message == null) ==> new.getMessage() == null;
   * @post      new.getCause() == cause;
   * @post      new.getLocalizedMessageResourceBundleLoadStrategy().getClass()
   *                == DefaultResourceBundleLoadStrategy.class;
   *
   * @idea (jand): check effectively that <code>propertyName</code> is
   *               a property of <code>origin</code>
   */
  public PropertyException(final Object origin,
                           final String propertyName,
                           final String message,
                           final Throwable cause) {
    super(message, cause);
    assert origin != null : "@pre origin != null;"; //$NON-NLS-1$
    assert (propertyName == null) || (!propertyName.equals("")) //$NON-NLS-1$
        : "property name cannot be the empty string"; //$NON-NLS-1$
    assert (message == null) || (!message.equals("")) //$NON-NLS-1$
        : "message name cannot be the empty string"; //$NON-NLS-1$
    $origin = origin;
    $originType = origin.getClass();
    $propertyName = propertyName;
  }


  /**
   * @param     origin
   *            The bean that has thrown this exception.
   * @param     inOriginInitialization
   *            Set to <code>true</code> if this active
   *            property is created during the origin initialization;
   *            if so, an exception will not carry a reference
   *            to the bean, but only to the bean type.
   * @param     propertyName
   *            The name of the property of which the setter has thrown
   *            this exception because parameter validation failed.
   * @param     message
   *            The message that describes the exceptional circumstance.
   * @param     cause
   *            The exception that occured, causing this exception to be
   *            thrown, if that is the case.
   *
   * @pre       origin != null;
   * @pre       (propertyName != null) ==> ! propertyName.equals("");
   * @pre       (message != null) ==> ! message.equals("");
   *
   * @post      inOriginInitialization ? new.getOrigin() == null
   *                                   : new.getOrigin() == origin;
   * @post      new.getOriginType() == origin.getClass();
   * @post      (propertyName != null)
   *                ==> new.getpropertyName().equals(propertyName);
   * @post      (propertyName == null) ==> new.getpropertyName() == null;
   * @post      (message != null) ==> new.getMessage().equals(message);
   * @post      (message == null) ==> new.getMessage() == null;
   * @post      new.getCause() == cause;
   * @post      new.getLocalizedMessageResourceBundleLoadStrategy().getClass()
   *                == DefaultResourceBundleLoadStrategy.class;
   *
   * @idea (jand): check effectively that <code>propertyName</code> is
   *               a property of <code>origin</code>
   * 
   * @since IV 1.1.0/1.0
   */
  public PropertyException(final Object origin,
                           final boolean inOriginInitialization,
                           final String propertyName,
                           final String message,
                           final Throwable cause) {
    super(message, cause);
    assert origin != null : "@pre origin != null;"; //$NON-NLS-1$
    assert (propertyName == null) || (!propertyName.equals("")) //$NON-NLS-1$
        : "property name cannot be the empty string"; //$NON-NLS-1$
    assert (message == null) || (!message.equals("")) //$NON-NLS-1$
        : "message name cannot be the empty string"; //$NON-NLS-1$
    if (! inOriginInitialization) {
      $origin = origin;
    }
    $originType = origin.getClass();
    $propertyName = propertyName;
  }


  /**
   * @param     originType
   *            The bean that has thrown this exception.
   * @param     propertyName
   *            The name of the property of which the setter has thrown
   *            this exception because parameter validation failed.
   * @param     message
   *            The message that describes the exceptional circumstance.
   * @param     cause
   *            The exception that occured, causing this exception to be
   *            thrown, if that is the case.
   *
   * @pre       originType != null;
   * @pre       (propertyName != null) ==> ! propertyName.equals("");
   * @pre       (message != null) ==> ! message.equals("");
   *
   * @post      new.getOriginType() == origin;
   * @post      new.getOrigin() == null;
   * @post      (propertyName != null)
   *                ==> new.getpropertyName().equals(propertyName);
   * @post      (propertyName == null) ==> new.getpropertyName() == null;
   * @post      (message != null) ==> new.getMessage().equals(message);
   * @post      (message == null) ==> new.getMessage() == null;
   * @post      new.getCause() == cause;
   * @post      new.getLocalizedMessageResourceBundleLoadStrategy().getClass()
   *                == DefaultResourceBundleLoadStrategy.class;
   *
   * @idea (jand): check effectively that <code>propertyName</code> is
   *               a property of <code>origin</code>
   * 
   * @since IV
   */
  public PropertyException(final Class originType,
                           final String propertyName,
                           final String message,
                           final Throwable cause) {
    super(message, cause);
    assert originType != null : "@pre originType != null;"; //$NON-NLS-1$
    assert (propertyName == null) || (!propertyName.equals("")) //$NON-NLS-1$
        : "property name cannot be the empty string"; //$NON-NLS-1$
    assert (message == null) || (!message.equals("")) //$NON-NLS-1$
        : "message name cannot be the empty string"; //$NON-NLS-1$
    $originType = originType;
    $propertyName = propertyName;
  }

  /*</construction>*/



  /*<property name="origin">*/
  //------------------------------------------------------------------

  /**
   * The bean that has thrown this exception.
   *
   * @basic
   */
  public final Object getOrigin() {
    return $origin;
  }

  private Object $origin;

  /*</property>*/



  /*<property name="originType">*/
  //------------------------------------------------------------------

  /**
   * The type of the bean that has thrown this exception.
   *
   * @basic
   */
  public final Class getOriginType() {
    return $originType;
  }

  private Class $originType;

  /*</property>*/



  /*<property name="propertyName">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final String getPropertyName() {
    return $propertyName;
  }

  private String $propertyName;

  /*</property>*/



  /*<property name="localizedMessageResourceBundleBasename">*/
  //------------------------------------------------------------------

  /**
   * This implementation returns the fully qualified class name of the
   * {@link #getOrigin() origin}, which cannot be <code>null</code>.
   *
   * @return    getOriginType().getName();
   */
  public final String getLocalizedMessageResourceBundleBasename() {
    return getOriginType().getName();
  }

  /*</property>*/



  /**
   * @param     other
   *            The object to compare the values of this with.
   * @return    boolean
   *            (other != null)
   *                 && hasProperties(other.getOriginType(),
   *                                  other.getPropertyName(),
   *                                  other.getMessage(),
   *                                  other.getCause())
   *                 && getOrigin() == other.getOrigin();
   */
  public boolean hasSameValues(final PropertyException other) {
    return (other != null)
             && hasProperties(other.getOriginType(),
                              other.getPropertyName(),
                              other.getMessage(),
                              other.getCause())
             && getOrigin() == other.getOrigin();
  }



  /**
   * @param     origin
   *            The origin to compare the one of this Exception with.
   * @param     propertyName
   *            The  propertyName to compare the one of this Exception with.
   * @param     message
   *            The message to compare the one of this Exception with..
   * @param     cause
   *            The cause to compare the one of this Exception with.
   * @return    boolean
   *            (getOrigin() == origin)
   *                && ((getPropertyName() == null)
   *                      ? propertyName == null
   *                      : getPropertyName().equals(propertyName))
   *                && ((getCause() == null)
   *                      ? cause == null
   *                      : getCause().equals(cause))
   *                && ((getMessage() == null)
   *                      ? message == null
   *                      : getMessage().equals(message));
   */
  public boolean hasProperties(final Object origin,
                               final String propertyName,
                               final String message,
                               final Throwable cause) {
    return (getOrigin() == origin)
              && ((getPropertyName() == null)
                    ? propertyName == null
                    : getPropertyName().equals(propertyName))
              && ((getCause() == null)
                    ? cause == null
                    : getCause().equals(cause))
              && ((getMessage() == null)
                    ? message == null
                    : getMessage().equals(message));
  }
  
  /**
   * @param     originType
   *            The type of origin to compare the one of this Exception with.
   * @param     propertyName
   *            The  propertyName to compare the one of this Exception with.
   * @param     message
   *            The message to compare the one of this Exception with..
   * @param     cause
   *            The cause to compare the one of this Exception with.
   * @return    boolean
   *            (getOriginType() == originType)
   *                && ((getPropertyName() == null)
   *                      ? propertyName == null
   *                      : getPropertyName().equals(propertyName))
   *                && ((getCause() == null)
   *                      ? cause == null
   *                      : getCause().equals(cause))
   *                && ((getMessage() == null)
   *                      ? message == null
   *                      : getMessage().equals(message));
   * 
   * @since IV
   */
  public boolean hasProperties(final Class originType,
                               final String propertyName,
                               final String message,
                               final Throwable cause) {
    return (getOriginType() == originType)
              && ((getPropertyName() == null)
                    ? propertyName == null
                    : getPropertyName().equals(propertyName))
              && ((getCause() == null)
                    ? cause == null
                    : getCause().equals(cause))
              && ((getMessage() == null)
                    ? message == null
                    : getMessage().equals(message));
  }
  
  /**
   * Does this exception, in some way, report about
   * an exceptional condition concerning <code>origin</code>,
   * <code>propertyName</code>, with <code>message</code>
   * and <code>cause</code>.
   * 
   * This is the non-deterministic version of
   * {@link #hasProperties(Object, String, String, Throwable)}, which
   * can be overwritten by subclasses.
   * The default implementation calls
   * {@link #hasProperties(Object, String, String, Throwable)}.
   * 
   * @protected.result hasProperties(origin,
   *                                 propertyName,
   *                                 message,
   *                                 cause);
   */
  public boolean reportsOn(final Object origin,
                            final String propertyName,
                            final String message,
                            final Throwable cause) {
    return hasProperties(origin, propertyName, message, cause);
  }

  /**
   * Does this exception, in some way, report about
   * an exceptional condition concerning <code>originType</code>,
   * <code>propertyName</code>, with <code>message</code>
   * and <code>cases</code>.
   * 
   * This is the non-deterministic version of
   * {@link #hasProperties(Class, String, String, Throwable)}, which
   * can be overwritten by subclasses.
   * The default implementation calls
   * {@link #hasProperties(Class, String, String, Throwable)}.
   * 
   * @protected.result hasProperties(originType,
   *                                 propertyName,
   *                                 message,
   *                                 cause);
   * 
   * @since IV
   */
  public boolean reportsOn(final Class originType,
                            final String propertyName,
                            final String message,
                            final Throwable cause) {
    return hasProperties(originType, propertyName, message, cause);
  }




  /*<property name="localizedMessageKeys">*/
  //------------------------------------------------------------------

  private static final String EMPTY = ""; //$NON-NLS-1$
  private static final String DOT = "."; //$NON-NLS-1$
  private static final String PREFIX = "message."; //$NON-NLS-1$

  /**
   * The keys that are tried consecutively are intended for use in
   * a properties file that comes with the
   * {@link #getOriginType() origin type} of the exception, with a fall-back
   * to a properties file that comes with the class of the exception.
   * The message that is given in the constructor (the
   * {@link #getMessage()} non-localized message) is intended as the
   * final discriminating key in a resource bundle.
   *
   * <p>The first key, for use in the properties file that comes with
   *   the {@link #getOriginType() origin type} of the exception, has the form
   *   <code>getClass().getName() + "." + getPropertyName()
   *         + "." + getMessage()</code>.
   *   If the property name is <code>null</code>, the form is
   *   <code>getClass().getName() + "." + getMessage()</code>. This is
   *   intended for exceptions that are not bound to a particular
   *   property. If the message is <code>null</code>, the form of the key
   *   is <code>getClass().getName() + "." + getPropertyName()</code>
   *   or <code>getClass().getName()</code>. These forms are intended for
   *   exceptions of which the type itself is discriminating enough
   *   for a good exception message. </p>
   * <p>The second key is intended for use in a properties file that
   *   comes with the exception class. It is intended for error messages
   *   that can be written independent of the actual origin or property
   *   for which they occured. Such messages should often be considered
   *   a fall-back.
   *   The key that should be used in these files is of the form
   *   <code>"message." + getMessage()</code>. If the message is
   *   <code>null</code>, no such key is added to the array.</p>
   * <p>When the {@link #getMessage() message} is used as a key,
   *   it should be in all caps.</p>
   *
   * @result    result != null;
   * @result    result.length == ((getMessage() != null) ? 2 : 1);
   * @result    result[0] != null;
   * @result    (getPropertyName() != null) && (getMessage() != null)
   *                ==> result[0].equals(getClass().getName()
   *                                     + "." + getPropertyName()
   *                                     + "." + getMessage());
   * @result    (getPropertyName() == null) && (getMessage() != null)
   *                ==> result[0].equals(getClass().getName()
   *                                     + "." + getMessage());
   * @result    (getPropertyName() != null) && (getMessage() == null)
   *                ==> result[0].equals(getClass().getName()
   *                                     + "." + getPropertyName());
   * @result    (getPropertyName() == null) && (getMessage() == null)
   *                ==> result[0].equals(getClass().getName());
   * @result    (getMessage() != null)
   *                ==> (result[1] != null)
   *                    && result[1].equals("message." + getMessage());
   */
  public final String[] getLocalizedMessageKeys() {
    String[] result = null;
    String firstKey = getClass().getName()
                      + (getPropertyName() != null
                            ? DOT + getPropertyName()
                            : EMPTY)
                      + (getMessage() != null ? DOT + getMessage() : EMPTY);
    String secondKey = getMessage() != null ? PREFIX + getMessage() : null;
    if (secondKey != null) {
      result = new String[] {firstKey, secondKey};
    }
    else {
      result = new String[] {firstKey};
    }
    return result;
  }

  /*</property>*/

}
