/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.persistence_I.hibernate;


import net.sf.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.persistence_I.dao.Dao;


/**
 * Support methods for Hibernate <acronym title="Data Access Object">DAO<acronym>'s.
 * These implementations need a Hibernate {@link Session}.
 *
 * @author Jan Dockx
 * @author Peopleware n.v.
 */
public class AbstractHibernateDao implements Dao {
  
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


  
  private static final Log LOG = LogFactory.getLog(AbstractHibernateDao.class);


  
  /**
   * @basic
   * @init      null;
   */
  public final Session getSession() {
    return $session;
  }

  /**
   * @param     session
   *            The hibernate session to use for database manipulations.
   * @post      new.getSession() == session;
   * @throws    IllegalStateException
   */
  public void setSession(final Session session) throws IllegalStateException {
    LOG.debug("setting session (" + session + ")");
    $session = session;
  }

  /**
   * @invar     $session != null;
   */
  private Session $session;

}
