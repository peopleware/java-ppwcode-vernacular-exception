package be.peopleware.jsf_II.persistence;


import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import be.peopleware.jsf_II.FatalFacesException;
import be.peopleware.jsf_II.RobustCurrent;
import be.peopleware.persistence_I.PersistentBean;


/**
 * Common functionality of JavaServer Faces backing beans that process requests
 * for a list of {@link PersistentBean PersistentBeans}.
 * 
 * @author     David Van Keer
 * @author     Peopleware n.v.
 * 
 * @mudo (jand) this must be adapted to be more then display; also edit-in-grid
 */
public abstract class AbstractPersistentBeanListHandler extends AbstractPersistentBeanHandler {

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


  private static final Log LOG = LogFactory.getLog(AbstractPersistentBeanListHandler.class);


  /*<property name="instances">*/
  //------------------------------------------------------------------
  
  /**
   * The set of {@link PersistentBean PersistentBeans},
   * retrieved from persistent storage, that contains all the instances
   * in the storage with type {@link #getType()}. If no data is found,
   * this method returns <code>null</code>.
   *
   * @basic
   * @init null;
   */
  public abstract Set getInstances();
  
  /*</property>*/



  /*<property name="creatable">*/
  //------------------------------------------------------------------
  
  /**
   * Can we create a new object of the type represented by this form?
   *
   * @basic
   * @init      false;
   */
  public final boolean isCreateable() {
    return $createable;
  }

  /**
   * @param     createable
   *            Marks the persistent bean as a object that can be created.
   * @post      new.isCreateable() == createable;
   */
  protected void setCreateable(final boolean createable) {
    $createable = createable;
  }

  private boolean $createable;
  
  /*</property>*/
  
  

  private final static String HANDLER_PACKAGE_EXTENSION = ".web.jsf";

  private final static String HANDLER_TYPE_SUFFIX = "Handler";

  
  private Class handlerClassFor(Class pbType) throws FatalFacesException {
    assert PersistentBean.class.isAssignableFrom(pbType);
    LOG.debug("looking for handler for instances of type " + pbType);
    Package pbPackage = pbType.getPackage();
    String handlerPackageName = pbPackage.getName() + HANDLER_PACKAGE_EXTENSION;
    String[] pbTypeNameParts = pbType.getName().split("\\.");
    LOG.debug("parts of type name: " + pbTypeNameParts);
    String simplePbClassName = pbTypeNameParts[pbTypeNameParts.length - 1];
    String handlerClassName = handlerPackageName + "." + simplePbClassName + HANDLER_TYPE_SUFFIX;
    LOG.debug("name of handler class we will try to load: " + handlerClassName);
    Class result = null;
    try {
      result  = Class.forName(handlerClassName);
    }
    catch (ClassNotFoundException cnfExc) {
      // try to get a handler for the supertype, if this is not PersistentBean itself
      LOG.debug("no handler found with name \"" + handlerClassName + "\" for class \"" +
                pbType + "\"");
      Class superClass = pbType.getSuperclass();
      if (superClass !=  PersistentBean.class) {
        LOG.debug("trying to load handler for superclass \"" + superClass + "\"");
        return handlerClassFor(superClass);
      }
      else {
        LOG.debug("superclass is \"" + PersistentBean.class + "\"; " +
                  "will use \"" + PersistentBeanCrudHandler.class + "\" as handler type");
        assert superClass == PersistentBean.class;
        return PersistentBeanCrudHandler.class;
      }
    }
    LOG.debug("handler class loaded: " + result);
    return result;
  }

  private PersistentBeanCrudHandler createInstanceHandler(Class handlerClass)
      throws FatalFacesException {
    assert PersistentBeanCrudHandler.class.isAssignableFrom(handlerClass);
    PersistentBeanCrudHandler handler = null;
    try {
      handler = (PersistentBeanCrudHandler)handlerClass.newInstance();
      LOG.debug("created new handler: " + handler);
    }
    catch (InstantiationException iExc) {
      RobustCurrent.fatalProblem("Failed to instantiate a handler of type " + handlerClass,
                                 iExc,
                                 LOG);
    }
    catch (IllegalAccessException iaExc) {
      RobustCurrent.fatalProblem("Failed to instantiate a handler of type " + handlerClass,
                                 iaExc,
                                 LOG);
    }
    return handler;
  }

  /**
   * Return a {@link PersistentBeanCrudHandler} bases on the given (@link PersistentBean}
   * 
   * @param     pb
   *            The PersistentBean for which to find a Handler
   * @return    PersistentBeanCrudHandler
   *            The Handler beloning to the given PersistentBean
   * @throws    FatalFacesException
   *            Thrown when no handler could be found for the given PersistentBean
   */
  protected PersistentBeanCrudHandler createInstanceHandler(final PersistentBean pb)
      throws FatalFacesException {
    LOG.debug("creating handler for " + pb);
    Class pbType = pb.getClass();
    PersistentBeanCrudHandler result = createInstanceHandler(handlerClassFor(pbType));
    result.setDao(getDao());
    result.setType(pbType);
    result.setInstance(pb);
    return result;
  }

  
}
