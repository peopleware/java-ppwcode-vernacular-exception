package be.peopleware.taglet.team;


import be.peopleware.taglet.AbstractStandaloneTaglet;
import be.peopleware.taglet.TagletRegistration;


/**
 * Taglet-class for custom taglet <code>@todo</code>
 *
 * @author    Abdul Shoudouev
 * @author    Peopleware n.v.
 */
public class TodoTaglet extends AbstractStandaloneTaglet {
  /**
   * @see       Registrar#getName()
   */
  public String getName() {
    return "todo"; //$NON-NLS-1$
  }

  /**
   * @see       AbstractStandaloneTaglet#getHeader()
   */
  public String getHeader() {
    return "Todo:"; //$NON-NLS-1$
  }

  /**
   * @see       Registrar#setTagletScopes()
   */
  public TodoTaglet() {
    $inField       = true;
    $inConstructor = true;
    inMethod      = true;
    inOverview    = true;
    inPackage     = true;
    inType        = true;
  }

}
