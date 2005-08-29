package be.peopleware.jsf_II.persistence;


import be.peopleware.jsf_II.FatalFacesException;
import be.peopleware.persistence_I.dao.AsyncCrudDao;


/**
 * <p>Common functionality for JavaServer Faces backing beans (handlers)
 *  that use a {@link AsyncCrudDao}.</p>
 *
 * @note There is no reason for now to make this an interface.
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 *
 * @invar getDaoType() == AsyncCrudDao.class;
 * @invar getDefaultDaoVariableName().equals(
 *          DEFAULT_DAO_VARIABLE_NAME
 *        );
 */
public abstract class AsyncCrudDaoHandler extends DaoHandler {

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

  /**
   * Suffix for the {@link #DEFAULT_DAO_VARIABLE_NAME}.
   *
   * <strong>= {@value}</strong>
   */
  public static final String DEFAULT_DAO_VARIABLE_NAME_SUFFIX = "_dao";

  /**
   * The default dao variable name for handlers of this type.
   * If you do not fill out the {@link #getDaoVariableName()},
   * a variable with this name is sought using the
   * JSF application {@link javax.faces.el.VariableResolver}
   * when a {@link #getDao() dao is requested}.
   *
   * <strong><code>= </code>{@link #fqcnVarName()}<code> +
   *    </code>{@link #DEFAULT_DAO_VARIABLE_NAME_SUFFIX}</strong>
   */
  public static final String DEFAULT_DAO_VARIABLE_NAME =
      fqcnVarName() + DEFAULT_DAO_VARIABLE_NAME_SUFFIX;

  /**
   * FQCN, with all "." replaced by "$";
   *
   * @return AsyncCrudDaoHandler.class.getName().replace('.', '$');
   */
  public static String fqcnVarName() {
    return AsyncCrudDaoHandler.class.getName().replace('.', '$');
  }


  /*<construction>*/
  //------------------------------------------------------------------


  /**
   * @post new.getDaoVariableName() == null;
   */
  protected AsyncCrudDaoHandler() {
    super(AsyncCrudDao.class, DEFAULT_DAO_VARIABLE_NAME);
  }

  /*</construction>*/



  /*<property name="dao">*/
  //------------------------------------------------------------------

  /**
   * The Data Access Object that will fulfill the request, with a more
   * narrow static type.
   *
   * @return getDao();
   * @throws FatalFacesException
   *         getDao();
   */
  public final AsyncCrudDao getAsyncCrudDao() throws FatalFacesException {
    return (AsyncCrudDao)getDao();
  }

  /*</property>*/

}
