package be.peopleware.persistence_I;

import be.peopleware.persistence_I.dao.Dao;

/**
 * A factory for creating dao objects.
 * The type of the factory determines the storage type of the dao.
 *
 * @author nsmeets
 * @author ashoudou
 *
 */
public interface DaoFactory {

  public static final String ASYNC_CRUD = "asyncCrud";
  public static final String FILTER = "filter"; // @mudo ok? ExtendedDao invoeren in persistence

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
