/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.exception_I;


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.el.ELException;


/**
 * Convenience methods for working with {@link java.lang.Exception}s.
 *
 * @author      Jan Dockx
 * @author      PeopleWare n.v.
 */
public class Exceptions {

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



  /**
   * Look in the {@link Throwable#getCause() cause},
   * {@link JspException#getRootCause() "root cause"} or
   * {@link ELException#getRootCause() "root cause"} for an exception
   * of type <code>exceptionType</code>. <code>null</code> is returned
   * if no such cause is found.
   *
   * @param     exc
   *            The exception to look in.
   * @param     exceptionType
   *            The type of Exception to look for
   *
   * @pre       exceptionType != null;
   * @result    ((exc == null) || (exceptionType.isInstance(exc)))
   *                ==> (result == exc);
   * @result    ((exc != null) && (! exceptionType.isInstance(exc)))
   *                ==> ((result == huntFor(exc.getRootCause)
   *                    || (result == huntFor(exc.getCause)))
   * @result    (result != null) ==> exceptionType.isInstance(result);
   */
  public static Throwable huntFor(final Throwable exc,
                                  final Class exceptionType) {
    Throwable result = null;
    if ((exc != null) && (exceptionType.isInstance(exc))) {
      result = exc;
    }
    else if (exc != null) {
      if (exc instanceof JspException) {
        result = huntFor(((JspException)exc).getRootCause(),
                         // if ClassCastException, we fail grand
                         exceptionType);
      }
      else if (exc instanceof ELException) {
        result = huntFor(((ELException)exc).getRootCause(),
                         // if ClassCastException, we fail grand
                         exceptionType);
      }
      if (result == null) {
        result = huntFor(exc.getCause(), exceptionType);
                         // if ClassCastException, we fail grand
      }
    }
    return result;
  }

}
