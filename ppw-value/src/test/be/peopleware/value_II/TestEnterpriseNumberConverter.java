package be.peopleware.value_II;

import junit.framework.TestCase;



public class TestEnterpriseNumberConverter extends TestCase {

  public static void main(String[] args) {
    junit.swingui.TestRunner.run(TestEnterpriseNumberConverter.class);
  }

  private EnterpriseNumberConverter $converter;

  protected void setUp() throws Exception {
    super.setUp();
    $converter = new EnterpriseNumberConverter();
  }

  protected void tearDown() throws Exception {
    $converter = null;
    super.tearDown();
  }

  /*
   * Test method for 'be.peopleware.value_II.EnterpriseNumberConverter.getAsObject(FacesContext, UIComponent, String)'
   */
  public final void testGetAsObject() {
    String in = "0123-456/749";
    Object result = $converter.getAsObject(null, null, in);
    assertTrue(result instanceof EnterpriseNumber);
    EnterpriseNumber en = (EnterpriseNumber)result;
    assertEquals("0123", en.getLeftNumber());
    assertEquals("456", en.getMiddleNumber());
    assertEquals("749", en.getRightNumber());
  }

  /*
   * Test method for 'be.peopleware.value_II.EnterpriseNumberConverter.getAsString(FacesContext, UIComponent, Object)'
   */
  public final void testGetAsString() {
    EnterpriseNumber en = new EnterpriseNumber();
    String result = $converter.getAsString(null, null, en);
    assertEquals("0123 456 749", result);
  }

}

