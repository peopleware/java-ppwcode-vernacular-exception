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


import static org.ppwcode.util.exception_III.ProgrammingErrorHelpers.preArgumentNotNull;

import java.rmi.RemoteException;

import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * <p>Exception for transporting {@link ApplicationException InternalExceptions} out of methods that
 *   only allow for {@link RuntimeException RuntimeExceptions} to be thrown.</p>
 * <p>In a number of modern frameworks (notably JPA, EJB3, Hibernate 3, Spring, etc...) there
 *   is a tendency towards using {@link RuntimeException RuntimeExceptions} instead of
 *   checked exceptions. An example are the interceptor and listener methods in JPA and EJB3,
 *   which are not allowed to throw any checked exception. The idea is not to bar implementors
 *   from throwing exceptions, but rather to have implementors throw {@link RuntimeException RuntimeExceptions}.
 *   In these frameworks, this is choosen over the pattern of providing 1 checked exception type
 *   that should be used to transport all user-defined exceptions out of the framework (e.g.,
 *   like {@link RemoteException}).</p>
 * <p>Since this conflicts with the ppwcode exception vernacular, this exception type is offered
 *   to resolve the problem.</p>
 * <p>Note that exception handling code should thus remember to filter not only on {@link ApplicationException},
 *   but also on the cause of {@link ApplicationExceptionTransportException InternalTransportRuntimeExceptions}.</p>
 *
 * @mudo This might have been a tad too soon. Maybe we are supposed to throw an EJBException as transport medium?
 */
public class ApplicationExceptionTransportException extends RuntimeException {

  @MethodContract(
    pre  = @Expression("_cause != null"),
    post = {
      @Expression("cause == _cause"),
      @Expression("message == null")
    }
  )
  public ApplicationExceptionTransportException(ApplicationException cause) {
    super(cause);
    assert preArgumentNotNull(cause, "cause");
  }

}

