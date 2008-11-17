/*<license>
Copyright 2004 - $Date$ by PeopleWare n.v.

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

import java.util.Set;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.toryt.annotations_I.Basic;
import org.toryt.annotations_I.Expression;
import org.toryt.annotations_I.MethodContract;
import org.toryt.annotations_I.Throw;


/**
 * <p>Compound exceptions make it possible for a method to signal several semantic issues
 *   at once.</p>
 * <p>With single exceptions, a method can report 1 exceptional condition. Often, there are more reasons
 *   possible to reject nominal execution. A traditional implementation tests the conditions one by one,
 *   and throws an exception when the first failure of occurs. This can than be reported to the end user,
 *   but once this mistake is corrected, it often results in the next rejection. This is especially annoying
 *   in web applications, that have a long round trip.</p>
 * <p>Compound exceptions make it possible to implement validation in such a way that all validation is done
 *   every time. A validation failure is stored and remembered until all validation is done, and then collected
 *   in a compound exception, which makes it possible to provide full feedback on validity to the end user in
 *   one pass.</p>
 * <p>Compound exceptions are build during a certain time, and migth eventually be thrown or not be
 *   thrown. An {@link #isEmpty() empty} compound exception should never be thrown. Before a compound
 *   exception is thrown, it should be {@link #isClosed closed}.</p>
 * <p>Compound exceptions are meant as a flat list. You should not nest compound property exceptions.
 *   This also avoids cyclic element exceptions.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2004 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public interface CompoundException<_ElementException_ extends ApplicationException> {

  /*<property name="closed">*/
  //------------------------------------------------------------------

  /**
   * To make the exception immutable, it needs to be closed before it is
   * thrown. Element exceptions can only be added to the compound when
   * it is not yet closed.
   */
  boolean isClosed();

  @MethodContract(
    post = @Expression("closed"),
    exc = @Throw(
      type = IllegalStateException.class,
      cond = @Expression("'closed")
    )
  )
  void close() throws IllegalStateException;

  /*</property>*/



  /*<property name="element exceptions">*/
  //------------------------------------------------------------------

  @Basic(invars = @Expression("for (_ElementException_ eExc : elementExceptions) {! eExc instanceof CompoundException)"))
  public Set<? extends _ElementException_> getElementExceptions();

  /**
   * Returns an element exception of this instance. Especially
   * intresting if <code>size == 1</code>, of course.
   * Returns <code>null</code> if <code>size == 0</code>.
   */
  @MethodContract(post = @Expression("result != null ? contains(result"))
  public _ElementException_ getAnElement();

  /**
   * Checks whether an application exception that expresses the same
   * as condition as {@code ae} is in the compound with reference semantics.
   */
  @MethodContract(
    post = @Expression("result ? _ea != null && exists(_ElementException_ ee : elementExceptions]) {ee.like(_ee)}")
  )
  boolean contains(_ElementException_ ee);

  /**
   * There are no element exceptions.
   */
  @MethodContract(post = @Expression("elementExceptions.empty"))
  boolean isEmpty();

  /**
   * The total number of exceptions in this compound.
   */
  @MethodContract(post = @Expression("elementExceptions.size)"))
  int getSize();

  /**
   * @param     eExc
   *            The exception to add as element to the compound.
   */
  @MethodContract(
    post = {
      @Expression("elementExceptions.contains(_eExc)"),
      @Expression(value = "! 'closed",
                  description = "since we cannot make true something in the old state, an exception " +
                                "has to be thrown when the exception is closed in the old state")
    },
    exc = {
      @Throw(type = IllegalStateException.class, cond = @Expression("'closed")),
      @Throw(type = IllegalArgumentException.class, cond = @Expression("_eExc instanceof CompoundException")),
      @Throw(type = IllegalArgumentException.class, cond = @Expression("_eExc == null")),
      @Throw(type = IllegalArgumentException.class, cond = @Expression("true"))
    }
  )
  public void addElementException(_ElementException_ eExc) throws IllegalStateException, IllegalArgumentException;

  /*</property>*/



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
        type = CompoundException.class,
        cond = {
          @Expression("size > 1"),
          @Expression("thrown == this"),
          @Expression("closed")
        }
      ),
      @Throw(
         type = ApplicationException.class,
         cond = {
           @Expression("size == 1"),
           @Expression("thrown == anElement"),
           @Expression("closed")
         }
       )
    }
  )
  void throwIfNotEmpty() throws _ElementException_;

}
