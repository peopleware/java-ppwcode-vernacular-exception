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
import be.peopleware.value_II.RSZNumber;


/**
 * Hibernate user type for RSZNumber.
 * Instances of {@link RSZNumber} are mapped to 1 column in the database.
 *
 * @author    nsmeets
 * @author    rclerckx
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public class RszNumberUserType implements UserType {

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
  public RszNumberUserType() {
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
    return RSZNumber.class;
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
   * @return  An object of the type RSZNumber containing the given string.
   * @throws  HibernateException
   *          true;
   * @throws  SQLException
   *          true;
   */
  public final Object nullSafeGet(final ResultSet resultSet,
                                  final String[] names,
                                  final Object owner)
          throws HibernateException, SQLException {
    RSZNumber result = null;
    String dbValue = null;
    try {
      dbValue = resultSet.getString(names[0]);
      if (!(resultSet.wasNull())) {
        result = new RSZNumber(dbValue); // PropertyException
      }
      // else, result stays null (NULL in DB)
    }
    catch (PropertyException pExc) {
      throw new HibernateException("could not transform " + dbValue + " into an RSZ number", pExc);
    }
    return result;
  }


  /**
   * Write an instance of the mapped class to a prepared statement.
   * Implementors should handle possibility of null values.
   * A multi-column type should be written to parameters starting from index.
   *
   * @post    Write a prepared statement for the given RSZ number.
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
      statement.setNull(index, Types.VARCHAR);
    }
    else {
      RSZNumber nn = (RSZNumber) value;
      statement.setString(index, nn.getLeftNumber() + nn.getMiddleNumber() + nn.getRightNumber());
    }
  }
}
