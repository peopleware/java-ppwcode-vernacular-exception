/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/


package be.peopleware.value_I;


import java.beans.PropertyEditor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Support for converters based on {@link PropertyEditor PropertyEditors}.
 * Derive from this class and implement {@link #getPropertyEditor(FacesContext, UIComponent)}.
 * Then add an entry in <kbd>faces-config.xml</kbd> to map the target types
 * to the correct converter class descendant.
 *
 * @author Wim Lambrechts
 * @author Jan Dockx
 * @author PeopleWare n.v.
 *
 * @invar getPropertyEditor() != null;
 */

public abstract class AbstractPropertyEditorConverter implements Converter {

  /*<section name="Meta Information">*/
  //------------------------------------------------------------------
  /** {@value} */
  public static final String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_TAG = "$Name$"; //$NON-NLS-1$
  /*</section>*/


  private static final Log LOG =
    LogFactory.getLog(AbstractPropertyEditorConverter.class);


  /**
   * @pre context != null;
   * @pre component != null;
   * @return ; the result of the conversion of <code>value</code>
   *           to an Object by {@link #getPropertyEditor(FacesContext, UIComponent)}
   * @throws ConverterException
   *         getPropertyEditor(context, component);
   * @throws ConverterException
   *         getPropertyEditor(context, component).getValue()#IllegalArgumentException;
   */
  public final Object getAsObject(FacesContext context, UIComponent component, String value)
      throws ConverterException {
    assert context != null;
    assert component != null;
    try {
      LOG.debug("request to convert \"" + value + "\" to object for " +
                component + "(id = " + component.getClientId(context) + ")");
      PropertyEditor editor = getPropertyEditor(context, component); // ConverterException
      LOG.debug("retrieved PropertyEditor: " + editor);
      editor.setAsText(value);
      Object result = editor.getValue();
      if (LOG.isDebugEnabled()) {
        LOG.debug("convertion result: " + result);
      }
      return result;
    }
    catch (IllegalArgumentException iae) {
      // MUDO (jand) good FacesMessage, i18n; find out what happens if this fails
      throw new ConverterException(iae);
    }
  }

  /**
   * @pre context != null;
   * @pre component != null;
   * @return ; the result of the conversion of <code>value</code>
   *           to a String by {@link #getPropertyEditor(FacesContext, UIComponent)}
   * @throws ConverterException
   *         getPropertyEditor(context, component);
   */
  public final String getAsString(FacesContext context, UIComponent component, Object value)
      throws ConverterException {
    assert context != null;
    assert component != null;
    if (LOG.isDebugEnabled()) {
      LOG.debug("request to convert object \"" + value + "\" to String for " +
                component + "(id = " + component.getClientId(context) + ")");
    }
    PropertyEditor editor = getPropertyEditor(context, component); // ConverterException
    LOG.debug("retrieved PropertyEditor: " + editor);
    editor.setValue(value);
    String result = editor.getAsText();
    LOG.debug("convertion result: " + result);
    return result;
  }

  /**
   * Subclasses need to implement this method. Return a {@link PropertyEditor}
   * for the target type of this converter. This method is not allowed to
   * return <code>null</code>.
   *
   * @pre context != null;
   * @pre component != null;
   * @basic
   * @throws ConverterException
   */
  protected abstract PropertyEditor getPropertyEditor(FacesContext context,
                                                      UIComponent component)
      throws ConverterException;

}
