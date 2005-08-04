package be.peopleware.jsf_II.persistence;


import java.util.Collection;
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
  public abstract Collection getInstances();

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
  
}
