package be.peopleware.struts_III.persistentBean.event;


import be.peopleware.persistence_I.PersistentBean;


/**
 * A event object carrying the former state of a bean which has been deleted.
 *
 * @author    David Van Keer
 * @author    Peopleware n.v.
 */
public class DeletedEvent extends CommittedEvent {

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
   * Create a new DeletedEvent signalling a deletion of a bean.
   *
   * @param     bean
   *            The deleted PersistentBean.
   */
  public DeletedEvent(final PersistentBean bean) {
    super(bean);
  }

}
