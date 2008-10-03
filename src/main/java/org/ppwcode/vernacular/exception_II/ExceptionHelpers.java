/*<license>
Copyright 2004 - $Date$ by PeopleWare n.v..

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

package org.ppwcode.vernacular.exception_II;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.vernacular.exception_II.ProgrammingErrorHelpers.preArgumentNotNull;

import java.sql.SQLException;
import java.sql.SQLWarning;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


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
public class ExceptionHelpers {

  /**
   * <p>Hunt for a {@link Throwable}  of type {@code exceptionType} in the exception chain carried by
   *   {@code exc}. This is a deeper {@code catch}.</p>
   * <p>The Java {@code catch} statement only catches exceptions of a given type if the top exception is
   *   of that type itself. Often however, exceptions are wrapped in exceptions of other types as they
   *   pass through the layers of an application (from persistence over semantics and logic towards the
   *   user). This method is intended to be used inside a more general {@code catch} statement to hunt
   *   for an exception of a specific type in those nested exceptions.</p>
   * <p>Since Java 1.4, {@link Throwable Throwables} all support a {@link Throwable#getCause() cause}
   *   to carry such wrapped exceptions. However, before that time, such a pattern was introduced ad hoc
   *   in some exception types in different parts of the Java API. This method looks for exceptions
   *   in the {@link Throwable#getCause() cause}, and in the wrapped exceptions used by a number of those
   *   ad hoc patterns. Since a number of thise parts of the Java API are not part of Java SE, and thus
   *   they require extra libraries, the code below takes great care to use reflection to avoid a
   *   dependency of this library on those optional libraries. In other words, we check for exceptions
   *   in the wrapped chains for specific types, if we can find those types in the classpath, and we
   *   do not check if those types are not found in the classpath. In all these cases, the specialized
   *   patterns get precedence over the general {@link Throwable#getCause() cause} chain.</p>
   * <p>In the current version, we look voor exceptions in:</p>
   * <ul>
   *   <li>the {@link SQLException#getNextException() next exception} of a {@link SQLException};
   *       this also works for the {@link SQLWarning#getNextWarning() next warning} of a
   *       {@link SQLWarning}</li>
   * </ul>
   * <p>We do no longer threat the following cases, since the Javadoc says the use of the specialized
   *   patters is deprecated:</p>
   * <ul>
   *   <li>the {@code JspException.getRootCause() root cause} of a {@code javax.servlet.jsp.JspException} (since JavaEE 5)</li>
   *   <li>the {@code ELException.getRootCause()} root cause} of an {@code javax.servlet.jsp.el.ELException} (since JavaEE 5)</li>
   * </ul>
   *
   * @note More checks will be added as more, relevant, exception classes are discovered that use another
   *       method than {@link Throwable#getCause()} as inspector in an exception chaining mechanisms.
   *       This property of {@link Throwable} was added since 1.4, and the entire JSE API has been
   *       retrofitted since.
   * @note There are currently no specialized cases we need to address via reflection. Exception cases
   *       of the previous version in Java EE are deprecated and retrofitted in Java EE 5.
   * @note There is a dependency cycle problem at the moment with the reflection code. We would like to
   *       use the code from ppwcode util reflection to do this work, but at the moment that library
   *       depends on us. Luckily, in this version, we have no need for the reflection code.
   *       Beware however if you have to reintroduce reflection code in the future.
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
  @MethodContract(
    pre  = {
      @Expression("_exceptionType != null")
    },
    post = {
      @Expression("_exc == null ? null"),
      @Expression("_exc != null && _exc instanceof _exceptionType ? exc"),
      @Expression("_exc != null && ! (_exc instanceof _exceptionType) && _exc instanceof SQLException ? huntFor(_exc.nextException, _exceptionType)"),
      @Expression("_exc != null && ! (_exc instanceof _exceptionType) && ! (_exc instanceof SQLException) ? huntFor(_exc.cause, _exceptionType)")
    }
  )
  public static <_Exc_ extends Throwable> _Exc_ huntFor(final Throwable exc, final Class<_Exc_> exceptionType) {
    assert preArgumentNotNull(exceptionType, "exceptionType");
    if (exc == null) {
      return null;
    }
    if (exceptionType.isInstance(exc)) {
      @SuppressWarnings("unchecked")
      _Exc_ excAs_Exc_ = (_Exc_)exc;
      return excAs_Exc_;
    }
    /* check types with specialized patters that exist in Java EE, for which we do not need a reflection
       approach */
    if (exc instanceof SQLException) {
      SQLException excAsSQLException = (SQLException)exc;
      _Exc_ result = huntFor(excAsSQLException.getNextException(), exceptionType);
      if (result != null) {
        return result;
      }
      // else, continue the search
    }
    /* check types with specialized patterns that do not exist in Java EE, for which we will use a reflection
       approach to verify them */
    // NONE AT THE MOMENT
    /* finally, check the general getCause(), in all cases, if no match was found yet */
    return huntFor(exc.getCause(), exceptionType);
  }

//  public static void handleThrowable(Throwable t) {
////    Log log =
//  }
//
//  /**
//   * Returns a logger
//   */
//  public static /*Log*/ void loggerForThrowable(Throwable t) {
//    // ??
//  }

}
