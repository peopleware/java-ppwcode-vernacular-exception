/*
 * Copyright 2002 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * -Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THE SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE SOFTWARE, EVEN IF
 * SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that Software is not designed, licensed or intended for use
 * in the design, construction, operation or maintenance of any nuclear
 * facility.
 */
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
 * @author Jamie Ho
 * @since 1.4
 */
public class Todo implements Taglet {

  private static final String NAME = "todo"; //$NON-NLS-1$
  private static final String HEADER = "To Do:"; //$NON-NLS-1$

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
    return true;
  }

  /**
   * Will return true since <code>@todo</code> can be used in constructor documentation.
   * @return true since <code>@todo</code> can be used in constructor documentation and false otherwise.
   */
  public boolean inConstructor() {
    return true;
  }

  /**
   * Will return true since <code>@todo</code> can be used in method documentation.
   * @return true since <code>@todo</code> can be used in method documentation and false otherwise.
   */
  public boolean inMethod() {
    return true;
  }

  /**
   * Will return true since <code>@todo</code> can be used in method documentation.
   * @return true since <code>@todo</code> can be used in overview documentation and false otherwise.
   */
  public boolean inOverview() {
    return true;
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
    Todo tag = new Todo();
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
    return "<DT><B>" + HEADER + "</B><DD>" //$NON-NLS-1$//$NON-NLS-2$
           + "<table cellpadding=2 cellspacing=0><tr><td bgcolor=\"yellow\">" //$NON-NLS-1$
           + tag.text() + "</td></tr></table></DD>\n"; //$NON-NLS-1$
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
    result += "<table cellpadding=2 cellspacing=0><tr><td bgcolor=\"yellow\">"; //$NON-NLS-1$
    for (int i = 0; i < tags.length; i++) {
      if (i > 0) {
        result += ", "; //$NON-NLS-1$
      }
      result += tags[i].text();
    }
    return result + "</td></tr></table></DD>\n"; //$NON-NLS-1$
  }
}