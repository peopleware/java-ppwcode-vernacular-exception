package be.peopleware.taglet;


import com.sun.javadoc.Tag;


/**
 * @todo (JAVADOC): Write class description.
 *
 * @mudo (UnitTest): test taglet <code>@mudo</code>
 *
 * @author    Jan Dockx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 */
public abstract class AbstractInlineTaglet extends AbstractTaglet {
  
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
   * @see       Registrar#toString(Tag)
   */
  public String toString(Tag taglet) {   
    return null;
    // @todo (dvankeer): Generalize?
  }

  /**
   * @see       Registrar#toString(Tag[])
   * @result    null
   */
  public final String toString(Tag[] taglets) {
    return null;
  }
  
}
