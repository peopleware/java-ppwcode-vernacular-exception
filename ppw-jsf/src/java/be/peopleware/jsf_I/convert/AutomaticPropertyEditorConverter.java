package be.peopleware.jsf_I.convert;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;


// MUDO (jand) needs mucho explanation, and a package.html
public class AutomaticPropertyEditorConverter extends AbstractPropertyEditorConverter {

  /**
   * @basic
   */
	protected PropertyEditor getPropertyEditor(FacesContext context,
                                             UIComponent component)
      throws ConverterException {
    assert component != null;
    assert context != null;
    if ($propertyEditor == null) {
      // try to find it
    	  Class targetType = component.getValueBinding("value").getType(context);
    	  //the class for which this converter is called
      $propertyEditor = PropertyEditorManager.findEditor(targetType);
      if ($propertyEditor == null) { // still
        throw new ConverterException("Could not locate a PropertyEditor for type "
                                     + targetType);
      }
    }
	  return $propertyEditor;
  }

  private PropertyEditor $propertyEditor;

}
