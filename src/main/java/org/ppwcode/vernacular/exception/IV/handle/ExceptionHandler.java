/*<license>
Copyright 2005 - 2016 by PeopleWare n.v..

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

package org.ppwcode.vernacular.exception.IV.handle;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ppwcode.vernacular.exception.IV.ApplicationException;
import org.ppwcode.vernacular.exception.IV.ExternalError;
import org.ppwcode.vernacular.exception.IV.util.ExceptionHelpers;

import java.util.ArrayList;
import java.util.List;

import static org.ppwcode.vernacular.exception.IV.util.ProgrammingErrorHelpers.newAssertionError;
import static org.ppwcode.vernacular.exception.IV.util.ProgrammingErrorHelpers.preArgumentNotNull;

/**
 * <p>Final handling of {@link Throwable Throwables} according to the ppwcode exception vernacular.
 *   {@link ApplicationException ApplicationExceptions} are thrown without ado. They should be caught in the UI layer
 *   and communicated to the end user. {@link ExternalError ExternalErrors} are communicated to the administrator,
 *   and then thrown. {@link AssertionError Programming errors} are communicated to the administrator and the
 *   developers, and then thrown. Other {@link Throwable Throwables} are translated in
 *   {@link AssertionError AssertionErrors} (programming errors) and also communicated to the administrator and the
 *   developers.</p>
 * <p>The handler looks for {@link ApplicationException ApplicationExceptions}, {@link ExternalError ExternalErrors}
 *   or {@link AssertionError AssertionErrors} inside the causal chain (and legacy patterns of the causal chain,
 *   see {@link ExceptionHelpers#huntFor(Throwable, Class)}) of the {@link Throwable} it is asked to handle.
 *   This is done by a chain of {@link ThrowableTriager ThrowableTriagers}, which have to be set up on an instance
 *   of this type. The first {@link ThrowableTriager} in the chain is always a {@link PpwcodeTriager}. This makes
 *   sure that earlier triage is appropriately handled.</p>
 * <p>Communication to the administrator is done via the log.</p>
 * <p>An remote API method, should setup its exception handler, e.g., with an extra
 *   {@code SqlExceptionTriager} (with a {@code SqlExceptionHandler} for the database type used), an extra
 *   {@code JpaTriager}, an extra {@code JtaTriager} and an extra {@code EjbTriager}. The order in which
 *   triagers can influence the outcome. Suppose in the example above, that there is a {@code SQLException}
 *   nested in a JPA exception, which in its turn is nested in a JTA exception, which in its turn nested in an
 *   EJB exception. With the {@code SqlExceptionTriager} before the others, the {@code SQLException} will be
 *   recognized as an {@link ApplicationException} or an {@link ExternalError}, and that will be reported. Extra
 *   information earlier in the causal chain is discarded. When the {@code JpaTriager} is configured to triage
 *   earlier, the JPA exception will probably be recognized as a programming error, and the
 *   {@code SqlExceptionTriager} will not be asked to triage the exception. This is comparable to the importance
 *   of the order of {@code catch} clauses.</p>
 * <p>The pattern to use for handling exceptions, in the method that is on the interface of an architectural layer,
 *   is:</p>
 * <pre>
 *   ...
 *
 *   private final static Log _LOG = LogFactory.getLog(<var>MyClass</var>.class.class);
 *
 *   ...
 *
 *   public <var>...</var> <var>someMethod(...)</var> <var>throws InternalExceptionA, InternalExceptionB, ...</var> {
 *     try {
 *       <var>normal algorithm</var>
 *     }
 *     catch (Throwable t) {
 *       try {
 *         getExceptionHandler().handleException(finalException, _LOG, <var>InternalExceptionA.class, InternalExceptionB.class, ...</var>);
 *       }
 *       <var>catch (InternalExceptionA a) {
 *         throw a;
 *       }
 *       catch (InternalExceptionB b) {
 *         throw b;
 *       }</var>
 *       catch (ApplicationException metaExc) {
 *         unexpectedException(metaExc, "handleException can throw no ApplicationExceptions");
 *       }
 *     }
 *   }
 * </pre>
 *
 * // TODO A way to log via an applicable logger automatically
 * // TODO Also warn administrators and developers via mail, in push fashion.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class ExceptionHandler {

  private final static Log _LOG = LogFactory.getLog(ExceptionHandler.class);

  /*<property name="triagers">*/
  //------------------------------------------------------------------

  /*
  @Basic(
    invars = {
      @Expression("triagers != null"),
      @Expression("triagers.get(0) instanceof PpwcodeTriager")
    },
    init = @Expression("triagers.size() == 1")
  )
  */
  public final List<ThrowableTriager> getTriagers() {
    @SuppressWarnings("unchecked")
    List<ThrowableTriager> clone = (List<ThrowableTriager>)$triagers.clone();
    return clone;
  }

  /*
  @MethodContract(pre  = @Expression("triagers != null"),
                  post = @Expression("triagers.addAll(_triagers)"))
  */
  public final void setTriagers(List<ThrowableTriager> triagers) {
    assert preArgumentNotNull(triagers, "triagers");
    $triagers.addAll(triagers);
  }

  /*
  @MethodContract(pre  = @Expression("triager != null"),
                  post = @Expression("triagers.add(_triager)"))
  */
  public final void addTriager(ThrowableTriager triager) {
    assert preArgumentNotNull(triager, "triager");
    $triagers.add(triager);
  }

  /*
  @Invars({
    @Expression("$triagers != null"),
    @Expression("$triagers.get(0) instanceof PpwcodeTriager")
  })
  */
  private ArrayList<ThrowableTriager> $triagers = new ArrayList<ThrowableTriager>();
  {
    $triagers.add(new PpwcodeTriager());
  }

  /*</property>*/



  /**
   * Ask the {@link #getTriagers() triagers} one after the other, in order, to {@link ThrowableTriager#triage(Throwable)}
   * {@code t}. If the resulting {@link Throwable} is an instance of {@link ApplicationException}, {@link ExternalError} or
   * {@link AssertionError}, return that {@link Throwable} and stop looking. If no {@link #getTriagers() triagers} can
   * return an instance of {@link ApplicationException}, {@link ExternalError} or {@link AssertionError}, return {@code t}
   * itself.
   */
  /*
  @MethodContract(
    pre  = @Expression("_t != null"),
    post = {
      @Expression("exists (int i : 0 .. triagers.size) {" +
                    "triagers.get(i).triage(_t) instanceof ApplicationException || " +
                    "triagers.get(i).triage(_t) instanceof ExternalError || " +
                    "triagers.get(i).triage(_t) instanceof AssertionError && " +
                    "for (int j : 0 .. i) {" +
                      "! triagers.get(j).triage(_t) instanceof ApplicationException && " +
                      "! triagers.get(j).triage(_t) instanceof ExternalError && " +
                      "! triagers.get(j).triage(_t) instanceof AssertionError " +
                    "} ?? " +
                  "result == triagers.get(i).triage(_t)"),
      @Expression("! exists (int i : 0 .. triagers.size) {" +
                      "triagers.get(i).triage(_t) instanceof ApplicationException || " +
                      "triagers.get(i).triage(_t) instanceof ExternalError || " +
                      "triagers.get(i).triage(_t) instanceof AssertionError" +
                    "} ?? result == _t")
    }
  )
  */
  public Throwable triage(Throwable t) {
    _LOG.debug("triaging " + t);
    assert $triagers.get(0) instanceof PpwcodeTriager : "first triager must be ppwcode exception vernacular triager";
    for (ThrowableTriager triager : $triagers) {
      Throwable triaged = triager.triage(t);
      if (triaged instanceof ApplicationException || triaged instanceof ExternalError || triaged instanceof AssertionError) {
        // triage succeeded
        _LOG.debug("triaging " + t + ": results in " + triaged);
        return triaged;
      }
      // else, try next
    }
    // if we are still here, triage did not succeed; return t itself
    _LOG.debug("triaging " + t + ": no match found; return the exception untriaged");
    return t;
  }

  /**
   * Handle {@link Throwable} {@code t}. {@link #triage(Throwable)} {@code _t}, and then, if triage results in
   * an acceptable application exception ({@code acceptableInternalExceptionType}), throw that. If triage results in
   * an {@link ExternalError}, warn the administrator, and throw that. If triage results in a {@link AssertionError
   * programming error}, warn the administrator and developers, and throw that. If triage results in an other
   * type of exception, treat it as a programming error.
   */
  /*
  @MethodContract(
    pre  = {
      @Expression("_t != null"),
      @Expression("log != null")
    },
    post = {
      @Expression("exist (int i : 0 .. _acceptableInternalExceptionType.length) {" +
                    "_acceptableInternalExceptionType[i].isInstance(triage(t))" +
                  "} ? handleApplicationException(triage(t), log)"),
      @Expression("triage(t) instanceof ExternalError ? handleExternalError(triage(t), log)"),
      @Expression("triage(t) instanceof AssertionError ? handleProgrammingError(triage(t), log)"),
      @Expression("! (exist (int i : 0 .. _acceptableInternalExceptionType.length) {" +
                        "_acceptableInternalExceptionType[i].isInstance(triage(t))" +
                      "} ||" +
                      "triage(t) instanceof ExternalError || " +
                      "triage(t) instanceof AssertionError) ? " +
                  "handleProgrammingError(new AssertionError(triage(t)), log)")
    }
  )
  */
  public void handleException(Throwable t, Log log, Class<? extends ApplicationException>... acceptableInternalExceptionType)
      throws ApplicationException, ExternalError, AssertionError {
    assert preArgumentNotNull(t, "t");
    assert preArgumentNotNull(log, "log");
    _LOG.debug("handling exception " + t);
    /* Case by case, we try to triage t, into an ApplicationException, ExternalError or AssertionError.
     * If triage fails, anything that is unclear is considered a programming error.
     * The finally resulting ApplicationException, ExternalError or AssertionError is then logged and thrown.
     * For internal exceptions, only types found in acceptableInternalExceptionTypes are actually acceptable.
     * If ApplicationExceptions occur that are not of an acceptable type, they are converted into programming errors too.
     */
    Throwable triaged = null;
    try {
      triaged = triage(t);
    }
    catch (Throwable metaT) {
      _LOG.error("triage produced an error", metaT);
      triaged = metaT; // an error trying to deal with a previous error
    }
    handleTriagedException(log, triaged, acceptableInternalExceptionType);
  }

  private void handleTriagedException(Log log, Throwable triaged, Class<? extends ApplicationException>... acceptableInternalExceptionType)
      throws ApplicationException, ExternalError, AssertionError {
    if (acceptableInternalExceptionType != null ) {
      for (int i = 0; i < acceptableInternalExceptionType.length; i++) {
        if (acceptableInternalExceptionType[i].isInstance(triaged)) {
          _LOG.trace("exception " + triaged + " recognized as of acceptable ApplicationException type " + acceptableInternalExceptionType[i]);
          handleApplicationException(acceptableInternalExceptionType[i].cast(triaged), log);
        }
      }
    }
    _LOG.trace("exception " + triaged + " is not of an acceptable ApplicationException type");
    // if we get here, and triaged is an ApplicationException, it will be handled as a programning error further down
    if (triaged instanceof ExternalError) {
      _LOG.trace("exception " + triaged + " recognized as an ExternalError");
      handleExternalError((ExternalError)triaged, log);
    }
    _LOG.trace("exception " + triaged + " is not an ExternalError");
    if (triaged instanceof AssertionError) {
      _LOG.trace("exception " + triaged + " recognized as a programming eror (AssertionError)");
      handleProgrammingError((AssertionError)triaged, log);
    }
    _LOG.trace("exception " + triaged + " is not a programming error (AssertionError");
    // else, finally
    AssertionError programmingErr = newAssertionError("exception was not recognized, so this a programming error in any case", triaged);
    handleProgrammingError(programmingErr, log);
  }

  /**
   * Throw {@code applicationExc}.
   */
  /*
  @MethodContract(
    pre  = @Expression("_internalExc != null"),
    post = @Expression("false"),
    exc  = @Throw(type = ApplicationException.class, cond = @Expression("thrown == _internalExc"))
  )
  */
  public void handleApplicationException(ApplicationException applicationExc, Log log) throws ApplicationException {
    // this is an application exception; its occurrence is non-nominal, but normal and expected; there is no need to warn anybody
    log.debug("internal exception; this is non-nominal, normal and expected behavior", applicationExc);
    // rethrow
    throw applicationExc;
  }

  /**
   * Throw {@code applicationExc}.
   * As a side effect, the error is logged as an error.
   *
   * // TODO mail the administrator
   */
  /*
  @MethodContract(
    pre  = @Expression("_externalErr != null"),
    post = @Expression("false"),
    exc  = @Throw(type = ExternalError.class, cond = @Expression("thrown == _externalErr"))
  )
  */
  public void handleExternalError(ExternalError externalErr, Log log) throws ExternalError {
    // log and warn the administrator
    log.error("A deployment issue occurred. This is an issue outside the scope of the developers, which requires " +
               "attention of the administrator", externalErr);
    // rethrow
    throw externalErr;
  }

  /**
   * Throw {@code programmingErr}.
   * As a side effect, the error is logged as an error.
   *
   * // TODO mail the administrator and developers
   */
  /*
  @MethodContract(
    pre  = @Expression("_programmingErr != null"),
    post = @Expression("false"),
    exc  = @Throw(type = AssertionError.class, cond = @Expression("thrown == _programmingErr"))
  )
  */
  public static void handleProgrammingError(AssertionError programmingErr, Log log) throws AssertionError {
    // log and warn the administrator and developers
    log.error("A programming error occurred. This is an issue where the administrator probably cannot help. " +
             "Please contact the developers and add as much information as possible in the communication," +
             "including this log file, and especially the stack trace following this message.", programmingErr);
    // rethrow
    throw programmingErr;
  }

}
