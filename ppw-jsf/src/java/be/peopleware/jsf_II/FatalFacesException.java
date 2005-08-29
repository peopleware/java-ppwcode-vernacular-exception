/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/


package be.peopleware.jsf_II;


import javax.faces.FacesException;


/**
 * A {@link FacesException} that can be thrown when a fatal situation
 * is detected. Exceptions of this kind should not be caught, until the last
 * moment, and then only to log the user out gracefully, and to notify
 * the user and administrators.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
public class FatalFacesException extends FacesException {

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

  private static final long serialVersionUID = 1L;

  /**
   * @post new.getMessage().equals(message);
   * @post new.getCause() == null;
   */
  public FatalFacesException(String message) {
    super(message);
  }

  /**
   * @post new.getMessage().equals(message);
   * @post new.getCause() == cause;
   */
  public FatalFacesException(String message, Throwable cause) {
    super(message, cause);
  }

}



