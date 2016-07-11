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

package org.ppwcode.vernacular.exception.IV.handle;


import static org.ppwcode.util.exception_III.ExceptionHelpers.huntFor;

import org.ppwcode.vernacular.exception.IV.ApplicationException;
import org.ppwcode.vernacular.exception.IV.ExternalError;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>A {@Link ThrowableTriager} for the ppwcode exception vernacular. This triages
 *   occurences of existing {@link ApplicationException InternalExceptions}, {@link ExternalError ExternalErrors}
 *   and {@link AssertionError AssertionErrors} into themselves. This is more than a no-op,
 *   since those exceptions might be buried inside the cause chain of the offered throwable, instead
 *   of being the exception itself. With this triager, triage work done higher in the stack is recovered.
 *   Outer transport exceptions are peeeled of the meaningful exceptions.</p>
 */
public class PpwcodeTriager implements ExceptionTriager {

  @MethodContract(
    post = {
      @Expression("huntFor(t, ApplicationException.class) != null ? result = huntFor(t, ApplicationException.class)"),
      @Expression("huntFor(t, ExternalError.class) != null ? result == huntFor(t, ExternalError.class)"),
      @Expression("huntFor(t, AssertionError.class) != null ? result == huntFor(t, AssertionError.class)"),
      @Expression("huntFor(t, ApplicationException.class) == null &&  huntFor(t, ExternalError.class) == null && " +
                  "huntFor(t, AssertionError.class) == null ? result == t")
    }
  )
  public Throwable triage(Throwable t) {
    ApplicationException internalExc = huntFor(t, ApplicationException.class);
    if (internalExc != null) {
      return internalExc;
    }
    ExternalError externalErr = huntFor(t, ExternalError.class);
    if (externalErr != null) {
      return externalErr;
    }
    AssertionError aErr = huntFor(t, AssertionError.class);
    if (aErr != null) {
      return aErr;
    }
    return t;
  }

}
