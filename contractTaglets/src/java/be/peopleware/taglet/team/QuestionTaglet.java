package be.peopleware.taglet.team;


/**
 * Taglet-class for custom taglet <code>@question</code>
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    René Clerckx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 */
public class QuestionTaglet extends AbstractTeamTaglet {

  public String getName() {
    return "question"; //$NON-NLS-1$
  }


  public String getHeader() {
    return "Question:"; //$NON-NLS-1$
  }


  public QuestionTaglet() {
    $inField       = true;
    $inConstructor = true;
    $inMethod      = true;
    $inOverview    = true;
    $inPackage     = true;
    $inType        = true;
  }

}
