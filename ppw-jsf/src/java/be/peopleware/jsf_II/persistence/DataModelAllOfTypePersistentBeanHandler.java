package be.peopleware.jsf_II.persistence;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import be.peopleware.jsf_II.FatalFacesException;
import be.peopleware.jsf_II.RobustCurrent;
import be.peopleware.persistence_I.PersistentBean;


/**
 * JSF action that retrieves all instances of a given subtype of {@link PersistentBean}
 * from persistent storage and makes them available in the associatied Handlers in the
 * form of a {@link DataModel}
 *
 * @author     David Van Keer
 * @author     Peopleware n.v.
 */
public class DataModelAllOfTypePersistentBeanHandler extends AllOfTypePersistentBeanHandler {

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



  private static final Log LOG = LogFactory.getLog(DataModelAllOfTypePersistentBeanHandler.class);


  /*<property name="comparator">*/
  //------------------------------------------------------------------

  /**
   * Return the comparator which is/can be used for sorting the
   * retrieved {@link PersistentBean}'s
   */
  public Comparator getComparator() {
    return $comparator;
  }

  /**
   * Set a comparator for sorting the retrieved {@link PersistentBean}'s
   *
   * @param     comparator
   *            The new Comparator to use for sorting
   */
  public void setComparator(final Comparator comparator) {
    $comparator = comparator;
  }

  private Comparator $comparator;

  /*</property>*/



  /*<property name="dataModel">*/
  //------------------------------------------------------------------

  /**
   * Returns a DataModel containing all peristentBeans wrapped in the associated Handler.
   *
   * @return    DataModel
   *            A datamodel with PersistentBeanHandlers.
   */
  public DataModel getDataModel() throws FatalFacesException {
    if ($dataModel == null) {
      LOG.debug("no datamodel cached; creating new datamodel");
      List handlers = new ArrayList();
      List beans = new ArrayList(getInstances());
      LOG.debug("retrieved instances");
      if (getComparator() != null) {
        Collections.sort(beans, getComparator());
      }
      LOG.debug("creating handler for each instance");
      Iterator iter = beans.iterator();
      while (iter.hasNext()) {
        PersistentBean bean = (PersistentBean)iter.next();
        LOG.debug("    instance is " + bean);
        PersistentBeanCrudHandler handler = createInstanceHandler(bean);
        handler.setViewMode(PersistentBeanCrudHandler.VIEWMODE_DISPLAY);
        LOG.debug("    handler is " + handler);
        handlers.add(handler);
      }
      $dataModel = new ListDataModel(handlers);
      LOG.debug("datamodel created and cached");
    }
    else {
      LOG.debug("returning cached datamodel");
    }
    return $dataModel;
  }

  private final static String HANDLER_PACKAGE_EXTENSION = ".web.jsf";

  private final static String HANDLER_TYPE_SUFFIX = "Handler";

  /**
   * @mudo (jand) hunt for a handler type; use superclasses of bean if not found directly;
   *       use PersistentBeanCrudHandler if not found still.
   */
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
      RobustCurrent.fatalProblem("Class not found for " + handlerClassName, cnfExc, LOG);
    }
    LOG.debug("handler class loaded: " + result);
    return result;
  }

  private PersistentBeanCrudHandler createInstanceHandler(Class handlerClass) throws FatalFacesException {
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

  private PersistentBeanCrudHandler createInstanceHandler(PersistentBean pb) throws FatalFacesException {
    LOG.debug("creating handler for " + pb);
    Class pbType = pb.getClass();
    PersistentBeanCrudHandler result = createInstanceHandler(handlerClassFor(pbType));
    result.setDao(getDao());
    result.setType(pbType);
    result.setInstance(pb);
    return result;
  }

  private DataModel $dataModel;

  /*</property>*/
  
  /**
   * Return the currently selected row of the DataModel or null if no row is selected or
   * available.
   * 
   * @post    (getDataModel().getRowcount() > 0) ? getDataModel().getRowData()
   *                                             : null 
   */
  public PersistentBeanCrudHandler getSelected() {
    PersistentBeanCrudHandler result = null;
    if (getDataModel().getRowCount() > 0) {
      result = (PersistentBeanCrudHandler)getDataModel().getRowData();
    }
    return result;
  }

}
