package be.peopleware.taglet;


import com.sun.javadoc.Tag;


/**
 * @todo (JAVADOC): Write class description
 *
 * @invar     getHeader() != null && getHeader().length() > 0
 *
 * @mudo (UnitTest): test taglet <code>@mudo</code>
 *
 * @author    Jan Dockx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 */
public abstract class AbstractStandaloneTaglet extends AbstractTaglet {
 
  /**
   * Return the header of this custom taglet.
   * 
   * @return    getHeader() != null && getHeader().length() > 0 
   */
  public abstract String getHeader();

  /**
   * Used to determine if this taglet can be used in <strong>inline
   * documentation </strong>. 
   *
   * @return    false because we are not a inline tag but a standalone tag
   */
  public final boolean isInlineTag() {
    return false;
  }

  /**
   * @see       Registrar#toString(Tag)
   */
  public final String toString(Tag taglet) {
    return toString(new Tag[] {taglet});
  }

  /**
   * @see       Registrar#toString(Tag[])
   */
  public String toString(Tag[] taglets) {
    if (taglets.length == 0) {
      return null;
    }
    StringBuffer result = new StringBuffer();
    result.append("\n<dl><dt><b>");//$NON-NLS-1$
    result.append(getHeader());
    result.append("</b></dt><dd>");//$NON-NLS-1$
    result.append("<table cellpadding=2 cellspacing=0>"); //$NON-NLS-1$
    for (int i = 0; i < taglets.length; i++) {
      result.append("<tr><td><code>"); //$NON-NLS-1$
      result.append(taglets[i].text());
      result.append("</code></td></tr>"); //$NON-NLS-1$
    }
    result.append("</table></dd></dl>\n"); //$NON-NLS-1$
    return result.toString();
  }

}
