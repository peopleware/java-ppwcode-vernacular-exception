package be.peopleware.taglet.contract;


/**
 * Taglet-class for custom taglet <code>@pre</code>
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    Ren&eacute; Clerckx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 */
public class PreTaglet extends AbstractContractTaglet {

  public String getName() {
    return "pre"; //$NON-NLS-1$
  }

  public String getHeader() {
    return "Preconditions:"; //$NON-NLS-1$
  }

  public PreTaglet() {
    $inConstructor = true;
    $inMethod      = true;
    $allowedKeywords.add(AbstractContractTaglet.KEYWORD_FORALL);
  }

}
