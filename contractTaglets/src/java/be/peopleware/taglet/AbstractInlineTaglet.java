package be.peopleware.taglet;


import com.sun.javadoc.Tag;


/**
 * @todo (JAVADOC): Write class description.
 * @mudo take a look at the contract of this class like {@link AbstractStandaloneTaglet}.
 * 
 * @mudo (UnitTest): test taglet <code>@mudo</code>
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    Ren&eacute; Clerckx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 */
public abstract class AbstractInlineTaglet extends AbstractTaglet {
  
  /**
   * Used to determine if this taglet can be used in 
   * <strong>inline documentation </strong>.
   *
   * @return    true because we are a inline tag, not a standalone tag
   */
  public final boolean isInlineTag() {
    return true;
  }

  /**
   * @see       AbstractTaglet#toString(Tag)
   */
  public String toString(Tag taglet) {   
    if (taglet == null) {
      return null;
    }
    return taglet.text();
  }

  /**
   * @see       AbstractTaglet#toString(Tag[])
   * @result    null
   */
  public final String toString(Tag[] taglets) {
    if (taglets.length == 0) {
      return null;
    }
    StringBuffer result = new StringBuffer();
    for (int i = 0; i < taglets.length; i++) {
      result.append(toString(taglets[i]));
    }

    return result.toString();
  }
  
  /**
   * Makes some additional formatting of the content of the taglet.
   * 
   * Does nothing at this moment.
   *
   * @param     text
   *            content of the taglet
   * @return    text - formatted content
   */
  public String parse(String text) {
    return text;
  }

}
