package be.peopleware.jsf_II.convert;


import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import be.peopleware.value_I.EnumerationValue;


/**
 * Converter for {@link EnumerationValue}.
 * The value must be the string representation of the class.
 *
 * @author nsmeets
 * @author Peopleware n.v.
 */
public abstract class EnumerationValueConverter implements Converter {

  public Object getAsObject(FacesContext context, UIComponent component, String value)
      throws ConverterException {
    Object result = getValuesMap().get(value);
    if (result == null) {
      throw new ConverterException();
    }
    return result;
  }

  public abstract Map getValuesMap();

  public final static String EMPTY = "";

  public String getAsString(FacesContext context, UIComponent component, Object value)
      throws ConverterException {
    if (value == null) {
      return EMPTY;
    }
    if (! (getType().isInstance(value))) {
      throw new ConverterException("value is not a "+ getClass().getName());
    }
    return ((EnumerationValue)value).toString();
  }

  public abstract Class getType();

}

