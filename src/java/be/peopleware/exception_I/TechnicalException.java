/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.exception_I;


/**
 * <p>This exception is a general exception signalling a fatal failure.</p>
 *
 * @invar     (getMessage() == null) || !getMessage().equals("");
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    PeopleWare n.v.
 */
public class TechnicalException extends LocalizedMessageException {

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
   *
   * @pre       (message != null) ==> ! message.equals("");
   * @post      (message != null) ==> new.getMessage().equals(message);
   * @post      (message == null) ==> new.getMessage() == null;
   * @post      new.getCause() == cause;
   * @post      new.getLocalizedMessageResourceBundleLoadStrategy().getClass()
   *                == DefaultResourceBundleLoadStrategy.class;
   */
  public TechnicalException(final String message,
                            final Throwable cause) {
    super(message, cause);
    assert (message == null) || (!message.equals("")) //$NON-NLS-1$
        : "message name cannot be the empty string"; //$NON-NLS-1$
  }

  /*</construction;>*/




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
