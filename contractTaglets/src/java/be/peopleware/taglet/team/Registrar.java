package be.peopleware.taglet.team;


import java.util.Map;

import be.peopleware.taglet.TagletRegistration;

import com.sun.tools.doclets.Taglet;


/**
 * This class registers all taglets in this package with the standard doclet.
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    René Clerckx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 */
public abstract class Registrar implements Taglet {

  public static final Taglet[] TAGLETS
      = new Taglet[] {new IdeaTaglet(),
                      new MudoTaglet(),
                      new QuestionTaglet(),
                      new TodoTaglet()};
  
  /**
   * Register all taglets defined in this package.
   */
  public static void register(Map tagletMap) {
    TagletRegistration.registerTaglets(tagletMap, TAGLETS);
  }

}
