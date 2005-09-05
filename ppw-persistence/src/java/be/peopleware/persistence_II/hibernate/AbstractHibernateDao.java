/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.persistence_II.hibernate;


import net.sf.hibernate.Session;
import be.peopleware.persistence_II.dao.Dao;


/**
 * Support methods for Hibernate <acronym title="Data Access Object">DAO<acronym>'s.
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

  /**
   * @basic
   * @init      null;
   */
  public abstract Session getSession();

}
