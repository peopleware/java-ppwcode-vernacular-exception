package be.peopleware.taglet;


import com.sun.javadoc.Tag;


/**
 * This is a Test
 * @todo (dvankeer): Complete javadoc
 *
 * @invar     getHeader() != null && getHeader().length() > 0
 *
 * @mudo (UnitTest): test taglet <code>@mudo</code>
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 */
public abstract class StandaloneTagletRegistrar extends TagletRegistrar {
  /**
   * @todo (dvankeer)
   * 
   * @return
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
   * Given the <code>com.sun.javadoc.Tag</code> representation of this custom
   * taglet, return its string representation.
   *
   * @param     taglet
   *            the <code>com.sun.javadoc.Tag</code> representation of this
   *            custom taglet.
   *
   * @result    null if taglet == null; valid html formatting code otherwise
   */
  public String toString(Tag taglet) {
    return toString(new Tag[] {taglet});
  }

  /**
   * Given an array of <code>com.sun.javadoc.Tag</code> s representing this
   * custom taglet, return its string representation.
   *
   * @param     taglets
   *            the array of <code>com.sun.javadoc.Tag</code> s representing of
   *            this custom taglet.
   * @result    null if taglets == null; valid html formatting code otherwise
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
