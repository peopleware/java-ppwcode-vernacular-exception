package be.peopleware.struts_II.persistentBean.event;


import be.peopleware.bean_I.persistent.PersistentBean;


/**
 * A event object carrying the old and new state of a bean which have been
 * altered.
 *
 * @author    David Van Keer
 * @author    Peopleware n.v.
 */
public class UpdatedEvent extends CommittedEvent {

  /*<section name="Meta Information">*/
  //------------------------------------------------------------------

  /** {@value} */
  public static final String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_TAG = "$Name$"; //$NON-NLS-1$

  /*</section>*/

  /**
   * Create a new PersistentBeanChangeEvent signalling a change in a existing
   * bean.
   *
   * @param     newBean
   *            A PersistentBean containing the new data of the bean which has
   *            been manipulated.
   * @param     oldBeanString
   *            A String representing the old data of the bean which has
   *            been manipulated.
   */
  public UpdatedEvent(final PersistentBean newBean,
                                   final String oldBeanString) {
    super(newBean);
    $oldBeanString = oldBeanString;
  }

  private String $oldBeanString;

  /**
   * @basic
   */
  public PersistentBean getNewBean() {
    return getBean();
  }

  /**
   * @basic
   */
  public String getOldBeanString() {
    return $oldBeanString;
  }

}
