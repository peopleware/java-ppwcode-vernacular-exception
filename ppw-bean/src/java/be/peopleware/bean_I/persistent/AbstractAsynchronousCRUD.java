package be.peopleware.bean_I.persistent;


/**
 * Abstract implementation of a number of methods of
 * interface {@link AsynchronousCRUD}.
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 */
public abstract class AbstractAsynchronousCRUD
    implements AsynchronousCRUD {

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



  public final boolean isInTransaction() {
    return $isInTransaction;
  }

  protected final void setInTransaction(final boolean inTransaction) {
    $isInTransaction = inTransaction;
  }

  private boolean $isInTransaction;

}
