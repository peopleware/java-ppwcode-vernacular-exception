package be.peopleware.taglet.team;


import be.peopleware.taglet.AbstractStandaloneTaglet;
import be.peopleware.taglet.TagletRegistration;


/**
 * Taglet-class for custom taglet <code>@question</code>
 *
 * @author    Adbul Shoudouev
 * @author    Peopleware n.v.
 */
public class QuestionTaglet extends AbstractStandaloneTaglet {
  /**
   * @see       Registrar#getName()
   */
  public String getName() {
    return "question"; //$NON-NLS-1$
  }

  /**
   * @see       AbstractStandaloneTaglet#getHeader()
   */
  public String getHeader() {
    return "Question:"; //$NON-NLS-1$
  }

  /**
   * @see       Registrar#setTagletScopes()
   */
  public QuestionTaglet() {
    $inField       = true;
    $inConstructor = true;
    inMethod      = true;
    inOverview    = true;
    inPackage     = true;
    inType        = true;
  }

}
