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

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>This error is thrown when an external condition occurs, which we know can happen
 *   (however unlikely), which we do not want to deal with in our application. Most often,
 *   these are exceptional conditions are of a technical nature. These conditions are
 *   considered preconditions on the system level. Examples are a disk that is full,
 *   a network connection that cannot be established, a power failure, etcetera.
 *   The indented audience of these errors is neither the end user, nor the developer,
 *   but the <em>administrator</em>, who is responsible for system configuration,
 *   integration and infrastructure.</p>
 * <p>The {@link #getMessage() message} should express the error as closely as possible.
 *   It is the only channel to which to communicate to the administrator what went wrong.
 *   If you cannot pinpoint the exact nature of the error, the message should say so
 *   explicitly. If you become aware of the external condition you do not want to deal
 *   with through an {@link Exception} or an {@link Error}, it should be carried
 *   by an instance of this class as its {@link #getCause() cause}.</p>
 * <p>These errors should not be mentioned in the exception part of a method contract.
 *   They could be mentioned in the preconditions of a method contract, but in general
 *   this is not appropriate. {@code ExternalError}s are a mechanism to signal
 *   <dfn>system precondition</dfn> violations to administrators, it is not a part of the
 *   contract between developers, but rather a contract between developers in general and
 *   the system administrator. These errors could be documented in a document that
 *   communicates between developers and administrators (e.g., {@code web.xml} for web
 *   applications), and this should be done in specific cases. But most often, these
 *   system preconditions are considered implicit (e.g., when we need a database, it is
 *   implied that the database connection works).</p>
 * <p>It probably does not make sense to create subtypes of this error for specific
 *   situations. There is no need for internationalization for external errors. If there
 *   is extra information that we can communicate to the administrator, we can add it to
 *   the message.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
@Invars({
  @Expression("message != null"),
  @Expression("message != EMPTY")
})
public class ExternalError extends Error {

  /**
   * The empty string.
   */
  public static final String EMPTY = "";

  /**
   * The message used if no message was given, and no {@link Throwable} was given.
   * This is needed, because we need to show <em>some</em> message to the administrator.
   * In fact, this message should never occur: developers should always try to give some explanation.
   *
   * {@code UNSPECIFIED_EXTERNAL_ERROR_MESSAGE == }{@value}
   */
  public static final String UNSPECIFIED_EXTERNAL_ERROR_MESSAGE =
      "Could not continue due to an unspecified external error.";

  /**
   * The message used if no message was given, but there was a {@link Throwable} given.
   * This is needed, because we need to show <em>some</em> message to the administrator.
   * This might be used quite often, because the nested {@link #getCause() cause exception} will probably
   * provide the administrator with the information he needs. In this case, there is little the developer
   * that catches the exception and wraps it in an {@link ExternalError} can add.
   *
   * {@code EXCEPTION_WITH_EXTERNAL_CAUSE_MESSAGE == }{@value}
   */
  public static final String EXCEPTION_WITH_EXTERNAL_CAUSE_MESSAGE =
      "An exception occured, which appears to be of an external nature.";



  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @param     message
   *            The message that describes the external exceptional condition.
   * @param     t
   *            The exception that occured, causing this error to be thrown, if that is the case.
   */
  @MethodContract(
    post = {
      @Expression("message == (_message == null) || (_message == EMPTY) ? " +
                    "((_t == null) ? UNSPECIFIED_EXTERNAL_ERROR_MESSAGE : EXCEPTION_WITH_EXTERNAL_CAUSE_MESSAGE) :" +
                    "_message"),
      @Expression("cause == _t")
    }
  )
  public ExternalError(final String message, final Throwable t) {
    super(defaultMessage(message, t), t);
  }

  /**
   * @param     t
   *            The exception that occured, causing this error to be thrown, if that is the case.
   */
  @MethodContract(
    post = {
      @Expression("message == (_t == null) ? UNSPECIFIED_EXTERNAL_ERROR_MESSAGE : " +
                                            "EXCEPTION_WITH_EXTERNAL_CAUSE_MESSAGE)"),
      @Expression("cause == _t")
    }
  )
  public ExternalError(final Throwable t) {
    this(null, t);
  }

  /**
   * @param     message
   *            The message that describes the external exceptional condition.
   */
  @MethodContract(
    post = {
      @Expression("message == (_message == null) || (_message == EMPTY) ? " +
                                 "UNSPECIFIED_EXTERNAL_ERROR_MESSAGE : _message"),
      @Expression("cause == null")
    }
  )
  public ExternalError(final String message) {
    this(message, null);
  }


  @MethodContract(
    post = @Expression("(_message == null) || (_message == EMPTY) ? " +
                         "((_t == null) ? UNSPECIFIED_EXTERNAL_ERROR_MESSAGE : " +
                                         "EXCEPTION_WITH_EXTERNAL_CAUSE_MESSAGE) : " +
                         "_message")
  )
  private static String defaultMessage(final String message, final Throwable t) {
    return ((message != null) && (! EMPTY.equals(message))) ?
               message :
               ((t != null) ? EXCEPTION_WITH_EXTERNAL_CAUSE_MESSAGE : UNSPECIFIED_EXTERNAL_ERROR_MESSAGE);
  }

  /*</construction>*/

}
