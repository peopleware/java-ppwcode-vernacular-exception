/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.servlet_I.hibernate;

import java.io.Serializable;


/**
 * <p>A wrapper class for application, session, request or page scope
 *   attributes that allows for lazy initilization. This is similar
 *   to {@link java.lang.ThreadLocal}, with the difference that
 *   we do not need to keep a map. That is done already for us
 *   by the scopes.</p>
 * <p>Users get the value of the attribute by calling {@link #getValue()}.
 *   The first time {@link #getValue()} is called, the value is initialized
 *   through a call to {@link #initialValue()}. There is no setter.
 *   <code>null</code> cannot be stored as a value.
 *   A good usage pattern is to implement a subclass as an anonymous
 *   inner class.</p>
 * <p>Instances are {@link Serializable}, but the cached value
 *   is <strong>transient</strong>.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 *
 * @invar     getValue() != null;
 */
public abstract class LazyAttribute implements Serializable {

  /*<section name="Meta Information">*/
  //------------------------------------------------------------------

  /** {@value} */
  public static final String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_TAG = "$Name$"; //$NON-NLS-1$

  /*</section>*/



  /*<construction>*/
  //------------------------------------------------------------------

  // default constructor

  /*</construction>*/


  /**
   * @basic
   *
   * @post      new.isInitialized();
   */
  public Object getValue() {
    if (!isInitialized()) {
      $value = initialValue();
    }
    return $value;
  }

  private transient Object $value;

  /**
   * @result    result != null;
   * @post      new.isInitialized();
   */
  protected abstract Object initialValue();

  /**
   * @basic
   *
   * @init      false;
   */
  public boolean isInitialized() {
    return $value != null;
  }

}
