package be.peopleware.value_II;


import java.util.Map;
import java.util.regex.Pattern;

import org.toryt.Condition;
import org.toryt.ExceptionCondition;
import org.toryt.TorytException;
import org.toryt.hard.ClassContract;
import org.toryt.hard.ConstructorContract;
import org.toryt.hard.MutatorContract;
import org.toryt.support.straightlist.ArrayStraightList;
import org.toryt.support.straightlist.EmptyStraightList;
import org.toryt.support.straightlist.LazyCombinationStraightList;
import org.toryt.support.straightlist.LazyMappingStraightList;
import org.toryt.support.straightlist.StraightList;

import be.peopleware.bean_V.PropertyException;


/**
 * A test class for EnterpriseNumber.
 *
 * @author    wlambrec
 */
public class _Contract_EnterpriseNumber extends ClassContract {

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

  public _Contract_EnterpriseNumber() throws TorytException {
    super(EnterpriseNumber.class);

    // constructors
    addConstructorContract(
      new ConstructorContract(this, EnterpriseNumber.class, "EnterpriseNumber(java.lang.String,java.lang.String,java.lang.String)") {
        public String[] getFormalParameters() {
          return new String[] {"leftNumber", "middleNumber", "rightNumber"};
        }

        {
          // no preconditions
          // postconditions
          addPostcondition(new Condition() {
            public boolean validate(Map context) {
              EnterpriseNumber subject = (EnterpriseNumber)context.get(SUBJECT_KEY);
              String leftNumber = (String)context.get("leftNumber");
              return (subject.getLeftNumber().equals(leftNumber));
            }});

          addPostcondition(new Condition() {
            public boolean validate(Map context) {
              EnterpriseNumber subject = (EnterpriseNumber)context.get(SUBJECT_KEY);
              String middleNumber = (String)context.get("middleNumber");
              return (subject.getMiddleNumber().equals(middleNumber));
            }});

          addPostcondition(new Condition() {
            public boolean validate(Map context) {
              EnterpriseNumber subject = (EnterpriseNumber)context.get(SUBJECT_KEY);
              String rightNumber = (String)context.get("rightNumber");
              return (subject.getRightNumber().equals(rightNumber));
            }});

          addExceptionCondition(new ExceptionCondition(PropertyException.class) {
            public boolean validate(Map context) {
              String leftNumber = (String)context.get("leftNumber");
              PropertyException pExc = (PropertyException)context.get(EXCEPTION_KEY);
              return  ( leftNumber == null )
                      && (pExc.reportsOn(
                          EnterpriseNumber.class, "leftNumber",
                              "LEFT_NUMBER_IS_NULL", null)
                      );
            }});

          addExceptionCondition(new ExceptionCondition(PropertyException.class) {
            public boolean validate(Map context) {
              String leftNumber = (String)context.get("leftNumber");
              PropertyException pExc = (PropertyException)context.get(EXCEPTION_KEY);
              return  ((leftNumber != null && !Pattern.matches(EnterpriseNumber.LEFT_PATTERN, leftNumber))
                       && pExc.reportsOn(
                                        EnterpriseNumber.class, "leftNumber",
                                        "LEFT_NUMBER_INVALID_PATTERN", null)
                      );
            }});

          addExceptionCondition(new ExceptionCondition(PropertyException.class) {
            public boolean validate(Map context) {
              String middleNumber = (String)context.get("middleNumber");
              PropertyException pExc = (PropertyException)context.get(EXCEPTION_KEY);
              return   ((middleNumber == null)
                          && pExc.reportsOn(
                                        EnterpriseNumber.class, "middleNumber",
                                       "MIDDLE_NUMBER_IS_NULL", null)
                       );
            }});

          addExceptionCondition(new ExceptionCondition(PropertyException.class) {
            public boolean validate(Map context) {
              String middleNumber = (String)context.get("middleNumber");
              PropertyException pExc = (PropertyException)context.get(EXCEPTION_KEY);
              return (  (middleNumber != null && !Pattern.matches(EnterpriseNumber.MIDDLE_PATTERN, middleNumber))
                        && pExc.reportsOn(
                                       EnterpriseNumber.class, "middleNumber",
                                       "MIDDLE_NUMBER_INVALID_PATTERN", null)
                     );
            }});

          addExceptionCondition(new ExceptionCondition(PropertyException.class) {
            public boolean validate(Map context) {
              String rightNumber = (String)context.get("rightNumber");
              PropertyException pExc = (PropertyException)context.get(EXCEPTION_KEY);
              return (  (rightNumber == null)
                        && pExc.reportsOn(
                                       EnterpriseNumber.class, "rightNumber",
                                       "RIGHT_NUMBER_IS_NULL", null)
                     );
            }});


          addExceptionCondition(new ExceptionCondition(PropertyException.class) {
            public boolean validate(Map context) {
              String rightNumber = (String)context.get("rightNumber");
              PropertyException pExc = (PropertyException)context.get(EXCEPTION_KEY);
              return (  (rightNumber != null && !Pattern.matches(EnterpriseNumber.RIGHT_PATTERN, rightNumber))
                         && pExc.reportsOn(
                                      EnterpriseNumber.class, "rightNumber",
                                       "RIGHT_NUMBER_INVALID_PATTERN", null)
                     );
            }});

          addExceptionCondition(new ExceptionCondition(PropertyException.class) {
            public boolean validate(Map context) {
              String leftNumber = (String)context.get("leftNumber");
              String middleNumber = (String)context.get("middleNumber");
              String rightNumber = (String)context.get("rightNumber");
              PropertyException pExc = (PropertyException)context.get(EXCEPTION_KEY);
              return (  (  leftNumber != null && Pattern.matches(EnterpriseNumber.LEFT_PATTERN, leftNumber) &&
                           middleNumber != null && Pattern.matches(EnterpriseNumber.MIDDLE_PATTERN, middleNumber) &&
                           rightNumber != null && Pattern.matches(EnterpriseNumber.RIGHT_PATTERN, rightNumber) &&
                           !EnterpriseNumber.checkEnterpriseNumber(leftNumber, middleNumber, rightNumber)
                        )
                        &&  pExc.reportsOn(EnterpriseNumber.class, null, "INVALID_CHECK", null)
                      );
            }});

          close();
        }

        public StraightList getTestCases() throws TorytException {
          return new LazyCombinationStraightList(
              new String[] {"leftNumber", "middleNumber", "rightNumber"}, VARIOUS_STRING_COMBINATIONS);
        }
      }
    );

    addConstructorContract(
        new ConstructorContract(this, EnterpriseNumber.class, "EnterpriseNumber()") {
          {
            // no preconditions
            // postconditions
            addPostcondition(new Condition() {
              public boolean validate(Map context) {
                EnterpriseNumber subject = (EnterpriseNumber)context.get(SUBJECT_KEY);
                return (subject.getLeftNumber().equals("0123"));
              }});

            addPostcondition(new Condition() {
              public boolean validate(Map context) {
                EnterpriseNumber subject = (EnterpriseNumber)context.get(SUBJECT_KEY);
                return (subject.getMiddleNumber().equals("456"));
              }});

            addPostcondition(new Condition() {
              public boolean validate(Map context) {
                EnterpriseNumber subject = (EnterpriseNumber)context.get(SUBJECT_KEY);
                return (subject.getRightNumber().equals("749"));
              }});

            close();
          }

          public StraightList getTestCases() throws TorytException {
            return EmptyStraightList.INSTANCE;
          }
      }
    );

    addConstructorContract(
        new ConstructorContract(this, EnterpriseNumber.class, "EnterpriseNumber(be.peopleware.value_II.EnterpriseNumber)") {
         public String[] getFormalParameters() {
            return new String[] {"enterpriseNumber"};
          }

         {
            // no preconditions
            // postconditions
            addPostcondition(new Condition() {
              public boolean validate(Map context) {
                EnterpriseNumber subject = (EnterpriseNumber)context.get(SUBJECT_KEY);
                EnterpriseNumber enterpriseNumber = (EnterpriseNumber)context.get("enterpriseNumber");
                return (subject.getLeftNumber().equals(enterpriseNumber.getLeftNumber()));
              }});

            addPostcondition(new Condition() {
              public boolean validate(Map context) {
                EnterpriseNumber subject = (EnterpriseNumber)context.get(SUBJECT_KEY);
                EnterpriseNumber enterpriseNumber = (EnterpriseNumber)context.get("enterpriseNumber");
                return (subject.getMiddleNumber().equals(enterpriseNumber.getMiddleNumber()));
              }});

            addPostcondition(new Condition() {
              public boolean validate(Map context) {
                EnterpriseNumber subject = (EnterpriseNumber)context.get(SUBJECT_KEY);
                EnterpriseNumber enterpriseNumber = (EnterpriseNumber)context.get("enterpriseNumber");
                return (subject.getRightNumber().equals(enterpriseNumber.getRightNumber()));
              }});

            close();
        }

         public StraightList getTestCases() throws TorytException {
          return new LazyCombinationStraightList(
              new String[] {"enterpriseNumber"},
              new StraightList[] {getCases()});
        }
       }
      );

    // Define a list of all basic inspectors
    addBasicInspector("getLeftNumber()");
    addBasicInspector("getMiddleNumber()");
    addBasicInspector("getRightNumber()");

    // instance methods
    addInstanceMethodContract(
      new MutatorContract(this, EnterpriseNumber.class, "checkEnterpriseNumber(java.lang.String,java.lang.String,java.lang.String)") {
        public String[] getFormalParameters() {
          return new String[] {"leftNumber", "middleNumber", "rightNumber"};
        }

        {
          addPrecondition (new Condition() {
            public boolean validate(Map context) {
              String leftNumber = (String)context.get("leftNumber");
              return leftNumber != null;
            }
          });

          addPrecondition (new Condition() {
              public boolean validate(Map context) {
                String leftNumber = (String)context.get("leftNumber");
                return leftNumber != null && Pattern.matches(EnterpriseNumber.LEFT_PATTERN, leftNumber);
              }
          });

          addPrecondition (new Condition() {
            public boolean validate(Map context) {
              String middleNumber = (String)context.get("middleNumber");
              return middleNumber != null;
            }
          });

          addPrecondition (new Condition() {
            public boolean validate(Map context) {
              String middleNumber = (String)context.get("middleNumber");
              return middleNumber != null && Pattern.matches(EnterpriseNumber.MIDDLE_PATTERN, middleNumber);
            }
          });

          addPrecondition (new Condition() {
            public boolean validate(Map context) {
              String rightNumber = (String)context.get("rightNumber");
              return rightNumber != null;
            }
          });

          addPrecondition (new Condition() {
            public boolean validate(Map context) {
              String rightNumber = (String)context.get("rightNumber");
              return rightNumber != null && Pattern.matches(EnterpriseNumber.RIGHT_PATTERN, rightNumber);
            }
          });

          addPostcondition(new Condition() {
            public boolean validate(Map context) {
              boolean result = ((Boolean)context.get(RESULT_KEY)).booleanValue();
              String leftNumber = (String)context.get("leftNumber");
              String middleNumber = (String)context.get("middleNumber");
              String rightNumber = (String)context.get("rightNumber");
              return ( result
                       ==
                       (  (  Integer.parseInt(leftNumber + middleNumber + rightNumber.substring(0,1))
                             +
                             Integer.parseInt(rightNumber.substring(1))
                          ) % 97
                          == 0
                       )
                     );
            }});

          close();
        }

        public StraightList getTestCases() throws TorytException {
          return new LazyCombinationStraightList(
              new String[] {SUBJECT_KEY, "leftNumber", "middleNumber", "rightNumber"},
              VARIOUS_STRING_COMBINATIONS_WITH_1_INSTANCE);
        }
      }

    );

    addInstanceMethodContract(
        new MutatorContract(this, EnterpriseNumber.class, "equals(java.lang.Object)") {

          public String[] getFormalParameters() {
            return new String[] {"o"};
          }

          {
            // no preconditions
            // postconditions
            addPostcondition(new Condition() {
              public boolean validate(Map context) {
                EnterpriseNumber subject = (EnterpriseNumber) context.get(SUBJECT_KEY);
                Object o = context.get("o");
                boolean result = ((Boolean)context.get(RESULT_KEY)).booleanValue();
                return result
                       ==
                        ( o instanceof EnterpriseNumber &&
                                   ((EnterpriseNumber) o).getLeftNumber().equals(subject.getLeftNumber()) &&
                                   ((EnterpriseNumber) o).getMiddleNumber().equals(subject.getMiddleNumber()) &&
                                   ((EnterpriseNumber) o).getRightNumber().equals(subject.getRightNumber())
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
        new MutatorContract(this, EnterpriseNumber.class, "hashCode()") {

          public String[] getFormalParameters() {
            return new String[] {};
          }

          {
            // no preconditions
            // postconditions
            addPostcondition(new Condition() {
              public boolean validate(Map context) {
                EnterpriseNumber subject = (EnterpriseNumber)context.get(SUBJECT_KEY);
                int result = ((Integer)context.get(RESULT_KEY)).intValue();
                return result
                       ==
                       subject.getLeftNumber().hashCode() + subject.getMiddleNumber().hashCode() + subject.getRightNumber().hashCode();
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
        new MutatorContract(this, EnterpriseNumber.class, "toString()") {

        public String[] getFormalParameters() {
          return new String[] {};
        }

        {
          // no preconditions
          // postconditions
          addPostcondition(new Condition() {
            public boolean validate(Map context) {
              EnterpriseNumber subject = (EnterpriseNumber) context.get(SUBJECT_KEY);
              String result = (String) context.get(RESULT_KEY);
              return result
                      .equals(subject.getLeftNumber() + " " + subject.getMiddleNumber() + " " + subject.getRightNumber());
            }});

          close();
      }

      public StraightList getTestCases() throws TorytException {
        return new LazyCombinationStraightList(
            new String[] {SUBJECT_KEY},
            new StraightList[] {getCases()});
      }

    });

    // type invariants
    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        EnterpriseNumber subject = (EnterpriseNumber) context.get(SUBJECT_KEY);
        return (subject.getLeftNumber() != null);
      }
    });

    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        EnterpriseNumber subject = (EnterpriseNumber) context.get(SUBJECT_KEY);
        return (Pattern.matches(EnterpriseNumber.LEFT_PATTERN, subject.getLeftNumber()));
      }
    });

    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        EnterpriseNumber subject = (EnterpriseNumber) context.get(SUBJECT_KEY);
        return (subject.getMiddleNumber() != null);
      }
    });

    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        EnterpriseNumber subject = (EnterpriseNumber) context.get(SUBJECT_KEY);
        return (Pattern.matches(EnterpriseNumber.MIDDLE_PATTERN, subject.getMiddleNumber()));
      }
    });

    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        EnterpriseNumber subject = (EnterpriseNumber) context.get(SUBJECT_KEY);
        return (subject.getRightNumber() != null);
      }
    });

    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        EnterpriseNumber subject = (EnterpriseNumber) context.get(SUBJECT_KEY);
        return (Pattern.matches(EnterpriseNumber.RIGHT_PATTERN, subject.getRightNumber()));
      }
    });

    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        EnterpriseNumber subject = (EnterpriseNumber) context.get(SUBJECT_KEY);
        return (EnterpriseNumber.checkEnterpriseNumber(subject.getLeftNumber(), subject.getMiddleNumber(), subject.getRightNumber()));
      }
    });

    close();

  }

  private static StraightList[] OK_STRINGS =
    new StraightList[] {new ArrayStraightList(new String[] {"0123-456-749",
                                                            "1222-333-424",
                                                            "9999-888-762"})};

