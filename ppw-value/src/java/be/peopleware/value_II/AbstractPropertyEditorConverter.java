/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/


package be.peopleware.value_II;


import java.beans.PropertyEditor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>Support for converters based on {@link PropertyEditor PropertyEditors}.
 *   Derive from this class and implement {@link #getPropertyEditor(FacesContext, UIComponent)}.
 *   Then add an entry in <kbd>faces-config.xml</kbd> to map the target types
 *   to the correct converter class descendant.</p>
 * <p>Often, there are 2 String representations of value objects:</p>
 * <ul>
 *   <li>a programmatic representation, e.g., to be used as values in a HTML
 *      select option tag; and</li>
 *   <li>a label, or display name, to be presented to end users.</li>
 * </ul>
 * <p>When {@link #isLabelRepresentation()} is <code>true</code>, this convertor's
 *   {@link #getAsString(FacesContext, UIComponent, Object)} method returns the
 *   label. If {@link #isLabelRepresentation()} is <code>false</code>
 *   (the default), the {@link #getAsString(FacesContext, UIComponent, Object)}
 *   method returns the programmatic representation. This only works if the
 *   property editor found for the type of the value to be converted</p>
 * <p><strong>Note that the {@link #getAsObject(FacesContext, UIComponent, String)}
 *   method only works when {@link #isLabelRepresentation()} is <code>false</code>:
 *   it is often impossible to convert a human-readable label to an
 *   object.</strong></p>
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

  protected AbstractPropertyEditorConverter() {
    LOG.debug("creation of new ...PropertyEditorConverter (" +
              getClass().getName() + ")");
  }


  /**
   * @pre context != null;
   * @pre component != null;
   * @return ; the result of the conversion of <code>value</code>
   *           to an Object by {@link #getPropertyEditor(FacesContext, UIComponent)}
   * @throws ConverterException
   *         getPropertyEditor(context, component);
   * @throws ConverterException
   *         getPropertyEditor(context, component).getValue()#IllegalArgumentException;
   * @throws ConverterException
   *         isLabelRepresenation();
   */
  public final Object getAsObject(FacesContext context, UIComponent component, String value)
      throws ConverterException {
    assert context != null;
    assert component != null;
    LOG.debug("request to convert \"" + value + "\" to object for " +
              component + "(id = " + component.getClientId(context) + ")");
    if (isLabelRepresentation()) {
      LOG.debug("Cannot convert from String to Object in label-representation-mode");
      throw new ConverterException("Cannot convert from String to Object in label-representation-mode");
    }
    try {
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
   * @basic
   * @init false;
   */
  public final boolean isLabelRepresentation() {
    return $labelRepresentation;
  }

  /**
   * @post new.isLabelRepresentation() == labelRepresentation;
   */
  public final void setLabelRepresentation(boolean labelRepresentation) {
    $labelRepresentation = labelRepresentation;
  }

  private boolean $labelRepresentation;

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
