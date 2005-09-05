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
 * A test class for NationalNumber.
 *
 * @author    wlambrec
 */
public class _Contract_NationalNumber extends ClassContract {

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

  public _Contract_NationalNumber() throws TorytException {
    super(NationalNumber.class);

    // constructors
    addConstructorContract(
      new ConstructorContract(this, NationalNumber.class, "NationalNumber(java.lang.String,java.lang.String,java.lang.String)") {
        public String[] getFormalParameters() {
          return new String[] {"leftNumber", "middleNumber", "rightNumber"};
        }

        {
        	// no preconditions
          // postconditions
          addPostcondition(new Condition() {
            public boolean validate(Map context) {
            	NationalNumber result = (NationalNumber)context.get(SUBJECT_KEY);
            	String leftNumber = (String)context.get("leftNumber");

            	return (result.getLeftNumber().equals(leftNumber));
            }});

          addPostcondition(new Condition() {
            public boolean validate(Map context) {
            	NationalNumber result = (NationalNumber)context.get(SUBJECT_KEY);
            	String middleNumber = (String)context.get("middleNumber");

            	return (result.getMiddleNumber().equals(middleNumber));
            }});

          addPostcondition(new Condition() {
            public boolean validate(Map context) {
            	NationalNumber result = (NationalNumber)context.get(SUBJECT_KEY);
            	String rightNumber = (String)context.get("rightNumber");

            	return (result.getRightNumber().equals(rightNumber));
            }});

          addExceptionCondition(new ExceptionCondition(PropertyException.class) {
            public boolean validate(Map context) {
            	String leftNumber = (String)context.get("leftNumber");

            	PropertyException pExc = (PropertyException)context.get(EXCEPTION_KEY);
            	return 	( leftNumber == null )
						          && (pExc.reportsOn(
						          		NationalNumber.class, "leftNumber",
								              "LEFT_NUMBER_IS_NULL", null)
								      );
            }});

          addExceptionCondition(new ExceptionCondition(PropertyException.class) {
            public boolean validate(Map context) {
            	String leftNumber = (String)context.get("leftNumber");

            	PropertyException pExc = (PropertyException)context.get(EXCEPTION_KEY);

              return	((leftNumber != null && !Pattern.matches(NationalNumber.LEFT_PATTERN, leftNumber))
						           && pExc.reportsOn(
						           									NationalNumber.class, "leftNumber",
																				"LEFT_NUMBER_INVALID_PATTERN", null)
              				);
            }});

          addExceptionCondition(new ExceptionCondition(PropertyException.class) {
            public boolean validate(Map context) {
            	String middleNumber = (String)context.get("middleNumber");

            	PropertyException pExc = (PropertyException)context.get(EXCEPTION_KEY);

              return   ((middleNumber == null)
						           		&& pExc.reportsOn(
						           									NationalNumber.class, "middleNumber",
						                           "MIDDLE_NUMBER_IS_NULL", null)
              				 );
            }});

          addExceptionCondition(new ExceptionCondition(PropertyException.class) {
            public boolean validate(Map context) {
            	String middleNumber = (String)context.get("middleNumber");

            	PropertyException pExc = (PropertyException)context.get(EXCEPTION_KEY);

              return ( 	(middleNumber != null && !Pattern.matches(NationalNumber.MIDDLE_PATTERN, middleNumber))
						           	&& pExc.reportsOn(
						           								 NationalNumber.class, "middleNumber",
																			 "MIDDLE_NUMBER_INVALID_PATTERN", null)
              			 );
            }});

          addExceptionCondition(new ExceptionCondition(PropertyException.class) {
            public boolean validate(Map context) {
            	String rightNumber = (String)context.get("rightNumber");

            	PropertyException pExc = (PropertyException)context.get(EXCEPTION_KEY);

              return (	(rightNumber == null)
						          	&& pExc.reportsOn(
						          								 NationalNumber.class, "rightNumber",
						                           "RIGHT_NUMBER_IS_NULL", null)
              			 );
            }});


          addExceptionCondition(new ExceptionCondition(PropertyException.class) {
            public boolean validate(Map context) {
            	String rightNumber = (String)context.get("rightNumber");

            	PropertyException pExc = (PropertyException)context.get(EXCEPTION_KEY);

              return (	(rightNumber != null && !Pattern.matches(NationalNumber.RIGHT_PATTERN, rightNumber))
						             && pExc.reportsOn(
						             							NationalNumber.class, "rightNumber",
						                           "RIGHT_NUMBER_INVALID_PATTERN", null)
										 );
            }});

          addExceptionCondition(new ExceptionCondition(PropertyException.class) {
            public boolean validate(Map context) {
            	String leftNumber = (String)context.get("leftNumber");
            	String middleNumber = (String)context.get("middleNumber");
            	String rightNumber = (String)context.get("rightNumber");

            	PropertyException pExc = (PropertyException)context.get(EXCEPTION_KEY);
              return (	(  leftNumber != null &&
              	           middleNumber != null &&
              	           rightNumber != null &&
              	           !NationalNumber.checkNationalNumber(leftNumber, middleNumber, rightNumber)
              	        )
              	        &&  pExc.reportsOn(NationalNumber.class, null, "INVALID_CHECK", null)
											);
            }});

          close();
        }

        public StraightList getTestCases() throws TorytException {
          return new LazyCombinationStraightList(
              new String[] {"leftNumber", "middleNumber", "rightNumber"},
							new StraightList[] {	new ArrayStraightList( new String[] {"000000", "781101", "700509", "760905"}),
              											new ArrayStraightList( new String[] {"000", "456", "169", "145"}),
																		new ArrayStraightList( new String[] {"97", "65", "96", "06"})
              });
        }
      }
    );

    addConstructorContract(
        new ConstructorContract(this, NationalNumber.class, "NationalNumber()") {
          {
          	// no preconditions
            // postconditions
            addPostcondition(new Condition() {
              public boolean validate(Map context) {
              	NationalNumber result = (NationalNumber)context.get(SUBJECT_KEY);
              	return (result.getLeftNumber().equals("000000"));
              }});

            addPostcondition(new Condition() {
              public boolean validate(Map context) {
              	NationalNumber result = (NationalNumber)context.get(SUBJECT_KEY);
              	return (result.getMiddleNumber().equals("000"));
              }});

            addPostcondition(new Condition() {
              public boolean validate(Map context) {
              	NationalNumber result = (NationalNumber)context.get(SUBJECT_KEY);
              	return (result.getRightNumber().equals("97"));
              }});

            close();
          }

          public StraightList getTestCases() throws TorytException {
            return EmptyStraightList.INSTANCE;
          }
      }
    );

    addConstructorContract(
        new ConstructorContract(this, NationalNumber.class, "NationalNumber(be.peopleware.value_I.NationalNumber)") {
         public String[] getFormalParameters() {
            return new String[] {"nationalNumber"};
          }

         {
            // no preconditions
            // postconditions
            addPostcondition(new Condition() {
              public boolean validate(Map context) {
              	NationalNumber result = (NationalNumber)context.get(SUBJECT_KEY);
              	NationalNumber nationalNumber = (NationalNumber)context.get("nationalNumber");

              	return (result.getLeftNumber().equals(nationalNumber.getLeftNumber()));
              }});

            addPostcondition(new Condition() {
              public boolean validate(Map context) {
              	NationalNumber result = (NationalNumber)context.get(SUBJECT_KEY);
              	NationalNumber nationalNumber = (NationalNumber)context.get("nationalNumber");

              	return (result.getMiddleNumber().equals(nationalNumber.getMiddleNumber()));
              }});

            addPostcondition(new Condition() {
              public boolean validate(Map context) {
              	NationalNumber result = (NationalNumber)context.get(SUBJECT_KEY);
              	NationalNumber nationalNumber = (NationalNumber)context.get("nationalNumber");

              	return (result.getRightNumber().equals(nationalNumber.getRightNumber()));
              }});

            close();
        }

         public StraightList getTestCases() throws TorytException {
          return new LazyCombinationStraightList(
              new String[] {"nationalNumber"},
							new StraightList[] {	getCases() }
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
      new MutatorContract(this, NationalNumber.class, "checkNationalNumber(java.lang.String,java.lang.String,java.lang.String)") {
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
              	return Pattern.matches(NationalNumber.LEFT_PATTERN, leftNumber);
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
            	return Pattern.matches(NationalNumber.MIDDLE_PATTERN, middleNumber);
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
            	return Pattern.matches(NationalNumber.RIGHT_PATTERN, rightNumber);
      			}
      		});

          addPostcondition(new Condition() {
            public boolean validate(Map context) {
            	boolean result = ((Boolean)context.get(RESULT_KEY)).booleanValue();
            	String leftNumber = (String)context.get("leftNumber");
            	String middleNumber = (String)context.get("middleNumber");
            	String rightNumber = (String)context.get("rightNumber");
            	return ( result == ((97 - Integer.parseInt(leftNumber + middleNumber) % 97) == (Integer.parseInt(rightNumber)))
            	    || ((97 - (Integer.parseInt(leftNumber + middleNumber) + NationalNumber.TWO_BILLION) % 97) == (Integer.parseInt(rightNumber))));
            }});

      		close();
      	}

        public StraightList getTestCases() throws TorytException {
          return new LazyCombinationStraightList(
              new String[] {SUBJECT_KEY, "leftNumber", "middleNumber", "rightNumber"},
							new StraightList[] {	getCases(),
              											new ArrayStraightList( new String[] {"000000", "781101", "700509", "760905", "020314", "030617"}),
              											new ArrayStraightList( new String[] {"000", "456", "169", "145", "120", "059"}),
																		new ArrayStraightList( new String[] {"97", "65", "96", "06", "37", "50"})
              });
        }
      }
    );

