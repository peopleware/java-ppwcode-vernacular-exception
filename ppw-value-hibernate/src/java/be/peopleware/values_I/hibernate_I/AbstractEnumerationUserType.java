package be.peopleware.values_I.hibernate_I;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.UserType;
import be.peopleware.value_I.EnumerationValue;
import be.peopleware.value_I.EnumerationValueEditor;

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
   * @return the object returned by the property editor for
   *          the string in the field with <code>names[0]</code>.
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