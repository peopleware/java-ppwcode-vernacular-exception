package be.peopleware.struts_I.persistentBean.tag;


import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.servlet.jsp.JspWriter;

import be.peopleware.struts_I.persistentBean.CrudDynaActionForm;
import be.peopleware.value_I.EnumerationValueEditor;


/**
 * <p>Build a HTML <code>select</code> tag for a bean property that returns an
 *   enumeration type, for which there exists an {@link EnumerationValueEditor},
 *   if {@link CrudDynaActionForm#isViewModeEdit()} is
 *   <code>true</code>. Otherwise, show the value as the label from
 *   {@link EnumerationValueEditor} as text.</p>
 * <p>The {@link #getActionForm()} must contain an entry in its map with name
 *  {@link #getProperty()} of type
 *  {@link be.peopleware.value_I.EnumerationValueEditor}.
 *
 * @invar     getDontShowTags() != null;
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
 public class EnumerationFieldTag extends CrudDynaActionFormTagSupport {

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



  // default constructor


  private static final String EMPTY = ""; //$NON-NLS-1$


  /**
   * @todo (jand): Write description; also check tld
   * @todo (jand): Think about exceptions
   *
   * @throws    IOException
   *            An I/O operations failed for some reason.
   */
  public void doTag() throws IOException {
    String otherAttributes = (getOtherAttributes() == null)
                                  ? EMPTY
                                  : getOtherAttributes();
    JspWriter out = getJspContext().getOut();
    if (getActionForm().isViewModeEdit()) {
      if (getPropertyEditor().getLabelsMap().isEmpty()) {
        out.println("no options"); //$NON-NLS-1$ // @todo (jand): i18n 
      }
      else {
        Set tagsAndLabels = getPropertyEditor().getLabelsMap().entrySet();
        TreeSet sortedTagsAndLabels
            = new TreeSet(new Comparator() {
                public int compare(final Object comparer,
                                   final Object compared) {
                  String comparerLabel = (String)((Map.Entry)comparer).getValue();
                  String comparedLabel = (String)((Map.Entry)compared).getValue();
                  return comparerLabel.compareTo(comparedLabel);
                }
            });
        sortedTagsAndLabels.addAll(tagsAndLabels);
        Iterator iter = sortedTagsAndLabels.iterator();
        if (getSize() >= 0) {
          generateList(otherAttributes, out, iter);
        }
        else {
          generateRadioButtons(otherAttributes, out, iter);
        }
      }
    }
    else {
      out.print(getPropertyEditor().getLabel());
    }
  }

  /**
   * Generate a select option as a list (dropdown box).
   *
   * @param     otherAttributes
   *            Other attributes to use in the html select tag.
   * @param     out
   *            The JspWrite to write the output to.
   * @param     iter
   *            The iterator to run over.
   * @throws    IOException
   *            An I/O operations failed for some reason.
   */
  private void generateList(final String otherAttributes,
                            final JspWriter out,
                            final Iterator iter)
      throws IOException {
    out.println("<select " //$NON-NLS-1$
                + "name=\"" + getProperty() + "\" " //$NON-NLS-1$ //$NON-NLS-2$
                + "id=\"" + getProperty() + "\" " //$NON-NLS-1$ //$NON-NLS-2$
                + "size=\"" + getSize() + "\" " //$NON-NLS-1$ //$NON-NLS-2$
                + otherAttributes
                + ">"); //$NON-NLS-1$
    while (iter.hasNext()) {
      Map.Entry entry = (Map.Entry)iter.next();
      String tag = (String)entry.getKey();
      if (getDontShowTags().indexOf(tag) == -1) {
        out.println("<option " //$NON-NLS-1$
                  + "value=\"" + tag + "\"" //$NON-NLS-1$ //$NON-NLS-2$
                  + writeSelected(tag, SELECTED)
                  + ">" //$NON-NLS-1$
                      + (String)entry.getValue()
                  + "</option>"); //$NON-NLS-1$
      }
    }
    out.println("</select>"); //$NON-NLS-1$
  }

  /**
   * Generate a select option with radiobuttons.
   *
   * @param     otherAttributes
   *            Other attributes to use in the html input tag.
   * @param     out
   *            The JspWrite to write the output to.
   * @param     iter
   *            The iterator to run over.
   * @throws    IOException
   *            An I/O operations failed for some reason.
   */
  private void generateRadioButtons(final String otherAttributes,
                                    final JspWriter out,
                                    final Iterator iter)
      throws IOException {
    while (iter.hasNext()) {
      Map.Entry entry = (Map.Entry)iter.next();
      String tag = (String)entry.getKey();
      if (getDontShowTags().indexOf(tag) == -1) {
        out.print("<input type=\"radio\" " //$NON-NLS-1$
                  + "value=\"" + tag + "\"" //$NON-NLS-1$ //$NON-NLS-2$
                  + "name=\"" + getProperty() + "\" " //$NON-NLS-1$ //$NON-NLS-2$
                  + "id=\"" + getProperty() + "\" "  //$NON-NLS-1$ //$NON-NLS-2$
                  + otherAttributes
                  + writeSelected(tag, CHECKED)
                  + " />" //$NON-NLS-1$
                  + "&nbsp;" + (String)entry.getValue()); //$NON-NLS-1$
        if (iter.hasNext()) {
          out.println("&nbsp;&nbsp;"); //$NON-NLS-1$
        }
        else {
          out.println();
        }
      }
    }
  }

  private static final String SELECTED = " selected"; //$NON-NLS-1$

  private static final String CHECKED = " checked"; //$NON-NLS-1$

  private String writeSelected(final String currentTag,
                               final String selectedString) {
    return ((getPropertyEditor().getAsText() != null)
              && getPropertyEditor().getAsText().equals(currentTag))
           ? selectedString
           : EMPTY;
  }


  /*<property name="propertyEditor">*/
  //------------------------------------------------------------------

  public final EnumerationValueEditor getPropertyEditor() {
    EnumerationValueEditor result = null;
    if ((getActionForm() != null)
          && (getProperty() != null)
          && (!getProperty().equals(EMPTY))) {
      result = (EnumerationValueEditor)getActionForm().get(getProperty());
    }
    return result;
  }

  /*</property>*/



  /*<property name="dont show tags">*/
  //------------------------------------------------------------------

  /**
   * A comma-separated list of the programmatic name of tags not to
   * show in input mode.
   *
   * @basic
   */
  public final String getDontShowTags() {
    return $dontShowTags;
  }

  /**
   * @post    new.getDontShowTags().equals(dontShowTags);
   */
  public void setDontShowTags(final String dontShowTags) {
    if (dontShowTags == null) {
      throw new IllegalArgumentException("dontShowTags cannot be null"); //$NON-NLS-1$
    }
    $dontShowTags = dontShowTags;
  }

  private String $dontShowTags = EMPTY;

  /*</property>*/



  /*<property name="size">*/
  //------------------------------------------------------------------

  /**
   * Controls how this select tag is shown. With <code>size == 0</code>,
   * a menu is shown in HTML. With <code>size > 0</code>, a combo box
   * or list is shown. With <code>size < 0</code>, radion buttons are
   * shown, separated by a double &quot;nbsp;. The default is
   * <code>0</code>.
   *
   * @basic
   */
  public final int getSize() {
    return $size;
  }

  /**
   * @param     size
   *            Set to 0 for combo box, < 0 for radio buttons.
   * @post      new.getSize() == size;
   */
  public void setSize(final int size) {
    $size = size;
  }

  private int $size;

  /*</property>*/

}
