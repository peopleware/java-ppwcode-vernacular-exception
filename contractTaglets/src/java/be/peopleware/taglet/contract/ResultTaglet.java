package be.peopleware.taglet.contract;


/**
 * Taglet-class for custom taglet <code>@result</code>
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    Ren&eacute; Clerckx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 */
public class ResultTaglet extends AbstractContractTaglet {

  public String getName() {
    return "result"; //$NON-NLS-1$
  }

  public String getHeader() {
    return "Result:"; //$NON-NLS-1$
  }

  public ResultTaglet() {
    $inMethod      = true;
    $allowedKeywords.add(AbstractContractTaglet.KEYWORD_RESULT);
    $allowedKeywords.add(AbstractContractTaglet.KEYWORD_NEW);
    $allowedKeywords.add(AbstractContractTaglet.KEYWORD_FORALL);
  }

}
