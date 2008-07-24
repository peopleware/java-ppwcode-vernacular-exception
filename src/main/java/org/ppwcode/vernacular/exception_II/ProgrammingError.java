/*<license>
Copyright 2004 - $Date: 2008-07-02 11:36:19 +0200 (Wed, 02 Jul 2008) $ by PeopleWare n.v..

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
import org.toryt.annotations_I.Throw;


/**
 * <p>This error is thrown when a programming error is detected during execution. This happens, e.g.,
 *   when the coder notices branches that can never be reached (<code>else</code>-statements in
 *   deep decision trees, or unreachable <code>default</code>-clause in a <code>switch</code>-statement,
 *   or a <code>catch</code>-clause of an exception that can never occur, ...). The intended audience
 *   of these errors is neither the end user, nor the administrator, but <em>we ourself, the
 *   developers</em>.</p>
 * <p>The {@link #getMessage() message} should describe the programming error as closely as possible.
 *   These messages will help you to find the problem if it occurs. Note that for server based
 *   applications (web applications, enterprise applications), debugging in the production environment
 *   is most often plain not possible, and the only available source of information about problems
 *   is a log of the problem as it occurred in the live situation (with desktop applications, certainly
 *   with file based applications, you often can try to replicate the problem with the end user on the
 *   phone, in debug mode). If you cannot pinpoint the exact nature of the programming error, you should
 *   say so explicitly. If you become aware of the programming error by catching an {@link Exception} or
 *   an {@link Error}, it should be carried by instances of this class as the
 *   {@link #getCause() cause}.</p>
 * <p>Instead of constructors, most of the time, the static methods should be used to signal programming
 *   errors. These static methods already feature a message, sparing you the effort. Because we
 *   probably do not foresee all cases, the 1-argument constructor is available too.</p>
 * <p>The best way to think about throwing these errors, is the following. We often document
 *   intricate points of our code. It is better to write assertions as comments, then mere text.
 *   Writing <code><b>assert</b> <i>condition that must be true</i></code> is the best documentation
 *   there is (note that this error should be considered equivalent to an {@link AssertionError}).
 *   It has the glorious benefits over mere comments that, if you are mistaken, you will be warned
 *   immediately, with exact information that points to the exact place of your mistake in thousands
 *   of lines of code. With {@code assert}, you have the benefit that the administrator can choose
 *   whether or not to enable the checks in production. Enabling them will give immediate information
 *   when an error occurs and <em>gives the opportunity to immediately stop processing when an error
 *   occurs, before or immediately after data was corrupted, before the user was given a false expectancy
 *   of success</em>. Disabling them will result in a performance boost (although this should not
 *   be overrated). Throwing a {@code ProgrammingError} should be preferred over an {@code assert}
 *   statement to document branches that normally should not be reached anyway (n this case, the
 *   performance benefit doesn't occur), or when we want to communicate an exception as part of the
 *   error information. We also use
 *
 *   MORE DOC
 *
 * <p>It probably does not make sense to create subtypes of this error for specific situations. There
 *   is no need for internationalization for external errors. If there is extra information that we
 *   can communicate, we can add it to the message.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date: 2008-07-02 11:36:19 +0200 (Wed, 02 Jul 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 1504 $",
         date     = "$Date: 2008-07-02 11:36:19 +0200 (Wed, 02 Jul 2008) $")
@Invars({
  @Expression("message != null"),
  @Expression("message != EMPTY")
})
public class ProgrammingError extends AssertionError {

  /**
   * The empty string.
   *
   * @todo use from smallfries
   */
  public final static String EMPTY = "";



  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * Use this constructor only to signal the occurrence of an exception that in a
   * correct program cannot occur.
   * Often, you need to catch checked exceptions in your code you can prove cannot occur.
   * Instead of documenting an empty catch clause, it is better to throw this exception.
   *
   * @param     message
   *            The message that describes the programming error.
   * @param     unexpected
   *            The unexpected exception that occured, causing this
   *            error to be thrown, if that is the case.
   */
  @MethodContract(
    pre  = @Expression("_message != EMPTY"),
    post = {
      @Expression("message == _message"),
      @Expression("cause == _unexpected")
    }
  )
  private ProgrammingError(final String message, final Throwable unexpected) {
    // asserts for preconditions don't make much sense in this case
    super(message);
    initCause(unexpected);
  }

  /**
   * @param     message
   *            The message that describes the programming error.
   */
  @MethodContract(
    pre  = {
      @Expression("message != null"),
      @Expression("message != EMPTY")
    },
    post = {
      @Expression("message == _message"),
      @Expression("cause == null")
    }
  )
  public ProgrammingError(final String message) {
    // asserts for preconditions don't make much sense in this case
    this(message, null);
  }

  /*</construction>*/



  /**
   * The message used if the constructor with 1 {@link Throwable} parameter is used,
   * to signal the occurrence of an unexpected exception, without further explanation.
   *
   * {@code UNEXPECTED_EXCEPTION_MESSAGE == }{@value}
   */
  public final static String UNEXPECTED_EXCEPTION_MESSAGE = "An unexpected exception occured.";

  /**
   * Use in a {@code catch} clause, to express that the catch clause should never be used, or
   * in other words, the exception should not occur.
   *
   * Usage:
   * <pre>
   * ...
   * catch (<var>SomeException</var> sExc) {
   *   unexpectedException(sExc);
   * }
   * ...
   * </pre>
   */
  @MethodContract(
    post = @Expression(value = "false",
                       description = "false cannot be made true; thus, there is no alternative but to throw an exception"),
    exc  = @Throw(type = ProgrammingError.class,
                  cond = {
                    @Expression("e.message == UNEXPECTED_EXCEPTION_MESSAGE"),
                    @Expression("e.cause == t")
                  })
  )
  public static void unexpectedException(Throwable t) {
    throw new ProgrammingError(UNEXPECTED_EXCEPTION_MESSAGE, t);
  }

  /**
   * Use in a {@code catch} clause, to express that the catch clause should never be used, or
   * in other words, the exception should not occur.
   *
   * Usage:
   * <pre>
   * ...
   * catch (<var>SomeException</var> sExc) {
   *   unexpectedException(sExc, <var>whyIsItUnexpected</var>);
   * }
   * ...
   * </pre>
   */
  @MethodContract(
    post = @Expression(value = "false",
                       description = "false cannot be made true; thus, there is no alternative but to throw an exception"),
    exc  = @Throw(type = ProgrammingError.class,
                  cond = {
                    @Expression("e.message == UNEXPECTED_EXCEPTION_MESSAGE + ': ' + _whyIsItUnexpected"),
                    @Expression("e.cause == t")
                  })
  )
  public static void unexpectedException(Throwable t, String whyIsItUnexpected) {
    throw new ProgrammingError(UNEXPECTED_EXCEPTION_MESSAGE + ": " + whyIsItUnexpected, t);
  }



  /**
   * The message used to signal a precondition violation.
   *
   * {@code PRECONDITION_VIOLATION_MESSAGE == }{@value}
   */
  public final static String PRECONDITION_VIOLATION_MESSAGE = "Precondition violation";

  /**
   * Verify a precondition.
   *
   * Usage: <code><b>assert</b> pre(<var>condition</var>)</code>
   */
  @MethodContract(
    post = {
      @Expression("return == true"),
      @Expression(value = "'_condition",
                  description = "old value cannot be made true if it is not true to begin with; in that case, " +
                              "there is no alternative but to throw an exception")
    },
    exc  = @Throw(type = ProgrammingError.class,
                  cond = @Expression("! _condition && e.message == PRECONDITION_VIOLATION_MESSAGE"))
  )
  public static boolean pre(boolean condition) {
    if (! condition) {
      throw new ProgrammingError(PRECONDITION_VIOLATION_MESSAGE);
    }
    return condition;
  }

  /**
   * Verify a precondition.
   *
   * Usage: <code><b>assert</b> pre(<var>condition</var>, &quot;<var>preconditionIdentification</var>&quot;)</code>
   */
  @MethodContract(
    pre  = {
      @Expression("_preconditionIdentification != null"),
      @Expression("_preconditionIdentification != EMPTY"),
    },
    post = {
      @Expression("return == true"),
      @Expression(value = "'_condition",
                  description = "old value cannot be made true if it is not true to begin with; in that case, " +
                              "there is no alternative but to throw an exception")
    },
    exc  = @Throw(type = ProgrammingError.class,
                  cond = @Expression("! _condition && " +
                                     "e.message == PRECONDITION_VIOLATION_MESSAGE + ': ' + _preconditionIdentification"))
  )
  public static boolean pre(boolean condition, String preconditionIdentification) {
    if (! condition) {
      throw new ProgrammingError(PRECONDITION_VIOLATION_MESSAGE + ": " + preconditionIdentification);
    }
    return condition;
  }



  /**
   * The message used to signal a dependency injection problem, part 1.
   *
   * {@code DEPENDENCY_INJECTION_PROBLEM_MESSAGE_1 == }{@value}
   */
  public final static String DEPENDENCY_INJECTION_PROBLEM_MESSAGE_1 = "Dependency injection problem: property \"";

  /**
   * The message used to signal a dependency injection problem, part 2.
   *
   * {@code DEPENDENCY_INJECTION_PROBLEM_MESSAGE_2 == }{@value}
   */
  public final static String DEPENDENCY_INJECTION_PROBLEM_MESSAGE_2 = "\" not set.";

  /**
   * Verify dependency injection: throw a programming error if the dependency is not
   * set. This is a special form of precondition.
   *
   * Usage: <code><b>assert</b> dependency(get<var>DependencyProperty</var>(), &quot;<var>dependencyProperty</var>&quot;)</code>
   */
  @MethodContract(
    pre  = {
      @Expression("_dependencyPropertyName != null"),
      @Expression("_dependencyPropertyName != EMPTY"),
    },
    post = {
      @Expression("return == true"),
      @Expression(value = "'_dependency != null",
                  description = "old value cannot be made true if it is not true to begin with; in that case, " +
                              "there is no alternative but to throw an exception")
    },
    exc  = @Throw(type = ProgrammingError.class,
                  cond = {
                    @Expression("_dependency != null"),
                    @Expression("e.message == DEPENDENCY_INJECTION_PROBLEM_MESSAGE_1 + _dependencyIdentifier + DEPENDENCY_INJECTION_PROBLEM_MESSAGE_2"),
                    @Expression("e.cause == null")
                  })
  )
  public static boolean dependency(Object dependency, String dependencyPropertyName) {
    return dependency(dependency != null, dependencyPropertyName);
  }

  /**
   * Verify dependency injection: throw a programming error if the condition is not met. This is a special form of precondition.
   *
   * Usage: <code><b>assert</b> dependency(<var>condition</var>, &quot;<var>dependencyProperty</var>&quot;)</code>
   */
  @MethodContract(
    pre  = {
      @Expression("_dependencyIdentifier != null"),
      @Expression("_dependencyIdentifier != EMPTY"),
    },
    post = {
      @Expression("return == true"),
      @Expression(value = "'_condition",
                  description = "old value cannot be made true if it is not true to begin with; in that case, " +
                              "there is no alternative but to throw an exception")
    },
    exc  = @Throw(type = ProgrammingError.class,
                  cond = {
                    @Expression("! _condition"),
                    @Expression("e.message == DEPENDENCY_INJECTION_PROBLEM_MESSAGE_1 + _dependencyIdentifier + DEPENDENCY_INJECTION_PROBLEM_MESSAGE_2"),
                    @Expression("e.cause == null")
                  })
  )
  public static boolean dependency(boolean condition, String dependencyIdentifier) {
    if (! condition) {
      throw new ProgrammingError(DEPENDENCY_INJECTION_PROBLEM_MESSAGE_1 + dependencyIdentifier +
                                 DEPENDENCY_INJECTION_PROBLEM_MESSAGE_2);
    }
    return condition;
  }

  /**
   * Verify dependency injection: throw a programming error if the condition is not met. This is a special form of precondition.
   *
   * Usage: <code><b>assert</b> dependency(<var>condition</var>, &quot;<var>dependencyProperty</var>&quot;, &quot;<var>description</var>&quot;)</code>
   */
  @MethodContract(
    pre  = {
      @Expression("_dependencyIdentifier != null"),
      @Expression("_dependencyIdentifier != EMPTY"),
      @Expression("_description != null"),
      @Expression("_description != EMPTY"),
    },
    post = {
      @Expression("return == true"),
      @Expression(value = "'_condition",
                  description = "old value cannot be made true if it is not true to begin with; in that case, " +
                              "there is no alternative but to throw an exception")
    },
    exc  = @Throw(type = ProgrammingError.class,
                  cond = {
                    @Expression("! _condition"),
                    @Expression("e.message == DEPENDENCY_INJECTION_PROBLEM_MESSAGE_1 + _dependencyIdentifier + " +
                                "DEPENDENCY_INJECTION_PROBLEM_MESSAGE_2 + ' ' + _description"),
                    @Expression("e.cause == null")
                  })
  )
  public static boolean dependency(boolean condition, String dependencyIdentifier, String description) {
    if (! condition) {
      throw new ProgrammingError(DEPENDENCY_INJECTION_PROBLEM_MESSAGE_1 + dependencyIdentifier +
                                 DEPENDENCY_INJECTION_PROBLEM_MESSAGE_2 + " " + description);
    }
    return condition;
  }

}
