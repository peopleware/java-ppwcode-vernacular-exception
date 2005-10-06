/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.jsf_II.myfaces.tree2;


import java.util.Collections;
import java.util.List;

import org.apache.myfaces.custom.tree2.TreeNode;

import be.peopleware.jsf_II.persistence.InstanceHandler;


/**
 * <p>Default implementation of some {@link TreeNode} methods,
 *   so that this handler can be used as a {@link TreeNode}
 *   without a wrapper.
 *   Subclasses should implement {@link #getChildren()},
 *   and can overwrite {@link #getChildCount()} for
 *   performance reasons.</p>
 * <p>The {@link #getType()} of the tree node, to be used as the
 *   facet name in the jspx page, is the FQCN of the
 *   {@link #getPersistentBeanType() persistent bean type} we
 *   are handling.</p>
 * <p>This default implementation can be used for a {@link #isLeaf() leaf}</p>
 *
 * @note The {@link TreeNode} interface defines some stupid methods
 *       that should not appear in an API interface in the first place.
 *       See <a href="http://issues.apache.org/jira/browse/MYFACES-447">the
 *       MyFaces JIRA</a>.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 *
 * @invar getChildCount() = getChildren().size();
 * @invar isLeaf() <==> getChildCount() == 0;
 */
public class InstanceHandlerTreeNode extends InstanceHandler implements TreeNode {

  /*<section name="Meta Information">*/
  //------------------------------------------------------------------
  /** {@value} */
  public static final String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_TAG = "$Name$"; //$NON-NLS-1$
  /*</section>*/


  // default constructor only


  /*<section name="treeNodeInterFaceImpl">*/
  //------------------------------------------------------------------

  /**
   * @return getPersistentBeanType().getName();
   */
  public final String getType() {
    return getPersistentBeanType().getName();
  }

  /**
   * Overwrite this method to define what
   * the children of this {@link TreeNode} are.
   *
   * @return-protected Collections.EMPTY_LIST
   */
  public List getChildren() {
    return Collections.EMPTY_LIST;
  }

  /**
   * Can be overwritten for performance reasons.
   *
   * @return getChildren().size();
   */
  public int getChildCount() {
    return getChildren().size();
  }

  /**
   * @return  getChildCount() == 0;
   */
  public boolean isLeaf() {
    return getChildCount() == 0;
  }

  /**
   * NOP; should not be in the interface
   *
   * @deprecated
   */
  public final void setLeaf(boolean arg0) {
    // NOP
  }

  /**
   * NOP; should not be in the interface
   *
   * @deprecated
   */
  public final void setType(String arg0) {
    // NOP
  }

  /**
   * should not be in the interface
   *
   * @return null;
   *
   * @deprecated
   */
  public final String getDescription() {
    return null;
  }

  /**
   * NOP; should not be in the interface
   *
   * @deprecated
   */
  public final void setDescription(String arg0) {
    // NOP
  }

  /**
   * NOP; should not be in the interface
   *
   * @deprecated
   */
  public final void setIdentifier(String arg0) {
    // NOP
  }

  /**
   * should not be in the interface
   *
   * @return null;
   *
   * @deprecated
   */
  public final String getIdentifier() {
    return null;
  }

  /*</section>*/

}
