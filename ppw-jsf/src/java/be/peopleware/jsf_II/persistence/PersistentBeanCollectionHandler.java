package be.peopleware.jsf_II.persistence;


import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.persistence_I.PersistentBean;


/**
 * Handler for instances of a {@link Collection}.
 *
 * @author    David Van keer
 * @author    Jan Dockx
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



  static final Log LOG = LogFactory.getLog(PersistentBeanCollectionHandler.class);


  /*<property name="instances">*/
  //------------------------------------------------------------------

  /**
   * Return the collection of {@link PersistentBean PersistentBeans},
   * which was set by means of setInstances(Set) .
   *
   * @basic
   * @init null;
   */
  public final Collection getInstances() {
    return ($persistentBeans == null)
              ? null
              : Collections.unmodifiableCollection($persistentBeans);
  }

  /**
   * Set a {@link Set} containing {@link PersistentBean PersistentBeans} as
   * the instances of this Handler.
   *
   * @post    getInstances().containsAll(instances);
   */
  public final void setInstances(final Collection instances) {
    // MUDO (dvankeer): Check if all entries in Insstances are persistent beans?
    LOG.debug("Setting " + instances + " as instances");
    $persistentBeans = instances;
  }

  private Collection $persistentBeans;

}
