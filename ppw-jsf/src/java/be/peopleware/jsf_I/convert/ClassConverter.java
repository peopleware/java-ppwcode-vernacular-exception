package be.peopleware.jsf_I.convert;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;


/**
 * Converter for {@link Class}. The value must be the FQCN of the class.
 * 
 * To active this, the following entry has to appear in <kbd>faces-config.xml</kbd>:
 * <pre>
 * &lt;converter&gt;
 *   &lt;converter-for-class&gt;java.lang.Class&lt;/converter-for-class&gt;
 *   &lt;converter-class&gt;be.peopleware.jsf_I.convert.ClassConverter&lt;/converter-class&gt;
 * &lt;/converter&gt;
 * </pre>
 * 
 * @author Wim Lambrechts
 * @author René Clerckx
 * @author Jan Dockx
 * @author Peopleware n.v.
 */
public class ClassConverter implements Converter {

  public Object getAsObject(FacesContext context, UIComponent component, String value)
      throws ConverterException {
    if (value == null) {
      return null;
    }
    else {
	    try {
	      return Class.forName(value);
	    }
	    catch (Throwable e) {
	      throw new ConverterException("class with name " + value + "could not be loaded");
	    }
    }
  }

  public final static String EMPTY = "";
  
  public String getAsString(FacesContext context, UIComponent component, Object value)
      throws ConverterException {
    if (value == null) {
      return EMPTY;
    }
    if (! (value instanceof Class)) {
      throw new ConverterException("value is not a Class");
    }
    return ((Class)value).getName();
  }

}

