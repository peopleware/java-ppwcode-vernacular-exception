package be.peopleware.taglet.inline;


import java.util.Map;
import com.sun.javadoc.Tag;
import be.peopleware.taglet.InlineTagletRegistrar;
import be.peopleware.taglet.TagletRegistrar;


/**
 * Taglet-class for custom inline taglet <code>@underline</code>
 *
 * @author    Abdul Shoudouev
 * @author    Peopleware n.v.
 */
public class UnderlineTaglet extends InlineTagletRegistrar {
  /**
   * name of the taglet.
   */
  public String getName() {
    return "underline"; //$NON-NLS-1$
  }

  /**
   * Register this taglet
   *
   * @param     tagletMap
   *            the map to register this taglet to.
   */
  public static void register(Map tagletMap) {
    TagletRegistrar.registerTaglet(tagletMap, new UnderlineTaglet());
  }

  protected void setTagletScopes() {
     bInField       = true;
     bInConstructor = true;
     bInMethod      = true;
     bInOverview    = true;
     bInPackage     = true;
     bInType        = true;
     bInLine        = true;
  }

  /**
   * Given the <code>Tag</code> representation of this custom
   * tag, return its string representation.
   * @param tag he <code>Tag</code> representation of this custom tag.
   */
  public String toString(Tag tag) {
      return "<u>" + tag.text() + "</u>";  //$NON-NLS-1$//$NON-NLS-2$
  }

}
