package be.peopleware.struts_II.persistentBean;


import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import be.peopleware.bean_I.persistent.AsynchronousCRUD;
import be.peopleware.exception_I.TechnicalException;


/**
 * An action for listing functionality with Hibernate.
 *
 * @author    David Van Keer
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public abstract class ListAction
    extends CrudAction {

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



  private static final Log LOG
      = LogFactory.getLog(ListAction.class);

  public ActionForward execute(final ActionMapping actionMapping,
                               final ActionForm form,
                               final HttpServletRequest request,
                               final HttpServletResponse response)
      throws TechnicalException {
    Set results = null;
    Class clazz = null;
    AsynchronousCRUD asyncCRUD = createAsynchronousCRUD(request);
    try {
      clazz = ((CrudDynaActionForm)form).getPersistentBeanType();
      if (LOG.isDebugEnabled()) {
        LOG.debug("retrieving all " //$NON-NLS-1$
                   + clazz.getName()
                   + " instances"); //$NON-NLS-1$
      }
      boolean retrieveSubclasses = Boolean.valueOf(
          ((CrudDynaActionForm)form).getRetrieveSubclasses())
          .booleanValue();
      results =  asyncCRUD.retrieveAllPersistentBeans(clazz,
                                                      retrieveSubclasses);
        // we are not doing this in a transaction, deliberately
        // @todo (jand): think more about this
      if (LOG.isDebugEnabled()) {
        LOG.debug("retrieve action succeeded"); //$NON-NLS-1$
      }
    }
    finally {
      releaseAsynchronousCRUD(asyncCRUD);
    }
    int last = clazz.getName().lastIndexOf("."); //$NON-NLS-1$
    request.setAttribute(clazz.getName().substring(++last) + "List", //$NON-NLS-1$
                         results);
    return actionMapping.findForward("success"); //$NON-NLS-1$
  }

}
