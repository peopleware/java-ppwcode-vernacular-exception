package be.peopleware.struts_I.persistentBean.hibernate;


import javax.servlet.ServletRequest;
import be.peopleware.bean_I.persistent.AsynchronousCRUD;
import be.peopleware.bean_I.persistent.hibernate.AsynchronousHibernateCRUD;
import be.peopleware.exception_I.TechnicalException;
import be.peopleware.servlet_I.hibernate.SessionInView;


/**
 * An action for CRUD functionality with Hibernate.
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public class DetailAction
    extends be.peopleware.struts_I.persistentBean.DetailAction {

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



  /*<construction>*/
  //------------------------------------------------------------------

  // default constructor

  /*</construction>*/

  /* @idea (jand): this is 100% the same code as in ListAction;
   *               generalize into separate factory object
   */

  protected final AsynchronousCRUD createAsynchronousCRUD(
      final ServletRequest request)
          throws TechnicalException {
    AsynchronousHibernateCRUD asyncCRUD
        = new AsynchronousHibernateCRUD();
    try {
      asyncCRUD.setSession(SessionInView.getSession(request));
    }
    catch (IllegalStateException isExc) {
      assert false
             : "IllegalStateException; should not happen, " //$NON-NLS-1$
                + "asyncCRUD is new"; //$NON-NLS-1$
    }
        // IllegalStateException; should not happen, this is new
    return asyncCRUD;
  }

  protected final void releaseAsynchronousCRUD(
      final AsynchronousCRUD asyncCRUD) {
    ((AsynchronousHibernateCRUD)asyncCRUD).setSession(null);
  }

}
