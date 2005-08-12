package be.peopleware.value_I;


import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;


/**
 * Utility class for values.
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 *
 * @deprecated
 */
public final class Util {

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

  /**
   * No instances.
   */
  private Util() {
    // NOP
  }

  /**
   * Register packages from this library as
   * path for {@link PropertyEditor PropertyEditors} with
   * the {@link PropertyEditorManager}.
   *
   * @deprecated
   */
  public static void registerPropertyEditors() {
    String[] oldPath = PropertyEditorManager.getEditorSearchPath();
    String[] newPath = new String[oldPath.length + 1];
    System.arraycopy(oldPath, 0, newPath, 0, oldPath.length);
    String currentPackageName = Util.class.getPackage().getName();
    newPath[newPath.length - 1] = currentPackageName;
    PropertyEditorManager.setEditorSearchPath(newPath);
  }

}
