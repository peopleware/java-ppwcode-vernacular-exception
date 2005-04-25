package be.peopleware.value_I;


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
import be.peopleware.bean_IV.PropertyException;


/**
 * A test class for RSZNumber.
 *
 * @author    wlambrec
 */
public class _Contract_RSZNumber extends ClassContract {

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

  public _Contract_RSZNumber() throws TorytException {
    super(RSZNumber.class);

    // constructors
    addConstructorContract(
      new ConstructorContract(this, RSZNumber.class, "RSZNumber(java.lang.String,java.lang.String,java.lang.String)") {
        public String[] getFormalParameters() {
          return new String[] {"leftNumber", "middleNumber", "rightNumber"};
        }

        {
          // no preconditions
          // postconditions
          addPostcondition(new Condition() {
            public boolean validate(Map context) {
              RSZNumber subject = (RSZNumber)context.get(SUBJECT_KEY);
              String leftNumber = (String)context.get("leftNumber");

              return (subject.getLeftNumber().equals(leftNumber));
            }});

          addPostcondition(new Condition() {
            public boolean validate(Map context) {
              RSZNumber subject = (RSZNumber)context.get(SUBJECT_KEY);
              String middleNumber = (String)context.get("middleNumber");

              return (subject.getMiddleNumber().equals(middleNumber));
            }});

          addPostcondition(new Condition() {
            public boolean validate(Map context) {
              RSZNumber subject = (RSZNumber)context.get(SUBJECT_KEY);
              String rightNumber = (String)context.get("rightNumber");

              return (subject.getRightNumber().equals(rightNumber));
            }});

          addExceptionCondition(new ExceptionCondition(PropertyException.class) {
            public boolean validate(Map context) {
              String leftNumber = (String)context.get("leftNumber");

              PropertyException pExc = (PropertyException)context.get(EXCEPTION_KEY);
              return  ( leftNumber == null )
                      && (pExc.reportsOn(
                          RSZNumber.class, "leftNumber",
                              "LEFT_NUMBER_IS_NULL", null)
                      );
            }});

          addExceptionCondition(new ExceptionCondition(PropertyException.class) {
            public boolean validate(Map context) {
              String leftNumber = (String)context.get("leftNumber");

              PropertyException pExc = (PropertyException)context.get(EXCEPTION_KEY);

              return  ((leftNumber != null && !Pattern.matches(RSZNumber.LEFT_PATTERN, leftNumber))
                       && pExc.reportsOn(
                                        RSZNumber.class, "leftNumber",
                                        "LEFT_NUMBER_INVALID_PATTERN", null)
                      );
            }});

          addExceptionCondition(new ExceptionCondition(PropertyException.class) {
            public boolean validate(Map context) {
              String middleNumber = (String)context.get("middleNumber");

              PropertyException pExc = (PropertyException)context.get(EXCEPTION_KEY);

              return   ((middleNumber == null)
                          && pExc.reportsOn(
                                        RSZNumber.class, "middleNumber",
                                       "MIDDLE_NUMBER_IS_NULL", null)
                       );
            }});

          addExceptionCondition(new ExceptionCondition(PropertyException.class) {
            public boolean validate(Map context) {
              String middleNumber = (String)context.get("middleNumber");

              PropertyException pExc = (PropertyException)context.get(EXCEPTION_KEY);

              return (  (middleNumber != null && !Pattern.matches(RSZNumber.MIDDLE_PATTERN, middleNumber))
                        && pExc.reportsOn(
                                       RSZNumber.class, "middleNumber",
                                       "MIDDLE_NUMBER_INVALID_PATTERN", null)
                     );
            }});

          addExceptionCondition(new ExceptionCondition(PropertyException.class) {
            public boolean validate(Map context) {
              String rightNumber = (String)context.get("rightNumber");

              PropertyException pExc = (PropertyException)context.get(EXCEPTION_KEY);

              return (  (rightNumber == null)
                        && pExc.reportsOn(
                                       RSZNumber.class, "rightNumber",
                                       "RIGHT_NUMBER_IS_NULL", null)
                     );
            }});


          addExceptionCondition(new ExceptionCondition(PropertyException.class) {
            public boolean validate(Map context) {
              String rightNumber = (String)context.get("rightNumber");

              PropertyException pExc = (PropertyException)context.get(EXCEPTION_KEY);

              return (  (rightNumber != null && !Pattern.matches(RSZNumber.RIGHT_PATTERN, rightNumber))
                         && pExc.reportsOn(
                                      RSZNumber.class, "rightNumber",
                                       "RIGHT_NUMBER_INVALID_PATTERN", null)
                     );
            }});

          addExceptionCondition(new ExceptionCondition(PropertyException.class) {
            public boolean validate(Map context) {
              String leftNumber = (String)context.get("leftNumber");
              String middleNumber = (String)context.get("middleNumber");
              String rightNumber = (String)context.get("rightNumber");

              PropertyException pExc = (PropertyException)context.get(EXCEPTION_KEY);
              return (  (  leftNumber != null &&
                           middleNumber != null &&
                           rightNumber != null &&
                           !RSZNumber.checkRSZNumber(leftNumber, middleNumber, rightNumber)
                        )
                        &&  pExc.reportsOn(RSZNumber.class, null, "INVALID_CHECK", null)
                      );
            }});

          close();
        }

        public StraightList getTestCases() throws TorytException {
          return new LazyCombinationStraightList(
              new String[] {"leftNumber", "middleNumber", "rightNumber"},
              new StraightList[] {
                  new ArrayStraightList( new String[] {"024", "024", "024"}),
                  new ArrayStraightList( new String[] {"1234567", "2223334", "9998887"}),
                  new ArrayStraightList( new String[] {"49", "08", "06"})
              });
        }
      }
    );

    addConstructorContract(
        new ConstructorContract(this, RSZNumber.class, "RSZNumber()") {
          {
            // no preconditions
            // postconditions
            addPostcondition(new Condition() {
              public boolean validate(Map context) {
                RSZNumber subject = (RSZNumber)context.get(SUBJECT_KEY);
                return (subject.getLeftNumber().equals("024"));
              }});

            addPostcondition(new Condition() {
              public boolean validate(Map context) {
                RSZNumber subject = (RSZNumber)context.get(SUBJECT_KEY);
                return (subject.getMiddleNumber().equals("1234567"));
              }});

            addPostcondition(new Condition() {
              public boolean validate(Map context) {
                RSZNumber subject = (RSZNumber)context.get(SUBJECT_KEY);
                return (subject.getRightNumber().equals("49"));
              }});

            close();
          }

          public StraightList getTestCases() throws TorytException {
            return EmptyStraightList.INSTANCE;
          }
      }
    );

    addConstructorContract(
        new ConstructorContract(this, RSZNumber.class, "RSZNumber(be.peopleware.value_I.RSZNumber)") {
         public String[] getFormalParameters() {
            return new String[] {"rszNumber"};
          }

         {
            // no preconditions
            // postconditions
            addPostcondition(new Condition() {
              public boolean validate(Map context) {
                RSZNumber subject = (RSZNumber)context.get(SUBJECT_KEY);
                RSZNumber rszNumber = (RSZNumber)context.get("rszNumber");

                return (subject.getLeftNumber().equals(rszNumber.getLeftNumber()));
              }});

            addPostcondition(new Condition() {
              public boolean validate(Map context) {
                RSZNumber subject = (RSZNumber)context.get(SUBJECT_KEY);
                RSZNumber rszNumber = (RSZNumber)context.get("rszNumber");

                return (subject.getMiddleNumber().equals(rszNumber.getMiddleNumber()));
              }});

            addPostcondition(new Condition() {
              public boolean validate(Map context) {
                RSZNumber subject = (RSZNumber)context.get(SUBJECT_KEY);
                RSZNumber rszNumber = (RSZNumber)context.get("rszNumber");

                return (subject.getRightNumber().equals(rszNumber.getRightNumber()));
              }});

            close();
        }

         public StraightList getTestCases() throws TorytException {
          return new LazyCombinationStraightList(
              new String[] {"rszNumber"},
              new StraightList[] {  getCases() }
          );
        }
       }
      );

    // Define a list of all basic inspectors
    addBasicInspector("getLeftNumber()");
    addBasicInspector("getMiddleNumber()");
    addBasicInspector("getRightNumber()");

    // instance methods
    addInstanceMethodContract(
      new MutatorContract(this, RSZNumber.class, "checkRSZNumber(java.lang.String,java.lang.String,java.lang.String)") {
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
                return Pattern.matches(RSZNumber.LEFT_PATTERN, leftNumber);
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
              return Pattern.matches(RSZNumber.MIDDLE_PATTERN, middleNumber);
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
              return Pattern.matches(RSZNumber.RIGHT_PATTERN, rightNumber);
            }
          });

