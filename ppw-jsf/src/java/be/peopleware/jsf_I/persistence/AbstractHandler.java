package be.peopleware.jsf_I.persistence;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.persistence_I.PersistentBean;
import be.peopleware.persistence_I.dao.Dao;


/**
 * Common functionality of JavaServer Faces backing beans that process requests
 * for {@link PersistentBean PersistentBeans}. This provides code for an
 * {@link Dao} and a {@link CrudSecurityStrategy}.
 * These are stateful instances, that should be stored in request scope.
 * 
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 */
public abstract class AbstractHandler {

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

  
  
  private static final Log LOG = LogFactory.getLog(AbstractHandler.class);

  
  
  /*<property name="dao">*/
  //------------------------------------------------------------------

  /**
   * The Data Access Object that will fulfill the request.
   * 
   * @basic
   */
  public final Dao getDao() {
    return $dao;
  }
  
  /**
   * Set the Data Access Object that will fulfill the request.
   * 
   * @post getDao() == dao;
   */
  public final void setDao(Dao dao) {
    // MUDO (jand) correct type
    LOG.debug("Dao set: " + this + ".setDao(" + dao + ")");
    $dao = dao;
  }
  
  /**
   * The DAO that will fullfil this request.
   */
  private Dao $dao;
  
  /*</property>*/

  
  
  /*<property name="securityStrategy">*/
  //------------------------------------------------------------------

  /**
   * @basic
   * @init null;
   */
  public final CrudSecurityStrategy getSecurityStrategy() {
    return $securityStrategy;
  }

  /**
   * @post new.getSecurityStragegy() == securityStrategy;
   */
  public final void setSecurityStrategy(final CrudSecurityStrategy securityStrategy) {
    LOG.debug("CrudSecurityStrategy set: "
              + ((securityStrategy == null) ? "null" : securityStrategy.toString()));
    $securityStrategy = securityStrategy;
  }

  private CrudSecurityStrategy $securityStrategy;
  
  /*</property>*/

  
  

  /**
   * Forward code for successful processing
   * <strong>= {@value}</strong>.
   */
  public final static String FORWARD_SUCCESS = "success"; 
  
}
