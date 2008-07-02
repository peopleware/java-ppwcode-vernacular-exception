/*<license>
Copyright 2005 - $Date: 2008-07-02 11:36:19 +0200 (Wed, 02 Jul 2008) $ by PeopleWare n.v..

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
 * <p>A {@link SemanticException} thrown while working with a
 *   JavaBean. The JavaBean is of type {@link #getBeanType()}.
 *   If the exception is not thrown by a constructor of the
 *   bean, {@link #getBean()} contains a reference to the
 *   bean we were working on. If the exception is thrown
 *   while constructing the bean, {@link #getBean()} is
 *   {@code null}.</p>
 *
 * @invar getBeanType() != null;
 * @invar getBean() != null ? getBeanType() == getBean().getClass();
 * @invar ! (getBean() instanceof Class);
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
@Copyright("2007 - $Date: 2008-07-02 11:36:19 +0200 (Wed, 02 Jul 2008) $, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 1504 $",
         date     = "$Date: 2008-07-02 11:36:19 +0200 (Wed, 02 Jul 2008) $")
public class BeanException extends InternalException {

  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @param     messageIdentifier
   *            The string that identifies a localized
   *            end user feedback message about the
   *            non-nominal behavior.
   * @param     cause
   *            The exception that occured, causing this
   *            exception to be thrown, if that is the case.
   * @pre       (messageIdentifier == null) ||
   *            EMPTY.equals(messageIdentifier) ||
   *            validMessageIdentifier(messageIdentifier);
   * @post      new.getMessage().equals((messageIdentifier == null) || (EMPTY.equals(messageIdentifier)) ?
   *                                       DEFAULT_MESSAGE_IDENTIFIER :
   *                                       messageIdentifier);
   * @post      new.getCause() == cause;
   */
  public BeanException(Object bean,
                       final String messageIdentifier,
                       final Throwable cause) {
    super(messageIdentifier, cause);
    assert bean != null;
    assert ! (bean instanceof Class);
    $beanType = bean.getClass();
    $bean = bean;
  }

  public BeanException(Class<?> beanType,
                       final String messageIdentifier,
                       final Throwable cause) {
    super(messageIdentifier, cause);
    assert beanType != null;
    $beanType = beanType;
  }

  /*</construction>*/



  /*<property name="bean">*/
  //------------------------------------------------------------------

  public final Object getBean() {
    return $bean;
  }

  private Object $bean;

  /*</property>*/



  /*<property name="beanType">*/
  //------------------------------------------------------------------

  public final Class<?> getBeanType() {
    return $beanType;
  }

  /**
   * @invar $beanType != null;
   * @invar $bean != null ? $beanType = $bean.getClass();
   */
  private Class<?> $beanType;

  /*</property>*/

}

