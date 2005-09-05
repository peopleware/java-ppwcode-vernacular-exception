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

import be.peopleware.bean_V.PropertyException;


/**
 * A test class for VATNumber.
 *
 * @author    wlambrec
 */
public class _Contract_VATNumber extends ClassContract {

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

  public _Contract_VATNumber() throws TorytException {
    super(VATNumber.class);

    // constructors
    addConstructorContract(
      new ConstructorContract(this, VATNumber.class, "VATNumber(java.lang.String,java.lang.String,java.lang.String)") {
        public String[] getFormalParameters() {
          return new String[] {"leftNumber", "middleNumber", "rightNumber"};
        }

        {
          // no preconditions
          // postconditions
          addPostcondition(new Condition() {
            public boolean validate(Map context) {
              VATNumber subject = (VATNumber)context.get(SUBJECT_KEY);
              String leftNumber = (String)context.get("leftNumber");

              return (subject.getLeftNumber().equals(leftNumber));
            }});

          addPostcondition(new Condition() {
            public boolean validate(Map context) {
              VATNumber subject = (VATNumber)context.get(SUBJECT_KEY);
              String middleNumber = (String)context.get("middleNumber");

              return (subject.getMiddleNumber().equals(middleNumber));
            }});

          addPostcondition(new Condition() {
            public boolean validate(Map context) {
              VATNumber subject = (VATNumber)context.get(SUBJECT_KEY);
              String rightNumber = (String)context.get("rightNumber");

              return (subject.getRightNumber().equals(rightNumber));
            }});

          addExceptionCondition(new ExceptionCondition(PropertyException.class) {
            public boolean validate(Map context) {
              String leftNumber = (String)context.get("leftNumber");

              PropertyException pExc = (PropertyException)context.get(EXCEPTION_KEY);
              return  ( leftNumber == null )
                      && (pExc.reportsOn(
                          VATNumber.class, "leftNumber",
                              "LEFT_NUMBER_IS_NULL", null)
                      );
            }});

          addExceptionCondition(new ExceptionCondition(PropertyException.class) {
            public boolean validate(Map context) {
              String leftNumber = (String)context.get("leftNumber");

              PropertyException pExc = (PropertyException)context.get(EXCEPTION_KEY);

              return  ((leftNumber != null && !Pattern.matches(VATNumber.LEFT_PATTERN, leftNumber))
                       && pExc.reportsOn(
                                        VATNumber.class, "leftNumber",
                                        "LEFT_NUMBER_INVALID_PATTERN", null)
                      );
            }});

          addExceptionCondition(new ExceptionCondition(PropertyException.class) {
            public boolean validate(Map context) {
              String middleNumber = (String)context.get("middleNumber");

              PropertyException pExc = (PropertyException)context.get(EXCEPTION_KEY);

              return   ((middleNumber == null)
                          && pExc.reportsOn(
                                        VATNumber.class, "middleNumber",
                                       "MIDDLE_NUMBER_IS_NULL", null)
                       );
            }});

          addExceptionCondition(new ExceptionCondition(PropertyException.class) {
            public boolean validate(Map context) {
              String middleNumber = (String)context.get("middleNumber");

              PropertyException pExc = (PropertyException)context.get(EXCEPTION_KEY);

              return (  (middleNumber != null && !Pattern.matches(VATNumber.MIDDLE_PATTERN, middleNumber))
                        && pExc.reportsOn(
                                       VATNumber.class, "middleNumber",
                                       "MIDDLE_NUMBER_INVALID_PATTERN", null)
                     );
            }});

          addExceptionCondition(new ExceptionCondition(PropertyException.class) {
            public boolean validate(Map context) {
              String rightNumber = (String)context.get("rightNumber");

              PropertyException pExc = (PropertyException)context.get(EXCEPTION_KEY);

              return (  (rightNumber == null)
                        && pExc.reportsOn(
                                       VATNumber.class, "rightNumber",
                                       "RIGHT_NUMBER_IS_NULL", null)
                     );
            }});


          addExceptionCondition(new ExceptionCondition(PropertyException.class) {
            public boolean validate(Map context) {
              String rightNumber = (String)context.get("rightNumber");

              PropertyException pExc = (PropertyException)context.get(EXCEPTION_KEY);

              return (  (rightNumber != null && !Pattern.matches(VATNumber.RIGHT_PATTERN, rightNumber))
                         && pExc.reportsOn(
                                      VATNumber.class, "rightNumber",
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
                           !VATNumber.checkVATNumber(leftNumber, middleNumber, rightNumber)
                        )
                        &&  pExc.reportsOn(VATNumber.class, null, "INVALID_CHECK", null)
                      );
            }});

          close();
        }

        public StraightList getTestCases() throws TorytException {
          return new LazyCombinationStraightList(
              new String[] {"leftNumber", "middleNumber", "rightNumber"},
              new StraightList[] {
                  new ArrayStraightList( new String[] {"123", "222", "999"}),
                  new ArrayStraightList( new String[] {"456", "333", "888"}),
                  new ArrayStraightList( new String[] {"749", "403", "767"})
              });
        }
      }
    );

    addConstructorContract(
        new ConstructorContract(this, VATNumber.class, "VATNumber()") {
          {
            // no preconditions
            // postconditions
            addPostcondition(new Condition() {
              public boolean validate(Map context) {
                VATNumber subject = (VATNumber)context.get(SUBJECT_KEY);
                return (subject.getLeftNumber().equals("123"));
              }});

            addPostcondition(new Condition() {
              public boolean validate(Map context) {
                VATNumber subject = (VATNumber)context.get(SUBJECT_KEY);
                return (subject.getMiddleNumber().equals("456"));
              }});

            addPostcondition(new Condition() {
              public boolean validate(Map context) {
                VATNumber subject = (VATNumber)context.get(SUBJECT_KEY);
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
        new ConstructorContract(this, VATNumber.class, "VATNumber(be.peopleware.value_I.VATNumber)") {
         public String[] getFormalParameters() {
            return new String[] {"vatNumber"};
          }

         {
            // no preconditions
            // postconditions
            addPostcondition(new Condition() {
              public boolean validate(Map context) {
                VATNumber subject = (VATNumber)context.get(SUBJECT_KEY);
                VATNumber vatNumber = (VATNumber)context.get("vatNumber");

                return (subject.getLeftNumber().equals(vatNumber.getLeftNumber()));
              }});

            addPostcondition(new Condition() {
              public boolean validate(Map context) {
                VATNumber subject = (VATNumber)context.get(SUBJECT_KEY);
                VATNumber vatNumber = (VATNumber)context.get("vatNumber");

                return (subject.getMiddleNumber().equals(vatNumber.getMiddleNumber()));
              }});

            addPostcondition(new Condition() {
              public boolean validate(Map context) {
                VATNumber subject = (VATNumber)context.get(SUBJECT_KEY);
                VATNumber vatNumber = (VATNumber)context.get("vatNumber");

                return (subject.getRightNumber().equals(vatNumber.getRightNumber()));
              }});

            close();
        }

         public StraightList getTestCases() throws TorytException {
          return new LazyCombinationStraightList(
              new String[] {"vatNumber"},
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
      new MutatorContract(this, VATNumber.class, "checkVATNumber(java.lang.String,java.lang.String,java.lang.String)") {
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
                return Pattern.matches(VATNumber.LEFT_PATTERN, leftNumber);
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
              return Pattern.matches(VATNumber.MIDDLE_PATTERN, middleNumber);
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
              return Pattern.matches(VATNumber.RIGHT_PATTERN, rightNumber);
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
              new StraightList[] {  getCases(),
                                    new ArrayStraightList( new String[] {"123", "222", "999"}),
                                    new ArrayStraightList( new String[] {"456", "333", "888"}),
                                    new ArrayStraightList( new String[] {"749", "403", "767"})
              });
        }
      }
    );

    addInstanceMethodContract(
        new MutatorContract(this, VATNumber.class, "equals(java.lang.Object)") {

          public String[] getFormalParameters() {
            return new String[] {"o"};
          }

          {
            // no preconditions
            // postconditions
            addPostcondition(new Condition() {
              public boolean validate(Map context) {
                VATNumber subject = (VATNumber) context.get(SUBJECT_KEY);
                Object o = context.get("o");
                boolean result = ((Boolean)context.get(RESULT_KEY)).booleanValue();
                return result
                       ==
                        ( o instanceof VATNumber &&
                                   ((VATNumber) o).getLeftNumber().equals(subject.getLeftNumber()) &&
                                   ((VATNumber) o).getMiddleNumber().equals(subject.getMiddleNumber()) &&
                                   ((VATNumber) o).getRightNumber().equals(subject.getRightNumber())
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
        new MutatorContract(this, VATNumber.class, "hashCode()") {

          public String[] getFormalParameters() {
            return new String[] {};
          }

          {
            // no preconditions
            // postconditions
            addPostcondition(new Condition() {
              public boolean validate(Map context) {
                VATNumber subject = (VATNumber)context.get(SUBJECT_KEY);
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
        new MutatorContract(this, VATNumber.class, "toString()") {

        public String[] getFormalParameters() {
          return new String[] {};
        }

        {
          // no preconditions
          // postconditions
          addPostcondition(new Condition() {
            public boolean validate(Map context) {
              VATNumber subject = (VATNumber) context.get(SUBJECT_KEY);
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
        VATNumber subject = (VATNumber) context.get(SUBJECT_KEY);
        return (subject.getLeftNumber() != null);
      }
    });

    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        VATNumber subject = (VATNumber) context.get(SUBJECT_KEY);
        return (Pattern.matches(VATNumber.LEFT_PATTERN, subject.getLeftNumber()));
      }
    });

    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        VATNumber subject = (VATNumber) context.get(SUBJECT_KEY);
        return (subject.getMiddleNumber() != null);
      }
    });

    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        VATNumber subject = (VATNumber) context.get(SUBJECT_KEY);
        return (Pattern.matches(VATNumber.MIDDLE_PATTERN, subject.getMiddleNumber()));
      }
    });

    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        VATNumber subject = (VATNumber) context.get(SUBJECT_KEY);
        return (subject.getRightNumber() != null);
      }
    });

    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        VATNumber subject = (VATNumber) context.get(SUBJECT_KEY);
        return (Pattern.matches(VATNumber.RIGHT_PATTERN, subject.getRightNumber()));
      }
    });

    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        VATNumber subject = (VATNumber) context.get(SUBJECT_KEY);
        return (VATNumber.checkVATNumber(subject.getLeftNumber(), subject.getMiddleNumber(), subject.getRightNumber()));
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

            VATNumber subject;
            try {
              subject = new VATNumber(leftNumber, middleNumber, rightNumber);
            } catch (PropertyException pe) {
              subject = new VATNumber();
            }
            return subject;
          }
    };
}