    addInstanceMethodContract(
        new MutatorContract(this, NationalNumber.class, "equals(java.lang.Object)") {

        	public String[] getFormalParameters() {
        		return new String[] {"o"};
        	}

        	{
		        // no preconditions
		        // postconditions
		        addPostcondition(new Condition() {
		          public boolean validate(Map context) {
		            NationalNumber subject = (NationalNumber) context.get(SUBJECT_KEY);
		            Object o = context.get("o");
		            boolean result = ((Boolean)context.get(RESULT_KEY)).booleanValue();
		            return result
		                   ==
		                    ( o instanceof NationalNumber &&
											             ((NationalNumber) o).getLeftNumber().equals(subject.getLeftNumber()) &&
											             ((NationalNumber) o).getMiddleNumber().equals(subject.getMiddleNumber()) &&
											             ((NationalNumber) o).getRightNumber().equals(subject.getRightNumber())
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
        new MutatorContract(this, NationalNumber.class, "hashCode()") {

		      public String[] getFormalParameters() {
		        return new String[] {};
		      }

		      {
		        // no preconditions
		        // postconditions
		        addPostcondition(new Condition() {
		          public boolean validate(Map context) {
		            NationalNumber subject = (NationalNumber)context.get(SUBJECT_KEY);
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
        new MutatorContract(this, NationalNumber.class, "toString()") {

	      public String[] getFormalParameters() {
	        return new String[] {};
	      }

	      {
	        // no preconditions
	        // postconditions
	        addPostcondition(new Condition() {
	          public boolean validate(Map context) {
	            NationalNumber subject = (NationalNumber) context.get(SUBJECT_KEY);
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
        NationalNumber subject = (NationalNumber) context.get(SUBJECT_KEY);
        return (subject.getLeftNumber() != null);
      }
    });

    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        NationalNumber subject = (NationalNumber) context.get(SUBJECT_KEY);
        return (Pattern.matches(NationalNumber.LEFT_PATTERN, subject.getLeftNumber()));
      }
    });

    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        NationalNumber subject = (NationalNumber) context.get(SUBJECT_KEY);
        return (subject.getMiddleNumber() != null);
      }
    });

    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        NationalNumber subject = (NationalNumber) context.get(SUBJECT_KEY);
        return (Pattern.matches(NationalNumber.MIDDLE_PATTERN, subject.getMiddleNumber()));
      }
    });

    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        NationalNumber subject = (NationalNumber) context.get(SUBJECT_KEY);
        return (subject.getRightNumber() != null);
      }
    });

    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        NationalNumber subject = (NationalNumber) context.get(SUBJECT_KEY);
        return (Pattern.matches(NationalNumber.RIGHT_PATTERN, subject.getRightNumber()));
      }
    });

    addTypeInvariantCondition(new Condition() {
      public boolean validate(Map context) {
        NationalNumber subject = (NationalNumber) context.get(SUBJECT_KEY);
        return (NationalNumber.checkNationalNumber(subject.getLeftNumber(), subject.getMiddleNumber(), subject.getRightNumber()));
      }
    });

    close();

  }

  public StraightList getCasesMaps() throws TorytException {
      return new LazyCombinationStraightList(
                  new String[] {"leftNumber", "middleNumber", "rightNumber"},
									new StraightList[] {	new ArrayStraightList(new String[] {"000000", "781101", "700509", "760905", "020314", "030617"}),
                  											new ArrayStraightList(new String[] {"000", "456", "169", "145", "120", "059"}),
																				new ArrayStraightList(new String[] {"97", "65", "96", "06", "37", "50"})
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

            NationalNumber subject;
            try {
            	subject = new NationalNumber(leftNumber, middleNumber, rightNumber);
            } catch (PropertyException pe) {
            	subject = new NationalNumber();
            }
            return subject;
          }
    };
}
