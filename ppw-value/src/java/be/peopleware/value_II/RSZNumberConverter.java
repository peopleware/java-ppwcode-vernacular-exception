package be.peopleware.value_II;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;


/**
 * Converter for {@link RSZNumber}. The {@link #getAsObject(FacesContext, UIComponent, String)}
 * method is very lenient: separators can be left out, or any of the characters
 * <code><var>space</var>-/|*:</code>.
 *
 * The value must be the string representation of the class.
 *
 * To activate this converter, the following entry has to appear in <kbd>faces-config.xml</kbd>:
 * <pre>
 * &lt;converter&gt;
 *   &lt;converter-for-class&gt;be.peopleware.value_I.RSZNumber&lt;/converter-for-class&gt;
 *   &lt;converter-class&gt;be.peopleware.value_I.RSZNumberConverter&lt;/converter-class&gt;
 * &lt;/converter&gt;
 * </pre>
 *
 * @author nsmeets
 * @author Peopleware n.v.
 */
public class RSZNumberConverter implements Converter {

  public Object getAsObject(FacesContext context, UIComponent component, String value)
      throws ConverterException {
    RSZNumber nn = null;
    if ((value == null) || (value.length() == 0)) {
      return null;
    }
    else {
      try {
        String[] array = value.split("[ -/|*:]+");
        StringBuffer buffer = new StringBuffer("");
        for (int i = 0; i<array.length;i++) {
          buffer.append(array[i]);
        }
        String nnString = buffer.toString();
        String leftNumber = nnString.substring(0,3);
        String middleNumber = nnString.substring(3,10);
        String rightNumber = nnString.substring(10,12);
        nn = new RSZNumber(leftNumber, middleNumber, rightNumber);
        return nn;
      }
      catch (Throwable e) {
        throw new ConverterException("RSZ number " + value + "could not be created");
      }
    }
  }

  public final static String EMPTY = "";

  public String getAsString(FacesContext context, UIComponent component, Object value)
      throws ConverterException {
    if (value == null) {
      return EMPTY;
    }
    if (! (value instanceof RSZNumber)) {
      throw new ConverterException("value is not a RSZNumber");
    }
    return ((RSZNumber)value).toString();
  }
}

