/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.jsf_II.myfaces.tree2;


import java.io.Serializable;
import java.util.List;

import org.apache.myfaces.custom.tree2.TreeNode;


/**
 * <p>An implementation of {@link TreeNode} that is intended as a wrapper around
 *   {@link #getWrapped() business objects}. The tree node type is the
 *   FQCN of the wrapped object.</p>
 * <p>The intention is for subclasses to implement {@link #getChildren()}
 *   in function of {@link #getWrapped()}.</p>
 *
 * @author Jan Dockx
 * @author Peopleware n.v.
 *
 * @invar getInstance() != null;
 */
public abstract class WrappingTreeNode implements TreeNode, Serializable {

  /*<section name="Meta Information">*/
  //------------------------------------------------------------------
  /** {@value} */
  public static final String CVS_REVISION = "$Revision$";
  /** {@value} */
  public static final String CVS_DATE = "$Date$";
  /** {@value} */
  public static final String CVS_STATE = "$State$";
  /** {@value} */
  public static final String CVS_TAG = "$Name$";
  /*</section>*/



//  private static final Log LOG = LogFactory.getLog(WrappingTreeNode.class);



  /**
   * After construction, {@link #getWrapped()} <code>== wrapped</code>.
   *
   * @param wrapped
   *        The business object this is a wrapper for.
   *        This cannot be <code>null</code>.
   * @post new.getWrapped() == wrapped;
   * @exception IllegalArgumentException
   *            <code>instance</code> is <code>null</code>.
   */
  protected WrappingTreeNode(final Object wrapped)
      throws IllegalArgumentException {
    if (wrapped == null) {
      throw new IllegalArgumentException("instance cannot be null");
    }
    $wrapped = wrapped;
  }

  /**
   * The business object this is a wrapper for. This will never return
   * <code>null</code>.
   *
   * @basic
   */
  public final Object getWrapped() {
    return $wrapped;
  }

  /**
   * The business object this is a wrapper for.
   * This cannot be <code>null</code>.
   *
   * @invar $wrapped != null;
   */
  private final Object $wrapped;

  /**
   * @basic
   */
  public final boolean isLeaf() {
    return getChildCount() == 0;
  }

  /**
   * This inherited method is not supported.
   *
   * @post   false;
   * @throws UnsupportedOperationException
   *         true;
   */
  public final void setLeaf(final boolean l) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * As a default implementation, we offer the FQCN of
   * the wrapped business object. Subclasses can override this method.
   *
   * @protected.result getInstance().getClass().getName(); the FQCN of {@link #getWrapped()}.
   */
  public String getType() {
    return getWrapped().getClass().getName();
  }

  /**
   * This inherited method is not supported.
   *
   * @post   false;
   * @throws UnsupportedOperationException
   *         true;
   */
  public final void setType(final String t) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * As a default implementation, we offer the {@link List#size()}
   * of {@link #getChildren()}. Subclasses can override this method.
   *
   * @protected.result getChildren().size();
   */
  public int getChildCount() {
    return getChildren().size();
  }

  /**
   * Returns the description.
   *
   * @basic
   */
  public final String getDescription() {
    return $description;
  }

  /**
   * Set the given description.
   *
   * @param description
   * @post  new.getDescription() == description;
   */
  public final void setDescription(final String description) {
    $description = description;
  }

  private String $description;



  /**
   * Returns the identifier.
   *
   * @basic
   */
  public final String getIdentifier() {
    return $identifier;
  }

  /**
   * Set the given identifier.
   *
   * @param identifier
   * @post  new.getIdentifier() == identifier;
   */
  public final void setIdentifier(final String identifier) {
    $identifier = identifier;
  }

  private String $identifier;

}

