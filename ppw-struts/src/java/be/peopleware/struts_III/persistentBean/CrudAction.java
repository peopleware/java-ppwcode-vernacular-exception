package be.peopleware.struts_III.persistentBean;


import javax.servlet.ServletRequest;
import org.apache.struts.action.Action;

import be.peopleware.persistence_I.dao.AsyncCrudDao;
import be.peopleware.exception_I.TechnicalException;


/**
 * Abstract superclass for PersistentBean actions. Persistent
 * bean actions require a fresh {@link AsyncCrudDao}
 * instance for each request.
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 */
public abstract class CrudAction extends Action {

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

  /**
   * Create a fresh instance of {@link AsyncCrudDao}. The
   * instance will be returned when it is no longer needed
   * through {@link #releaseAsynchronousCRUD(AsyncCrudDao)}.
   *
   * @pre       request != null;
   * @param     request
   *            Servlet request information.
   * @result    result != null;
   * @throws    TechnicalException
   *            A fatal error from which recovery is not possible.
   */
  protected abstract AsyncCrudDao createAsynchronousCRUD(
      final ServletRequest request)
          throws TechnicalException;

  /**
   * Release an instance of {@link AsyncCrudDao}, created
   * by {@link #createAsynchronousCRUD(ServletRequest)}.
   *
   * @param     asyncCRUD
   *            The AsyncCrudDao instance to be released.
   * @pre       asyncCRUD != null;
   */
  protected abstract void releaseAsynchronousCRUD(
      final AsyncCrudDao asyncCRUD);


  /**
   * @basic
   */
  public final CrudActionSecurityStrategy getSecurityStrategy() {
    return $securityStrategy;
  }

  /**
   * @post new.getSecurityStragegy() == securityStrategy;
   */
  public final void setSecurityStrategy(
      final CrudActionSecurityStrategy securityStrategy) {
    $securityStrategy = securityStrategy;
  }

  private CrudActionSecurityStrategy $securityStrategy;

}

