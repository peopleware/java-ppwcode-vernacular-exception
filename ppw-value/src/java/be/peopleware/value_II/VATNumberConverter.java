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
 * Converter for {@link VATNumber}. The {@link #getAsObject(FacesContext, UIComponent, String)}
 * method is very lenient: separators can be left out, or any of the characters
 * <code><var>space</var>-/|*:</code>.
 *
 * To activate this converter, the following entry has to appear in <kbd>faces-config.xml</kbd>:
 * <pre>
 * &lt;converter&gt;
 *   &lt;converter-for-class&gt;be.peopleware.value_II.VATNumber&lt;/converter-for-class&gt;
 *   &lt;converter-class&gt;be.peopleware.value_II.VATNumberConverter&lt;/converter-class&gt;
 * &lt;/converter&gt;
 * </pre>
 *
 * @author nsmeets
 * @author Peopleware n.v.
 */
public class VATNumberConverter implements Converter {

  public Object getAsObject(FacesContext context, UIComponent component, String value)
      throws ConverterException {
    if ((value == null) || (value.length() == 0)) {
      return null;
    }
    else {
      try {
        return new VATNumber(value);
      }
      catch (Throwable e) {
        throw new ConverterException("VAT number " + value + "could not be created");
      }
    }
  }

  public final static String EMPTY = "";

  public String getAsString(FacesContext context, UIComponent component, Object value)
      throws ConverterException {
    if (value == null) {
      return EMPTY;
    }
    if (! (value instanceof VATNumber)) {
      throw new ConverterException("value is not a VATNumber");
    }
    return ((VATNumber)value).toString();
  }
}

