/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/


package be.peopleware.value_II;


import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.el.EvaluationException;
import javax.faces.el.ValueBinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>This implementation of {@link AbstractPropertyEditorConverter} finds
 *   the target type by looking at the <code>component</code> value binding,
 *   and finds a {@link PropertyEditor} by asking the {@link PropertyEditorManager}
 *   one for the target type. This means a converter of this type
 *   works completely automatic if the system can find the
 *   {@link PropertyEditor} type.</p>
 * <p><strong>It is however still necessary to tell JavaServer Faces about
 *   this converter for each separate type that needs to use it, with an
 *   entry in <kbd>faces-config.xml</kbd>:</strong></p>
 * <pre>
 * &lt;converter&gt;
 *   &lt;converter-for-class&gt;<var>FqcnOfTargetType</var>&lt;/converter-for-class&gt;
 *   &lt;converter-class&gt;be.peopleware.jsf_II.convert.AutomaticPropertyEditorConverter&lt;/converter-class&gt;
 * &lt;/converter&gt;
 * </pre>
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
public class AutomaticPropertyEditorConverter extends AbstractPropertyEditorConverter {

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
    LogFactory.getLog(AutomaticPropertyEditorConverter.class);


  /**
   * A {@link PropertyEditor} for the {@link ValueBinding#getType(FacesContext) type of the value binding}
   * is requested from the {@link PropertyEditorManager}. If no such
   * editor is found, a {@link ConverterException} is thrown.
   * If the retrieved {@link PropertyEditor} is of type,
   * {@link DisplayLocaleBasedEnumerationValueEditor},
   * the {@link DisplayLocaleBasedEnumerationValueEditor#setDisplayLocale(Locale) display locale}
   * of the editor is set to the locale of the current {@link UIViewRoot}.
   *
   * @basic
   * @throws ConverterException
   *         PropertyEditorManager.findEditor(component.getValueBinding("value").getType(context)) == null;
   *         no {@link PropertyEditor} found for the type of the value binding of <code>component</code>
   */
  protected PropertyEditor getPropertyEditor(FacesContext context, UIComponent component)
      throws ConverterException {
    assert component != null;
    assert context != null;
    if ($propertyEditor == null) {
      LOG.debug("no PropertyEditor in cache; retrieving fresh one");
      try {
        // try to find target type
        Class targetType = component.getValueBinding("value").getType(context);
          /* throws EvaluationException < PropertyNotFoundException
             throws NullPointerException, not  for component == null, context == null
             or "value" == null, but because their might not be a value binding */
        // try to find a property editor for target type
        LOG.debug("target type of component value binding \"value\": " + targetType);
        $propertyEditor = PropertyEditorManager.findEditor(targetType);
        if ($propertyEditor == null) { // still
          LOG.debug("no PropertyEditor found for type \"" + targetType + "\"");
          throw new ConverterException("Could not locate a PropertyEditor for type "
                                       + targetType);
        }
        else {
          LOG.debug("PropertyEditor found: " + $propertyEditor);
          if ($propertyEditor instanceof DisplayLocaleBasedEnumerationValueEditor) {
            assert $propertyEditor != null;
            Locale displayLocale = context.getViewRoot().getLocale();
            LOG.debug("PropertyEditor is of type DisplayLocaleBasedEnumerationValueEditor; " +
                      "setting display locale to " + displayLocale);
            ((DisplayLocaleBasedEnumerationValueEditor)$propertyEditor).
                setDisplayLocale(displayLocale);
          }
        }
      }
      catch (NullPointerException npExc) {
        new ConverterException("Could not retrieve target type from component, " +
                               "because there is no value binding");
      }
      catch (EvaluationException eExc) { // and PropertyNotFoundException
        new ConverterException("Could not retrieve target type from component", eExc);
      }
    }
    LOG.debug("returning PropertyEditor: " + $propertyEditor);
    return $propertyEditor;
  }

  private PropertyEditor $propertyEditor;

}
