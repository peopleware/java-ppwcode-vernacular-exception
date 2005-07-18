package be.peopleware.jsf_II.persistence;


import javax.servlet.http.HttpServletRequest;


/**
 * @MUDO (jand): AANPASSEN
 *
 * Security strategy API for {@link PersistentBeanCrudHandler AsyncCrudHandlers}.
 * AsyncCrudHandlers enable the user to
 * get a page for a new object, to get a page to create an object,
 * and to view, edit, update, delete an existing object, and to
 * enable the user the get a list of all objects of a given type.
 * This API provides a veto-method for each of these 7 user requests.
 * This class has a default implementation for each of these methods
 * that does nothing, and does not throw an exception.
 * Subclass implementation are not allowed to do anything. They can
 * only decide to return <code>true</code> or <code>false</code>.
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 * 
 * @mudo (jand) not clear what is the best parameter; bidir binding?
 * @mudo (jand) not in the handler, in the Dao!
 */
public class CrudSecurityStrategy {

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


  public boolean hasDisplayRigths(final HttpServletRequest request) {
    return true;
  }

  public boolean hasEditRights(final HttpServletRequest request) {
    return true;
  }

  public boolean hasUpdateRights(final HttpServletRequest request) {
    return true;
  }

  public boolean hasNewRights(final HttpServletRequest request) {
    return true;
   }

  public boolean hasCreateRights(final HttpServletRequest request) {
    return true;
  }

  public boolean hasDeleteRights(final HttpServletRequest request) {
    return true;
  }

  public boolean hasDisplayAllRights(final HttpServletRequest request) {
    return true;
  }

}

