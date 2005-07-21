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
import be.peopleware.jsf_II.RobustCurrent;
import be.peopleware.persistence_I.PersistentBean;


/**
 * TODO (dvankeer): (JAVADOC) Write class description.
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

  private String handlerSubPackageName = ".web.jsf";


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
   * TODO: Write better docs.
   *
   * Wrap all the retrieve beans in their respective Handler and add them into a ListDataModel.
   *
   * @return    DataModel
   *            ...
   */
  public DataModel getDataModel() {
    if ($dataModel == null) {
      List handlers = new ArrayList();
      List beans = new ArrayList(getInstances());
      if (getComparator() != null) {
        Collections.sort(beans, getComparator());
      }

      Class clazz = getType();
      String beanPackage = clazz.getPackage().getName();
      String handlerPackage = beanPackage + handlerSubPackageName;
      String className = clazz.getName();
      className.replaceAll(beanPackage, handlerPackage);


      Iterator iter = beans.iterator();
      while (iter.hasNext()) {
        PersistentBean bean = (PersistentBean)iter.next();

        PersistentBeanCrudHandler handler = null;
        try {
          Class classDefinition = Class.forName(className);
          handler = (PersistentBeanCrudHandler)classDefinition.newInstance();
        }
        catch (InstantiationException iExc) {
          RobustCurrent.fatalProblem("Failed to instantiate a handler for " + className,
                                     iExc,
                                     LOG);
        }
        catch (IllegalAccessException iaExc) {
          RobustCurrent.fatalProblem("Encounterd an IllegalAccessException while trying to "
                                     + "instantiate a handler for " + className,
                                     iaExc,
                                     LOG);
        }
        catch (ClassNotFoundException cnfExc) {
          RobustCurrent.fatalProblem("Class not found for ",
                                     cnfExc,
                                     LOG);
        }
        handler.setId(bean.getId());
        handlers.add(handler);
      }
      $dataModel = new ListDataModel(handlers);
    }
    return $dataModel;
  }

  private DataModel $dataModel;

  /*</property>*/

}
