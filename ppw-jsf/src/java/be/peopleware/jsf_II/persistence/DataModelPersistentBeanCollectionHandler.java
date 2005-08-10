package be.peopleware.jsf_II.persistence;


import java.util.ArrayList;
import java.util.Comparator;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import be.peopleware.jsf_II.FatalFacesException;
import be.peopleware.persistence_I.PersistentBean;


/**
 * JSF action that retrieves has a set of {@link PersistentBean} and makes them
 * available in the associatied Handlers in the form of a {@link DataModel}
 *
 * @author     David Van Keer
 * @author     Peopleware n.v.
 */
public class DataModelPersistentBeanCollectionHandler extends PersistentBeanCollectionHandler{

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



  private static final Log LOG = LogFactory.getLog(DataModelPersistentBeanCollectionHandler.class);


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
    LOG.debug("request for datamodel");
    if ($dataModel == null) {
      LOG.debug("no datamodel cached; creating new datamodel");
      $dataModel = new ListDataModel(new ArrayList(getInstanceHandlers()));
      LOG.debug("datamodel created and cached");
    }
    else {
      LOG.debug("returning cached datamodel");
    }
    return $dataModel;
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
    if (getDataModel() != null && getDataModel().getRowCount() > 0) {
      result = (PersistentBeanCrudHandler)getDataModel().getRowData();
    }
    return result;
  }

  /**
   * <strong>= {@value}</strong>
   */
  public static final String HANDLER_VARNAME_SUFFIX = "_datamodel";

  /**
   * @invar RESOLVER.getHandlerDefaultClass() == DataModelPersistentBeanCollectionHandler.class;
   * @invar RESOLVER.getHandlerVarNameSuffix().equals(HANDLER_VARNAME_SUFFIX);
   */
  public final static PersistentBeanHandlerResolver RESOLVER =
      new PersistentBeanHandlerResolver(DataModelPersistentBeanCollectionHandler.class,
                                        HANDLER_VARNAME_SUFFIX);

}
