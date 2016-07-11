/*<license>
Copyright 2004 - 2016 by PeopleWare n.v..

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

package org.ppwcode.vernacular.exception.IV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <p>Supertype for exceptions we consider <em>internal to the system we are developing,
 *   expected to occur and normal (though non-nominal) behavior</em>. Application exceptions are
 *   thrown when a method cannot perform its nominal task.</p>
 * <p>{@code ApplicationExceptions} should be caught somewhere, and possibly result in end-user
 *   feedback. {@code ApplicationExceptions} should never result in a crash of the application.
 *   Neither developers not administrators have a need for the information this exception
 *   expresses.</p>
 * <p>When the reason to throw the {@code ApplicationException} is itself an exception, it should be
 *   provided as the {@link #getCause()} of the {@code ApplicationException}.</p>
 *
 * <p>Because {@code ApplicationExceptions} often result in feedback to the end user, the messages that
 *   are based on them should be localized. When instances of this class are created, you should
 *   use an identifying string (in all caps, without spaces) as {@link #getMessage() message},
 *   which can be used to retrieve the appropriate end user message when the exception is dealt with.
 *   The {@link #getMessage() message} thus should not be an extensive end user feedback message itself.
 *   If the given message identifier is {@code null}, {@link #DEFAULT_MESSAGE_KEY} is used
 *   instead. Handling code should always provide a default localized
 *   message for the {@link #DEFAULT_MESSAGE_KEY}.</p>
 * <p><strong>{@link #getLocalizedMessage()} is not used: this turned out to be a difficult pattern
 *   to use.</strong></p>
 *
 * <p>Note: in a previous version, it was suggested that exceptions could translate themselves.
 *   However, that was in a time when users interfaces where also handled on the server. Now, we aim at a completely
 *   separate UI as a one-page-app, in HTML / CSS / JavaScript. Translation should be handled there.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
/*
@Invars(@Expression("validMessageKey(message)"))
*/
@SuppressWarnings("WeakerAccess")
public class ApplicationException extends Exception {

  /**
   * The empty string.
   */
  public static final String EMPTY = "";

  /**
   * The default key used if no key is provided. This is necessary, because we cannot use
   * {@code null} or {@code EMPTY} to lookup i18ned end-user messages.
   *
   * {@code DEFAULT_MESSAGE_KEY == }{@value}
   */
  public static final String DEFAULT_MESSAGE_KEY = "DEFAULT";



  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @param     messageKey
   *            The string that identifies a localized end user feedback message about the
   *            non-nominal behavior.
   * @param     cause
   *            The exception that occurred, causing this exception to be thrown, if that is
   *            the case.
   */
  /*
  @MethodContract(
    pre  = @Expression("_messageKey == null || _messageKey == EMPTY || validMessageKey(_messageKey)"),
    post = {
      @Expression("message == (_messageKey == null || _messageIdentifier == EMPTY) ? DEFAULT_MESSAGE_KEY : _messageKey"),
      @Expression("cause == _cause")
    }
  )
  */
  public ApplicationException(final String messageKey, final Throwable cause) {
    super(defaultMessageKey(messageKey), cause);
  }

  private static String defaultMessageKey(final String messageKey) {
    return ((messageKey == null) || (EMPTY.equals(messageKey))) ? DEFAULT_MESSAGE_KEY : messageKey;
  }

  /*</construction>*/



  /*<section name="comparison">*/
  //------------------------------------------------------------------

  /**
   * <p>Compare {@code other} to this: is other of the the exact same
   * type and does other have the exact same properties.</p>
   *
   * <p>This method is an alternative to {@link #equals(Object)}, which
   * we cannot override, because we need to keep reference semantics
   * for exceptions.</p>
   *
   * <p>This method is introduced mainly for use in contracts of methods
   * that throw property exceptions, and in unit tests for those
   * methods.</p>
   *
   * <p>This method must be overridden in every subclass that adds a property
   * to include that property in the comparison.</p>
   *
   * <p>Note: methods was formerly called {@code hasSameValues}, and now replaces
   *  {@code hasSameValues}, 2 {@code contains} methods and 2 {@code reportsOn}
   *  methods, which in practice did not fulfill their promise.</p>
   *
   * @since III
   */
  /*
  @MethodContract(
    post = @Expression("result ? (_other != null) && (_other.class = class) && " +
                       "(message == _other.message) && (cause == _other.cause) && " +
                       "(_other.cause == null ? cause == null : cause != null && _other.cause.class == cause.class)")
  )
  */
  public boolean like(ApplicationException other) {
    return (other != null) && (other.getClass() == getClass()) &&
           eqn(other.getMessage(), getMessage()) &&
           (other.getCause() == null ? getCause() == null :
             (getCause() != null && other.getCause().getClass() == getCause().getClass()));
  }

  protected final boolean eqn(Object o1, Object o2) {
    return o1 == null ? o2 == null : o1.equals(o2);
  }

  /*</section>*/



  /*
  @MethodContract(
    post = @Expression("messageKey != null && matchesMessageKeyPattern(messageKey)")
  )
  */
  public static boolean validMessageKey(final String messageKey) {
    return (messageKey != null) &&
           matchesMessageKeyPattern(messageKey);
  }

  /**
   * {@code MESSAGE_KEY_PATTERN ==}{@value}
   */
  public static final String MESSAGE_KEY_PATTERN = "[A-Z][A-Z_]*[A-Z]";

  /*
  @MethodContract(
    pre  = @Expression("messageKey != null"),
    post = @Expression("Pattern.compile(MESSAGE_KEY_PATTERN).matcher(messageKey).matches()")
  )
  */
  public static boolean matchesMessageKeyPattern(final String messageKey) {
    assert messageKey != null;
    Pattern p = Pattern.compile(MESSAGE_KEY_PATTERN);
    Matcher m = p.matcher(messageKey);
    return m.matches();
  }

}

