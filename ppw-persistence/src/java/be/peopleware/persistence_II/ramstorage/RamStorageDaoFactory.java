/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.persistence_II.ramstorage;


import be.peopleware.persistence_II.DaoFactory;
import be.peopleware.persistence_II.dao.Dao;


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
  public Dao getDao(final String type) {
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
