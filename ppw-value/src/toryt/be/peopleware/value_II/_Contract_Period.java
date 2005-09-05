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

import be.peopleware.value_II.InvalidPeriodException;
import be.peopleware.value_II.Period;


/**
 * A test class for Period.
 *
 * @author    nsmeets
 */
public class _Contract_Period extends ClassContract {

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

  public _Contract_Period() throws TorytException {
    super(Period.class);

    // constructors
    addConstructorContract(
      new ConstructorContract(this, Period.class, "Period()") {
        {
          // no preconditions
          // postconditions
          addPostcondition(new Condition() {
            public boolean validate(Map context) {
              Period result = (Period)context.get(SUBJECT_KEY);
              return result.getStartDate() == null;
            }});
          addPostcondition(new Condition() {
            public boolean validate(Map context) {
              Period result = (Period)context.get(SUBJECT_KEY);
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
            this, Period.class,
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
            Period subject = (Period)context.get(SUBJECT_KEY);
            Date startDate = (Date)context.get("startDate");
            return  (startDate == null)
                       ? subject.getStartDate() == null
                       : subject.getStartDate().equals(startDate);
          }});
        addExceptionCondition(new ExceptionCondition(InvalidPeriodException.class) {
          public boolean validate(Map context) {
            Period subject = (Period)context.get(SUBJECT_KEY);
            Date startDate = (Date)context.get("startDate");
            InvalidPeriodException pExc = (InvalidPeriodException)context.get(EXCEPTION_KEY);
            return
                (  startDate != null
                   &&
                   subject.getEndDate() != null
                   &&
                   !startDate.before(subject.getEndDate())
                )
                &&
                (startDate == null)
                                ? (pExc.getStartDate() == null)
                                : (pExc.getStartDate().equals(startDate))
                &&
                (subject.getEndDate() == null)
                                ? (pExc.getEndDate() == null)
                                : (pExc.getEndDate().equals(subject.getEndDate()))
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
            this, Period.class,
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
            Period subject = (Period)context.get(SUBJECT_KEY);
            Date endDate = (Date)context.get("endDate");
            return  (endDate == null)
                       ? subject.getEndDate() == null
                       : subject.getEndDate().equals(endDate);
          }});
        addExceptionCondition(new ExceptionCondition(InvalidPeriodException.class) {
          public boolean validate(Map context) {
            Period subject = (Period)context.get(SUBJECT_KEY);
            Date endDate = (Date)context.get("endDate");
            InvalidPeriodException pExc = (InvalidPeriodException)context.get(EXCEPTION_KEY);
            return  ( subject.getStartDate() != null
                      && endDate != null
                      && !subject.getStartDate().before(endDate)
                    )
                    && (subject.getStartDate() == null)
                         ? (pExc.getStartDate() == null)
                         : (pExc.getStartDate().equals(subject.getStartDate()))
                    && (endDate == null)
                         ? (pExc.getEndDate() == null)
                         : (pExc.getEndDate().equals(endDate))
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
            this, Period.class,
            "clone()") {

      public String[] getFormalParameters() {
        return new String[] {};
      }
      {
        // no preconditions
        // postconditions
        addPostcondition(new Condition() {
          public boolean validate(Map context) {
            Period subject = (Period) context.get(SUBJECT_KEY);
            Object result = context.get(RESULT_KEY);
            return ( result instanceof Period)
                     &&
                     ( subject.getStartDate() == null
                         ? ((Period)result).getStartDate() == null
                         : ((Period)result).getStartDate().equals(subject.getStartDate()
                     )
                     &&
                     ( subject.getEndDate() == null
                         ? ((Period)result).getEndDate() == null
                         : ((Period)result).getEndDate().equals(subject.getEndDate())
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
            this, Period.class,
            "equals(java.lang.Object)") {

      public String[] getFormalParameters() {
        return new String[] {"o"};
      }
      {
        // no preconditions
        // postconditions
        addPostcondition(new Condition() {
          public boolean validate(Map context) {
            Period subject = (Period) context.get(SUBJECT_KEY);
            Object o = context.get("o");
            boolean result = ((Boolean)context.get(RESULT_KEY)).booleanValue();
            return result
                   ==
                   ( o instanceof Period &&
                     ( subject.getStartDate() == null
                          ? ((Period)o).getStartDate() == null
                          : subject.getStartDate().equals(((Period)o).getStartDate())
                     )
                     &&
                     ( subject.getEndDate() == null
                          ? ((Period)o).getEndDate() == null
                          : subject.getEndDate().equals(((Period)o).getEndDate())
                     )
                   );
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
            this, Period.class,
            "hashCode()") {

      public String[] getFormalParameters() {
        return new String[] {};
      }
      {
        // no preconditions
        // postconditions
        addPostcondition(new Condition() {
          public boolean validate(Map context) {
            Period subject = (Period) context.get(SUBJECT_KEY);
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
            this, Period.class,
            "toString()") {

      public String[] getFormalParameters() {
        return new String[] {};
      }
      {
        // no preconditions
        // postconditions
        addPostcondition(new Condition() {
          public boolean validate(Map context) {
            Period subject = (Period) context.get(SUBJECT_KEY);
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
            this, Period.class,
            "getNbDaysInPeriod()") {

      public String[] getFormalParameters() {
        return new String[] {};
      }

      {
        // no preconditions
        // postconditions
        addPostcondition(new Condition() {
          public boolean validate(Map context) {
            Period subject = (Period)context.get(SUBJECT_KEY);
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
                          )
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
    // basic inspectors
    addBasicInspector("getStartDate()");
    addBasicInspector("getEndDate()");
    // type invariants
    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        Period subject = (Period) context.get(SUBJECT_KEY);
        return
            (subject.getStartDate() != null && subject.getEndDate() != null)
                ? subject.getStartDate().before(subject.getEndDate())
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
            Period subject = new Period();
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
