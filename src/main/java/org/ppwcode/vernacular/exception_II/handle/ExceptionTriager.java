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

package org.ppwcode.vernacular.exception_II.handle;


import org.ppwcode.vernacular.exception_II.ExceptionHelpers;
import org.ppwcode.vernacular.exception_II.ExternalError;
import org.ppwcode.vernacular.exception_II.InternalException;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>Triage {@link Throwable Throwables} into {@link InternalException InternalExceptions},
 *   {@link ExternalError ExternalErrors} or {@link AssertionError AssertionErrors}.</p>
 * <p>In the ppwcode exception vernacular, we intend as much as possible to communicate with
 *   either {@link InternalException InternalExceptions}, {@link ExternalError ExternalErrors}
 *   or {@link AssertionError AssertionErrors}, especially between layers. Implementations of
 *   this interface are responsible for trying to convert any {@link Throwable} into one
 *   of these specialized kinds.</p>
 * <p>Implementations of this interface will be used in a chain of responsibility, trying to
 *   triage exceptions in a concerted effort.</p>
 * <p>Specialized triagers can be found in libraries specialized in specific domains.</p>
 */
public interface ExceptionTriager {

  /**
   * <p>Try to convert {@code t} into an {@link InternalException}, {@link ExternalError} or
   *   {@link AssertionError}.</p>
   * <p>Implementations should base themselves on the occurrence of specific exception types for
   *   {@code t} or in the {@link Throwable#getCause() cause chain} of {@code t} (see
   *   {@link ExceptionHelpers#huntFor(Throwable, Class)}). If {@code t} cannot sensibly be
   *   triaged in the context of this specific {@code ExceptionTriager}, this method should
   *   return {@code t}. Non-triaged throwables will finally be treated as programming errors.</p>
   * <p>This method can only throw programming errors. When it cannot do its job for a configuration
   *   reason, it should return {@code t} and log its problem at warn level.</p>
   */
  @MethodContract(
    pre  = @Expression("_t != null"),
    post = @Expression("t != null")
  )
  Throwable triage(Throwable t);

}
