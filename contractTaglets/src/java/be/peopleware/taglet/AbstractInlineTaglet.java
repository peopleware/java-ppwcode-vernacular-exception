package be.peopleware.taglet;


import com.sun.javadoc.Tag;


/**
 * Abstract superclass for common code in inline taglets.
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
   * Returns unformatted content of the taglet.
   * 
   * @see       AbstractTaglet#toString(Tag)
   */
  public String toString(Tag taglet) {   
    if (taglet == null) {
      return null;
    }
    return taglet.text();
  }

  /**
   * Returns unformatted content of the taglets.
   * 
   * @see       AbstractTaglet#toString(Tag[])
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
   * Simply returns text of the taglet since 
   * we do not parse content of the inline tags.
   * 
   * @param     tag
   *            taglet to be parsed.
   * @return    tag.text()
   */
  public String parse(Tag tag) {
    return tag.text();
  }

}
