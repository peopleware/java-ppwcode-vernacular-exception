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

import java.text.MessageFormat;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;


/**
 * <p>This error is thrown when a {@link Throwable} is detected
 *   during execution, for which is reasoned that it should not
 *   occur. If the {@link Throwable} does occur, it means the
 *   reasoning was false, and we thus have a {@link ProgrammingError
 *   programming error}.</p>
 * <p>When methods that can throw checked exceptions are encountered,
 *   the programmer is forced to reason about the exceptions that can
 *   possible be thrown. One of 3 things can be the case:</p>
 * <ul>
 *   <li>the programmer decides the exception should propagate; he adds
 *     the exception type to the signature of the method we are
 *     developing</li>
 *   <li>the programmer decides that the exception should be dealt
 *     with in some form; he surrounds the method call that can throw
 *     the exception with {@code try ... catch ...}, to catch the
 *     potential exception, and deal with it</li>
 *   <li>the programmer reasons that, althoug the exception can be
 *     thrown by the method called in general, it can never occur,
 *     if the called method is implemented correctly, in the current
 *     circumstances.</li>
 * </ul>
 * <p>In the latter case, the programmer wants to write a {@code catch}
 *   block, to avoid having to mentiond the exception type in the
 *   signature of the method under development, knowing very well
 *   that the code in the {@code catch} block can never be reached,
 *   if the called method is implemented correctly.</p>
 * <p>It is then good practice to take down the reasoning that leads
 *   to the certainty that the exception can never occur, and to
 *   see to it that if it does nevertheless, the programmer is warned.
 *   This error is created especially for that case. It should also
 *   be used to signal impossible exceptions for non-checked exceptions
 *   and {@link Error errors}, if they are mentioned explicitly
 *   in the signature or the documentation of the called method.</p>
 * <p>The {@link #getMessage() message} should describe the reasoning as to
 *   why the exception cannot occor as closely as possible. The offending
 *   {@link Throwable} should be carried by instances of this class as the
 *   {@link #getCause() cause}.</p>
 * <p>Thus, the idiom to use this error is:</p>
 * <pre>
 *   try {
 *     ...
 *   }
 *   catch (ExceptionThatCannotOccurException etcoE) {
 *     throw new ExceptionShouldNotHappenError(&quot;<var>why the exception cannot occur</var>&quot;, etcoE);
 *   }
 * </pre>
 *
 * @invar     getMessage() != null;
 * @invar     getCause() != null;
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date: 2008-07-02 11:36:19 +0200 (Wed, 02 Jul 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 1504 $",
         date     = "$Date: 2008-07-02 11:36:19 +0200 (Wed, 02 Jul 2008) $")
public class ExceptionShouldNotHappenError extends ProgrammingError {

  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @param     message
   *            The message that describes why {@code unexpected}
   *            cannot occur.
   * @param     unexpected
   *            The unexpected exception that occured, causing this
   *            error to be thrown.
   * @pre message != null;
   * @pre unexpected != null;
   * @post      new.getMessage().equals(prependMessage(message, unexpected));
   * @post      new.getCause() == unexpected;
   */
  public ExceptionShouldNotHappenError(final String message,
                                       final Throwable unexpected) {
    super(prependMessage(message, unexpected), unexpected);
    assert message != null;
    assert unexpected != null : "unexpected throwable cannot be null";
  }

  /**
   * {@value}
   */
  public static final String MESSAGE_PATTERN =
    "An exception of type {0} occured, " +
    "in a situation where this could not happen because {1}";

  /**
   * @pre unexpected != null;
   * @return MessageFormat.format(MESSAGE_PATTERN,
   *                    unexpected.getClass().getName(), message);
   */
  public static String prependMessage(String message, Throwable unexpected) {
    assert unexpected != null;
    return MessageFormat.format(MESSAGE_PATTERN, unexpected.getClass().getName(), message);
  }

  /*</construction>*/

}
