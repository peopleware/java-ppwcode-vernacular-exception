package be.peopleware.value_I;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.toryt.TorytException;
import org.toryt.support.straightlist.ArrayStraightList;
import org.toryt.support.straightlist.NullFirstStraightList;
import org.toryt.support.straightlist.StraightList;

import be.peopleware.bean_IV.PropertyException;


/**
 * <p>Static methods to retrieve TestObjectList instances.</p>
 *
 * @author    Nele Smeets
 * @author    PeopleWare n.v.
 */
public final class Cases {

  /*<section name="Meta Information">*/
  //------------------------------------------------------------------

  /** {@value} */
  public static final String CVS_REVISION = "$Revision$";
  /** {@value} */
  public static final String CVS_DATE = "$Date$";
  /** {@value} */
  public static final String CVS_STATE = "$State$";
  /** {@value} */
  public static final String CVS_TAG = "$Name$";

  /*</section>*/



  public static void loadClass() {
    // NOP, something to call to load the class
  }

 //  NationalNumber
  private final static StraightList TOL_BE_PEOPLEWARE_VALUE_I_NATIONAL_NUMBER;
  static {
    NationalNumber[] nn = new NationalNumber[4];
    try {
      nn[0] = new NationalNumber("000000", "000", "97");
      nn[1] = new NationalNumber("781101", "456", "65");
      nn[2] = new NationalNumber("700509", "169", "96");
      nn[3] = new NationalNumber("760905", "145", "06");
    }
    catch(PropertyException exc) {
      assert false : "Shouldn't happen";
    }
    TOL_BE_PEOPLEWARE_VALUE_I_NATIONAL_NUMBER = new ArrayStraightList(nn);
    org.toryt.Cases.addTol(NationalNumber.class, TOL_BE_PEOPLEWARE_VALUE_I_NATIONAL_NUMBER);
  }

  //  Period
  private final static StraightList TOL_BE_PEOPLEWARE_VALUE_I_PERIOD;
  static {
    StraightList testValues = null;
    try {
      testValues = new NullFirstStraightList(
                       org.toryt.Cases.findTestObjectList(Date.class)
                   );
    }
    catch (TorytException e) {
      assert false : "Could not load date TOL";
    }
    List periods = new ArrayList();
    Iterator i1 = testValues.iterator();
    while (i1.hasNext()) {
      Date date1 = (Date) i1.next();
      Iterator i2 = testValues.iterator();
      while (i2.hasNext()) {
        Date date2 = (Date) i2.next();
        Period period = new Period();
        try {
          if (
              date1 == null ||
              date2 == null ||
              ( date1 != null && date2 != null && date1.before(date2) )
           ) {
            period.setStartDate(date1);
            period.setEndDate(date2);
            periods.add(period);
          }
          else if (date1 != null && date2 != null && date2.before(date1)) {
            period.setStartDate(date2);
            period.setEndDate(date1);
            periods.add(period);
          }
        }
        catch(InvalidPeriodException exc) {
          assert false : "Shouldn't happen";
        }
      }
    }
    Period[] array = new Period[periods.size()];
    for(int i = 0;i<array.length;i++) {
      array[i] = (Period)periods.get(i);
    }
    TOL_BE_PEOPLEWARE_VALUE_I_PERIOD = new ArrayStraightList(array);
    org.toryt.Cases.addTol(Period.class, TOL_BE_PEOPLEWARE_VALUE_I_PERIOD);
  }

}
