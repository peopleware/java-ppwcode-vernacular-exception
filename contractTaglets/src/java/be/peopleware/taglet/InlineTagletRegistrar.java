package be.peopleware.taglet;


import com.sun.javadoc.Tag;


/**
 * @todo (dvankeer) JAVADOC
 * 
 * @default Test
 *
 * @mudo (UnitTest): test taglet <code>@mudo</code>
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 */
public abstract class InlineTagletRegistrar extends TagletRegistrar {
  /**
   * Used to determine if this taglet can be used in <strong>inline
   * documentation </strong>.
   *
   * @return    true because we are a inline tag, not a standalone tag
   */
  public final boolean isInlineTag() {
    return true;
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
    return null;
    // @todo (dvankeer): Generalize?
  }

  /**
   * Given an array of <code>com.sun.javadoc.Tag</code> s representing this
   * custom taglet, return its string representation.
   *
   * @result    null
   */
  public final String toString(Tag[] taglets) {
    return null;
  }
  
}
