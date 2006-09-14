/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.value_II;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.toryt.TorytException;
import org.toryt.support.straightlist.ArrayStraightList;
import org.toryt.support.straightlist.NullFirstStraightList;
import org.toryt.support.straightlist.StraightList;

import be.peopleware.bean_V.PropertyException;


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

  /**
   * Empty static method, used to activate the loading of this class.
   *
   * @post  true;
   */
  public static void loadClass() {
    // NOP, something to call to load the class
  }

  // EnterpriseNumber
  private static final StraightList TOL_BE_PEOPLEWARE_VALUE_II_ENTERPRISE_NUMBER;

  static {
    EnterpriseNumber[] enrs = new EnterpriseNumber[3];
    try {
      enrs[0] = new EnterpriseNumber("0123-456-749");
      enrs[1] = new EnterpriseNumber("1222-333-424");
      enrs[1] = new EnterpriseNumber("9999-888-762");
   }
    catch (PropertyException e) {
      assert false : "PropertyException should not happen: " + e;
    }
    TOL_BE_PEOPLEWARE_VALUE_II_ENTERPRISE_NUMBER = new ArrayStraightList(enrs);
    org.toryt.Cases.addTol(EnterpriseNumber.class, TOL_BE_PEOPLEWARE_VALUE_II_ENTERPRISE_NUMBER);
  }



  // VATNumber
  private static final StraightList TOL_BE_PEOPLEWARE_VALUE_II_VATNUMBER;

  static {
    VATNumber[] vatnrs = new VATNumber[2];
    try {
      vatnrs[0] = new VATNumber();
      vatnrs[1] = new VATNumber("680334244");
    }
    catch (PropertyException e) {
      assert false : "PropertyException should not happen: " + e;
    }
    TOL_BE_PEOPLEWARE_VALUE_II_VATNUMBER = new ArrayStraightList(vatnrs);
    org.toryt.Cases.addTol(VATNumber.class, TOL_BE_PEOPLEWARE_VALUE_II_VATNUMBER);
  }



  // RSZNumber
  private static final StraightList TOL_BE_PEOPLEWARE_VALUE_II_RSZNUMBER;

  static {
    RSZNumber[] rsznrs = new RSZNumber[2];
    try {
      rsznrs[0] = new RSZNumber();
      rsznrs[1] = new RSZNumber("026178217129");
    }
    catch (PropertyException e) {
      assert false : "PropertyException should not happen: " + e;
    }
    TOL_BE_PEOPLEWARE_VALUE_II_RSZNUMBER = new ArrayStraightList(rsznrs);
    org.toryt.Cases.addTol(RSZNumber.class, TOL_BE_PEOPLEWARE_VALUE_II_RSZNUMBER);
  }



  //  NationalNumber
  private static final StraightList TOL_BE_PEOPLEWARE_VALUE_I_NATIONAL_NUMBER;
  static {
    NationalNumber[] nn = new NationalNumber[4];
    try {
      nn[0] = new NationalNumber("000000", "000", "97");
      nn[1] = new NationalNumber("781101", "456", "65");
      nn[2] = new NationalNumber("700509", "169", "96");
      nn[3] = new NationalNumber("760905", "145", "06");
    }
    catch (PropertyException exc) {
      assert false : "Shouldn't happen";
    }
    TOL_BE_PEOPLEWARE_VALUE_I_NATIONAL_NUMBER = new ArrayStraightList(nn);
    org.toryt.Cases.addTol(NationalNumber.class, TOL_BE_PEOPLEWARE_VALUE_I_NATIONAL_NUMBER);
  }



  //  Period
  private static final StraightList TOL_BE_PEOPLEWARE_VALUE_I_PERIOD;
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
              date1 == null
              || date2 == null
              || (date1 != null && date2 != null && date1.before(date2))
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
        catch (InvalidPeriodException exc) {
          assert false : "Shouldn't happen";
        }
      }
    }
    Period[] array = new Period[periods.size()];
    for (int i = 0; i < array.length; i++) {
      array[i] = (Period)periods.get(i);
    }
    TOL_BE_PEOPLEWARE_VALUE_I_PERIOD = new ArrayStraightList(array);
    org.toryt.Cases.addTol(Period.class, TOL_BE_PEOPLEWARE_VALUE_I_PERIOD);
  }




  //  DayPeriod
  private static final StraightList TOL_BE_PEOPLEWARE_VALUE_II_DAY_PERIOD;
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
        DayPeriod period = new DayPeriod();
        try {
          if (
              date1 == null
              || date2 == null
              || (date1 != null && date2 != null && (! date1.after(date2)))
           ) {
            period.setStartDate(date1);
            period.setEndDate(date2);
          }
          else if (date1 != null && date2 != null && (! date2.after(date1))) {
            period.setStartDate(date2);
            period.setEndDate(date1);
          }
          periods.add(period);
        }
        catch (InvalidPeriodException exc) {
          assert false : "Shouldn't happen";
        }
      }
    }
    DayPeriod[] array = new DayPeriod[periods.size()];
    for (int i = 0; i < array.length; i++) {
      array[i] = (DayPeriod)periods.get(i);
    }
    TOL_BE_PEOPLEWARE_VALUE_II_DAY_PERIOD = new ArrayStraightList(array);
    org.toryt.Cases.addTol(DayPeriod.class, TOL_BE_PEOPLEWARE_VALUE_II_DAY_PERIOD);
  }

}
