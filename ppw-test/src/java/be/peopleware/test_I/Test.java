package be.peopleware.test_I;


import java.util.Date;

import be.peopleware.test_I.java.lang._Test_Object;



/**
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public abstract class Test {

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
   * Run a testcase.
   *
   * @param     args
   *            Arguments for the test.
   */
  public static void main(final String[] args) {
    System.out.println("Starting tests."); //$NON-NLS-1$
    System.out.println(new Date());
    System.out.println("\n"); //$NON-NLS-1$
    for (int i = 0; i < args.length; i++) {
      System.out.println("Starting test for \"" + args[i] + "\"."); //$NON-NLS-1$ //$NON-NLS-2$
      Class testClass = instantiateClass(args[i]);
      if (testClass != null) {
        _Test_Object test = getTestInstance(testClass, args[i]);
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
      System.out.println("ERROR: class \"" + clazz + "\" not found.\n"); //$NON-NLS-2$ //$NON-NLS-1$
    }
    catch (ExceptionInInitializerError eiiErr) {
      System.out.println("ERROR: \"" + clazz //$NON-NLS-1$
          + "\" threw an exception during static initialisation.\n"); //$NON-NLS-1$
      eiiErr.printStackTrace();
      System.out.println();
    }
    catch (LinkageError lErr) {
      System.out.println("ERROR: a linkage error occured when loading class\"" //$NON-NLS-1$
                         + clazz + "\". This is extreme.\n"); //$NON-NLS-1$
      lErr.printStackTrace();
      System.out.println();
    }
    return result;
  }

  private static _Test_Object getTestInstance(final Class testClass,
                                      final String clazz) {
    _Test_Object test = null;
    try {
      test = (_Test_Object)testClass.newInstance();
        // throws ClassCastException if this was not a test class
    }
    catch (ClassCastException ccExc) {
      System.out.println("ERROR: \"" + clazz //$NON-NLS-1$
          + "\" does not extend \"be.peopleware.test._Test_Object\".\n"); //$NON-NLS-1$
    }
    catch (IllegalAccessException iaExc) {
      System.out.println("ERROR: \"" + clazz //$NON-NLS-1$
          + "\" no-args constructor is not public.\n"); //$NON-NLS-1$
    }
    catch (InstantiationException iExc) {
      System.out.println("ERROR: \"" + clazz + "\" cannot be instantiated " //$NON-NLS-1$ //$NON-NLS-2$
          + "(class is abstract, interface, array, primitive type, void, or " //$NON-NLS-1$
          + "does not have a no-args constructor).\n"); //$NON-NLS-1$
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

  private static void runTest(final _Test_Object test, final String clazz) {
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
    System.out.println("_Test_Object for \"" + clazz + "\" done.\n^\n^"); //$NON-NLS-1$ //$NON-NLS-2$
  }

}
