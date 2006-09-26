package be.peopleware.value_II;


import java.util.Date;
import java.util.Map;

import org.toryt.Cases;
import org.toryt.Condition;
import org.toryt.ExceptionCondition;
import org.toryt.TorytException;
import org.toryt.hard.ClassContract;
import org.toryt.hard.ConstructorContract;
import org.toryt.hard.MutatorContract;
import org.toryt.support.straightlist.EmptyStraightList;
import org.toryt.support.straightlist.LazyCombinationStraightList;
import org.toryt.support.straightlist.LazyMappingStraightList;
import org.toryt.support.straightlist.NullFirstStraightList;
import org.toryt.support.straightlist.StraightList;


/**
 * A test class for {@link DayPeriod}.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 *
 * @mudo contract for public int be.peopleware.value_II.DayPeriod.compareTo(java.lang.Object)
 */
public class _Contract_DayPeriod extends ClassContract {

  /*<section name="Meta Information">*/
  //  ------------------------------------------------------------------
  /** {@value} */
  public static final String CVS_REVISION = "$Revision$";
  /** {@value} */
  public static final String CVS_DATE = "$Date$";
  /** {@value} */
  public static final String CVS_STATE = "$State$";
  /** {@value} */
  public static final String CVS_TAG = "$Name$";
  /*</section>*/

