package be.peopleware.persistence_I.hibernate;

import be.peopleware.persistence_I.DaoFactory;
import be.peopleware.persistence_I.dao.Dao;

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
  public Dao getDao(String type) {
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
