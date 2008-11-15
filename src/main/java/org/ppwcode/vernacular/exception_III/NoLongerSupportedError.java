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
import org.ppwcode.vernacular.exception_III.ExternalError;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;


/**
 * Signals that the method throwing this exception no longer supports the promised operation.
 * This is a vehicle in application business's semantics evolution. When business semantics or
 * business logic changes, and a new version is released, we should try to make the old API
 * forward compatible as much as possible. Often, this will not be possible with all methods
 * in all cases. To make it possible to be forward compatible as much as possible, and not be
 * forced to abandon all forward compatibility for one small problem, methods in a business logic
 * API should, from first version, mention that they might, in the future, throw a
 * {@link NoLongerSupportedError}. This is an {@link ExternalError}: the audience in the
 * first place is the organizational architect, which is close to an administrator or a developer.
 * The exception message should express why the method is no longer supported. This exception can
 * be considered a sort of &quot;runtime, distributed deprecated notice&quot;.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class NoLongerSupportedError extends ExternalError {

  @MethodContract(post = {
    @Expression("message == _message"),
    @Expression("cause == null")
  })
  public NoLongerSupportedError(String message) {
    super(message);
  }

}

