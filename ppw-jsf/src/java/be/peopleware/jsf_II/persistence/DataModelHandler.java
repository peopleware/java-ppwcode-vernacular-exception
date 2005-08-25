package be.peopleware.jsf_II.persistence;


import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.jsf_II.FatalFacesException;


/**
 * Handler that presents {@link #getInstances()} as a {@link DataModel}
 * for the JSF <code>dataTable</code> tag.
 *
 * @author     David Van Keer
 * @author     Jan Dockx
 * @author     Peopleware n.v.
 */
public class DataModelHandler extends CollectionHandler {

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



  private static final Log LOG = LogFactory.getLog(DataModelHandler.class);



  /*<property name="dataModel">*/
  //------------------------------------------------------------------

  /**
   * Returns a DataModel containing all peristentBeans wrapped in the associated Handler.
   * This is skimmed when {@link #skim()} is called.
   *
   * @return    DataModel
   *            A datamodel with PersistentBeanHandlers.
   */
  public final DataModel getDataModel() throws FatalFacesException {
    LOG.debug("request for datamodel");
    if ($dataModel == null) {
      LOG.debug("no datamodel cached; creating new datamodel");
      $dataModel = new ListDataModel(getInstanceHandlers());
      LOG.debug("datamodel created and cached");
    }
    else {
      LOG.debug("returning cached datamodel");
    }
    return $dataModel;
  }

  private transient DataModel $dataModel;

  /*</property>*/

  /**
   * Return the currently selected row of the DataModel or null if no row is selected or
   * available.
   *
   * @post    (getDataModel().getRowcount() > 0) ? getDataModel().getRowData()
   *                                             : null
   */
  public final InstanceHandler getSelected() {
    InstanceHandler result = null;
    if (getDataModel().getRowCount() > 0) {
      result = (InstanceHandler)getDataModel().getRowData();
    }
    return result;
  }

  public void skim() {
    super.skim();
    LOG.debug("datamodel is skimmed");
    $dataModel = null;
  }

}
