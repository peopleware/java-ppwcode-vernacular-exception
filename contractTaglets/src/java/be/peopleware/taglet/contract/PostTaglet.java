package be.peopleware.taglet.contract;


/**
 * Taglet-class for custom taglet <code>@post</code>
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    René Clerckx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 */
public class PostTaglet extends AbstractContractTaglet {

  public String getName() {
    return "post";  //$NON-NLS-1$
  }

  public String getHeader() {
    return "Post Conditions:"; //$NON-NLS-1$
  }

  public PostTaglet() {
    $inField       = false;
    $inConstructor = true;
    $inMethod      = true;
    $inOverview    = false;
    $inPackage     = false;
    $inType        = false;

    $allowedKeywords.add(AbstractContractTaglet.KEYWORD_NEW);
    $allowedKeywords.add(AbstractContractTaglet.KEYWORD_FORALL);
  }

}
