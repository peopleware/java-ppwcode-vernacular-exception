/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.persistence_II.hibernate;

import be.peopleware.persistence_II.DaoFactory;
import be.peopleware.persistence_II.dao.Dao;

/**
 * A factory for creating hibernate dao objects.
 *
 * @author nsmeets
 * @author ashoudou
 */
public class HibernateDaoFactory implements DaoFactory {

  /**
   * Returns a hibernate dao of the given type.
   * @param   type
   *          A string describing the type of the dao to return.
   * @return  result instanceof AbstractHibernateDao;
   */
  public Dao getDao(final String type) {
    if (ASYNC_CRUD.equals(type)) {
      return new HibernateAsyncCrudDao();
    }
    else if (FILTER.equals(type)) {
      return new HibernateFilterDao();
    }
    else {
      assert false : "Unknown Dao type";
      return null;
    }
  }
}
