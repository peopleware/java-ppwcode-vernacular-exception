package be.peopleware.taglet;


import com.sun.javadoc.Tag;


/**
 * Abstract superclass for common code in contract taglets.
 * @mudo take a look at the contract of this class 
 * like {@link AbstractStandaloneTaglet}.
 *
 * @mudo (UnitTest): test taglet <code>@mudo</code>
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    Ren&eacute; Clerckx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 * 
 * @invar getHeader() != null;
 * @invar getHeader().length() > 0; 
 */
public abstract class AbstractStandaloneTaglet extends AbstractTaglet {
 
  /**
   * Return the header of this custom taglet.
   * 
   * @basic
   * 
   * @return    getHeader() != null && getHeader().length() > 0; 
   */
  public abstract String getHeader();

  /**
   * @return    false;
   */
  public final boolean isInlineTag() {
    return false;
  }

  /**
   * @result toString(new Tag[] {taglet});
   */
  public final String toString(Tag taglet) {
    return toString(new Tag[] {taglet});
  }

/**
 * <p>Formats the output of the taglet. Tag's are rendered by the
 *   standard doclet inside a <code>&lt;dl&gt;</code> block.
 *   This is outside our control. The parsed contents
 *   of each individual tag is placed in a table cell.</p>
 * 
 * @protected
 * <p>This is a hook method that writes a header, returned by
 *   {@link #getHeader()}, and then the contents of the tags,
 *   processed by {@link #parse(String)}.</p>
 */
  public String toString(Tag[] taglets) {
    if (taglets.length == 0) {
      return null;
    }
    StringBuffer result = new StringBuffer();
    result.append("\n<dt><b>");//$NON-NLS-1$
    result.append(getHeader());
    result.append("</b></dt><dd>");//$NON-NLS-1$
    result.append("<table cellpadding=2 cellspacing=0>"); //$NON-NLS-1$
    for (int i = 0; i < taglets.length; i++) {
      result.append("<tr><td>"); //$NON-NLS-1$
      result.append(parse(taglets[i]));
      result.append("</td></tr>"); //$NON-NLS-1$
    }
    result.append("</table></dd>\n"); //$NON-NLS-1$
    return result.toString();
  }

}
