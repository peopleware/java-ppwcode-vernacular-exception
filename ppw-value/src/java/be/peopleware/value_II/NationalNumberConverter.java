/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.value_II;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;


/**
 * Converter for {@link NationalNumber}. The {@link #getAsObject(FacesContext, UIComponent, String)}
 * method is very lenient: separators can be left out, or any of the characters
 * <code><var>space</var>-/|*:</code>.
 *
 * To activate this converter, the following entry has to appear in <kbd>faces-config.xml</kbd>:
 * <pre>
 * &lt;converter&gt;
 *   &lt;converter-for-class&gt;be.peopleware.value_II.NationalNumber&lt;/converter-for-class&gt;
 *   &lt;converter-class&gt;be.peopleware.value_II.NationalNumberConverter&lt;/converter-class&gt;
 * &lt;/converter&gt;
 * </pre>
 *
 * @author Abdul Shoudouev
 * @author Wim Lambrechts
 * @author Ren&eacute; Clerckx
 * @author Jan Dockx
 * @author Peopleware n.v.
 */
public class NationalNumberConverter implements Converter {

  /**
   * Convert the specified string value, which is associated with the
   * specified UIComponent, into a model data object that is appropriate for
   * being stored during the Apply Request Values phase of the request
   * processing lifecycle.
   *
   * @result  value == null || value.length == 0
   *            ==> result == null;
   * @result  let
   *            nnString = the given string, where all the following characters
   *                       are removed : [ -/|*:]+
   *          in
   *            value != null && value.length > 0
   *            ==> result instanceof NationalNumber
   *                && result.getLeftNumber().equals(nnString.subString(0, 6))
   *                && result.getMiddleNumber().equals(nnString.subString(6, 9))
   *                && result.getRightNumber().equals(nnString.subString(9, 11));
   * @throws  ConverterException
   *          The national number cannot be created from the given string.
   *          See: {@link NationalNumber#NationalNumber(String, String, String)}
   */
  public Object getAsObject(final FacesContext context, final UIComponent component,
      final String value) throws ConverterException {
    NationalNumber nn = null;
    if ((value == null) || (value.length() == 0)) {
      return null;
    }
    else {
      try {
        String[] array = value.split("[ -/|*:]+");
        StringBuffer buffer = new StringBuffer("");
        for (int i = 0; i < array.length; i++) {
          buffer.append(array[i]);
        }
        String nnString = buffer.toString();
        String leftNumber = nnString.substring(0, 6);
        String middleNumber = nnString.substring(6, 9);
        String rightNumber = nnString.substring(9, 11);
        nn = new NationalNumber(leftNumber, middleNumber, rightNumber);
        return nn;
      }
      catch (Throwable e) {
        throw new ConverterException("National number " + value + "could not be created");
      }
    }
  }

  /**
   * The empty string.
   *
   * <strong>= &quot;&quot;</strong>
   */
  public static final String EMPTY = "";

  /**
   * Convert the specified model object value, which is associated with the
   * specified UIComponent, into a String that is suitable for being included
   * in the response generated during the Render Response phase of the request
   * processing lifecycle.
   *
   * @result  value == null
   *            ==> result == EMPTY;
   * @result  value != null && value instanceof NationalNumber
   *            ==> result == ((NationalNumber)value).toString();
   * @throws  ConverterException
   *          value != null && !(value instanceof NationalNumber)
   */
  public String getAsString(final FacesContext context, final UIComponent component,
      final Object value) throws ConverterException {
    if (value == null) {
      return EMPTY;
    }
    if (!(value instanceof NationalNumber)) {
      throw new ConverterException("value is not a NationalNumber");
    }
    return ((NationalNumber)value).toString();
  }
}

