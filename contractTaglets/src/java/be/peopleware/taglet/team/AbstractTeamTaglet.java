package be.peopleware.taglet.team;


import be.peopleware.taglet.AbstractStandaloneTaglet;


/**
 * Abstract superclass for common code in team taglets.
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    René Clerckx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 */
public abstract class AbstractTeamTaglet extends AbstractStandaloneTaglet {

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
