/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.persistence_II;

import be.peopleware.persistence_II.dao.Dao;
import be.peopleware.persistence_II.dao.AsyncCrudDao;
import be.peopleware.persistence_II.dao.FilterDao;

/**
 * A factory for creating dao objects.
 * The type of the factory determines the storage type of the dao.
 *
 * @author nsmeets
 * @author ashoudou
 *
 */
public interface DaoFactory {

  /**
   * String identifying an {@link AsyncCrudDao}.
   *
   * <strong>= &quot;asyncCrud&quot;</strong>
   */
  public static final String ASYNC_CRUD = "asyncCrud";

  /**
   * String identifying a {@link FilterDao}.
   *
   * <strong>= &quot;filter&quot;</strong>
   */
  public static final String FILTER = "filter";

  /**
   * Returns a dao of the given type.
   * @param   type
   *          A string describing the type of the dao to return.
   * @return  if (type.equals(ASYNC_CRUD))
   *             then
   *               result instanceof AsyncCrudDao
   *             else
   *               if (type.equals(FILTER))
   *                 then
   *                   result instanceof FilterDao;
   */
  Dao getDao(String type);
}
