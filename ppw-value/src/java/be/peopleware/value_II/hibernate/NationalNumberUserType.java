/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.value_II.hibernate;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.UserType;
import be.peopleware.bean_V.PropertyException;
import be.peopleware.value_II.NationalNumber;


/**
 * Hibernate user type for NationalNumber.
 * Instances of {@link NationalNumber} are mapped to 1 column in the database.
 *
 * @author    nsmeets
 * @author    rclerckx
 */
public class NationalNumberUserType implements UserType {

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
   */
  public NationalNumberUserType() {
    // NOP
  }

  private static final int[] SQL_TYPES = {Types.VARCHAR};

  /**
   * @return {Types.VARCHAR};
   */
  public final int[] sqlTypes() {
    return SQL_TYPES;
  }

  /**
   * @return NationalNumber.class;
   */
  public Class returnedClass() {
    return NationalNumber.class;
  }

  /**
   * @return (x == null) ? (y == null) : x.equals(y);
   */
  public final boolean equals(final Object x, final Object y) {
    return (x == null)
               ? (y == null)
               : x.equals(y);
  }

  /**
   * Since this is an immutable class, we needn't make a copy.
   * @return value;
   */
  public Object deepCopy(final Object value) {
    return value;
  }

  /**
   * @return false;
   */
  public final boolean isMutable() {
    return false;
  }

  /**
   * Retrieve an instance of the mapped class from a JDBC resultset.
   * Implementors should handle possibility of null values.
   *
   * @return  An object of the type NationalNumber containing the given string.
   * @throws  HibernateException
   *          true;
   * @throws  SQLException
   *          true;
   */
  public final Object nullSafeGet(final ResultSet resultSet,
                                  final String[] names,
                                  final Object owner)
          throws HibernateException, SQLException {

    NationalNumber result = null;
    String nn = resultSet.getString(names[0]);

    if (!(resultSet.wasNull())) {
      String nn_left = nn.substring(0,6);
      String nn_middle = nn.substring(6,9);
      String nn_right = nn.substring(9);
      try {
        result = new NationalNumber(nn_left, nn_middle, nn_right);
      }
      catch(PropertyException pExc) {
        assert false : "Shouldn't happen";
      }
    }
    return result;
  }


  /**
   * Write an instance of the mapped class to a prepared statement.
   * Implementors should handle possibility of null values.
   * A multi-column type should be written to parameters starting from index.
   *
   * @post    Write a prepared statement for the given national number.
   * @throws  HibernateException
   *          true;
   * @throws  SQLException
   *          true;
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
      statement.setNull(index, sqlTypes()[0]);
    }
    else {
      NationalNumber nn = (NationalNumber) value;
      statement.setString(index, nn.getLeftNumber()+nn.getMiddleNumber()+nn.getRightNumber());
    }
  }
}
