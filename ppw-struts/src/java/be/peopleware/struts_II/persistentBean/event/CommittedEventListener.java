package be.peopleware.struts_II.persistentBean.event;


import java.util.EventListener;


/**
 * PersistentBean event listener interface, all persisent bean event listeners
 * must implement this interface.
 *
 * @author    David Van Keer
 * @author    Peopleware n.v.
 */
public interface CommittedEventListener extends EventListener {

  /*<section name="Meta Information">*/
  //------------------------------------------------------------------

  /** {@value} */
  String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  String CVS_TAG = "$Name$"; //$NON-NLS-1$

  /*</section>*/

  /**
   * Fire the PersistentBean event for processing.
   *
   * @param     cEvent
   *            The PersistenBeanEvent to handle.
   */
  void committed(final CommittedEvent cEvent);

}
