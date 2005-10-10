/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.value_II;


import java.beans.PropertyEditor;
import java.util.Map;


/**
 * <p>An interface that adds support for I18N for enumeration types.</p>
 * <p>Property editors for enumeration type implement the {@link #getAsText()}
 *   and {@link #setAsText(String)} and {@link #getTags()} methods. These
 *   methods process strings used by programmers in some circumstances as the
 *   representation of the enumeration type values. The tags are the
 *   <def>programmatic String representation</def> of values of the enumeration
 *   type. All possible tags are returned by {@link #getTags()}.</p>
 * <p>These programmatic representations of the enumeration type values
 *   however are not what we show to end users. Combo boxes, pop-up menu's
 *   radio buttons, and HTML select tags should show an I18N label, but
 *   internally should work with the programmatic representation of
 *   the property values.<br />
 *   Normally, a formatter (see {@link java.text.Format} should be used
 *   for I18N, but formatters require the implementation of both
 *   a format method for presentation, and a parse method for input.
 *   The latter is not applicable here. Therefor, we bypass formatters,
 *   and just extend the {@link java.beans.PropertyEditor} interface
 *   with a method {@link #getLabel()} to return I18N labels for an
 *   enumeration type value supported by {@link #getTags()}.</p>
 * <p>To make the building of combo boxes, pop-up menu's or radio
 *   buttons easier, a {@link #getLabelsMap() map} is offered that
 *   contains all tag / label combinations.</p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 *
 * @invar     getEnumerationValue() != null;
 * @invar     (getAsText() != null)
 *                <==> getEnumerationValue().isInstance(getValue());
 * @invar     (getAsText() != null)
 *                ==> Arrays().asList(getTags()).contains(getAsText());
 * @invar     getLabelsMap() != null;
 * @invar     ! getLabelsMap().keySet().contains(null);
 * @invar     (foreach Object k; getLabelsMap().keySet().contains(k);
 *                k instanceof String);
 * @invar     ! getLabelsMap().values().contains(null);
 * @invar     (foreach Object l; getLabelsMap().values().contains(l);
 *                l instanceof String);
 */
public interface EnumerationValueEditor extends PropertyEditor {

  /*<section name="Meta Information">*/
  //------------------------------------------------------------------

  /** {@value} */
  String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  String CVS_TAG = "$Name$"; //$NON-NLS-1$

  /*</section>*/


  /**
   * <p>The type we are an editor for. If the editor follows the
   *   normal naming scheme,
   *   <code>getClass()toString().equals(getEnumerationValue()
   *               .getClass().toString() + "Editor")</code>.</p>
   * <p>This is not mandatory, but enables the automatic finding of the
   *   editor by IDE's and other tools. Thus, this naming scheme is
   *   highly recommended.</p>
   *
   * @basic
   */
  Class getEnumerationValueType();

  /**
   * The programmatic String representations of values of
   * {@link #getEnumerationValueType()}.
   *
   * @return getLabelsMap().keySet().toArray();
   */
  String[] getTags();

  /**
   * @post   tag.equals(new.getAsText());
   *         <code>getValue()</code> is changed so that
   *         <code>getAsText()</code> will return
   *         <code>tag</code>.
   * @throws IllegalArgumentException
   *         ! Arrays.asList(getTags()).contains(tag);
   *         This includes <code>tag == null</code>.
   */
  void setAsText(String tag) throws IllegalArgumentException;

  /**
   * Return an I18N label for {@link #getValue()}.
   * This should return a valid label for all entries in {@link #getTags()}.
   * <code>null</code> is returned if no label is found for the value.
   *
   * @return  getLabelsMap().get(getAsText());
   */
  String getLabel();

  /**
   * A map of programmatic tag / presentation label combinations
   * for all possible values of the enumeration type.
   *
   * @basic
   */
  Map getLabelsMap();

}
