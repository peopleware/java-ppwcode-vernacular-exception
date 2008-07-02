/*<license>
Copyright 2005 - $Date$ by PeopleWare n.v..

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</license>*/

package be.peopleware.exception_II;

import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;



//import org.apache.commons.logging.Log;


/**
 * Convenience methods for working with {@link java.lang.Throwable}s.
 *
 * @author      Jan Dockx
 * @author      PeopleWare n.v.
 */
@Copyright("2007 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
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
   * @todo This method makes this library dependent on the JEE JSP API.
   *       This should be circumvented: this dependency should be optional.
   * @note More checks will be added as more, relevant, exception
   *       classes are discovered that use another method than
   *       {@link Throwable#getCause()} as inspector in an exception
   *       chaining mechanisms. This property of {@link Throwable} was added
   *       since 1.4, and the entire JSE API has been retrofitted since.
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
                                  final Class<?> exceptionType) {
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

  public static void handleThrowable(Throwable t) {
//    Log log =
  }

  /**
   * Returns a logger
   */
  public static /*Log*/ void loggerForThrowable(Throwable t) {
    // ??
  }

}
