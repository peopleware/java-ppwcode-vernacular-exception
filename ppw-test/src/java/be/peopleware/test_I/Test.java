/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/


package be.peopleware.test_I;


import java.util.ArrayList;


/**
 * The superclass for all unit tests.
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public abstract class Test extends CaseProvider {

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
   * The main method to start the tests. FQCN of test classes (..._Test_...)
   * need to be given as CL parameters.
   */
  public static void main(final String[] args) {
    System.out.println("Starting tests.\n"); //$NON-NLS-1$
    for (int i = 0; i < args.length; i++) {
      System.out.println("Starting test for \"" //$NON-NLS-1$
                         + args[i]
                         + "\"."); //$NON-NLS-1$
      Class testClass = instantiateClass(args[i]);
      if (testClass != null) {
        Test test = getTestInstance(testClass, args[i]);
        if (test != null) {
          runTest(test, args[i]);
        }
      }
    }
    System.out.println("Tests done."); //$NON-NLS-1$
  }

  private static Class instantiateClass(final String clazz) {
    Class result = null;
    try {
      result = Class.forName(clazz);
    }
    catch (ClassNotFoundException cnfExc) {
      System.out.println("ERROR: class \"" //$NON-NLS-1$
                          + clazz
                          + "\" not found.\n"); //$NON-NLS-1$
    }
    catch (ExceptionInInitializerError eiiErr) {
      System.out.println("ERROR: \"" + clazz //$NON-NLS-1$
          + "\" threw an exception during " //$NON-NLS-1$
          + "static initialisation.\n"); //$NON-NLS-1$
      eiiErr.printStackTrace();
      System.out.println();
    }
    catch (LinkageError lErr) {
      System.out.println("ERROR: a linkage error " //$NON-NLS-1$
                         + "occured when loading class\"" //$NON-NLS-1$
                         + clazz + "\". This is extreme.\n"); //$NON-NLS-1$
      lErr.printStackTrace();
      System.out.println();
    }
    return result;
  }

  private static Test getTestInstance(final Class testClass,
                                      final String clazz) {
    Test test = null;
    try {
      test = (Test)testClass.newInstance();
        // throws ClassCastException if this was not a test class
    }
    catch (ClassCastException ccExc) {
      System.out.println("ERROR: \"" + clazz //$NON-NLS-1$
          + "\" does not extend \"be.peopleware.test_I.Test\".\n"); //$NON-NLS-1$
    }
    catch (IllegalAccessException iaExc) {
      System.out.println("ERROR: \"" + clazz //$NON-NLS-1$
          + "\" no-args constructor is not public.\n"); //$NON-NLS-1$
    }
    catch (InstantiationException iExc) {
      System.out.println("ERROR: \"" + clazz //$NON-NLS-1$
          + "\" cannot be instantiated " //$NON-NLS-1$
          + "(class is abstract, interface, array, primitive " //$NON-NLS-1$
          + "type, void, or does not have a no-args " //$NON-NLS-1$
          + "constructor).\n"); //$NON-NLS-1$
    }
    catch (ExceptionInInitializerError eiiExc) {
      System.out.println("ERROR: \"" + clazz //$NON-NLS-1$
          + "\" no-args constructor threw an exception:"); //$NON-NLS-1$
      eiiExc.getCause().printStackTrace();
      System.out.println();
    }
    catch (SecurityException sExc) {
      System.out.println("ERROR: \"" + clazz //$NON-NLS-1$
          + "\" security exception during construction."); //$NON-NLS-1$
      sExc.getCause().printStackTrace();
      System.out.println();
    }
    return test;
  }

  /**
   * Run a particular test class (a subtype of this class).
   */
  private static void runTest(final Test test, final String clazz) {
    try {
      test.test();
    }
    catch (TooManyFailuresError tmfErr) {
      System.out.println("ABORT: " + tmfErr.toString()); //$NON-NLS-1$
    }
    catch (Throwable t) {
      System.out.println("ERROR: \"" + clazz //$NON-NLS-1$
          + "\" test ended with a throwable."); //$NON-NLS-1$
      t.printStackTrace();
      System.out.println();
    }
    System.out.println(test);
    test.printErrors(System.out);
    System.out.println("Test for \"" //$NON-NLS-1$
                       + clazz
                       + "\" done.\n^\n^"); //$NON-NLS-1$
  }


  /**
   * Subclasses can call this method during a test
   * to check whether the <code>condition</code> is
   * satisfied. If so, NOP happens. If not, this is logged
   * as a failure.
   *
   * @throws TooManyFailuresError
   *         The number of errors surpassed the threshold that makes
   *         it silly to continue the test. First fix these errors.
   */
  protected final void validate(final boolean condition)
      throws TooManyFailuresError {
    if (!condition) {
      Throwable t = new Throwable();
      addFailure(t.getStackTrace());
    }
  }

  /**
   * Subclasses can call this method during a test
   * to signal that an unexpected throwable occured.
   * This is logged as a failure.
   *
   * @throws TooManyFailuresError
   *         The number of errors surpassed the threshold that makes
   *         it silly to continue the test. First fix these errors.
   */
  protected final void unexpectedThrowable(final Throwable t)
      throws TooManyFailuresError {
    addFailure(t);
  }

  /**
   * Report on the test. Call this after calling {@link #test()}.
   */
  public final String toString() {
    String result = null;
    if ($failures.size() == 0) {
      result = "Class ok"; //$NON-NLS-1$
    }
    else {
      result = $failures.size() + " errors"; //$NON-NLS-1$
    }
    return result;
  }

  /**
   * If there are errors, write a report on the errors to
   * <code>out</code>.
   */
  public final void printErrors(final java.io.PrintStream out) {
    if (!$failures.isEmpty()) {
      out.println("----"); //$NON-NLS-1$
      java.util.Iterator iter = $failures.iterator();
      while (iter.hasNext()) {
        Object next = iter.next();
        if (next instanceof StackTraceElement[]) {
          printStackTrace(next, out);
        }
        else if (next instanceof Throwable) {
          printThrowable(next, out);
        }
        else {
          assert false : "representation invariant of $failures"; //$NON-NLS-1$
        }
        out.println("----"); //$NON-NLS-1$
      }
    }
  }

  /**
   * @pre st instanceof StackTraceElement[];
   */
  private void printStackTrace(final Object st, final java.io.PrintStream out) {
    StackTraceElement[] errorTrace = (StackTraceElement[])st;
    for (int i = 0; i < errorTrace.length; i++) {
      out.println("    " + errorTrace[i]); //$NON-NLS-1$
    }
  }

  /**
   * @pre t instanceof Throwable;
   */
  private void printThrowable(final Object t, final java.io.PrintStream out) {
    ((Throwable)t).printStackTrace(out);
  }

  /**
   * If there are too many errors, we give up.
   * <strong>= {@value}</strong>
   */
  public static final int UPPER_FAILURE_LIMIT = 20;

  /**
   * Thrown when there are so much errors that it is silly to continue
   * the test. Fix these errors first.
   */
  public class TooManyFailuresError extends Error {

    /**
     * The message to show when there are too many errors.
     */
    public String toString() {
      return "There are more then " //$NON-NLS-1$
              + UPPER_FAILURE_LIMIT
              + " failures. Aborting."; //$NON-NLS-1$
    }

  }

  /**
   * Add a failure (Throwable or StackTrace) to {@link #$failures}.
   * Don't do that, but throw an error, if there are too many failures.
   *
   * @throws TooManyFailuresError
   *         The number of errors surpassed the threshold that makes
   *         it silly to continue the test. First fix these errors.
   */
  private void addFailure(final Object error) throws TooManyFailuresError {
    if ($failures.size() > UPPER_FAILURE_LIMIT) {
      throw new TooManyFailuresError();
    }
    $failures.add(error);
  }

  /**
   * @invar $failures != null;
   * @invar ! $failures.contains(null);
   * @invar (foreach Object o; $failures.contains(o);
   *            (o instanceof Throwable)
   *              || (o instanceof StackTraceElement[]));
   */
  private ArrayList $failures = new ArrayList();

  /**
   * Perform the test of the class this test class was created
   * for. Calls {@link #testClassMethods()} and
   * {@link #testInstanceMethods()}.
   *
   * @throws TooManyFailuresError
   *         The number of errors surpassed the threshold that makes
   *         it silly to continue the test. First fix these errors.
   * @throws Throwable
   *         Anything can wrong during a test.
   */
  public final void test() throws TooManyFailuresError, Throwable {
    testClassMethods();
    testInstanceMethods();
  }

  /**
   * Called by {@link #test()} to perform tests on class methods.
   * Tests on class methods (static methods) are not repeated in
   * subtypes.
   *
   * @throws TooManyFailuresError
   *         The number of errors surpassed the threshold that makes
   *         it silly to continue the test. First fix these errors.
   */
  protected abstract void testClassMethods() throws TooManyFailuresError;

  /**
   * Called by {@link #test()} to perform tests on instance methods.
   * Implementation should start with a call to
   * <code>super.testInstanceMethods()</code>, because inherited methods
   * should be tested again in the context of the subtype.
   *
   * @throws TooManyFailuresError
   *         The number of errors surpassed the threshold that makes
   *         it silly to continue the test. First fix these errors.
   */
  protected abstract void testInstanceMethods() throws TooManyFailuresError;

}
