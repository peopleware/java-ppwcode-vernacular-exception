package be.peopleware.persistence_I.ramstorage;


import be.peopleware.persistence_I.DaoFactory;
import be.peopleware.persistence_I.dao.Dao;


/**
 * A factory for creating ramstorage dao objects.
 *
 * @author nsmeets
 * @author ashoudou
 */
public class RamStorageDaoFactory implements DaoFactory {

  /**
   * Returns a hibernate dao of the given type.
   * @param   type
   *          A string describing the type of the dao to return.
   * @return  result instanceof AbstractRamStorageDao;
   */
  public Dao getDao(String type) {
    if (ASYNC_CRUD.equals(type)) {
      return new RamStorageAsyncCrudDao();
    }
    else if (FILTER.equals(type)) {
      return new RamStorageFilterDao();
    }
    else {
      assert false : "Unknown Dao type";
      return null;
    }
  }
}