  public _Contract_DayPeriod() throws TorytException {
    super(DayPeriod.class);

    // constructors
    addConstructorContract(
      new ConstructorContract(this, DayPeriod.class, "DayPeriod()") {
        {
          // no preconditions
          // postconditions
          addPostcondition(new Condition() {
            public boolean validate(Map context) {
              DayPeriod result = (DayPeriod)context.get(SUBJECT_KEY);
              return result.getStartDate() == null;
            }});
          addPostcondition(new Condition() {
            public boolean validate(Map context) {
              DayPeriod result = (DayPeriod)context.get(SUBJECT_KEY);
              return result.getEndDate() == null;
            }});
          close();
        }

        public StraightList getTestCases() throws TorytException {
          return EmptyStraightList.INSTANCE;
        }

      }
    );
    // instance methods
    addInstanceMethodContract(
        new MutatorContract(
            this, DayPeriod.class,
            "setStartDate(java.util.Date)"
        ) {

      public String[] getFormalParameters() {
        return new String[] {"startDate"};
      }

      {
        // preconditions
        // postconditions
        addPostcondition(new Condition() {
          public boolean validate(Map context) {
            DayPeriod subject = (DayPeriod)context.get(SUBJECT_KEY);
            Date startDate = (Date)context.get("startDate");
            return  (startDate == null)
                       ? subject.getStartDate() == null
                       : subject.getStartDate().equals(DateUtil.dayDate(startDate));
          }});
        addExceptionCondition(new ExceptionCondition(InvalidPeriodException.class) {
          public boolean validate(Map context) {
            DayPeriod subject = (DayPeriod)context.get(SUBJECT_KEY);
            Date startDate = (Date)context.get("startDate");
            InvalidPeriodException pExc = (InvalidPeriodException)context.get(EXCEPTION_KEY);
            return
                (  startDate != null
                   &&
                   subject.getEndDate() != null
                   &&
                   startDate.after(subject.getEndDate())
                )
                && DateUtil.sameDay(pExc.getStartDate(), startDate)
                && DateUtil.sameDay(pExc.getEndDate(), subject.getEndDate())
                &&
                pExc.getMessage()
                     .equals("The given start date is not before the current end date.");
          }});
        close();
      }

      public StraightList getTestCases() throws TorytException {
        return new LazyCombinationStraightList(
            new String[] {SUBJECT_KEY, "startDate"},
            new StraightList[] {
                getCases(),
                new NullFirstStraightList(
                  Cases.findTestObjectList(Date.class)
                )
            });
      }

    });
    addInstanceMethodContract(
        new MutatorContract(
            this, DayPeriod.class,
            "setEndDate(java.util.Date)"
        ) {

      public String[] getFormalParameters() {
        return new String[] {"endDate"};
      }

      {
        // preconditions
        // postconditions
        addPostcondition(new Condition() {
          public boolean validate(Map context) {
            DayPeriod subject = (DayPeriod)context.get(SUBJECT_KEY);
            Date endDate = (Date)context.get("endDate");
            return  (endDate == null)
                       ? subject.getEndDate() == null
                       : subject.getEndDate().equals(DateUtil.dayDate(endDate));
          }});
        addExceptionCondition(new ExceptionCondition(InvalidPeriodException.class) {
          public boolean validate(Map context) {
            DayPeriod subject = (DayPeriod)context.get(SUBJECT_KEY);
            Date endDate = (Date)context.get("endDate");
            InvalidPeriodException pExc = (InvalidPeriodException)context.get(EXCEPTION_KEY);
            return  ( subject.getStartDate() != null
                      && endDate != null
                      && subject.getStartDate().after(endDate)
                    )
                    && DateUtil.sameDay(pExc.getStartDate(), subject.getStartDate())
                    && DateUtil.sameDay(pExc.getEndDate(), endDate)
                    && pExc.getMessage()
                        .equals("The current start date is not before the given end date.");
          }});
        close();
      }

      public StraightList getTestCases() throws TorytException {
        return new LazyCombinationStraightList(
            new String[] {SUBJECT_KEY, "endDate"},
            new StraightList[] {
                getCases(),
                new NullFirstStraightList(
                  Cases.findTestObjectList(Date.class)
                )
            });
      }

    });
    addInstanceMethodContract(
        new MutatorContract(
            this, DayPeriod.class,
            "clone()") {

      public String[] getFormalParameters() {
        return new String[] {};
      }
      {
        // no preconditions
        // postconditions
        addPostcondition(new Condition() {
          public boolean validate(Map context) {
            DayPeriod subject = (DayPeriod) context.get(SUBJECT_KEY);
            Object result = context.get(RESULT_KEY);
            return (result instanceof DayPeriod) &&
                   DateUtil.sameDay(subject.getStartDate(), ((DayPeriod)result).getStartDate()) &&
                   DateUtil.sameDay(subject.getEndDate(), ((DayPeriod)result).getEndDate());
          }});
        close();
      }

      public StraightList getTestCases() throws TorytException {
        return new LazyCombinationStraightList(
            new String[] {SUBJECT_KEY},
            new StraightList[] {getCases()});
      }

    });
    addInstanceMethodContract(
        new MutatorContract(
            this, DayPeriod.class,
            "equals(java.lang.Object)") {

      public String[] getFormalParameters() {
        return new String[] {"o"};
      }
      {
        // no preconditions
        // postconditions
        addPostcondition(new Condition() {
          public boolean validate(Map context) {
            DayPeriod subject = (DayPeriod) context.get(SUBJECT_KEY);
            Object o = context.get("o");
            boolean result = ((Boolean)context.get(RESULT_KEY)).booleanValue();
            return result
                   ==
                   ((o instanceof DayPeriod) &&
                    DateUtil.sameDay(subject.getStartDate(), ((DayPeriod)o).getStartDate()) &&
                    DateUtil.sameDay(subject.getEndDate(), ((DayPeriod)o).getEndDate()));
          }});
        close();
      }

      public StraightList getTestCases() throws TorytException {
        return new LazyCombinationStraightList(
            new String[] {SUBJECT_KEY, "o"},
            new StraightList[] {getCases(), getCases()});
      }

    });
    addInstanceMethodContract(
        new MutatorContract(
            this, DayPeriod.class,
            "hashCode()") {

      public String[] getFormalParameters() {
        return new String[] {};
      }
      {
        // no preconditions
        // postconditions
        addPostcondition(new Condition() {
          public boolean validate(Map context) {
            DayPeriod subject = (DayPeriod) context.get(SUBJECT_KEY);
            int result = ((Integer)context.get(RESULT_KEY)).intValue();
            return result
                   ==
                   ( ( subject.getStartDate() == null
                          ? 0
                          : subject.getStartDate().hashCode()
                     )
                     +
                     ( subject.getEndDate() == null
                          ? 0
                          : subject.getEndDate().hashCode()
                      )
                   );
          }});
        close();
      }

      public StraightList getTestCases() throws TorytException {
        return new LazyCombinationStraightList(
            new String[] {SUBJECT_KEY},
            new StraightList[] {getCases()});
      }

    });
    addInstanceMethodContract(
        new MutatorContract(
            this, DayPeriod.class,
            "toString()") {

      public String[] getFormalParameters() {
        return new String[] {};
      }
      {
        // no preconditions
        // postconditions
        addPostcondition(new Condition() {
          public boolean validate(Map context) {
            DayPeriod subject = (DayPeriod) context.get(SUBJECT_KEY);
            String result = (String) context.get(RESULT_KEY);
            return result
                    .equals(
                        ( (subject.getStartDate() == null)
                              ? "???"
                              : subject.getStartDate().toString()
                        )
                        +
                        " - "
                        +
                        ( (subject.getEndDate() == null)
                              ? "???"
                              : subject.getEndDate().toString()
                        )
                    );
          }});
        close();
      }

      public StraightList getTestCases() throws TorytException {
        return new LazyCombinationStraightList(
            new String[] {SUBJECT_KEY},
            new StraightList[] {getCases()});
      }

    });
    addInstanceMethodContract(
        new MutatorContract(
            this, DayPeriod.class,
            "getNbDaysInPeriod()") {

      public String[] getFormalParameters() {
        return new String[] {};
      }

      {
        // no preconditions
        // postconditions
        addPostcondition(new Condition() {
          public boolean validate(Map context) {
            DayPeriod subject = (DayPeriod)context.get(SUBJECT_KEY);
            long result = ((Long) context.get(RESULT_KEY)).longValue();
            return result
                   ==
                   ( ( subject.getStartDate() != null &&
                       subject.getEndDate() != null
                     )
                       ? (  (  subject.getEndDate().getTime()
                               -
                               subject.getStartDate().getTime()
                            )
                            /
                            (24*60*60*1000)
                          ) + 1
                       : -1
                   );
          }});
        close();
      }

      public StraightList getTestCases() throws TorytException {
        return new LazyCombinationStraightList(
              new String[] {SUBJECT_KEY},
              new StraightList[] {getCases()});
      }

    });
    addInstanceMethodContract(
        new MutatorContract(
            this, DayPeriod.class,
            "getNbDaysInPeriodInclusive()") {

      public String[] getFormalParameters() {
        return new String[] {};
      }

      {
        // no preconditions
        // postconditions
        addPostcondition(new Condition() {
          public boolean validate(Map context) {
            DayPeriod subject = (DayPeriod)context.get(SUBJECT_KEY);
            long result = ((Long) context.get(RESULT_KEY)).longValue();
            return result == subject.getNbDaysInPeriod();
          }});
        close();
      }

      public StraightList getTestCases() throws TorytException {
        return new LazyCombinationStraightList(
              new String[] {SUBJECT_KEY},
              new StraightList[] {getCases()});
      }

    });
    addInstanceMethodContract(
        new MutatorContract(
            this, DayPeriod.class,
            "containsInclusive(java.util.Date)") {

      public String[] getFormalParameters() {
        return new String[] {"date"};
      }

      {
        // no preconditions
        // postconditions
        addPostcondition(new Condition() {
          public boolean validate(Map context) {
            DayPeriod subject = (DayPeriod)context.get(SUBJECT_KEY);
            Date date = (Date)context.get("date");
            boolean result = ((Boolean)context.get(RESULT_KEY)).booleanValue();
            return result == subject.contains(date);
          }});
        close();
      }

      public StraightList getTestCases() throws TorytException {
        return new LazyCombinationStraightList(
              new String[] {SUBJECT_KEY, "date"},
              new StraightList[] {getCases(),
                                  new NullFirstStraightList(
                                        Cases.findTestObjectList(Date.class))});
      }

    });
    addInstanceMethodContract(
        new MutatorContract(
            this, DayPeriod.class,
            "contains(java.util.Date)") {

      public String[] getFormalParameters() {
        return new String[] {"date"};
      }

      {
        // no preconditions
        // postconditions
        addPostcondition(new Condition() {
          public boolean validate(Map context) {
            DayPeriod subject = (DayPeriod)context.get(SUBJECT_KEY);
            Date date = (Date)context.get("date");
            boolean result = ((Boolean)context.get(RESULT_KEY)).booleanValue();
            boolean noNulls = (date != null) && (subject.getStartDate() != null) && (subject.getEndDate() != null);
            if (noNulls) {
              Date dayDate = DateUtil.dayDate(date);
              boolean tooEarly = dayDate.before(subject.getStartDate());
              boolean tooLate = dayDate.after(subject.getEndDate());
              boolean expected = ((! tooEarly) && (! tooLate));
              return (result == expected);
            }
            else {
              return (result == false);
            }
          }
          public String toString() {
            return "(date != null) && (getStartDate() != null) && (getEndDate() != null) &&" +
                  " (!date.before(getStartDate())) && (!date.after(getEndDate()));";
          }
        });
        close();
      }

      public StraightList getTestCases() throws TorytException {
        return new LazyCombinationStraightList(
              new String[] {SUBJECT_KEY, "date"},
              new StraightList[] {getCases(),
                                  new NullFirstStraightList(
                                        Cases.findTestObjectList(Date.class))});
      }

    });
    // basic inspectors
    addBasicInspector("getStartDate()");
    addBasicInspector("getEndDate()");
    // type invariants
    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        DayPeriod subject = (DayPeriod) context.get(SUBJECT_KEY);
        if (subject.getStartDate() != null) {
          return DateUtil.isDayDate(subject.getStartDate());
        }
        else {
          return true;
        }
      }
      public String toString() {
        return "getStartDate() != null ? DateUtil.isDayDate(getStartDate());";
      }
    });
    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        DayPeriod subject = (DayPeriod) context.get(SUBJECT_KEY);
        if (subject.getEndDate() != null) {
          return DateUtil.isDayDate(subject.getEndDate());
        }
        else {
          return true;
        }
      }
      public String toString() {
        return "getEndDate() != null ? DateUtil.isDayDate(getEndDate());";
      }
    });
    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        DayPeriod subject = (DayPeriod) context.get(SUBJECT_KEY);
        return
            (subject.getStartDate() != null && subject.getEndDate() != null)
                ? ! subject.getStartDate().after(subject.getEndDate())
                : true;
      }
    });
    close();
  }

  public StraightList getCasesMaps() throws TorytException {
      return new LazyCombinationStraightList(
                  new String[] {
                      "startDate",
                      "endDate"
                  },
                  new StraightList[] {
                      new NullFirstStraightList(
                          Cases.findTestObjectList(Date.class)
                      ),
                      new NullFirstStraightList(
                          Cases.findTestObjectList(Date.class)
                      )
                  });
  }

  public LazyMappingStraightList.Mapping getCaseMapping() {
    return CASE_MAPPING;
  }

  public final static LazyMappingStraightList.Mapping CASE_MAPPING
      = new LazyMappingStraightList.Mapping() {

          public Object map(Object o) {
            Map m = (Map)o;
            DayPeriod subject = new DayPeriod();
            try {
              subject.setStartDate((Date) m.get("startDate"));
              subject.setEndDate((Date) m.get("endDate"));
            }
            catch (InvalidPeriodException pExc) {
              assert false : "InvalidPeriodException cannot be thrown.";
            }
            return subject;
          }

          public boolean isValid(Object o) {
            Map m = (Map)o;
            Date startDate = (Date)m.get("startDate");
            Date endDate = (Date)m.get("endDate");
            return
               (startDate != null && endDate != null)
                   ? (startDate.before(endDate))
                   : (true);
          }

    };

}
