/*<license>
Copyright 2005 - $Date: 2008-07-02 11:36:19 +0200 (Wed, 02 Jul 2008) $ by PeopleWare n.v..

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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>Supertype for exceptions we consider <em>internal to the system we are developing,
 *   expected to occur and normal behavior</em> . Internal exceptions are thrown when a
 *   method cannot perform its nominal task.</p>
 * <p>{@code InternalExceptions} should be caught somewhere, and possibly result in end-user
 *   feedback. {@code InternalExceptions} should never result in a crash of the application.</p>
 * <p>Because {@code InternalExceptions} often result in feedback to the end user, the messages that
 *   are based on them should be localized. When instances of this class are created, you should
 *   use an identifying string (in all caps, without spaces) as {@link #getMessage() message},
 *   which can be used to retrieve the appropriate end user message when the exception is dealt with.
 *   The {@link #getMessage() message} thus should not be an extensive end user feedback message itself.
 *   If the given message identifier is {@code null}, {@link #DEFAULT_MESSAGE_KEY} is used
 *   instead. Handling code should always provide a default localized
 *   message for the {@link #DEFAULT_MESSAGE_KEY}.</p>
 * <p><strong>{@link #getLocalizedMessage()} is not used: this turned out to be a difficult pattern
 *   to use.</strong></p>
 * <p>When the reason to throw the {@code InternalException} is itself an exception, it should be
 *   provided as the {@link #getCause()} of the {@code InternalException}.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2007 - $Date: 2008-07-02 11:36:19 +0200 (Wed, 02 Jul 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 1504 $",
         date     = "$Date: 2008-07-02 11:36:19 +0200 (Wed, 02 Jul 2008) $")
@Invars(@Expression("validMessageKey(message)"))
public class InternalException extends Exception {

  /**
   * The empty string.
   *
   * @todo use from smallfries
   */
  public final static String EMPTY = "";

  /**
   * {@code DEFAULT_MESSAGE_INDENTIER == }{@value}
   */
  public final static String DEFAULT_MESSAGE_KEY = "DEFAULT";



  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @param     messageKey
   *            The string that identifies a localized end user feedback message about the
   *            non-nominal behavior.
   * @param     cause
   *            The exception that occured, causing this exception to be thrown, if that is
   *            the case.
   */
  @MethodContract(
    pre  = @Expression("_messageKey == null || _messageKey == EMPTY || validmessageKey(_messageKey)"),
    post = {
      @Expression("message == (_messageKey == null || _messageIdentfier == EMPTY) ? DEFAULT_MESSAGE_KEY : _messageKey"),
      @Expression("cause == _cause")
    }
  )
  public InternalException(final String messageKey, final Throwable cause) {
    super(defaultMessageKey(messageKey), cause);
  }

  private static String defaultMessageKey(String messageKey) {
    return ((messageKey == null) || (EMPTY.equals(messageKey))) ? DEFAULT_MESSAGE_KEY : messageKey;
  }

  /*</construction>*/



  @MethodContract(
    post = @Expression("messageKey != null && matchesMessageKeyPattern(messageKey)")
  )
  public static boolean validMessageKey(String messageKey) {
    return (messageKey != null) &&
           matchesMessageKeyPattern(messageKey);
  }

  /**
   * {@code MESSAGE_KEY_PATTERN ==}{@value}
   */
  public final static String MESSAGE_KEY_PATTERN = "[A-Z][A-Z_]*[A-Z]";

  @MethodContract(
    pre  = @Expression("messageKey != null"),
    post = @Expression("Pattern.compile(MESSAGE_KEY_PATTERN).matcher(messageKey).matches()")
  )
  public static boolean matchesMessageKeyPattern(String messageKey) {
    assert messageKey != null;
    Pattern p = Pattern.compile(MESSAGE_KEY_PATTERN);
    Matcher m = p.matcher(messageKey);
    return m.matches();
  }

}

