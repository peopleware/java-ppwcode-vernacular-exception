package be.peopleware.test_I.java.lang;


import be.peopleware.test_I.CaseProvider;
import java.util.Set;
import java.util.HashSet;


/**
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
public class _Test_Double extends CaseProvider {

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


  public Set getCases() {
    Set result = new HashSet();
    result.add(new Double(0));
    result.add(new Double(1));
    result.add(new Double(5.50));
    result.add(new Double(45.99));
    result.add(new Double(511));
    result.add(new Double(512));
    result.add(new Double(513));
    result.add(new Double(1023.7456));
    result.add(new Double(1024));
    result.add(new Double(1025));
    result.add(new Double(-Integer.MIN_VALUE));
    result.add(new Double(Integer.MAX_VALUE));
    result.add(new Double(4 * Integer.MAX_VALUE));
    result.add(new Double(10 * Integer.MAX_VALUE));
    result.add(new Double(100000 * Integer.MAX_VALUE));
    result.add(new Double(Double.MAX_VALUE / 4));
    result.add(new Double(Double.MAX_VALUE / 2));
    result.add(new Double(Double.MAX_VALUE));
    result.add(new Double(-1));
    result.add(new Double(-550));
    result.add(new Double(-45.99));
    result.add(new Double(-511));
    result.add(new Double(-512));
    result.add(new Double(-513));
    result.add(new Double(-1023.7456));
    result.add(new Double(-1024));
    result.add(new Double(-1025));
    result.add(new Double(Integer.MIN_VALUE));
    result.add(new Double(Integer.MAX_VALUE - 1));
    result.add(new Double(Integer.MIN_VALUE + 1));
    result.add(new Double(4 * Integer.MIN_VALUE));
    result.add(new Double(10 * Integer.MIN_VALUE));
    result.add(new Double(100000 * Integer.MIN_VALUE));
    result.add(new Double(Double.MIN_VALUE / 4));
    result.add(new Double(Double.MIN_VALUE / 2));
    result.add(new Double(Double.MIN_VALUE));
    result.add(new Double(Double.MAX_VALUE - 1));
    result.add(new Double(Double.MIN_VALUE + 1));
    return result;
  }

  public Set getSomeCases() {
    Set result = new HashSet();
    result.add(new Double(0));
    result.add(new Double(1));
    result.add(new Double(Double.MAX_VALUE));
    result.add(new Double(-1));
    result.add(new Double(Double.MIN_VALUE));
    result.add(new Double(Double.MAX_VALUE - 1));
    result.add(new Double(Double.MIN_VALUE + 1));
    return result;
  }

}
