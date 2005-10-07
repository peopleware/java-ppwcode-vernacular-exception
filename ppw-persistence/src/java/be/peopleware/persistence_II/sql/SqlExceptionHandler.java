/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.persistence_II.sql;


import java.sql.SQLException;

import be.peopleware.bean_V.PropertyException;
import be.peopleware.exception_I.TechnicalException;
import be.peopleware.persistence_II.PersistentBean;
import be.peopleware.persistence_II.dao.Dao;


/**
 * <p>Abstraction of how to handle {@link SQLException SQLExceptions}
 *   for {@link Dao Dao's}.</p>
 * <p>{@link Dao Dao's} often must deal with potential exceptions
 *   from a JDBC driver. These either come from the driver, the
 *   database server, or from exceptions that are raised by
 *   triggers or stored procedures, or by constraint violations.
 *   They all turn up as {@link SQLException SQLExceptions}.</p>
 * <p>The first kinds are of a technical nature. Normally,
 *   this is fatal for an application. According to ppw standards,
 *   they should be encapsulated in a {@link TechnicalException}.
 *   The latter kinds are semantic exceptions. Normally, they
 *   would result in a rollback, user feedback, and continuation
 *   of the normal operation of the application. According to
 *   ppw standards, they should be encapsulated in a
 *   {@link PropertyException}</p>
 * <p>It is impossible for {@link Dao Dao's} to decide of which
 *   kind the {@link SQLException} is in general. Database exceptions
 *   are not much more than a string, and there is no standardization
 *   over different database engines. Furthermore, exceptions
 *   raised by triggers and stored procedures, and constraints,
 *   are application specific.</p>
 * <p>Specific {@link Dao} implementations can use specific
 *   implementations of this interface to make abstraction of
 *   how to handle {@link SQLException SQLExceptions}.</p>
 * <p>Implementations either should return a {@link PropertyException}
 *   that wraps the given {@link SQLException} if they find it of
 *   a semantic nature. If not, they should return <code>null</code>.
 *   Implementation methods should have no effects.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public interface SqlExceptionHandler {

  /* <section name="Meta Information"> */
  //------------------------------------------------------------------

  /** {@value} */
  public static final String CVS_REVISION = "$Revision$";
  /** {@value} */
  public static final String CVS_DATE = "$Date$";
  /** {@value} */
  public static final String CVS_STATE = "$State$";
  /** {@value} */
  public static final String CVS_TAG = "$Name$";

  /* </section> */


  /**
   * Return a {@link PropertyException} wrapping <code>sqlException</code>
   * if you find the latter of a semantic nature. If not, return <code>null</code>.
   *
   * @param sqlException
   *        The exception to handle.
   * @param pb
   *        The persistent bean for which we are performing a database
   *        operation. This can be <code>null</code>.
   * @pre sqlException != null;
   * @throws TechnicalException
   *         Could not find this out, for a technical reason.
   */
  PropertyException handle(SQLException sqlException, PersistentBean pb) throws TechnicalException;

}
