package be.peopleware.value_II;


import be.peopleware.bean_V.PropertyException;
import be.peopleware.value_II.EnterpriseNumber;
import junit.framework.TestCase;



public class TestEnterpriseNumber extends TestCase {

  public static void main(String[] args) {
    junit.swingui.TestRunner.run(TestEnterpriseNumber.class);
  }

  public void testEnterpriseNumber_String_String_String_valid1() {
    String left = "0123"; String middle = "456"; String right = "749";
    try {
      new EnterpriseNumber(left, middle, right);
    }
    catch (PropertyException exc) {
      fail("failed with exception: " + exc);
    }
  }

  public void testEnterpriseNumber_String_String_String_valid2() {
    String left = "1222"; String middle = "333"; String right = "424";
    try {
      new EnterpriseNumber(left, middle, right);
    }
    catch (PropertyException exc) {
      fail("failed with exception: " + exc);
    }
  }

  public void testEnterpriseNumber_String_String_String_valid3() {
    String left = "9999"; String middle = "888"; String right = "762";
    try {
      new EnterpriseNumber(left, middle, right);
    }
    catch (PropertyException exc) {
      fail("failed with exception: " + exc);
    }
  }

  public void testEnterpriseNumber_String_String_String_wrong_check() {
    String left = "0123"; String middle = "456"; String right = "750";
    try {
      new EnterpriseNumber(left, middle, right);
      fail("expected an exception, and didn't get one");
    }
    catch (PropertyException exc) {
      // expect an exception
    }
  }

  public void testEnterpriseNumber_String_String_String_wrong_first_pattern() {
    String left = "01234"; String middle = "56"; String right = "789";
    try {
      new EnterpriseNumber(left, middle, right);
      fail("expected an exception, and didn't get one");
    }
    catch (PropertyException exc) {
      // expect an exception
    }
  }

}

