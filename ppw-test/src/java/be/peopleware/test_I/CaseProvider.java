package be.peopleware.test_I;


import java.util.Set;


/**
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public abstract class CaseProvider {

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


  public final Set getCasesWithNull() {
    Set result = getCases();
    result.add(null);
    return result;
  }

  protected abstract Set getCases();

  public final Set getSomeCasesWithNull() {
    Set result = getSomeCases();
    result.add(null);
    return result;
  }

  protected Set getSomeCases() {
    return getCases();
  }
}
