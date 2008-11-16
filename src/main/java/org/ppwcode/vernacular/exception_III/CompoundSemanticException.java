/*<license>
Copyright 2004 - $Date: 2008-10-05 17:13:42 +0200 (Sun, 05 Oct 2008) $ by PeopleWare n.v.

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

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.Invars;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * <p>Compound semantic exceptions make it possible for a method to signal several semantic issues
 *   at once.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date: 2008-10-05 17:13:42 +0200 (Sun, 05 Oct 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 2930 $",
         date     = "$Date: 2008-10-05 17:13:42 +0200 (Sun, 05 Oct 2008) $")
public final class CompoundSemanticException extends SemanticException implements CompoundException<SemanticException> {

  /**
   * The empty String.
   */
  public final static String EMPTY = "";



  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @param     message
   *            The message that describes the exceptional circumstance.
   * @param     cause
   *            The exception that occurred, causing this exception to be
   *            thrown, if that is the case.
   */
  @MethodContract(
    pre  = {
      @Expression("_message == null || ! _message.equals(EMPTY)")
    },
    post = {
      @Expression("message == _message == null ? DEFAULT_MESSAGE_KEY : _message"),
      @Expression("cause == _cause")
    }
  )
  public CompoundSemanticException(final String message, final Throwable cause) {
    super(message, cause);
  }

  /*</construction;>*/



  /*<property name="closed">*/
  //------------------------------------------------------------------

  public boolean isClosed() {
    return $closed;
  }

  public void close() throws IllegalStateException {
    if ($closed) {
      throw new IllegalStateException("can't close twice");
    }
    $closed = true;
    makeImmutable();
  }

  private void makeImmutable() {
    $elementExceptions = Collections.unmodifiableSet($elementExceptions);
  }

  private boolean $closed;

  /*</property>*/



  /*<property name="element exceptions">*/
  //------------------------------------------------------------------

  public Set<SemanticException> getElementExceptions() {
    if (! $closed) {
      return new HashSet<SemanticException>($elementExceptions);
    }
    else {
      return $elementExceptions;
    }
  }

  public SemanticException getAnElement() {
    if (isEmpty()) {
      return null;
    }
    else {
      Iterator<SemanticException> iter = $elementExceptions.iterator();
      return iter.next();
    }
  }

  public final boolean contains(SemanticException ee) {
    if (ee == null) {
      return false;
    }
    for (SemanticException candidate : $elementExceptions) {
      if (candidate.like(ee)) {
        return true;
      }
    }
    return false;
  }

  @MethodContract(post = @Expression("elementExceptions.empty"))
  public final boolean isEmpty() {
    return $elementExceptions.isEmpty();
  }

  public int getSize() {
    return $elementExceptions.size();
  }

  public void addElementException(SemanticException eExc) throws IllegalStateException, IllegalArgumentException {
    if (isClosed()) {
      throw new IllegalStateException("cannot add exceptions to compound when closed");
    }
    if (eExc instanceof CompoundException) {
      throw new IllegalArgumentException("Cannot add a compound exception. This should be a flat list.");
    }
    if (eExc == null) {
      throw new IllegalArgumentException("Cannot add null exception.");
    }
    $elementExceptions.add(eExc);
  }

  @Invars({
    @Expression("$elementExceptions != null"),
    @Expression("! $elementExceptions.contains(null)")
  })
  private Set<SemanticException> $elementExceptions = new HashSet<SemanticException>();

  /*</property>*/



  /*<section name="comparison">*/
  //------------------------------------------------------------------

  @Override
  @MethodContract(
    post = @Expression("result ? " +
                         "for (SemanticException otherEe : _other.elementExceptions) {contains(otherEe)} && " +
                         "for (SemanticException ee : elementExceptions) {_other.contains(ee)}")
  )
  public boolean like(ApplicationException other) {
    if (! super.like(other)) {
      return false;
    }
    CompoundSemanticException cse = (CompoundSemanticException)other;
    Set<SemanticException> otherElementException = cse.getElementExceptions();
    if ($elementExceptions.size() != otherElementException.size()) {
      return false;
    }
    for (SemanticException otherEe : $elementExceptions) {
      if (! cse.contains(otherEe)) {
        return false;
      }
    }
    return true;
  }

  /*</section>*/



  /**
   * This method throws this exception if it is not empty.
   * If this is not empty, this is closed. If the number of element
   * exceptions is larger than 1, this is thrown. If there is exactly
   * 1 element exception, that is thrown instead.
   */
  @MethodContract(
    post = {
      @Expression(
        value = "'empty",
        description = "since we cannot change the old value of empty, we are " +
                      "forced to throw an exception if we are not empty"
      ),
      @Expression("closed")
    },
    exc = {
      @Throw(
        type = CompoundSemanticException.class,
        cond = {
          @Expression("size > 1"),
          @Expression("thrown == this"),
          @Expression("closed")
        }
      ),
      @Throw(
         type = SemanticException.class,
         cond = {
           @Expression("size == 1"),
           @Expression("thrown == anElement"),
           @Expression("closed")
         }
       )
    }
  )
  public final void throwIfNotEmpty() throws SemanticException {
    if (! isClosed()) {
      close();
    }
    if (!isEmpty()) {
      if (getSize() > 1) {
        throw this;
      }
      else {
        throw getAnElement();
      }
    }
  }

}
