package be.peopleware.taglet.contract;


/**
 * Taglet-class for custom taglet <code>@post</code>
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    Ren&eacute; Clerckx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 */
public class PostTaglet extends AbstractContractTaglet {

  public String getName() {
    return "post";  //$NON-NLS-1$
  }

  public String getHeader() {
    return "Postconditions:"; //$NON-NLS-1$
  }

  public PostTaglet() {
    $inConstructor = true;
    $inMethod      = true;
    $allowedKeywords.add(AbstractContractTaglet.KEYWORD_NEW);
    $allowedKeywords.add(AbstractContractTaglet.KEYWORD_FORALL);
  }

}
