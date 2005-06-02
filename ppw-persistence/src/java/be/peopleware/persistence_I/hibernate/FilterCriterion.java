/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.persistence_I.hibernate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A class of filter criteria.
 *
 * @author nsmeets
 *
 * @invar  getPropertyName() != null;
 * @invar  getValues() != null;
 * @invar  getOperators().contains(getOperator());
 * @mudo (nsmeets) Afhankelijk van de operator zijn er beperkingen op de lijst van values
 *                 -> beter subklassen maken?
 */
public class FilterCriterion {

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

  public static final String EQ = "eq";
  public static final String LIKE = "like";

  /* <construction> */
  //------------------------------------------------------------------

  /**
   * Create a new criterion with the given property name, values and operator.
   *
   * @pre   propertyName != null;
   * @pre   values != null;
   * @pre   getOperators().contains(operator);
   * @post  new.getPropertyName().equals(propertyName);
   * @post  new.getValues().size() == values.size();
   * @post  (forall int i;
   *             0 <= i < values.size();
   *             new.getValues().get(i) == values.get(i));
   * @post  getOperator().equals(operator);
   */
  public FilterCriterion(String propertyName, List values, String operator) {
    initialise(propertyName, values, operator);
  }

  /**
   * Create a new criterion with the given property name, value and operator.
   *
   * @pre   propertyName != null;
   * @pre   getOperators().contains(operator);
   * @post  new.getPropertyName().equals(propertyName);
   * @post  new.getValues().size() == 1;
   * @post  new.getValues().get(0) == value;
   * @post  getOperator().equals(operator);
   */
  public FilterCriterion(String propertyName, Object value, String operator) {
    List values = new ArrayList();
    values.add(value);
    initialise(propertyName, values, operator);
  }

  /* </construction> */

  /**
   * @pre   propertyName != null;
   * @pre   values != null;
   * @pre   getOperators().contains(operator);
   * @post  new.getPropertyName().equals(propertyName);
   * @post  new.getValues().size() == values.size();
   * @post  (forall int i;
   *             0 <= i < values.size();
   *             new.getValues().get(i) == values.get(i));
   * @post  getOperator().equals(operator);
   */
  private void initialise(String propertyName, List values, String operator) {
    $propertyName = propertyName;
    $values = Collections.unmodifiableList(values);
    $operator = operator;
  }

  /*<property name="propertyName">*/
  //------------------------------------------------------------------

  /**
   * The name of a property.
   *
   * @basic
   */
  public final String getPropertyName() {
    return $propertyName;
  }

  /**
   * Set the property name to the given string.
   * @param     propertyName
   *            The property name to be set.
   * @pre       propertyName != null
   * @post      new.getPropertyName().equals(propertyName);
   */
  public final void setPropertyName(final String propertyName) {
    $propertyName = propertyName;
  }

  private String $propertyName;

  /*</property>*/

  /*<property name="values">*/
  //------------------------------------------------------------------

  /**
   * The values to compare with.
   *
   * @basic
   */
  public final List getValues() {
    return Collections.unmodifiableList($values);
  }

  /**
   * Set the values to the given list.
   * @param     values
   *            The list to be set.
   * @pre       values != null
   * @post      new.getValues().size() == values.size();
   * @post      (forall int i;
   *                 0 <= i < values.size();
   *                 new.getValues().get(i) == values.get(i));
   */
  public final void setValues(final List values) {
    $values = Collections.unmodifiableList(values);
  }

  /**
   * @invar  $values != null;
   */
  private List $values = new ArrayList();

  /*</property>*/

  /*<property name="operator">*/
  //------------------------------------------------------------------

  /**
   * The name of the operator.
   *
   * @basic
   */
  public final String getOperator() {
    return $operator;
  }

  /**
   * Set the operator to the given string.
   * @param     operator
   *            The operator to be set.
   * @pre       getOperators().contains(operator);
   * @post      new.getOperator().equals(operator);
   */
  public final void setOperator(final String operator) {
    $operator = operator;
  }

  /**
   * @invar  getOperators().contains($operator);
   */
  private String $operator;

  /*</property>*/

  /**
   * Return a set containing all operator strings.
   *
   * @post  result != null;
   * @post  result.size() == 2;
   * @post  result.contains(EQ);
   * @post  result.contains(LIKE);
   */
  public static Set getOperators() {
    Set result = new HashSet();
    result.add(EQ);
    result.add(LIKE);
    return result;
  }
}
