package be.peopleware.jsp_I.displaytag;


import java.text.NumberFormat;
import java.util.Collection;
import java.util.Locale;
import org.displaytag.decorator.TableDecorator;

import be.peopleware.persistence_I.PersistentBean;
import be.peopleware.jsp_I.tag.Functions;


/**
 * A displaytag decorator super class for
 * {@link PersistenBean} derivates.
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

  protected final PersistentBean getPersistentBean() {
    return (PersistentBean)getCurrentRowObject();
  }

  public final String getDetailBaseHref() {
    String clazz = getCurrentRowObject().getClass().toString();
    int index = clazz.lastIndexOf("."); //$NON-NLS-1$
    return clazz.substring(index + 1) + "Detail.do";  //$NON-NLS-1$
  }

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


  protected static final String EMPTY = ""; //$NON-NLS-1$
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
