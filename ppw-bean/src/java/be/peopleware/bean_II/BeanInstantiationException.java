package be.peopleware.bean_II;


import be.peopleware.exception_I.TechnicalException;


/**
 * <p>This exception is thrown when you try to instantiate a Bean, and it
 *   failed.</p>
 *
 * @invar     getBeanType() != null;
 * @invar     (getMessage() == null) || !getMessage().equals("");
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public class BeanInstantiationException extends TechnicalException {

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
   * @param     beanType
   *            The type of bean which failed to instantiate.
   * @param     message
   *            The message that describes the exceptional circumstance.
   * @param     cause
   *            The exception that occured, causing this exception to be
   *            thrown, if that is the case.
   *
   * @pre       beanType != null;
   * @pre       (message != null) ==> ! message.equals("");
   * @post      new.getBeanType() == beanType;
   * @post      (message != null) ==> new.getMessage().equals(message);
   * @post      (message == null) ==> new.getMessage() == null;
   * @post      new.getCause() == cause;
   * @post      new.getLocalizedMessageResourceBundleLoadStrategy().getClass()
   *                == DefaultResourceBundleLoadStrategy.class;
   */
  public BeanInstantiationException(final String message,
                                    final Throwable cause,
                                    final Class beanType) {
    super(message, cause);
    assert beanType != null : "@pre beanType != null;"; //$NON-NLS-1$
    assert (message == null) || (!message.equals("")) //$NON-NLS-1$
        : "message name cannot be the empty string"; //$NON-NLS-1$
    $beanType = beanType;
  }

  /*</construction;>*/



  /*<property name="beanType">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Class getBeanType() {
    return $beanType;
  }

  private Class $beanType;

  /*</property>*/

}
