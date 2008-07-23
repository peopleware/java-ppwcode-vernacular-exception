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


/**
 * <p>This error is thrown when a programming error is detected
 *   during execution. This happens when the coder notices branches
 *   that can never be reached (<code>else</code>-statements in deep
 *   decision trees, or unreachable <code>default</code>-clause
 *   in a <code>switch</code>-statement, or a <code>catch</code>-clause
 *   of an exception that can never occur, ...).</p>
 * <p>The {@link #getMessage() message} should describe the programming
 *   error as closely as possible. If you cannot pinpoint the exact
 *   nature of the programming error, you should say so explicitly.
 *   If you become aware of the programming error by catching an
 *   {@link Exception} or an {@link Error}, it should be carried by
 *   instances of this class as the {@link #getCause() cause}.</p>
 *
 * @invar     getMessage() != null;
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date: 2008-07-02 11:36:19 +0200 (Wed, 02 Jul 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 1504 $",
         date     = "$Date: 2008-07-02 11:36:19 +0200 (Wed, 02 Jul 2008) $")
public class ProgrammingError extends Error {

  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @param     message
   *            The message that describes the programming error.
   * @param     unexpected
   *            The unexpected exception that occured, causing this
   *            error to be thrown, if that is the case.
   *
   * @post      new.getMessage().equals(defaultMessage(message, unexpected));
   * @post      new.getCause() == unexpected;
   */
  public ProgrammingError(final String message,
                          final Throwable unexpected) {
    super(defaultMessage(message, unexpected), unexpected);
  }

  /**
   * @param     unexpected
   *            The unexpected exception that occured, causing this
   *            error to be thrown, if that is the case.
   *
   * @post      new.getMessage().equals(defaultMessage(null, unexpected));
   * @post      new.getCause() == unexpected;
   */
  public ProgrammingError(final Throwable unexpected) {
    this(null, unexpected);
  }

  /**
   * @param     message
   *            The message that describes the programming error.
   *
   * @post      new.getMessage().equals(defaultMessage(message, null));
   * @post      new.getCause() == null;
   */
  public ProgrammingError(final String message) {
    this(message, null);
  }

  public final static String COULD_NOT_CONTINUE_MESSAGE =
    "Could not continue due to an unspecified programming error.";

  public final static String UNEXPECTED_MESSAGE = "An unexpected exception occured.";

  /**
   * @return (message != null) ?
   *           message :
   *           ((unexpected != null) ?
   *                 UNEXPECTED_MESSAGE :
   *                 COULD_NOT_CONTINUE_MESSAGE);
   */
  public static String defaultMessage(String message, Throwable unexpected) {
    return (message != null) ?
             message :
             ((unexpected != null) ?
                UNEXPECTED_MESSAGE :
                COULD_NOT_CONTINUE_MESSAGE);
  }

  /*</construction>*/

}
