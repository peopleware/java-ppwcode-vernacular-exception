/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.bean_V;


import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;


/**
 * <p>Compound property exceptions make it possible for an implementation
 *   of a setter, a constructor, or a validation method of a bean to report
 *   all that is wrong with arguments or the bean at once.</p>
 * <p>With single exceptions, a method can report 1 exceptional
 *   condition. Often, there are more reasons possible to reject an actual
 *   argument. A traditional implementation tests the conditions one by one,
 *   and throws an exception when the first failure of
 *   occurs. This can than be reported to the end user, but once this mistake
 *   is corrected, it often results in the next rejection. This is especially
 *   annoying in web applications, that have a long round trip.</p>
 * <p>Compound exceptions make it possible to implement validation in such
 *   a way that all validation is done every time. A validation failure
 *   is stored and remembered until all validation is done, and then
 *   collected in a compound exception, which makes it possible to provide
 *   full feedback on validity to the end user in one pass.</p>
 *
 * @invar     getElementExceptions() != null;
 * @invar     (foreach Object o; getElementExceptions().containsKey(o);
 *                o instanceof String);
 * @invar     (foreach Object o; getElementExceptions().containsValue(o);
 *                o instanceof java.util.Set);
 * @invar     !getElementExceptions().contains("");
 * @invar     !getElementExceptions().containsValue(null);
 * @invar     (foreach java.util.Set s; getElementExceptions().containsValue(s);
 *                !s.isEmpty());
 * @invar     (foreach java.util.Set s; getElementExceptions().containsValue(s);
 *                (foreach Object o; s.contains(o);
 *                    o instanceof PropertyException));
 * @invar     (foreach java.util.Set s; getElementExceptions().containsValue(s);
 *                !s.contains(null));
 * @invar     (foreach String key; getElementExceptions().containsKey(key);
 *                (foreach PropertyException e;
 *                    getElementsException().get(key).contains(e);
 *                        e.getPropertyName().equals(key)));
 * @invar     (getElementsExceptions().size() > 1) ==>
 *                getPropertyName() == null;
 * @invar     (getElementsExceptions().size() == 1) ==>
 *                (getElementsExceptions().get(getPropertyName()) != null);
 * @invar     (foreach String key; getElementExceptions().containsKey(key);
 *                (foreach PropertyException e;
 *                    getElementsException().get(key).contains(e);
 *                        e.getOrigin() == getOrigin()));
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public final class CompoundPropertyException extends PropertyException {

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



  /*<construction;>*/
  //------------------------------------------------------------------

  /**
   * @param     origin
   *            The bean that has thrown this exception.
   * @param     propertyName
   *            The name of the property of which the setter has thrown
   *            this exception because parameter validation failed.
   * @param     message
   *            The message that describes the exceptional circumstance.
   * @param     cause
   *            The exception that occured, causing this exception to be
   *            thrown, if that is the case.
   *
   * @pre       origin != null;
   * @pre       (propertyName != null) ==> ! propertyName.equals("");
   * @pre       (message != null) ==> ! message.equals("");
   * @post      new.getOrigin() == origin;
   * @post      (propertyName != null) ==>
   *                new.getpropertyName().equals(propertyName);
   * @post      (propertyName == null) ==> new.getpropertyName() == null;
   * @post      (message != null) ==> new.getMessage().equals(message);
   * @post      (message == null) ==> new.getMessage() == null;
   * @post      new.getCause() == cause ;
   * @post      new.getElementExceptions().isEmpty();
   * @post      !new.isClosed();
   */
  public CompoundPropertyException(final Object origin,
                                   final String propertyName,
                                   final String message,
                                   final Throwable cause) {
    super(origin, propertyName, message, cause);
  }

  /**
   * @param     origin
   *            The bean that has thrown this exception.
   * @param     inOriginInitialization
   *            Set to <code>true</code> if this active
   *            property is created during the origin initialization;
   *            if so, an exception will not carry a reference
   *            to the bean, but only to the bean type.
   * @param     propertyName
   *            The name of the property of which the setter has thrown
   *            this exception because parameter validation failed.
   * @param     message
   *            The message that describes the exceptional circumstance.
   * @param     cause
   *            The exception that occured, causing this exception to be
   *            thrown, if that is the case.
   *
   * @pre       origin != null;
   * @pre       (propertyName != null) ==> ! propertyName.equals("");
   * @pre       (message != null) ==> ! message.equals("");
   * @post      new.getOrigin() == origin;
   * @post      (propertyName != null) ==>
   *                new.getpropertyName().equals(propertyName);
   * @post      (propertyName == null) ==> new.getpropertyName() == null;
   * @post      (message != null) ==> new.getMessage().equals(message);
   * @post      (message == null) ==> new.getMessage() == null;
   * @post      new.getCause() == cause ;
   * @post      new.getElementExceptions().isEmpty();
   * @post      !new.isClosed();
   * @since IV 1.0.1/1.0
   */
  public CompoundPropertyException(final Object origin,
                                   final boolean inOriginInitialization,
                                   final String propertyName,
                                   final String message,
                                   final Throwable cause) {
    super(origin, inOriginInitialization, propertyName, message, cause);
  }

  /**
   * @param     originType
   *            The type of bean that has thrown this exception during initialization.
   * @param     propertyName
   *            The name of the property of which the setter has thrown
   *            this exception because parameter validation failed.
   * @param     message
   *            The message that describes the exceptional circumstance.
   * @param     cause
   *            The exception that occured, causing this exception to be
   *            thrown, if that is the case.
   *
   * @pre       origin != null;
   * @pre       (propertyName != null) ==> ! propertyName.equals("");
   * @pre       (message != null) ==> ! message.equals("");
   * @post      new.getOriginType() == originType;
   * @post      new.getOrigin() == null;
   * @post      (propertyName != null) ==>
   *                new.getpropertyName().equals(propertyName);
   * @post      (propertyName == null) ==> new.getpropertyName() == null;
   * @post      (message != null) ==> new.getMessage().equals(message);
   * @post      (message == null) ==> new.getMessage() == null;
   * @post      new.getCause() == cause ;
   * @post      new.getElementExceptions().isEmpty();
   * @post      !new.isClosed();
   * @since IV
   */
  public CompoundPropertyException(final Class originType,
                                   final String propertyName,
                                   final String message,
                                   final Throwable cause) {
    super(originType, propertyName, message, cause);
  }

  /*</construction;>*/



  /*<property name="closed">*/
  //------------------------------------------------------------------

  /**
   * To make the exception immutable, it needs to be closed before it is
   * thrown. Element exceptions can only be added to the compound when
   * it is not yet closed.
   *
   * @basic
   */
  public boolean isClosed() {
    return $closed;
  }

  /**
   * @post      new.isClosed();
   * @throws IllegalStateException
   *         isClosed();
   */
  public void close() throws IllegalStateException {
    if ($closed) {
      throw new IllegalStateException("can't close twice");
    }
    $closed = true;
    makeImmutable();
  }

  private boolean $closed;

  /*</property>*/



  /*<property name="element exceptions">*/
  //------------------------------------------------------------------

  /**
   * There are no element exceptions.
   *
   * @return getElementExceptions().isEmpty();
   */
  public boolean isEmpty() {
    return $elementExceptions.isEmpty();
  }

  /**
   * A map of sets of exceptions. The key in the map is the name
   * of the property for which the exceptions in the set stored as
   * value for that key occured. Since the exception is itself a property
   * exception, the property name must match. <code>null</code> is used
   * as the key for general exceptions that can not be attributed
   * to a single exception. If there are multiple entries in the map,
   * the property name of this exception must be <code>null</code>.
   *
   * @basic
   */
  public Map getElementExceptions() {
    Map result = null;
    if (isClosed()) {
      result = $elementExceptions; // is made unmodifiable by now
    }
    else {
      // deep copy
      result = deepImmutableCopy();
    }
    return result;
  }

  /**
   * @return    getElementExceptions().get(null);
   */
  public Set getGeneralElementExceptions() {
    return (Set)getElementExceptions().get(null);
  }

  /**
   * @param     pExc
   *            The exception to add als element to the compound.
   * @post      new.getElementExceptions().get(pExc.getPropertyName())
   *                .contains(pExc);
   * @post      !isClosed();
   * @throws    IllegalStateException
   *            isClosed();
   * @throws    IllegalArgumentException
   *            pExc == null;
   * @throws    IllegalArgumentException
   *            (getPropertyName() != null)
   *            && (! getPropertyName().equals(pExc.getPropertyName()));
   * @throws    IllegalArgumentException
   *            Same origin or origin type!
   * @mudo (jand) implement and document this last exception!
   */
  public void addElementException(final PropertyException pExc)
      throws IllegalStateException, IllegalArgumentException {
    if (isClosed()) {
      throw new IllegalStateException("cannot add exceptions to " //$NON-NLS-1$
                                      + "compound when closed"); //$NON-NLS-1$
    }
    if (pExc == null) {
      throw new IllegalArgumentException("Cannot add null exception."); //$NON-NLS-1$
    }
    if ((getPropertyName() != null)
        && (!getPropertyName().equals(pExc.getPropertyName()))) {
      throw new IllegalArgumentException("only properties for property " //$NON-NLS-1$
                                         + getPropertyName()
                                         + " are allowed"); //$NON-NLS-1$
    }
    Set propertySet = (Set)$elementExceptions.get(pExc.getPropertyName());
    if (propertySet == null) {
      propertySet = new java.util.HashSet();
      $elementExceptions.put(pExc.getPropertyName(), propertySet);
    }
    propertySet.add(pExc);
  }

  /**
   * @pre       $elementExceptions instanceof java.util.HashMap;
   */
  private Map deepImmutableCopy() {
    Map result = (Map)((HashMap)$elementExceptions).clone();
    Iterator iter = result.keySet().iterator();
    while (iter.hasNext()) {
      Object key = iter.next();
      result.put(key, Collections.unmodifiableSet(
                          (Set)((HashSet)result.get(key)).clone()));
    }
    return Collections.unmodifiableMap(result);
  }

  private void makeImmutable() {
    Iterator iter = $elementExceptions.keySet().iterator();
    while (iter.hasNext()) {
      Object key = iter.next();
      $elementExceptions.put(key, Collections.unmodifiableSet(
                                      (Set)$elementExceptions.get(key)));
    }
    $elementExceptions = Collections.unmodifiableMap($elementExceptions);
  }

  /**
   * @invar     $elementExceptions != null;
   * @invar     (foreach Object o; $elementExceptions.containsKey(o);
   *                o instanceof String);
   * @invar     (foreach Object o; $elementExceptions.containsValue(o);
   *                o instanceof java.util.Set);
   * @invar     !$elementExceptions.contains("");
   * @invar     !$elementExceptions.containsValue(null);
   * @invar     (foreach java.util.Set s; $elementExceptions.containsValue(s);
   *                !s.isEmpty());
   * @invar     (foreach java.util.Set s; $elementExceptions.containsValue(s);
   *                (foreach Object o; s.contains(o);
   *                    o instanceof PropertyException));
   * @invar     (foreach java.util.Set s; $elementExceptions.containsValue(s);
   *                !s.contains(null));
   * @invar     (foreach String key; $elementExceptions.containsKey(key);
   *                (foreach PropertyException e;
   *                    $elementExceptions.get(key).contains(e);
   *                        e.getPropertyName() == key));
   * @invar     ($elementExceptions.size() > 1) ==> getPropertyName() == null;
   * @invar     ($elementExceptions.size() == 1) ==>
   *                ($elementExceptions.get(getPropertyName()) != null);
   */
  private Map $elementExceptions = new HashMap();

  /*</property>*/



  /**
   * @return    (other != null)
   *                && (getOrigin() == other.getOrigin())
   *                && ((getPropertyName() == null)
   *                    ? other.getPropertyName() == null
   *                    : getPropertyName().equals(other.getPropertyName()))
   *                && ((getCause() == null)
   *                    ? other.getCause() == null
   *                    : getCause().equals(other.getCause()))
   *                && ((getMessage() == null)
   *                    ? other.getMessage() == null
   *                    : getMessage().equals(other.getMessage()));
   */
  public boolean hasSameValues(final PropertyException other) {
    return super.hasSameValues(other)
            && (other instanceof CompoundPropertyException)
            && sourceContainsAllInTarget(this,
                                         (CompoundPropertyException)other)
            && sourceContainsAllInTarget((CompoundPropertyException)other,
                                         this);
  }

  private boolean sourceContainsAllInTarget(
      final CompoundPropertyException source,
      final CompoundPropertyException target) {
    Iterator sets = source.$elementExceptions.entrySet().iterator();
    while (sets.hasNext()) {
      Map.Entry entry = (Entry)sets.next();
      Set otherSet = (Set)target.getElementExceptions().get(entry.getKey());
      if (otherSet == null) {
        return false;
      }
      Iterator exceptions = ((Set)entry.getValue()).iterator();
      while (exceptions.hasNext()) {
        PropertyException pe = (PropertyException)exceptions.next();
        boolean found = false;
        Iterator otherExceptions = otherSet.iterator();
        while (otherExceptions.hasNext() && !found) {
          PropertyException otherPe = (PropertyException)otherExceptions.next();
          if (pe.hasSameValues(otherPe)) {
            found = true;
          }
        }
        if (!found) {
          return false;
        }
      }
      // if we get here, all exceptions in this entry are matched
    }
    // if we get here, all exceptions in all entries are matched
    return true;
  }

  /**
   * Checks whether there is a property exception with these parameters
   * in this compounds element exceptions.
   *
   * @result    getElementExceptions().get(propertyName) != null;
   * @result    !getElementExceptions().get(propertyName).isEmpty();
   * @result    (exists PropertyException pe;
   *                getElementExceptions().get(propertyName).contains(pe);
   *                (pe.hasProperties(origin, propertyName, message, cause)));
   */
  public boolean contains(final Object origin,
                          final String propertyName,
                          final String message,
                          final Throwable cause) {
    boolean result = false;
    Set propertySet = (Set)$elementExceptions.get(propertyName);
    if (propertySet != null) {
      assert (propertySet != null) && (!propertySet.isEmpty());
      Iterator pexcs = propertySet.iterator();
      while (pexcs.hasNext()) {
        PropertyException candidate = (PropertyException)pexcs.next();
        if (candidate.hasProperties(origin, propertyName, message, cause)) {
          result = true;
        }
      }
    }
    return result;
  }


  /**
   * Checks whether there is a property exception with these parameters
   * in this compounds element exceptions.
   *
   * @result    getElementExceptions().get(propertyName) != null;
   * @result    !getElementExceptions().get(propertyName).isEmpty();
   * @result    (exists PropertyException pe;
   *                getElementExceptions().get(propertyName).contains(pe);
   *                (pe.hasProperties(originType, propertyName, message, cause)));
   * @since IV
   */
  public boolean contains(final Class originType,
                          final String propertyName,
                          final String message,
                          final Throwable cause) {
    boolean result = false;
    Set propertySet = (Set)$elementExceptions.get(propertyName);
    if (propertySet != null) {
      assert (propertySet != null) && (!propertySet.isEmpty());
      Iterator pexcs = propertySet.iterator();
      while (pexcs.hasNext()) {
        PropertyException candidate = (PropertyException)pexcs.next();
        if (candidate.hasProperties(originType, propertyName, message, cause)) {
          result = true;
        }
      }
    }
    return result;
  }

  /**
   * This method throws this exception if it is not empty.
   * If this is not empty, this is closed. If the number of element
   * exceptions is larger than 1, this is thrown. If there is exactly
   * 1 element exception, that is thrown instead.
   *
   * @mudo contract
   */
  public void throwIfNotEmpty() throws PropertyException {
    if (!isEmpty()) {
      close();
      if (getSize() > 1) {
        throw this;
      }
      else {
        throw getAnElement();
      }
    }
  }

  /**
   * @return sum(foreach Set s; getElementExceptions().values().contains(s);
   *              s.size());
   */
  public int getSize() {
    int acc = 0;
    Iterator iter = getElementExceptions().values().iterator();
    while (iter.hasNext()) {
      Set s = (Set)iter.next();
      acc += s.size();
    }
    return acc;
  }

  /**
   * Returns an element exception of this instance. Especially
   * intresting if <code>getSize() == 1</code>, of course.
   * Returns <code>null</code> if <code>getSize() == 0</code>.
   */
  public PropertyException getAnElement() {
    if (isEmpty()) {
      return null;
    }
    else {
      Iterator iter = getElementExceptions().values().iterator();
      Set s = (Set)iter.next();
      iter = s.iterator();
      return (PropertyException)iter.next();
    }
  }

  /**
   * @result contains(final Object origin,
   *                  final String propertyName,
   *                  final String message,
   *                  final Throwable cause);
   */
  public boolean reportsOn(final Object origin,
                           final String propertyName,
                           final String message,
                           final Throwable cause) {
    return contains(origin, propertyName, message, cause);
  }

  /**
   * @result contains(final Object origin,
   *                  final String propertyName,
   *                  final String message,
   *                  final Throwable cause);
   * @since IV
   */
  public boolean reportsOn(final Class originType,
                           final String propertyName,
                           final String message,
                           final Throwable cause) {
    return contains(originType, propertyName, message, cause);
  }

}
