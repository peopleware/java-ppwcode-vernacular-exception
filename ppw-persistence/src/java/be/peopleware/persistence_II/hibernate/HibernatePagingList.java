/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.persistence_II.hibernate;


import java.util.AbstractSequentialList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ListIterator;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.persistence_II.PersistentBean;


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
public final class HibernatePagingList extends AbstractSequentialList {

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
   * @throws HibernateException
   */
  public HibernatePagingList(Query query, Query countQuery, int pageSize) throws HibernateException {
    assert pageSize > 0;
    assert query != null;
    assert countQuery != null;
    $query = query;
    $countQuery = countQuery;
    $pageSize = pageSize;
    initRecordCount();
    initSize();
  }

  /**
   * @pre criteria != null;
   * @pre countCriteria != null;
   * @pre pageSize > 0;
   * @post new.getQuery() == query;
   * @post new.getCountQuery() == countQuery;
   * @post new.getPageSize() == pageSize;
   * @throws HibernateException
   */
  public HibernatePagingList(Criteria criteria, Query countQuery, int pageSize) throws HibernateException {
    assert pageSize > 0;
    assert criteria != null;
    assert countQuery != null;
    $criteria = criteria;
    $countQuery = countQuery;
    $pageSize = pageSize;
    initRecordCount();
    initSize();
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

//  private void initCountQuery() {
//    String q = $query.getQueryString();
//    int fromPos = q.indexOf("from");
//    String partialCq = q.substring(fromPos);
//    $countQuery = $query.ge
//  }

  private int retrieveRecordCount() throws HibernateException {
    LOG.debug("retrieving record count");
    int result = ((Integer)$countQuery.uniqueResult()).intValue();
    LOG.debug("record count is " + result);
    return result;
  }

  /**
   * @invar $countQuery != null;
   */
  private Query $countQuery;

  /*</property>*/



  /*<property name="page size">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final int getPageSize() {
    return $pageSize;
  }

  /**
   * @invar $pageSize > 0;
   */
  private int $pageSize;

  /*</property>*/



  /*<property name="record count">*/
  //------------------------------------------------------------------

  /**
   * This must be the same on the next DB access,
   * or we give a {@link java.util.ConcurrentModificationException}.
   *
   * @basic
   */
  public final int getRecordCount() {
    return $recordCount;
  }

  private void initRecordCount() throws HibernateException {
    $recordCount = retrieveRecordCount();
  }

  /**
   * @invar $recordCount >= 0;
   */
  private int $recordCount;

  /*</property>*/



  /*<property name="size">*/
  //------------------------------------------------------------------

  /**
   * The number of pages. This might change,
   * when a next page is requested.
   *
   * @basic
   */
  public final int size() {
    return $size;
  }

  private void initSize() {
    assert $recordCount >= 0;
    $size = ($recordCount == 0) ? 0 : (($recordCount - 1) / getPageSize()) + 1;
    LOG.debug("number of pages = " + $size);
  }

  /**
   * @invar $pageSize > 0;
   */
  private int $size;

  /*</property>*/



  /**
   * @result result instanceof PagesIterator;
   */
  public ListIterator listIterator(int index) {
    return new PagesIterator(index);
  }

  public final class PagesIterator implements ListIterator {

    /*<construction>*/
    //------------------------------------------------------------------

    /**
     * @pre page >= 0;
     * @pre page < size();
     * @post new.nextIndex() == page;
     */
    public PagesIterator(int page) {
      assert page >= 0;
      assert page < size();
      $nextPage = page;
    }

    /*</construction>*/



    /*<property name="currentPage">*/
    //------------------------------------------------------------------

    /**
     * @basic
     */
    public final int nextIndex() {
      return $nextPage;
    }

    /**
     * @return = nextIndex() - 1;
     */
    public final int previousIndex() {
      return $nextPage - 1;
    }

    /**
     * @return nextIndex() < size() - 1;
     */
    public final boolean hasNext() {
      return $nextPage < size();
    }

    /**
     * @return nextIndex() > 0;
     */
    public final boolean hasPrevious() {
      return $nextPage > 0;
    }

    private int $nextPage;

    /*</property>*/



    /*<property name="expected pk of next and previous page">*/
    //------------------------------------------------------------------

    private Long $expectedFirstPkOfNextPage;

    private Long $expectedLastPkOfPreviousPage;

    /*</property>*/



    /**
     * We will retrieve 1 record extra before and after this page,
     * and remember it's PK; for the next or previous page, we can
     * check that it is the expected record; we cannot do this
     * for the first retrieval, or for the first or last page
     */
    public Object next() throws ConcurrentModificationException {
      LOG.debug("retrieving next page (" + $nextPage + ")");
      try {
        validateCount();
        boolean isFirstPage = $nextPage <= 0;
        boolean isLastPage = $nextPage >= size() - 1;
        List page = retrievePage(isFirstPage, isLastPage, $nextPage);
        validateOverlap($expectedFirstPkOfNextPage, page, 1);
          /* real first page record is at 1 (0 is a dummy for
           * the previous after this, which we will remember in a
           * moment; nothing will happen for the first page,
           * where this is not true.
           */
        rememberPks(isFirstPage, isLastPage, page);
        $nextPage++; // update cursor info
        return page;
      }
      catch (HibernateException hExc) {
        throw new TechnicalProblemException(hExc);
      }
    }

    /**
     * We will retrieve 1 record extra before and after this page,
     * and remember it's PK; for the next or previous page, we can
     * check that it is the expected record; we cannot do this
     * for the first retrieval, or for the first or last page
     */
    public Object previous() throws ConcurrentModificationException {
      int pageToRetrieve = $nextPage - 1;
      LOG.debug("retrieving previous page (" + pageToRetrieve + ")");
      try {
        validateCount();
        boolean isFirstPage = pageToRetrieve <= 0;
        boolean isLastPage = pageToRetrieve >= size() - 1;
        List page = retrievePage(isFirstPage, isLastPage, pageToRetrieve);
        validateOverlap($expectedLastPkOfPreviousPage, page, size() - 2);
          /* real first page record is at size() - 2 (size() - 1 is a dummy for
           * the next after this, which we will remember in a
           * moment; nothing will happen for the last page,
           * where this is not true.
           */
        rememberPks(isFirstPage, isLastPage, page);
        $nextPage--; // update cursor info
        return page;
      }
      catch (HibernateException hExc) {
        throw new TechnicalProblemException(hExc);
      }
    }

    public class TechnicalProblemException extends RuntimeException {

      public TechnicalProblemException(Throwable cause) {
        super(cause);
      }

    }

    private void validateCount() throws ConcurrentModificationException, HibernateException {
      LOG.debug("validating that count of total set has not changed");
      int newRecordCount = retrieveRecordCount();
      if (newRecordCount != getRecordCount()) {
        throw new ConcurrentModificationException("total record count for this query changed since last DB access");
      }
    }

    private List retrievePage(boolean isFirstPage, boolean isLastPage, int pageToRetrieve)
        throws HibernateException, ConcurrentModificationException {
      LOG.debug("retrieving page " + pageToRetrieve);
      int retrieveSize = getPageSize() + (isFirstPage ? 0 : 1) + (isLastPage ? 0 : 1);
      int realStartOfPage = pageToRetrieve * getPageSize();
      int startOfPage = isFirstPage ? realStartOfPage : realStartOfPage - 1;
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
      if (page.isEmpty()) {
        throw new ConcurrentModificationException("page is empty: resultset for this query changed since last DB access");
      }
      return page;
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

    /**
     * With a next page, we want to check the first real record: is it what we expected?
     * With a previous page, we want to check the last real record: is it what we expected?
     * We do not have an expected PK though, when this is the first page to access
     * (which could be in the middle of things).
     *
     * This does nothing if <code>expectedKey</code> is <code>null</code>.
     *
     * For a next, the position to check is 1 (at position 0 there is a dummy
     * to store for the "previous" after this). For a previous, the position to
     * check is size() - 2 (at position size() - 1 there is a dummy to store for
     * the "next" after this). For the first page however, there is no dummy at
     * the start, and for the last there is no dummy at the end, so we would need
     * to differ the position at which to look in the list. However, this method will
     * never be called for those cases: the last page can only be retrieved with
     * next, or with a previous when <code>!hasNext()</code>, that potentially follows a next.
     * For a next, we only check the first record, which follows the rules. The last
     * next overwrites the remembered PK with null (see {@link #rememberPks(boolean, boolean, List)},
     * so no check will be done with the previous that follows. The first page
     * can only be retrieved with a next when <code>!hasPrevious()</code>,
     * that potentially follows a previous, or with a previous. For a previous,
     * we only check the last record, which follows the rules. The last previous
     * overwrites the remembered PK with null (see {@link #rememberPks(boolean, boolean, List)},
     * so no check will be done in the next that follows. Both remembered PK's are null
     * initially.
     * The last issue arises when a page is both the first and the last page. This
     * page can be reached with both a next and a previous, but always with
     * either !hasPrevious or !hasNext(). In this case, nothing is remembered, ever,
     * and there will be no test.
     * In conclusion we can say that using 1 and size() - 2 as the positions to test
     * is ok in all cases, and diversification is not needed.
     */
    private void validateOverlap(Long expectedKey, List page, int overlapPosition) throws ConcurrentModificationException {
      LOG.debug("validating overlap: expectedKey = " + expectedKey + " for position = " + overlapPosition);
      if ($expectedLastPkOfPreviousPage != null) {
        PersistentBean pb = (PersistentBean)page.get(overlapPosition);
        LOG.debug("actual id = " + pb.getId());
        if (! expectedKey.equals(pb.getId())) {
          throw new ConcurrentModificationException("resultset for this query changed since last DB access");
        }
      }
    }

    private void rememberPks(boolean isFirstPage, boolean isLastPage, List page) {
      LOG.debug("remembering PK of first and last record");
      /*
       * remember the potential extra records at start and end for the
       * next retrieve, and remove them from the result
       */
      if (! isLastPage) {
        int lastIndex = page.size() - 1;
        PersistentBean pb = (PersistentBean)page.get(lastIndex);
        $expectedFirstPkOfNextPage = pb.getId();
        LOG.debug("new $expectedFirstPkOfNextPage: " + $expectedFirstPkOfNextPage);
        page.remove(lastIndex);
      }
      else {
        $expectedFirstPkOfNextPage = null;
      }
      if (! isFirstPage) {
        PersistentBean pb = (PersistentBean)page.get(0);
        $expectedLastPkOfPreviousPage = pb.getId();
        LOG.debug("new $expectedLastPkOfPreviousPage: " + $expectedLastPkOfPreviousPage);
        page.remove(0);
      }
      else {
        $expectedLastPkOfPreviousPage = null;
      }
    }

    /**
     * @post   false;
     * @throws UnsupportedOperationException
     *         true;
     */
    public void remove() throws UnsupportedOperationException {
      throw new UnsupportedOperationException();
    }

    /**
     * @post   false;
     * @throws UnsupportedOperationException
     *         true;
     */
    public void set(Object o) throws UnsupportedOperationException  {
      throw new UnsupportedOperationException();
    }

    /**
     * @post   false;
     * @throws UnsupportedOperationException
     *         true;
     */
    public void add(Object o) throws UnsupportedOperationException {
      throw new UnsupportedOperationException();
    }

  }

}
