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

package org.ppwcode.vernacular.exception_III;


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * <p>When a programming error is detected during execution, an {@link AssertionError} is to be thrown.
 *   This happens, e.g., when the coder notices branches that can never be reached
 *   (<code>else</code>-statements in deep decision trees, or unreachable <code>default</code>-clause in
 *   a <code>switch</code>-statement, or a <code>catch</code>-clause of an exception that can never occur,
 *   when a precondition is violated, ...). The intended audience of these errors is neither the end user,
 *   nor the administrator, but <em>we ourself, the developers</em>.</p>
 *
 * <p>The {@link AssertionError#getMessage() message} should describe the programming error as closely as possible.
 *   These messages will help you to find the problem if it occurs. Note that for server based
 *   applications (web applications, enterprise applications), debugging in the production environment
 *   is most often plain not possible, and the only available source of information about problems
 *   is a log of the problem as it occurred in the live situation (with desktop applications, certainly
 *   with file based applications, you often can try to replicate the problem with the end user on the
 *   phone, in debug mode). If you cannot pinpoint the exact nature of the programming error, you should
 *   say so explicitly. If you become aware of the programming error by catching an {@link Exception} or
 *   an {@link Error}, it should be carried by instances of this class as the
 *   {@link AssertionError#getCause() cause}.</p>
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
 *   be overrated). Throwing a {@code AssertionError} should be preferred over an {@code assert}
 *   statement to document branches that normally should not be reached anyway (n this case, the
 *   performance benefit doesn't occur), or when we want to communicate an exception as part of the
 *   error information. We also use
 *
 *   MUDO MORE DOC
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
 *
 * <p>It probably does not make sense to create subtypes of this error for specific situations. There
 *   is no need for internationalization for external errors. If there is extra information that we
 *   can communicate, we can add it to the message.</p>
 *
 * @note In draft versions of this vernacular, we introduced a separate {@code AssertionError} for
 *       this, and it would be cleaner if {@link AssertionError} would be a subtype of such a general
 *       {@code AssertionError} throwable. This, however, is not possible. In a second version, we
 *       succeeded in making such a {@code AssertionError} a subtype of {@link AssertionError}.
 *       Yet, it turned out that there was little extra added by this subtype (apart from setting
 *       the cause and message of the error in a controlled fashion, which is now done in the static
 *       methods in this class). To that class, we added a lot of static methods to make documenting
 *       programming errors easier and more consistent. Now it turns out that we can do all of the
 *       necessary work in those static methods, immediately on the existing {@link AssertionError}.
 *       Applying Occam's Razor, the specialized {@code AssertionError} hence disappeared.
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
public final class ProgrammingErrorHelpers {

  /**
   * <p>The empty String.</p>
   * <p>{@code EMPTY == }{@value}
   */
  public static final String EMPTY = "";

  /**
   * <p>A colon and a space.</p>
   * <p>{@code COLON == }{@value}
   */
  public static final String COLON = ": ";

  /**
   * No public constructor for this class.
   */
  private ProgrammingErrorHelpers() {
    // NOP
  }

  /**
   * {@link AssertionError} has no true constructor with a cause argument.
   * This method emulates that.
   */
  public static AssertionError newAssertionError(String message, Throwable t) {
    AssertionError aErr = new AssertionError(message);
    aErr.initCause(t);
    return aErr;
  }

  /**
   * <p>The message used to signal entry in a branch that should never be visited.</p>
   * <p>{@code UNEXPECTED_EXCEPTION_MESSAGE == }{@value}</p>
   */
  public static final String DEAD_BRANCH_MESSAGE = "Execution entered a branch that should never be entered.";

  /**
   * <p>Use in a code branch ({@code if}, {@code else}, {@code default}, ...) that should never be reached if
   *   the code is correct. For dead {@code catch} clauses, see {@link #unexpectedException(Throwable)}.<p>
   * <p>Usage:</p>
   * <pre>
   * ...
   * else {
   *   deadBranch();
   * }
   * ...
   * </pre>
   */
  @MethodContract(
    post = @Expression(value = "false",
                       description = "false cannot be made true; " +
                                    "thus, there is no alternative but to throw an exception"),
    exc  = @Throw(type = AssertionError.class,
                  cond = {
                    @Expression("e.message == DEAD_BRANCH_MESSAGE"),
                    @Expression("e.cause == null")
                  })
  )
  public static void deadBranch() {
    throw new AssertionError(DEAD_BRANCH_MESSAGE);
  }

  /**
   * <p>Use in a code branch ({@code if}, {@code else}, {@code default}, ...) that should never be reached if
   * vvthe code is correct. For dead {@code catch} clauses, see {@link #unexpectedException(Throwable, String)}.</p>
   * <p>Usage:</p>
   * <pre>
   * ...
   * else {
   *   deadBranch(&quot;<var>why it is dead</var>&quot;);
   * }
   * ...
   * </pre>
   */
  @MethodContract(
    post = @Expression(value = "false",
                       description = "false cannot be made true; " +
                                     "thus, there is no alternative but to throw an exception"),
    exc  = @Throw(type = AssertionError.class,
                  cond = {
                    @Expression("e.message == DEAD_BRANCH_MESSAGE + COLON + _whyItIsDead"),
                    @Expression("e.cause == null")
                  })
  )
  public static void deadBranch(String whyItIsDead) {
    throw new AssertionError(DEAD_BRANCH_MESSAGE + COLON + whyItIsDead);
  }



  /**
   * <p>The message used to signal the occurrence of an unexpected exception, without further
   *   explanation.</p>
   * <p>{@code UNEXPECTED_EXCEPTION_MESSAGE == }{@value}</p>
   */
  public static final String UNEXPECTED_EXCEPTION_MESSAGE = "An unexpected exception occured.";

  /**
   * Use in a {@code catch} clause, to express that the catch clause should never be used, or
   * in other words, the exception should not occur.
   * This is a special case of {@link #deadBranch()}.
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
                       description = "false cannot be made true; " +
                                     "thus, there is no alternative but to throw an exception"),
    exc  = @Throw(type = AssertionError.class,
                  cond = {
                    @Expression("e.message == UNEXPECTED_EXCEPTION_MESSAGE"),
                    @Expression("e.cause == t")
                  })
  )
  public static void unexpectedException(Throwable t) {
    throw newAssertionError(UNEXPECTED_EXCEPTION_MESSAGE, t);
  }

  /**
   * <p>Use in a {@code catch} clause, to express that the catch clause should never be used, or
   *   in other words, the exception should not occur.
   *   This is a special case of {@link #deadBranch(String)}.</p>
   * <p>Usage:</p>
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
                       description = "false cannot be made true; " +
                                     "thus, there is no alternative but to throw an exception"),
    exc  = @Throw(type = AssertionError.class,
                  cond = {
                    @Expression("e.message == UNEXPECTED_EXCEPTION_MESSAGE + COLON + _whyIsItUnexpected"),
                    @Expression("e.cause == t")
                  })
  )
  public static void unexpectedException(Throwable t, String whyIsItUnexpected) {
    throw newAssertionError(UNEXPECTED_EXCEPTION_MESSAGE + COLON + whyIsItUnexpected, t);
  }



  /**
   * <p>The message used to signal a precondition violation.</p>
   * <p>{@code PRECONDITION_VIOLATION_MESSAGE == }{@value}</p>
   */
  public static final String PRECONDITION_VIOLATION_MESSAGE = "Precondition violation";

  /**
   * <p>Verify a precondition.</p>
   * <p>Usage: <code><b>assert</b> pre(<var>condition</var>)</code></p>
   */
  @MethodContract(
    post = {
      @Expression("return == true"),
      @Expression(value = "'_condition",
                  description = "old value cannot be made true if it is not true to begin with; in that case, " +
                              "there is no alternative but to throw an exception")
    },
    exc  = @Throw(type = AssertionError.class,
                  cond = @Expression("! _condition && e.message == PRECONDITION_VIOLATION_MESSAGE"))
  )
  public static boolean pre(boolean condition) {
    if (! condition) {
      throw new AssertionError(PRECONDITION_VIOLATION_MESSAGE);
    }
    return condition;
  }

  /**
   * <p>Verify a precondition.</p>
   * <p>Usage: <code><b>assert</b> pre(<var>condition</var>,
   *   &quot;<var>preconditionIdentification</var>&quot;)</code></p>
   */
  @MethodContract(
    pre  = {
      @Expression("_preconditionIdentification != null"),
      @Expression("_preconditionIdentification != EMPTY")
    },
    post = {
      @Expression("return == true"),
      @Expression(value = "'_condition",
                  description = "old value cannot be made true if it is not true to begin with; in that case, " +
                              "there is no alternative but to throw an exception")
    },
    exc  = @Throw(type = AssertionError.class,
                  cond = @Expression("! _condition && " +
                                     "e.message == PRECONDITION_VIOLATION_MESSAGE + COLON +" +
                                                    " _preconditionIdentification"))
  )
  public static boolean pre(boolean condition, String preconditionIdentification) {
    if (! condition) {
      throw new AssertionError(PRECONDITION_VIOLATION_MESSAGE + COLON + preconditionIdentification);
    }
    return condition;
  }

  /**
   * <p>The message used to signal a {@code null}-argument problem.</p>
   * <p>{@code ARGUMENT_NOT_NULL_MESSAGE == }{@value}</p>
   */
  public static final String ARGUMENT_NOT_NULL_MESSAGE = "argument should not be null";

  /**
   * <p>Verify precondition: {@code argument != null}: throw a programming error if {@code argument} is not
   *   set. This is a special form of precondition ({@link #pre(boolean)}).</p>
   * <p> Usage: <code><b>assert</b> preArgumentNotNull(<var>argument</var>)</code></p>
   */
  @MethodContract(
    post = {
      @Expression("return == true"),
      @Expression(value = "'_argument != null",
                  description = "old value cannot be made true if it is not true to begin with; in that case, " +
                              "there is no alternative but to throw an exception")
    },
    exc  = @Throw(type = AssertionError.class,
                  cond = {
                    @Expression("_argument == null"),
                    @Expression("e.message == PRECONDITION_VIOLATION_MESSAGE + COLON + ARGUMENT_NOT_NULL_MESSAGE"),
                    @Expression("e.cause == null")
                  })
  )
  public static boolean preArgumentNotNull(Object argument) {
    return pre(argument != null, ARGUMENT_NOT_NULL_MESSAGE);
  }

  /**
   * <p>Verify precondition: {@code argument != null}: throw a programming error if {@code argument} is not
   *   set. This is a special form of precondition ({@link #pre(boolean)}).</p>
   * <p> Usage: <code><b>assert</b> preArgumentNotNull(<var>argument</var>, <var>argumentName</var>)</code></p>
   */
  @MethodContract(
    pre  = {
            @Expression("_argumentName != null"),
            @Expression("_argumentName != EMPTY")
          },
    post = {
      @Expression("return == true"),
      @Expression(value = "'_argument != null",
                  description = "old value cannot be made true if it is not true to begin with; in that case, " +
                              "there is no alternative but to throw an exception")
    },
    exc  = @Throw(type = AssertionError.class,
                  cond = {
                    @Expression("_argument == null"),
                    @Expression("e.message == PRECONDITION_VIOLATION_MESSAGE + COLON + ARGUMENT_NOT_NULL_MESSAGE + " +
                                "' (' + _argumentName + ')'"),
                    @Expression("e.cause == null")
                  })
  )
  public static boolean preArgumentNotNull(Object argument, String argumentName) {
    return pre(argument != null, ARGUMENT_NOT_NULL_MESSAGE + " (" + argumentName + ")");
  }
  /**
   * <p>The message used to signaling a {@code null} or empty String argument.</p>
   * <p>{@code STRING_ARGUMENT_NOT_EMPTY_MESSAGE == }{@value}</p>
   */
  public static final String STRING_ARGUMENT_NOT_EMPTY_MESSAGE = "argument should not be null nor empty";

  /**
   * <p>Verify precondition: {@code (argument != null) && ! EMPTY.equals(argument)}}: throw a programming error if
   *   {@code argument} is {@code null} or empty. This is a special form of precondition ({@link #pre(boolean)}).</p>
   * <p> Usage: <code><b>assert</b> preArgumentNotNull(<var>argument</var>)</code></p>
   */
  @MethodContract(
    post = {
      @Expression("return == true"),
      @Expression(value = "'_argument != null && '_argument != EMPTY",
                  description = "old value cannot be made true if it is not true to begin with; in that case, " +
                              "there is no alternative but to throw an exception")
    },
    exc  = @Throw(type = AssertionError.class,
                  cond = {
                    @Expression("_argument == null || _argument == EMPTY"),
                    @Expression("e.message == PRECONDITION_VIOLATION_MESSAGE + COLON + STRING_ARGUMENT_NOT_EMPTY_MESSAGE"),
                    @Expression("e.cause == null")
                  })
  )
  public static boolean preArgumentNotEmpty(String argument) {
    return pre(argument != null && ! EMPTY.equals(argument), STRING_ARGUMENT_NOT_EMPTY_MESSAGE);
  }

  /**
   * <p>Verify precondition: {@code (argument != null) && ! EMPTY.equals(argument)}}: throw a programming error if
   *   {@code argument} is {@code null} or empty. This is a special form of precondition ({@link #pre(boolean)}).</p>
   * <p> Usage: <code><b>assert</b> preArgumentNotNull(<var>argument</var>, <var>argumentName</var>)</code></p>
   */
  @MethodContract(
    post = {
      @Expression("return == true"),
      @Expression(value = "'_argument != null && '_argument != EMPTY",
                  description = "old value cannot be made true if it is not true to begin with; in that case, " +
                              "there is no alternative but to throw an exception")
    },
    exc  = @Throw(type = AssertionError.class,
                  cond = {
                    @Expression("_argument == null || _argument == EMPTY"),
                    @Expression("e.message == PRECONDITION_VIOLATION_MESSAGE + COLON + " +
                                "STRING_ARGUMENT_NOT_EMPTY_MESSAGE + ' (' + argumentName + ')'"),
                    @Expression("e.cause == null")
                  })
  )
  public static boolean preArgumentNotEmpty(String argument, String argumentName) {
    return pre(argument != null && ! EMPTY.equals(argument), STRING_ARGUMENT_NOT_EMPTY_MESSAGE +
               " (" + argumentName + ")");
  }



  /**
   * <p>The message used to signal a dependency injection problem, part 1.</p>
   * <p>{@code DEPENDENCY_INJECTION_PROBLEM_MESSAGE_1 == }{@value}</p>
   */
  public static final String DEPENDENCY_INJECTION_PROBLEM_MESSAGE_1 = "Dependency injection problem: property \"";

  /**
   * <p>The message used to signal a dependency injection problem, part 2.</p>
   * <p>{@code DEPENDENCY_INJECTION_PROBLEM_MESSAGE_2 == }{@value}</p>
   */
  public static final String DEPENDENCY_INJECTION_PROBLEM_MESSAGE_2 = "\" not set.";

  /**
   * <p>Verify dependency injection: throw a programming error if the dependency is not
   *   set. This is a special form of precondition ({@link #pre(boolean)}).</p>
   * <p> Usage: <code><b>assert</b> dependency(get<var>DependencyProperty</var>(),
   *   &quot;<var>dependencyProperty</var>&quot;)</code></p>
   */
  @MethodContract(
    pre  = {
      @Expression("_dependencyPropertyName != null"),
      @Expression("_dependencyPropertyName != EMPTY")
    },
    post = {
      @Expression("return == true"),
      @Expression(value = "'_dependency != null",
                  description = "old value cannot be made true if it is not true to begin with; in that case, " +
                              "there is no alternative but to throw an exception")
    },
    exc  = @Throw(type = AssertionError.class,
                  cond = {
                    @Expression("_dependency == null"),
                    @Expression("e.message == DEPENDENCY_INJECTION_PROBLEM_MESSAGE_1 + _dependencyIdentifier + " +
                                             "DEPENDENCY_INJECTION_PROBLEM_MESSAGE_2"),
                    @Expression("e.cause == null")
                  })
  )
  public static boolean dependency(Object dependency, String dependencyPropertyName) {
    return dependency(dependency != null, dependencyPropertyName);
  }

  /**
   * <p>Verify dependency injection: throw a programming error if the condition is not met.
   *   This is a special form of precondition ({@link #pre(boolean)}).</p>
   * <p>Usage: <code><b>assert</b> dependency(<var>condition</var>,
   *   &quot;<var>dependencyProperty</var>&quot;)</code></p>
   */
  @MethodContract(
    pre  = {
      @Expression("_dependencyIdentifier != null"),
      @Expression("_dependencyIdentifier != EMPTY")
    },
    post = {
      @Expression("return == true"),
      @Expression(value = "'_condition",
                  description = "old value cannot be made true if it is not true to begin with; in that case, " +
                              "there is no alternative but to throw an exception")
    },
    exc  = @Throw(type = AssertionError.class,
                  cond = {
                    @Expression("! _condition"),
                    @Expression("e.message == DEPENDENCY_INJECTION_PROBLEM_MESSAGE_1 + _dependencyIdentifier + " +
                                             "DEPENDENCY_INJECTION_PROBLEM_MESSAGE_2"),
                    @Expression("e.cause == null")
                  })
  )
  public static boolean dependency(boolean condition, String dependencyIdentifier) {
    if (! condition) {
      throw new AssertionError(DEPENDENCY_INJECTION_PROBLEM_MESSAGE_1 + dependencyIdentifier +
                                 DEPENDENCY_INJECTION_PROBLEM_MESSAGE_2);
    }
    return condition;
  }

  // TODO version with only property name + reflection

  /**
   * <p>Verify dependency injection: throw a programming error if the condition is not met.
   *   This is a special form of precondition ({@link #pre(boolean)}).</p>
   * <p>Usage: <code><b>assert</b> dependency(<var>condition</var>,
   *   &quot;<var>dependencyProperty</var>&quot;, &quot;<var>description</var>&quot;)</code></p>
   */
  @MethodContract(
    pre  = {
      @Expression("_dependencyIdentifier != null"),
      @Expression("_dependencyIdentifier != EMPTY"),
      @Expression("_description != null"),
      @Expression("_description != EMPTY")
    },
    post = {
      @Expression("return == true"),
      @Expression(value = "'_condition",
                  description = "old value cannot be made true if it is not true to begin with; in that case, " +
                              "there is no alternative but to throw an exception")
    },
    exc  = @Throw(type = AssertionError.class,
                  cond = {
                    @Expression("! _condition"),
                    @Expression("e.message == DEPENDENCY_INJECTION_PROBLEM_MESSAGE_1 + _dependencyIdentifier + " +
                                "DEPENDENCY_INJECTION_PROBLEM_MESSAGE_2 + ' ' + _description"),
                    @Expression("e.cause == null")
                  })
  )
  public static boolean dependency(boolean condition, String dependencyIdentifier, String description) {
    if (! condition) {
      throw new AssertionError(DEPENDENCY_INJECTION_PROBLEM_MESSAGE_1 + dependencyIdentifier +
                                 DEPENDENCY_INJECTION_PROBLEM_MESSAGE_2 + " " + description);
    }
    return condition;
  }

}
