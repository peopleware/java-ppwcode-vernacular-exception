package be.peopleware.struts_II;


import java.security.Principal;

import be.peopleware.exception_I.LocalizedMessageException;


/**
 * <p>This exception is a general exception signalling that access is denied
 * for a specific web resource requested by the user.</p>
 *
 * @invar     (getMessage() == null) || !getMessage().equals("");
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    PeopleWare n.v.
 */
public class IllegalAccessException extends LocalizedMessageException {

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
   *            The exception that occured, causing this exception to be
   *            thrown, if that is the case.
   * @param     principal
   *            The user Principal causing this exception to be thrown.
   * @param     requestedPath
   *            The requested path of the webapplication.
   *
   * @pre       (message != null) ==> !message.equals("");
   * @post      (message != null) ==> new.getMessage().equals(message);
   * @post      (message == null) ==> new.getMessage() == null;
   * @post      new.getCause() == cause;
   * @post      new.getPrincipal() == principal;
   * @post      (requestedPath != null)
   *                ? new.getRequestedPath().equals(requestedPath)
   *                : null
   * @post      new.getLocalizedMessageResourceBundleLoadStrategy().getClass()
   *                == DefaultResourceBundleLoadStrategy.class;
   */
  public IllegalAccessException(final String message,
                                final Throwable cause,
                                final Principal principal,
                                final String requestedPath) {
    super(message, cause);
    assert (message == null) || (!message.equals("")) //$NON-NLS-1$
        : "message name cannot be the empty string"; //$NON-NLS-1$
    setPrincipal(principal);
    setRequestedPath(requestedPath);
  }

  /*</construction;>*/



  /*<property name="principal">*/
  //------------------------------------------------------------------

  /**
   * Set the principal for this IllegalAccessException.
   *
   * @param     principal
   *            The principal triggering this IllegalAccessException
   */
  public void setPrincipal(final Principal principal) {
    $principal = principal;
  }

  /**
   * @basic
   */
  public Principal getPrincipal() {
    return $principal;
  }

  private Principal $principal;

  /*</property>*/



  /*<property name="requestedPath">*/
  //------------------------------------------------------------------

  /**
   * Set the requestedPath which resulted in this IllegalAccessException.
   *
   * @param     requestedPath
   *            The path requested by the Principal.
   */
  public void setRequestedPath(final String requestedPath) {
    $requestedPath = requestedPath;
  }

  /**
   * @basic
   */
  public String getRequestedPath() {
    return $requestedPath;
  }

  private String $requestedPath;

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