          addPostcondition(new Condition() {
            public boolean validate(Map context) {
              boolean result = ((Boolean)context.get(RESULT_KEY)).booleanValue();
              String middleNumber = (String)context.get("middleNumber");
              String rightNumber = (String)context.get("rightNumber");
              return ( result
                       ==
                       (  Integer.parseInt(rightNumber)
                          ==
                          ( ( ( 96 - ( (Integer.parseInt(middleNumber) * 100 ) % 97 ) ) == 0)
                                 ? (97)
                                 : ( 96 - ( (Integer.parseInt(middleNumber) * 100 ) % 97 ) )
                          )
                       )
                     );
            }});

          close();
        }

        public StraightList getTestCases() throws TorytException {
          return new LazyCombinationStraightList(
              new String[] {SUBJECT_KEY, "leftNumber", "middleNumber", "rightNumber"},
              new StraightList[] {
                  getCases(),
                  new ArrayStraightList( new String[] {"024", "024", "024"}),
                  new ArrayStraightList( new String[] {"1234567", "2223334", "9998887"}),
                  new ArrayStraightList( new String[] {"49", "08", "06"})
              });
        }
      }
    );

    addInstanceMethodContract(
        new MutatorContract(this, RSZNumber.class, "equals(java.lang.Object)") {

          public String[] getFormalParameters() {
            return new String[] {"o"};
          }

          {
            // no preconditions
            // postconditions
            addPostcondition(new Condition() {
              public boolean validate(Map context) {
                RSZNumber subject = (RSZNumber) context.get(SUBJECT_KEY);
                Object o = context.get("o");
                boolean result = ((Boolean)context.get(RESULT_KEY)).booleanValue();
                return result
                       ==
                        ( o instanceof RSZNumber &&
                                   ((RSZNumber) o).getLeftNumber().equals(subject.getLeftNumber()) &&
                                   ((RSZNumber) o).getMiddleNumber().equals(subject.getMiddleNumber()) &&
                                   ((RSZNumber) o).getRightNumber().equals(subject.getRightNumber())
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
        new MutatorContract(this, RSZNumber.class, "hashCode()") {

          public String[] getFormalParameters() {
            return new String[] {};
          }

          {
            // no preconditions
            // postconditions
            addPostcondition(new Condition() {
              public boolean validate(Map context) {
                RSZNumber subject = (RSZNumber)context.get(SUBJECT_KEY);
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
        new MutatorContract(this, RSZNumber.class, "toString()") {

        public String[] getFormalParameters() {
          return new String[] {};
        }

        {
          // no preconditions
          // postconditions
          addPostcondition(new Condition() {
            public boolean validate(Map context) {
              RSZNumber subject = (RSZNumber) context.get(SUBJECT_KEY);
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
        RSZNumber subject = (RSZNumber) context.get(SUBJECT_KEY);
        return (subject.getLeftNumber() != null);
      }
    });

    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        RSZNumber subject = (RSZNumber) context.get(SUBJECT_KEY);
        return (Pattern.matches(RSZNumber.LEFT_PATTERN, subject.getLeftNumber()));
      }
    });

    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        RSZNumber subject = (RSZNumber) context.get(SUBJECT_KEY);
        return (subject.getMiddleNumber() != null);
      }
    });

    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        RSZNumber subject = (RSZNumber) context.get(SUBJECT_KEY);
        return (Pattern.matches(RSZNumber.MIDDLE_PATTERN, subject.getMiddleNumber()));
      }
    });

    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        RSZNumber subject = (RSZNumber) context.get(SUBJECT_KEY);
        return (subject.getRightNumber() != null);
      }
    });

    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        RSZNumber subject = (RSZNumber) context.get(SUBJECT_KEY);
        return (Pattern.matches(RSZNumber.RIGHT_PATTERN, subject.getRightNumber()));
      }
    });

    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        RSZNumber subject = (RSZNumber) context.get(SUBJECT_KEY);
        return (RSZNumber.checkRSZNumber(subject.getLeftNumber(), subject.getMiddleNumber(), subject.getRightNumber()));
      }
    });

    close();

  }

  public StraightList getCasesMaps() throws TorytException {
      return new LazyCombinationStraightList(
                  new String[] {"leftNumber", "middleNumber", "rightNumber"},
                  new StraightList[] {
                      new ArrayStraightList( new String[] {"123", "222", "999"}),
                      new ArrayStraightList( new String[] {"456", "333", "888"}),
                      new ArrayStraightList( new String[] {"749", "403", "767"})
                  }
      );
  }

  public LazyMappingStraightList.Mapping getCaseMapping() {
    return CASE_MAPPING;
  }

  public final static LazyMappingStraightList.Mapping CASE_MAPPING
   = new LazyMappingStraightList.AllValidMapping() {

          public Object map(Object o) {
            Map m = (Map)o;
            String leftNumber = (String)m.get("leftNumber");
            String middleNumber = (String)m.get("middleNumber");
            String rightNumber = (String)m.get("rightNumber");

            RSZNumber subject;
            try {
              subject = new RSZNumber(leftNumber, middleNumber, rightNumber);
            } catch (PropertyException pe) {
              subject = new RSZNumber();
            }
            return subject;
          }
    };
}
