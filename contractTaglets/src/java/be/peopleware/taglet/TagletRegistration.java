package be.peopleware.taglet;


import java.util.Map;

import com.sun.tools.doclets.Taglet;


/**
 * Utility methods for registering a batch of taglets.
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    Peopleware n.v.
 */
public abstract class TagletRegistration implements Taglet {

  /**
   * Register a Taglet.
   *
   * @param     tagletMap
   *            the map to register this taglet to.
   * @pre       tag != null
   * @post      tagletMap.get(tag.getName()) == tag
   */
  public static final void registerTaglet(Map tagletMap, Taglet taglet) {
    Taglet t = (Taglet)tagletMap.get(taglet.getName());
    if (t != null) {
      tagletMap.remove(taglet.getName());
    }
    tagletMap.put(taglet.getName(), taglet);
  }

  /**
   * Registers an array of taglets.
   * 
   * @pre taglets != null;
   *      the parameter <code>taglets</code> cannot be null
   */
  public static void registerTaglets(Map tagletMap, Taglet[] taglets) {
    assert taglets != null;
    for (int i = 0; i < taglets.length; i++) {
      TagletRegistration.registerTaglet(tagletMap, taglets[i]);
    } 
  }

}
