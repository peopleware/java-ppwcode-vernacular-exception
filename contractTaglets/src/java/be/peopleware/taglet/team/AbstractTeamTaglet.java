package be.peopleware.taglet.team;


import com.sun.javadoc.Tag;

import be.peopleware.taglet.AbstractStandaloneTaglet;


/**
 * Abstract superclass for common code in team taglets.
 * 
 * @mudo at-link stuff in tag text should become a link like in the rest of the Javadoc
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    Ren&eacute; Clerckx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 */
public abstract class AbstractTeamTaglet extends AbstractStandaloneTaglet {

  public String parse(Tag tag) {
    return tag.text();
  }

  
}
