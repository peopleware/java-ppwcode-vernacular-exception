package be.peopleware.value_I;


import java.beans.PropertyEditorSupport;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * <p>Implementations of a number of methods of
 *   {@link EnumerationValueEditor}.</p>
 *
 * <p>This implementation depends on the fact that the enumeration type
 *  implements <code>toString()</code> to return the tag for that value,
 *  as requested by {@link EnumerationValue}.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public abstract class AbstractEnumerationValueEditor
    extends PropertyEditorSupport
    implements EnumerationValueEditor, Serializable {

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



  /**
   * The enumeration type this is an editor for.
   * <p><strong>protected</strong></p>
   * <p>This default implementation assumes that we follow the property
   *   editor naming scheme, and that
   *   <code>getClass()toString().equals(getEnumerationValue().getClass()
   *         .toString() + "Editor")</code>.</p>
   *
   * @return    Class.forName(expectedEnumerationValueTypeClassName());
   */
  public Class getEnumerationValueType() {
    Class result = null;
    try {
      result = Class.forName(getExpectedEnumerationValueTypeClassName());
    }
    catch (ClassNotFoundException cnfExc) {
      assert false : getExpectedEnumerationValueTypeClassName()
                         + " should exist"; //$NON-NLS-1$
    }
    return result;
  }

  /**
   * @return    getClass().toString().substring(0, getClass().toString()
   *                .lastIndexOf("Editor"));
   */
  public final String getExpectedEnumerationValueTypeClassName() {
    String me = getClass().getName();
    return me.substring(0, me.lastIndexOf("Editor")); //$NON-NLS-1$
  }



  /*<property name="tags">*/
  //------------------------------------------------------------------

  public final String[] getTags() {
    return (String[])getValuesMap().keySet().toArray();
  }

  /*</property>*/



  /*<property name="valuesMap">*/
  //------------------------------------------------------------------

  /**
   * A map that contains all the tags, associated with the instance
   * the tag represents.
   *
   * @protected
   * It would be wise for the implementation of this method to return
   * a staticically constructed Map.
   *
   * @basic
   */
  public abstract Map getValuesMap();

  /*</property>*/



  /*<property name="labelsMap">*/
  //------------------------------------------------------------------

  /**
   * @protected
   * This implementation builds the labels map
   * based on information in the {@link #getValuesMap()}
   * and uses the method {@link #getLabel()}.
   */
  public final Map getLabelsMap() {
    Map result = new HashMap();
    /* we are going to use this object itself to generate the labels
       let's store the value for a while */
    Iterator iter = getValuesMap().values().iterator();
    Object originalValue = getValue();
    while (iter.hasNext()) {
      setValue(iter.next());
      result.put(getAsText(), getLabel());
    }
    // restore the original value
    setValue(originalValue);
    return result;
  }

  /*</property>*/


  private static final String EMPTY = ""; //$NON-NLS-1$
  private static final String NBSP = " "; //$NON-NLS-1$


  /*<property name="asText">*/
  //------------------------------------------------------------------

  /**
   * @result    getValue() == null)
   *                ? " "
   *                : getValue().toString();
   */
  public final String getAsText() {
    String result = (getValue() == null)
                       ? NBSP
                       : getValue().toString();
    return result;
  }

  /**
   * @post      ((text == null) || (text.equals("") || (text.equals(" "))
   *                ? new.getValue() == null
   *                : new.getValue() = getValuesMap().get(text);
   * @throws    IllegalArgumentException
   *            (text != null)
   *                && !text.equals("")
   *                && !text.equals(" ")
   *                && getValuesMap().get(text) == null;
   */
  public final void setAsText(final String text)
      throws IllegalArgumentException {
    if ((text == null) || text.equals(EMPTY) || text.equals(NBSP)) { //$NON-NLS-1$
      setValue(null);
    }
    else {
      Object result = getValuesMap().get(text);
      if (result == null) {
        throw new IllegalArgumentException("\"" + text //$NON-NLS-1$
                                           + "\" was not recognized as a tag"); //$NON-NLS-1$
      }
      setValue(result);
    }
  }

  /*</property>*/

}
