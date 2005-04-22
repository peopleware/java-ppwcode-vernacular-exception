/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.persistence_I.hibernate;


import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.sf.hibernate.Criteria;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.expression.Criterion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import be.peopleware.exception_I.TechnicalException;
import be.peopleware.persistence_I.dao.FilterDao;
import be.peopleware.persistence_I.hibernate.HibernateAsyncCrudDao;


/**
 * A {@link FilterDao} for Hibernate.
 *
 * @author nsmeets
 * @author ashoudou
 * @author Peopleware n.v.
 */
public final class HibernateFilterDao extends HibernateAsyncCrudDao implements FilterDao {

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

  private static final String NULL_SESSION = "Session is null";

  private static final Log LOG = LogFactory.getLog(HibernateFilterDao.class);

  /* <construction> */
  //------------------------------------------------------------------

  /**
   *
   */
  public HibernateFilterDao() {
    // NOP
  }

  /* </construction> */

  /**
   * @see    FilterDao
   */
  public final Set retrievePersistentBeans(final Class type, final Map criteriaMap)
      throws TechnicalException {
		LOG.debug("starting find for persistent beans with type="+type.getName()+
               "and properties="+criteriaMap.toString());
    if (getSession() == null) {
      throw new TechnicalException(NULL_SESSION, null);
    }
    // @mudo Mag dit hier staan?
		LOG.debug("Hibernate session retrieved: " + getSession());

		Set result = Collections.EMPTY_SET;

		try {
			Criteria criteria = getSession().createCriteria(type);
      // sort on id
      Iterator i = criteriaMap.keySet().iterator();
      while (i.hasNext()) {
        String key = (String) i.next();
        Object value = criteriaMap.get(key);
        if (value != null) {
          // empty string are not accepted as a criterion
          if (  !( value instanceof String
                   &&
                   ((String)value).length() == 0
                )
          ) {
            Criterion criterion =
              net.sf.hibernate.expression.Expression.eq(key, value);
            criteria.add(criterion);
          }
        }
      }
			result = Collections.unmodifiableSet (new HashSet (criteria.list()));
		}
    catch (HibernateException e) {
			throw new TechnicalException(e.getMessage(), e);
		}

		LOG.debug("found " + result.size() + " matching persistentBean(s)");
		LOG.debug("session released");
		return result;
	}
}
