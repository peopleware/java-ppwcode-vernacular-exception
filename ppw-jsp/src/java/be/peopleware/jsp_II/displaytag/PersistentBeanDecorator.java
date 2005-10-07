/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/
package be.peopleware.jsp_II.displaytag;


import java.text.NumberFormat;
import java.util.Collection;
import java.util.Locale;

import org.displaytag.decorator.TableDecorator;

import be.peopleware.jsp_II.tag.Functions;
import be.peopleware.persistence_II.PersistentBean;


/**
 * A displaytag decorator super class for
 * {@link PersistentBean} derivates.
 *
 * @author    David Van Keer
 * @author    Peopleware n.v.
 */
public abstract class PersistentBeanDecorator extends TableDecorator {

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
  * Returns the current row object.
  *
  * @return  getCurrentRowObject();
  */
  protected final PersistentBean getPersistentBean() {
    return (PersistentBean)getCurrentRowObject();
  }

  /**
  * Returns the simple name of the type of the current row object,
  * followed by "Detail.do".
  *
  * @return  let
  *            clazz == getCurrentRowObject().getClass().toString()
  *          in
  *            clazz.substring(clazz.lastIndexOf(".") + 1) + "Detail.do";
  */
  public final String getDetailBaseHref() {
    String clazz = getCurrentRowObject().getClass().toString();
    int index = clazz.lastIndexOf("."); //$NON-NLS-1$
    return clazz.substring(index + 1) + "Detail.do";  //$NON-NLS-1$
  }

  /**
   * Returns the simple name of the type of the current row object,
   * followed by "List.do".
   *
   * @return  let
   *            clazz == getCurrentRowObject().getClass().toString()
   *          in
   *            clazz.substring(clazz.lastIndexOf(".") + 1) + "List.do";
   */
  public final String getListBaseHref() {
    String clazz = getCurrentRowObject().getClass().toString();
    int index = clazz.lastIndexOf("."); //$NON-NLS-1$
    return clazz.substring(index + 1) + "List.do";  //$NON-NLS-1$
  }

  /**
   * Lazy init of number format.
   */
  protected final NumberFormat getNumberFormat() {
    if ($numberFormat == null) {
      Locale loc = Functions.locale(getPageContext());
      $numberFormat = NumberFormat.getIntegerInstance(loc);
    }
    return $numberFormat;
  }

  /**
   * Cannot initialize with locale, since there is no page context
   * yet at construction time.
   */
  private NumberFormat $numberFormat;

  /**
   * Convenience method to format a number.
   */
  protected final String numberOf(final int integer) {
    return getNumberFormat().format(integer);
  }

  /**
   * Convenience method to return the size of a collection property
   * of the represented object. Uses I18N number format.
   */
  protected final String numberOf(final Collection collection) {
    return numberOf(collection.size());
  }

  /**
   * An index in the list. In contrast to {@link #getListIndex()},
   * this number is 1-based ({@link #getListIndex()} is 0-based).
   */
  public String getIndex() {
    return getNumberFormat().format(getListIndex() + 1);
  }

  /**
   * The empty string.
   *
   * <strong>= &quot;&quot;</strong>
   */
  protected static final String EMPTY = ""; //$NON-NLS-1$
  /**
   * The separator.
   *
   * <strong>= &quot;; &quot;</strong>
   */
  protected static final String SEPERATOR = "; "; //$NON-NLS-1$

  /**
   * A disabled check box to represent boolean values.
   */
  protected final String getCheckBox(final boolean value) {
    return "<input type=\"checkbox\" disabled=\"disabled\" " //$NON-NLS-1$
            + (value ? "checked" : EMPTY) //$NON-NLS-1$
            + "/>"; //$NON-NLS-1$
  }

}
