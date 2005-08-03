package be.peopleware.jsf_II.persistence;


import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.exception_I.TechnicalException;
import be.peopleware.jsf_II.RobustCurrent;
import be.peopleware.persistence_I.PersistentBean;
import be.peopleware.persistence_I.dao.AsyncCrudDao;


/**
 * JSF action that retrieves all instances of a given subtype of
 * {@link PersistentBean} from persistent storage.
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 */
public class AllOfTypePersistentBeanHandler extends AbstractPersistentBeanListHandler {

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



  private static final Log LOG = LogFactory.getLog(AllOfTypePersistentBeanHandler.class);



  /*<property name="subtypesIncluded">*/
  //------------------------------------------------------------------

  /**
   * Whether we will retrieve instances of only {@link #getType()},
   * or also of all subtypes of {@link #getType()}.
   * <strong>Note that the default value is <code>true</code></strong>
   * (where it is <code>false</code> for most boolean properties).
   *
   * @basic
   * @init true;
   */
  public final boolean isSubtypesIncluded() {
    return $subtypesIncluded;
  }

  /**
   * Set whether we will retrieve instances of only {@link #getType()},
   * or also of all subtypes of {@link #getType()}.
   *
   * @post getType() == type;
   */
  public final void setSubtypesIncluded(boolean subtypesIncluded) {
    $subtypesIncluded = subtypesIncluded;
  }

  /**
   * Whether we will retrieve instances of only {@link #getType()},
   * or also of all subtypes of {@link #getType()}.
   */
  private boolean $subtypesIncluded = true;

  /*</property>*/



  /*<property name="instances">*/
  //------------------------------------------------------------------

  /**
   * The resulting set of {@link PersistentBean PersistentBeans},
   * retrieved from persistent storage, that contains all the instances
   * in the storage with type {@link #getType()}. If no data is found,
   * this method returns <code>null</code>.
   *
   * @basic
   * @init null;
   */
  public final Collection getInstances() {
    if ($persistentBeans == null) {
      LOG.debug("Looking for all PersistentBeans of type "
                + ((getType() == null) ? "null" : getType().getName()));
      // MUDO (jand) security
      Class clazz = null;
      AsyncCrudDao asyncCRUD = getDao();
      clazz = getType();
      LOG.debug("Retrieving all " + clazz.getName() + " instances");
      try {
        // we are not doing this in a transaction, deliberately
        $persistentBeans =  asyncCRUD.retrieveAllPersistentBeans(clazz,
                                                                 isSubtypesIncluded());
      }
      catch (TechnicalException tExc) {
        RobustCurrent.fatalProblem("Failed to retrieve all PersistenBeans of type"
                                     + ((getType() == null) ? "null" : getType().getName()),
                                   tExc,
                                   LOG);
      }
      // @todo (jand): think more about this
      LOG.debug("Retrieve action succeeded");
      setCreateable(true); // MUDO (jand) security
      // @question (dvankeer): Why is creatable set to true here, are subclasses not
      // responsibly to set this value themself? And a default value of false would be
      // more appropriate.
    }
    else {
      LOG.debug("returning cached instances");
    }
    return ($persistentBeans == null)
              ? null
              : Collections.unmodifiableCollection($persistentBeans);
  }

  private Set $persistentBeans;

  /*</property>*/

}
