package be.peopleware.taglet;


import java.util.Map;
import com.sun.javadoc.Tag;
import com.sun.tools.doclets.Taglet;


/**
 * A sample Taglet representing
 * 
 * @todo. This tag can be used in any kind of {@link com.sun.javadoc.Doc}. It
 *        is not an inline tag. The text is displayed in yellow to remind the
 *        developer to perform a task. For example, "@todo Fix this!" would be
 *        shown as:
 *        <DL>
 *        <DT><B>To Do: </B>
 *        <DD><table cellpadding=2 cellspacing=0>
 *        <tr>
 *        <td bgcolor="yellow">Fix this!</td>
 *        </tr>
 *        </table></DD>
 *        </DL>
 * 
 * @author      Jan Dockx
 */
public class Invar implements Taglet {

  private static final String NAME = "invar"; //$NON-NLS-1$
  private static final String HEADER = "Type Invariants:"; //$NON-NLS-1$

  /**
   * Return the name of this custom tag.
   */
  public String getName() {
    return NAME;
  }

  /**
   * Will return true since <code>@todo</code> can be used in field documentation.
   * @return true since <code>@todo</code> can be used in field documentation and false otherwise.
   */
  public boolean inField() {
    return false;
  }

  /**
   * Will return true since <code>@todo</code> can be used in constructor documentation.
   * @return true since <code>@todo</code> can be used in constructor documentation and false otherwise.
   */
  public boolean inConstructor() {
    return false;
  }

  /**
   * Will return true since <code>@todo</code> can be used in method documentation.
   * @return true since <code>@todo</code> can be used in method documentation and false otherwise.
   */
  public boolean inMethod() {
    return false;
  }

  /**
   * Will return true since <code>@todo</code> can be used in method documentation.
   * @return true since <code>@todo</code> can be used in overview documentation and false otherwise.
   */
  public boolean inOverview() {
    return false;
  }

  /**
   * Will return true since <code>@todo</code> can be used in package documentation.
   * @return true since <code>@todo</code> can be used in package documentation and false otherwise.
   */
  public boolean inPackage() {
    return true;
  }

  /**
   * Will return true since <code>@todo</code> can be used in type documentation (classes or interfaces).
   * @return true since <code>@todo</code> can be used in type documentation and false otherwise.
   */
  public boolean inType() {
    return true;
  }

  /**
   * Will return false since <code>@todo</code> is not an inline tag.
   * @return false since <code>@todo</code> is not an inline tag.
   */
  public boolean isInlineTag() {
    return false;
  }

  /**
   * Register this Taglet.
   * 
   * @param tagletMap
   *          the map to register this tag to.
   */
  public static void register(Map tagletMap) {
    Invar tag = new Invar();
    Taglet t = (Taglet)tagletMap.get(tag.getName());
    if (t != null) {
      tagletMap.remove(tag.getName());
    }
    tagletMap.put(tag.getName(), tag);
  }

  /**
   * Given the <code>Tag</code> representation of this custom tag, return its
   * string representation.
   * 
   * @param tag
   *          the <code>Tag</code> representation of this custom tag.
   */
  public String toString(Tag tag) {
    return toString(new Tag[] {tag}); 
  }

  /**
   * Given an array of <code>Tag</code> s representing this custom tag, return
   * its string representation.
   * 
   * @param tags
   *          the array of <code>Tag</code> s representing of this custom tag.
   */
  public String toString(Tag[] tags) {
    if (tags.length == 0) {
      return null;
    }
    String result = "\n<DT><B>" + HEADER + "</B><DD>"; //$NON-NLS-1$ //$NON-NLS-2$
    result += "<table cellpadding=2 cellspacing=0>"; //$NON-NLS-1$
    for (int i = 0; i < tags.length; i++) {
      result += "<tr><td><code>"; //$NON-NLS-1$
      result += tags[i].text();
      result += "</code></td></tr>"; //$NON-NLS-1$
    }
    return result + "</table></DD>\n"; //$NON-NLS-1$
  }
}