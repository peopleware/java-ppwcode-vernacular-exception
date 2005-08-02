package be.peopleware.jsf_II.persistence;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import be.peopleware.persistence_I.PersistentBean;


/**
 * JSF action that retrieves all instances of a given subtype of
 * {@link PersistentBean} from persistent storage.
 *
 * @author    David Van keer
 * @author    Peopleware n.v.
 *
 * @mudo (jand) this must be adapted to be more then display; also edit-in-grid
 */
public class PersistentBeanCollectionHandler extends AbstractPersistentBeanListHandler {

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



  private static final Log LOG = LogFactory.getLog(PersistentBeanCollectionHandler.class);


  /*<property name="instances">*/
  //------------------------------------------------------------------

  /**
   * Return the set of {@link PersistentBean PersistentBeans},
   * which was set by means of setInstances(Set) .
   *
   * @basic
   * @init null;
   */
  public final Set getInstances() {
    return ($persistentBeans == null)
              ? null
              : Collections.unmodifiableSet($persistentBeans);
  }
  
  /**
   * Set a {@link Set} containing {@link PersistentBean PersistentBeans} as
   * the instances of this Handler.
   *
   * @post    getInstances().containsAll(instances);
   */
  public final void setInstances(final Set instances) {
    // MUDO (dvankeer): Check if all entries in Insstances are persistent beans?
    LOG.debug("Setting " + instances + " as instances");
    $persistentBeans = instances;
  }

  private Set $persistentBeans;

  /*</property>*/
  
  /**
   * Returns a Set containing all peristentBeans wrapped in the associated Handler.
   *
   * @return    Set
   *            A Set with PersistentBeanHandlers based on the instances of this Handler..
   */
  public final Set getInstanceHandlers() {
    if ($handlers == null) {
      LOG.debug("no handlers cached; creating new handlers");
      List handlers = new ArrayList();
      List beans = new ArrayList(getInstances());
      LOG.debug("retrieved instances");
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
      $handlers = new HashSet(handlers);
      LOG.debug("handlers created and cached");
    }
    else {
      LOG.debug("returning cached handlers");
    }
    return $handlers;
  }
  
  private Set $handlers;

}
