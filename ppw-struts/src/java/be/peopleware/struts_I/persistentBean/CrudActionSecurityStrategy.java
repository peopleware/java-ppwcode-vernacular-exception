package be.peopleware.struts_I.persistentBean;


import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import be.peopleware.struts_I.IllegalAccessException;

/**
 * Security strategy API for {@link CrudAction}s.
 * {@link DetailAction} enables the user to
 * get a page for a new object, to get a page to create an object,
 * and to view, edit, update, delete an existing object. {@link ListAction}
 * enables the user the get a list of all objects of a given type.
 * This API provides a veto-method for each of these 7 user requests.
 * This class has a default implementation for each of these methods
 * that does nothing, and does not throw an exception.
 * Subclass implementation are not allowed to do anything. They can
 * only decide whether or not to throw an {@link IllegalAccessException}.
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    Peopleware n.v.
 */
public class CrudActionSecurityStrategy {

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

  
  public void checkDisplayRigths(final HttpServletRequest request,
                          final ActionMapping actionMapping,
                          final ActionForm actionForm)
      throws IllegalAccessException {
    // NOP
  }
  
  public void checkEditRights(final HttpServletRequest request,
                              final ActionMapping actionMapping,
                              final ActionForm actionForm)
      throws IllegalAccessException {
    // NOP
  }
  
  public void checkUpdateRights(final HttpServletRequest request,
                                final ActionMapping actionMapping,
                                final ActionForm actionForm)
      throws IllegalAccessException {
    // NOP
  }
  
  public void checkNewRights(final HttpServletRequest request,
                             final ActionMapping actionMapping,
                             final ActionForm actionForm)
       throws IllegalAccessException {
     // NOP
   }
  
  public void checkCreateRights(final HttpServletRequest request,
                                final ActionMapping actionMapping,
                                final ActionForm actionForm)
      throws IllegalAccessException {
    // NOP
  }
  
  public void checkDeleteRights(final HttpServletRequest request,
                                final ActionMapping actionMapping,
                                final ActionForm actionForm)
      throws IllegalAccessException {
    // NOP
  }

  public void checkDisplayAllRights(final HttpServletRequest request,
                                    final ActionMapping actionMapping,
                                    final ActionForm actionForm)
      throws IllegalAccessException {
    // NOP
  }

}

