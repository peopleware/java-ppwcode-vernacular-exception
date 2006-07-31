/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.persistence_II.hibernate;


import java.util.ConcurrentModificationException;
import java.util.List;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.exception_I.TechnicalException;
import be.peopleware.persistence_II.dao.PagingList;


/**
 * A list of lists, that contains the result of a Hibernate query
 * using paging. Each page, except for the last,
 * has size {@link #getPageSize()}.
 * When the virtual resultset on the DB changes during iteration,
 * we throw a {@link ConcurrentModificationException} when the next
 * page is requested.
 *
 * @author Jan Dockx
 * @author Peopleware n.v.
 *
 * @invar getPageSize() > 0;
 */
public final class HibernatePagingList extends PagingList {

  /*<section name="Meta Information">*/
  //------------------------------------------------------------------
  /** {@value} */
  public static final String CVS_REVISION = "$Revision$";
  /** {@value} */
  public static final String CVS_DATE = "$Date$";
  /** {@value} */
  public static final String CVS_STATE = "$State$";
  /** {@value} */
  public static final String CVS_TAG = "$Name$";
  /*</section>*/


  private static final Log LOG = LogFactory.getLog(HibernatePagingList.class);


  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @pre query != null;
   * @pre countQuery != null;
   * @pre pageSize > 0;
   * @post new.getQuery() == query;
   * @post new.getCountQuery() == countQuery;
   * @post new.getPageSize() == pageSize;
   * @throws TechnicalException
   */
  public HibernatePagingList(Query query, Query countQuery, int pageSize) throws TechnicalException {
    super(pageSize, retrieveRecordCount(countQuery));
    assert query != null;
    assert countQuery != null;
    $query = query;
    $countQuery = countQuery;
  }

  /**
   * @pre criteria != null;
   * @pre countCriteria != null;
   * @pre pageSize > 0;
   * @post new.getQuery() == query;
   * @post new.getCountQuery() == countQuery;
   * @post new.getPageSize() == pageSize;
   * @throws TechnicalException
   */
  public HibernatePagingList(Criteria criteria, Query countQuery, int pageSize) throws TechnicalException {
    super(pageSize, retrieveRecordCount(countQuery));
    assert criteria != null;
    assert countQuery != null;
    $criteria = criteria;
    $countQuery = countQuery;
  }

  /*</construction>*/



  /*<property name="query">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Query getQuery() {
    return $query;
  }

  /**
   * @invar $query != null;
   */
  private Query $query;

  /*</property>*/



  /*<property name="criteria">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Criteria getCriteria() {
    return $criteria;
  }

  /**
   * @invar $criteria != null;
   */
  private Criteria $criteria;

  /*</property>*/



  /*<property name="countQuery">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Query getCountQuery() {
    return $countQuery;
  }

  private static int retrieveRecordCount(Query countQuery) throws TechnicalException {
    try {
      LOG.debug("retrieving record count");
      int result = ((Integer)countQuery.uniqueResult()).intValue();
      LOG.debug("record count is " + result);
      return result;
    }
    catch (HibernateException e) {
      throw new TechnicalException("cannot retrieve count", e);
    }
  }

  protected final int retrieveRecordCount() throws TechnicalException {
      return retrieveRecordCount(getCountQuery());
  }

  /**
   * @invar $countQuery != null;
   */
  private Query $countQuery;

  /*</property>*/



  protected final List retrievePage(int retrieveSize, int startOfPage) throws TechnicalException {
    try {
      List page = null;
      if ($criteria != null) {
        page = retrievePageCriteria(retrieveSize, startOfPage);
      }
      else if ($query !=  null) {
        page = retrievePageQuery(retrieveSize, startOfPage);
      }
      else {
        assert false : "Cannot happen";
      }
      LOG.debug("page retrieved successfully");
      return page;
    }
    catch (HibernateException hExc) {
      throw new TechnicalException("cannot retrieve page", hExc);
    }
  }

  private List retrievePageCriteria(int retrieveSize, int startOfPage) throws HibernateException {
    $criteria.setMaxResults(retrieveSize); // first and last record is for check only (depending on booleans)
    $criteria.setFirstResult(startOfPage);
    List page = $criteria.list();
    return page;
  }

  private List retrievePageQuery(int retrieveSize, int startOfPage) throws HibernateException {
    $query.setMaxResults(retrieveSize); // first and last record is for check only (depending on booleans)
    $query.setFirstResult(startOfPage);
    List page = $query.list();
    return page;
  }

}
