package be.peopleware.taglet.contract;


import be.peopleware.taglet.AbstractStandaloneTaglet;


/**
 * Taglet-class for custom taglet <code>@invar</code>
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    René Clerckx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 */
public class InvarTaglet extends AbstractContractTaglet {
  /**
   * @see       AbstractTaglet#getName()
   */
  public String getName() {
    return "invar"; //$NON-NLS-1$
  }

  /**
   * @see       AbstractStandaloneTaglet#getHeader()
   */
  public String getHeader() {
    return "Type Invariants:"; //$NON-NLS-1$
  }

  /**
   * @see       AbstractTaglet#AbstractTaglet()
   */
  public InvarTaglet() {
    $inField       = false;
    $inConstructor = false;
    $inMethod      = false;
    $inOverview    = false;
    $inPackage     = true;
    $inType        = true;
  }

}
