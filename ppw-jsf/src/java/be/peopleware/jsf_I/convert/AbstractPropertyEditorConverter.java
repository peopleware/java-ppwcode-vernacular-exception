package be.peopleware.jsf_I.convert;

import java.beans.PropertyEditor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;


/* Converter for PropertyEditors. This is an abstract class: derive from
 * this class and implement the getPropertyEditorObject method
 * Then add a mapping in faces-config.xml to map the classes
 * to the correct converter class descendant.
 * 
 * @author wlambrec
 * 
 * @invar getPropertyEditor() != null;
 */

public abstract class AbstractPropertyEditorConverter implements Converter {

	public final Object getAsObject(FacesContext context, UIComponent component, String value)
      throws ConverterException {
    // MUDO (jand) document exception
		try {
		  PropertyEditor editor = getPropertyEditor(context, component);
                                  // ConverterException
		  editor.setAsText(value);
			return editor.getValue();
		}
    catch (IllegalArgumentException iae) {
      // MUDO (jand) good FacesMessage, i18n
			throw new ConverterException (iae);
		}
	}

	public final String getAsString(FacesContext context, UIComponent component, Object value) {
		PropertyEditor editor = getPropertyEditor(context, component);
		editor.setValue(value);
		return editor.getAsText();
	}

  /**
   * @basic
   */
	protected abstract PropertyEditor getPropertyEditor(FacesContext context,
                                                      UIComponent component)
      throws ConverterException;

}