//  private static StraightList[] VARIOUS_STRINGS =
//    new StraightList[] {new ArrayStraightList( new String[] {"0123-456-749"}),
//                        new ArrayStraightList( new String[] {"1222-333-424"}),
//                        new ArrayStraightList( new String[] {"9999-888-762"}),
//                        new ArrayStraightList( new String[] {"0123-456-743"}),
//                        new ArrayStraightList( new String[] {"1W98-3R3-4D4"}),
//                        new ArrayStraightList( new String[] {"99994-88-762"})};

  private static StraightList[] VARIOUS_STRING_COMBINATIONS =
    new StraightList[] {new ArrayStraightList( new String[] {"0123", "1222", "9999", null, "1hh7", "889", "78556"}),
                        new ArrayStraightList( new String[] {"456", "333", "888", null, "1W4", "78", "8977"}),
                        new ArrayStraightList( new String[] {"749", "424", "762", null, "1E4", "78", "8977"})};

  private static StraightList[] VARIOUS_STRING_COMBINATIONS_WITH_1_INSTANCE =
    new StraightList[] {new ArrayStraightList( new EnterpriseNumber[] {new EnterpriseNumber()}),
                        new ArrayStraightList( new String[] {"0123", "1222", "9999", null, "1hh7", "889", "78556"}),
                        new ArrayStraightList( new String[] {"456", "333", "888", null, "1W4", "78", "8977"}),
                        new ArrayStraightList( new String[] {"749", "424", "762", null, "1E4", "78", "8977"})};

  public StraightList getCasesMaps() throws TorytException {
      return new LazyCombinationStraightList(
                  new String[] {"pattern"}, OK_STRINGS);
  }

  public LazyMappingStraightList.Mapping getCaseMapping() {
    return CASE_MAPPING;
  }

  public final static LazyMappingStraightList.Mapping CASE_MAPPING
   = new LazyMappingStraightList.AllValidMapping() {

          public Object map(Object o) {
            Map m = (Map)o;
            String pattern = (String)m.get("pattern");
            EnterpriseNumber subject = null;
            try {
              subject = new EnterpriseNumber(pattern);
            }
            catch (PropertyException pe) {
              assert false : "should not happen";
            }
            return subject;
          }
    };
}
