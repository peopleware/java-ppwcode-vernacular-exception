/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.value_II.hibernate;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.UserType;
import be.peopleware.value_II.EnumerationValue;
import be.peopleware.value_II.EnumerationValueEditor;

/**
 * Support class for Hibernate mapping for
 * {@link EnumerationValue} subtypes.
 * Instance of {@link EnumerationValue} subtypes are mapped
 * to a VARCHAR, that contains the
 * {@link EnumerationValueEditor#getAsText() tag} for the enumeration
 * type value, as defined by the property editor. According to the
 * contract of {@link EnumerationValue} and
 * {@link EnumerationValueEditor}, this is equal to
 * {@link EnumerationValue#toString()}.
 *
 * @annotation
 *    Based on <a href="http://www.hibernate.org/172.html">work by
 *    <em>Christian</em> and <em>sethladd</em></a>.
 *
 * @invar getEnumerationValueEditor() != null;
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 */
public class AbstractEnumerationUserType implements UserType {

  /*<section name="Meta Information">*/
  //  ------------------------------------------------------------------

  /** {@value} */
  public static final String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_TAG = "$Name$"; //$NON-NLS-1$

  /*</section>*/

  /**
   * @post new.getEnumerationValueEditor() == editor;
   */
  protected AbstractEnumerationUserType(final EnumerationValueEditor editor) {
    $editor = editor;
  }

  /**
   * An instance of the enumeration type editor of the enumeration
   * type this UserType works for.
   *
   * @basic
   */
  public final EnumerationValueEditor getEnumerationValueEditor() {
    return $editor;
  }

  private EnumerationValueEditor $editor;

  private static final int[] SQL_TYPES = {Types.VARCHAR};

  /**
   * @return {Types.VARCHAR};
   */
  public final int[] sqlTypes() {
    return SQL_TYPES;
  }

  /**
   * @return getEnumerationValueEditor().getEnumerationValue();
   */
  public final Class returnedClass() {
    return getEnumerationValueEditor().getEnumerationValueType();
  }

  /**
   * @return (x == null) ? (y == null) : x.equals(y);
   */
  public final boolean equals(final Object x, final Object y) {
    return (x == null) ? (y == null) : x.equals(y);
  }

  /**
   * @return value;
   */
  public final Object deepCopy(final Object value) {
    return value;
  }

  /**
   * @return false;
   *         EnumerationValue implements ImmutableValue
   */
  public final boolean isMutable() {
    return false;
  }

  /**
   * Retrieve an instance of the mapped class from a JDBC resultset.
   * Implementors should handle possibility of null values.
   *
   * @result  resultSet.wasNull()
   *            ==> result == null;
   * @result  !resultSet.wasNull()
   *            ==> result == the object returned by the property editor for
   *                          the string in the field with <code>names[0]</code>.
   * @throws  HibernateException
   *          Hibernate.STRING.nullSafeGet(resultSet, names[0]);
   * @throws  SQLException
   *          Hibernate.STRING.nullSafeGet(resultSet, names[0]);
   * @throws  SQLException
   *          resultSet.wasNull();
   */
  public final Object nullSafeGet(final ResultSet resultSet,
                                  final String[] names,
                                  final Object owner)
          throws HibernateException, SQLException {
    Object result = null;
    String tag = (String)Hibernate.STRING.nullSafeGet(resultSet, names[0]);
    if (!(resultSet.wasNull())) {
      getEnumerationValueEditor().setAsText(tag);
      result = getEnumerationValueEditor().getValue();
    }
    return result;
  }

  /**
   * Write an instance of the mapped class to a prepared statement.
   *
   * @post    value == null
   *            ==> the parameter at the given index is set to null
   * @post    value != null
   *            ==> the parameter at the given index is set to the string
   *                representation of the given value as returned by the
   *                enumeration value editor
   *
   * @throws  HibernateException
   *          (value != null)
              && !returnedClass().getName().equals(value.getClass().getName())
   * @throws  SQLException
   *          value == null
   *          && statement.setNull(index, Types.VARCHAR) throws a SQLException;
   * @throws  SQLException
   *          value != null
   *          && statement.setString(index, getEnumerationValueEditor().getAsText());
   */
  public final void nullSafeSet(final PreparedStatement statement,
                                final Object value,
                                final int index)
      throws HibernateException, SQLException {
    // make sure the received value is of the right type
    if ((value != null)
          && !returnedClass().getName().equals(value.getClass().getName())) {
      throw new HibernateException("\"" //$NON-NLS-1$
                                   + ((value == null)
                                        ? "null" //$NON-NLS-1$
                                        : value.toString())
                                   + "\" is not a " //$NON-NLS-1$
                               + returnedClass().getName());
    }
    if (value == null) {
      statement.setNull(index, Types.VARCHAR);
    }
    else {
      getEnumerationValueEditor().setValue(value);
      statement.setString(index,
                          getEnumerationValueEditor().getAsText());
    }
  }

}
