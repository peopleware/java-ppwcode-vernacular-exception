package be.peopleware.struts_III.persistentBean.event;


import java.util.EventObject;

import be.peopleware.persistence_I.PersistentBean;


/**
 * A event object carrying the state of the bean which has been newly created.
 *
 * @author    David Van Keer
 * @author    Peopleware n.v.
 */
public abstract class CommittedEvent extends EventObject {

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
   * Create a new CreatedEvent signalling the creation of a bean.
   *
   * @param     bean
   *            The newly created PersistentBean.
   */
  public CommittedEvent(final PersistentBean bean) {
    super(bean);
  }

  /**
   * @basic
   */
  public PersistentBean getBean() {
    return (PersistentBean)getSource();
  }

}
