package be.peopleware.taglet.contract;


import be.peopleware.taglet.AbstractStandaloneTaglet;


/**
 * Taglet-class for custom taglet <code>@result</code>
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    René Clerckx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 */
public class ResultTaglet extends AbstractContractTaglet {
  /**
   * @see       AbstractTaglet#getName()
   */
  public String getName() {
    return "result"; //$NON-NLS-1$
  }

  /**
   * @see       AbstractStandaloneTaglet#getHeader()
   */
  public String getHeader() {
    return "Result:"; //$NON-NLS-1$
  }

  /**
   * @see       AbstractTaglet#AbstractTaglet()
   */
  public ResultTaglet() {
    $inField       = false;
    $inConstructor = false;
    $inMethod      = true;
    $inOverview    = false;
    $inPackage     = false;
    $inType        = false;
  }

}
