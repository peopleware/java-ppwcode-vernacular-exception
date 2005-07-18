package be.peopleware.jsf_II.persistence;


import java.util.Collections;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.exception_I.TechnicalException;
import be.peopleware.persistence_I.PersistentBean;
import be.peopleware.persistence_I.dao.AsyncCrudDao;


/**
 * JSF action that retrieves all instances of a given subtype of
 * {@link PersistentBean} from persistent storage.
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 */
public class RetrieveAllHandler extends AbstractHandler {

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



  private static final Log LOG = LogFactory.getLog(RetrieveAllHandler.class);


  // @question: is het een invariant dat deze handler naar een AsyncCrudDao wijst?
  public final AsyncCrudDao getAsyncCrudDao() {
    return (AsyncCrudDao)getDao();
  }



  /*<property name="type">*/
  //------------------------------------------------------------------

  /**
   * The type of the {@link PersistentBean} that will be handled
   * by the requests.
   *
   * @basic
   * @init null;
   */
  public final Class getType() {
    return $type;
  }

  /**
   * Set the type of the {@link PersistentBean} that will be handled
   * by the requests.
   *
   * @post getType() == type;
   */
  public final void setType(Class type) {
    $type = type;
  }

  /**
   * The type of the {@link PersistentBean} that will be handled
   * by the requests.
   */
  private Class $type;

  /*</property>*/



  /*<property name="includeSubtypes">*/
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



  /*<property name="persistentBeans">*/
  //------------------------------------------------------------------

  /**
   * The resulting set of {@link PersistentBean PersistentBeans},
   * retrieved from persistent storage, that contains all the instances
   * in the storage with type {@link #getType()}. If {@link #display()} is not
   * executed, this method returns <code>null</code>.
   *
   * @basic
   * @init null;
   */
  public final Set getPersistentBeans() {
    return ($persistentBeans == null)
              ? null
              : Collections.unmodifiableSet($persistentBeans);
  }

  private Set $persistentBeans;

  /*</property>*/



  /*<property name="createable">*/
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
   * @param     creatable
   *            Marks the persistent bean as a object that can be created.
   * @post      new.isCreateable() == createable;
   */
  private void setCreateable(final boolean createable) {
    $createable = createable;
  }

  private boolean $createable;

  /*</property>*/


  /**
   * Retrieve the {@link PersistentBean} instances of type
   * {@link #getType()}. If retrieval is successful, forward to
   * {@link #FORWARD_SUCCESS}. If no instance of type {@link #getType()}
   * was found, the empty set is returned.
   *
   * @return FORWARD_SUCCESS.equals(result);
   * @throws TechnicalException
   *         ; Could not create an AsyncCrudDao
   */
  public final String display() throws TechnicalException {
    LOG.debug("looking for all PersistentBeans of type "
              + ((getType() == null) ? "null" : getType().getName()));
    // MUDO (jand) security
    Class clazz = null;
    AsyncCrudDao asyncCRUD = getAsyncCrudDao();
    clazz = getType();
    LOG.debug("retrieving all " + clazz.getName() + " instances");
    $persistentBeans =  asyncCRUD.retrieveAllPersistentBeans(clazz, isSubtypesIncluded());
      // we are not doing this in a transaction, deliberately
      // @todo (jand): think more about this
    LOG.debug("retrieve action succeeded");
    setCreateable(true); // MUDO (jand) security
    return getType().getName();
  }

}
