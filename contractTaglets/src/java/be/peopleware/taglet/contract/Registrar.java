package be.peopleware.taglet.contract;


import java.util.Map;

import be.peopleware.taglet.TagletRegistration;

import com.sun.tools.doclets.Taglet;


/**
 * This class registers all taglets in this package with the standard doclet.
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    Ren&eacute; Clerckx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 */
public abstract class Registrar implements Taglet {

  public static final Taglet[] TAGLETS
      = new Taglet[] {new InvarTaglet(),
                      new PostTaglet(),
                      new PreTaglet(),
                      new ResultTaglet()};
  
  /**
   * Register all taglets defined in this package.
   */
  public static void register(Map tagletMap) {
    TagletRegistration.registerTaglets(tagletMap, TAGLETS);
  }

}
