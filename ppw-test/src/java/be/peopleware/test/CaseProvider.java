/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/


package be.peopleware.test;


import java.util.Set;


/**
 * Subtypes are created to provide cases of a type, for use
 * by the tests of other types.
 *
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


  /**
   * Cases of the type this provides for, to use by tests of other types.
   */
  protected abstract Set getCases();

  /**
   * Cases of the type this provides for, to use by tests of other types.
   * This includes <code>null</code>.
   *
   * @return getCases().add(null);
   */
  public final Set getCasesWithNull() {
    Set result = getCases();
    result.add(null);
    return result;
  }

  /**
   * limited set of cases of the type this provides for, to use by tests
   * of other types.
   * This includes <code>null</code>.
   *
   * @return getSomeCases().add(null);
   */
  public final Set getSomeCasesWithNull() {
    Set result = getSomeCases();
    result.add(null);
    return result;
  }

  /**
   * Limited set of cases of the type this provides for, to use by
   * tests of other types. By default, this returns {@link #getCases()}.
   */
  protected Set getSomeCases() {
    return getCases();
  }

}
