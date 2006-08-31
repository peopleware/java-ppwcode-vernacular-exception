/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.persistence_II.hibernate;


import java.sql.SQLException;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import be.peopleware.bean_V.CompoundPropertyException;
import be.peopleware.bean_V.PropertyException;
import be.peopleware.exception_I.Exceptions;
import be.peopleware.exception_I.TechnicalException;
import be.peopleware.persistence_II.PersistentBean;
import be.peopleware.persistence_II.dao.Dao;
import be.peopleware.persistence_II.sql.MySqlSqlExceptionHandler;
import be.peopleware.persistence_II.sql.SqlExceptionHandler;


/**
 * Support methods for Hibernate <acronym title="Data Access Object">DAO</acronym>'s.
 * These implementations need a Hibernate {@link Session}.
 *
 * @author Jan Dockx
 * @author Peopleware n.v.
 */
public abstract class AbstractHibernateDao implements Dao {

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

  protected final void handleHibernateException(final HibernateException hExc,
                                          final String operationName,
                                          final PersistentBean pb)
      throws TechnicalException, CompoundPropertyException {
    SQLException sqlExc = (SQLException)Exceptions.huntFor(hExc, SQLException.class);
    if ((sqlExc != null) && (getSqlExceptionHandler() != null)) {
      PropertyException pExc = getSqlExceptionHandler().handle(sqlExc, pb);
      if (pExc != null) {
        wrapInCompound(pExc);
      }
      // if we are here, the above handler did not translate into a PropertyException
      // cannot be that the record is not found
      throw new TechnicalException("problem "
                                      + operationName
                                      + " record",
                                    hExc);
    }
    CompoundPropertyException cp = (CompoundPropertyException)Exceptions
         .huntFor(hExc, CompoundPropertyException.class);
    if (cp != null) {
      throw cp;
    }
    else {
      // cannot be that the record is not found
      throw new TechnicalException("problem "
                                      + operationName
                                      + " record",
                                  hExc);
    }
  }

  private void wrapInCompound(final PropertyException pExc) throws CompoundPropertyException {
    assert pExc != null;
    CompoundPropertyException cpExc =
        new CompoundPropertyException(pExc.getOrigin(), null, null, null);
    cpExc.addElementException(pExc);
    cpExc.close();
    throw cpExc;
  }
  
  
	/*<property name="session">*/
	//------------------------------------------------------------------

	/**
	 * Return the Hibernate session for this Dao.
	 */
	public final Session getSession() {
		return $session;
	}
	
	
	/**
	 * @param     session
	 *            The hibernate session to use for database manipulations.
	 * @post      new.getSession() == session;
	 */
	public void setSession(final Session session) throws TechnicalException {
		$session = session;
	}
	
	private Session $session;

	/*</property>*/



  /*<property name="sqlExceptionHandler">*/
  //------------------------------------------------------------------

  /**
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
   * <p>Implementations either throw a {@link PropertyException}
   *   that wraps the given {@link SQLException} if they find it of
   *   a semantic nature. If not, they end nominally.
   *   Implementations have no effects.</p>
   *
   * @init a {@link MySqlSqlExceptionHandler}.
   */
  public final SqlExceptionHandler getSqlExceptionHandler() {
    return $sqlExceptionHandler;
  }

  /**
   * @post new.getSqlExceptionHandler() == sqlExceptionHandler;
   */
  public final void setSqlExceptionHandler(final SqlExceptionHandler sqlExceptionHandler) {
    $sqlExceptionHandler = sqlExceptionHandler;
  }

	private SqlExceptionHandler $sqlExceptionHandler = new MySqlSqlExceptionHandler();

	/*</property>*/

}
