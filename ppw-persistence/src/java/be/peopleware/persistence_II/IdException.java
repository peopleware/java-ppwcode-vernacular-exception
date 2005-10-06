package be.peopleware.persistence_II;


import be.peopleware.exception_I.LocalizedMessageException;


/**
 * <p>This exception signals failure to locate a object with the given ID.</p>
 *
 * @invar     (getMessage() == null) || !getMessage().equals("");
 * @invar     getBeanClass() != null;
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    PeopleWare n.v.
 */
public class IdException extends LocalizedMessageException {

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
   * @param     message
   *            The message that describes the exceptional circumstance.
   * @param     cause
   *            The exception that occurred, causing this exception to be
   *            thrown, if that is the case.
   * @param     beanClass
   *            The bean class which is involved in this Exception.
   *
   * @pre       (message != null) ==> ! message.equals("");
   * @pre       beanClass != null;
   * @post      (message != null) ==> new.getMessage().equals(message);
   * @post      (message == null) ==> new.getMessage() == null;
   * @post      new.getCause() == cause;
   * @post      new.getLocalizedMessageResourceBundleLoadStrategy().getClass()
   *                == DefaultResourceBundleLoadStrategy.class;
   * @post      new.getBeanClassName() == beanClassName;
   */
  public IdException(final String message,
                     final Throwable cause,
                     final Class beanClass) {
    super(message, cause);
    assert (message == null) || (!message.equals("")) //$NON-NLS-1$
        : "message name cannot be the empty string"; //$NON-NLS-1$
    assert beanClass != null;
    $beanClass = beanClass;
  }

  /*</construction;>*/



  /*<property name="beanClass">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public Class getBeanClass() {
    return $beanClass;
  }

  /**
   * @invar     $beanClass != null;
   */
  private Class $beanClass;

  /*</property>*/


  /**
   * <strong>= {@value}</strong>
   */
  public static final String KEY = "DEFAULT"; //$NON-NLS-1$

  /**
   * @return    getMessage() != null ? getMessage() : KEY;
   */
  public final String[] getLocalizedMessageKeys() {
    return new String[] {getMessage() != null ? getMessage() : KEY};
  }

  /*</property>*/

}
