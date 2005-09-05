/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.persistence_II.hibernate;


import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.expression.Criterion;
import net.sf.hibernate.expression.Expression;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.exception_I.TechnicalException;
import be.peopleware.persistence_II.dao.FilterDao;


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

  /*<construction>*/
  //------------------------------------------------------------------

  // Default constructor

  /*</construction>*/

  /**
   * @see FilterDao
   * @mudo (dvankeer) This code is duplicated in JsfHibernateFilterDao.
   */
  public final Set retrievePersistentBeans(final Class type,
                                           final List criteriaList)
      throws TechnicalException {
    LOG.debug("Starting find for persistent beans with type=" + type.getName()
              + "and criteria " + criteriaList.toString());
    if (getSession() == null) {
      throw new TechnicalException(NULL_SESSION, null);
    }
    Set result = Collections.EMPTY_SET;
    try {
      // the persistent beans should be of the given type
      Criteria criteria = getSession().createCriteria(type);
      // the persistent beans should satisfy the given criteria
      Iterator i = criteriaList.iterator();
      while (i.hasNext()) {
        FilterCriterion filterCriterion = (FilterCriterion) i.next();
        Criterion hibernateCriterion = createHibernateCriterion(filterCriterion);
        if (hibernateCriterion != null) {
          criteria.add(hibernateCriterion);
        }
      }
      // retrieve the persistent beans of the given type satisfying the given criteria
      result = Collections.unmodifiableSet(new HashSet(criteria.list()));
    }
    catch (HibernateException e) {
      throw new TechnicalException(e.getMessage(), e);
    }
    LOG.debug("found " + result.size() + " matching persistentBean(s)");
    LOG.debug("session released");
    return result;
  }

  /**
   * Create a hibernate criterion from the given filter criterion.
   * @param   filterCriterion
   * @pre     filterCriterion != null;
   * @return  if (operator.equals(FilterCriterion.EQ) &&
   *              !isNullOrEmptyString(values.get(0))
   *          )
   *            then
   *              result == Expression.eq(filterCriterion.getPropertyName(), values.get(0));
   *            else
   *              if (operator.equals(FilterCriterion.LIKE) &&
   *                  !isNullOrEmptyString(values.get(0))
   *              )
   *                then
   *                  result == Expression.like(filterCriterion.getPropertyName(), values.get(0));
   *                else
   *                  result == null;
   */
  public Criterion createHibernateCriterion(FilterCriterion filterCriterion) {
    String operator = filterCriterion.getOperator();
    String propertyName = filterCriterion.getPropertyName();
    List values = filterCriterion.getValues();
    if (operator.equals(FilterCriterion.EQ)) {
      // create 'eq' criterion
      Object value = values.get(0);
      // null values and empty strings are ignored (they are interpreted as no constraint)
      if (!(isNullOrEmptyString(value))) {
        return Expression.eq(propertyName, value);
      }
      else {
        return null;
      }
    }
    if (operator.equals(FilterCriterion.LIKE)) {
      // create 'like' criterion
      Object value = values.get(0);
      // null values and empty strings are ignored (they are interpreted as no constraint)
      if (!(isNullOrEmptyString(value))) {
        String str = value.toString();
        str = "%" + str + "%";
        return Expression.like(propertyName, str);
      }
      else {
        return null;
      }
    }
    assert false : "Unknown operator in filter criterion: "+operator;
    return null;
  }

  /**
   * Returns true when the given object is null, or when it is an empty string.
   * Returns false otherwise.
   *
   * @param   value
   *          The value to check.
   * @return  (value == null)
   *          ||
   *          ((value instanceof String) && (((String)value).length() == 0))
   */
  private boolean isNullOrEmptyString(Object value) {
    return
      (value == null)
      ||
      ((value instanceof String) && (((String)value).length() == 0));
  }

}