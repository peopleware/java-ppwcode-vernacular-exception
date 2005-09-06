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
 *   &lt;converter-for-class&gt;be.peopleware.value_I.NationalNumber&lt;/converter-for-class&gt;
 *   &lt;converter-class&gt;be.peopleware.value_I.NationalNumberConverter&lt;/converter-class&gt;
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

  public Object getAsObject(FacesContext context, UIComponent component, String value)
      throws ConverterException {
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
        String leftNumber = nnString.substring(0,6);
        String middleNumber = nnString.substring(6,9);
        String rightNumber = nnString.substring(9,11);
        nn = new NationalNumber(leftNumber, middleNumber, rightNumber);
        return nn;
      }
      catch (Throwable e) {
        throw new ConverterException("National number " + value + "could not be created");
      }
    }
  }

  public final static String EMPTY = "";

  public String getAsString(FacesContext context, UIComponent component, Object value)
      throws ConverterException {
    if (value == null) {
      return EMPTY;
    }
    if (! (value instanceof NationalNumber)) {
      throw new ConverterException("value is not a NationalNumber");
    }
    return ((NationalNumber)value).toString();
  }
}

