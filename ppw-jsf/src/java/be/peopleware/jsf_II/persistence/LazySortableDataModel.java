package be.peopleware.jsf_II.persistence;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.model.DataModel;
import javax.faces.model.DataModelEvent;
import javax.faces.model.DataModelListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * A lazy data model. If wrapped around a lazy collection (e.g., like you get from Hibernate),
 * elements of the collection are only accessed when an element of the datamodel is first
 * requested.
 *
 * @author    Jan Dockx
 * @author    Peopleware n.v.
 *
 * @invar (getCollection() == null) ? getRowIndex() == -1;
 *
 * @mudo implement sortability
 */
public class LazySortableDataModel extends DataModel {

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



  private static final Log LOG = LogFactory.getLog(LazySortableDataModel.class);


  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @param collection
   *        The, preferably lazy, collection, to make a data model for. This
   *        can be empty or <code>null</code>. <code>collection</code> can be unmodifiable.
   * @post new.getCollection() == collection;
   */
  public LazySortableDataModel(Collection collection) {
    assignCollection(collection); // no listeners, so no events
  }


  /*</construction>*/



  /*<property name="collection">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Collection getCollection() {
    return $collection;
  }

  /**
   * @return getCollection();
   */
  public final Object getWrappedData() {
    return getCollection();
  }

  /**
   * @param collection
   *        The, preferably lazy, collection, to make a data model for. This
   *        can be empty or <code>null</code>. <code>collection</code> can be unmodifiable.
   * @post new.getCollection() == collection;
   * @post new.getRowIndex() = (collection != null) ? 0 : -1;
   */
  public final void setCollection(Collection collection) {
    assignCollection(collection);
  }

  /**
   * @param collection
   *        The, preferably lazy, collection, to make a data model for. This
   *        can be empty. <code>collection</code> can be unmodifiable.
   * @post new.getCollection() == collection;
   * @post new.getRowIndex() = (collection != null) ? 0 : -1;
   * @throws ClassCastException
   *         ! collection instanceof Collection;
   */
  public final void setWrappedData(Object collection) {
    setCollection((Collection)collection); // ClassCastException
  }

  /**
   * @param collection
   *        The, preferably lazy, collection, to make a data model for. This
   *        can be empty. <code>collection</code> can be unmodifiable.
   * @post new.$collection == collection;
   * @post new.$list == null;
   * @post new.$rowIndex = (collection != null) ? 0 : -1;
   */
  private void assignCollection(Collection collection) {
    $collection = collection;
    if (collection != null) {
      $rowIndex = 0;
      /* yes, also 0 if collection is empty, according to standard at
         http://java.sun.com/j2ee/javaserverfaces/1.1_01/docs/api/javax/faces/model/DataModel.html */
      fireEvents(); // also if row index alreay was 0
    }
    else {
      $rowIndex = -1;
      // no events
    }
    $list = null;
  }

  /**
   * @invar $collection != null;
   */
  private Collection $collection;

  /*</property>*/



  /**
   * @return (getCollection() == null) ? -1 : getCollection().size();
   */
  public final int getRowCount() {
    return (getCollection() == null) ? -1 : getCollection().size();
  }



  /*<property name="rowIndex">*/
  //------------------------------------------------------------------

  public final int getRowIndex() {
    return $rowIndex;
  }

  /**
   * @return (getCollection() != null) && (getRowIndex() >= 0) && (getRowIndex() < getRowCount());
   */
  public final boolean isRowAvailable() {
    return (getCollection() != null) && (getRowIndex() >= 0) && (getRowIndex() < getRowCount());
  }

  /**
   * @post new.getRowIndex() == rowIndex;
   * @throws IllegalArgumentException
   *          rowIndex < -1;
   */
  public final void setRowIndex(int rowIndex) {
    if (rowIndex < -1) {
      throw new IllegalArgumentException("rowIndex cannot be smaller than -1, " +
                                         "and you want it to be " + rowIndex);
    }
    LOG.debug("setting row index to " + rowIndex);
    int oldRowIndex = $rowIndex;
    $rowIndex = rowIndex;
    if (($collection != null) && (oldRowIndex != $rowIndex)) {
      fireEvents();
    }
  }

  private void fireEvents() {
    DataModelListener[] listeners = getDataModelListeners();
    LOG.debug("Firing events to " + listeners.length + " listeners; this will access the element at index " +
              $rowIndex + " if there are listeners");
    if (listeners.length > 0) {
      Object rowData = isRowAvailable() ? getRowData() : null;
      DataModelEvent event = new DataModelEvent(this, $rowIndex, rowData);
      for (int i = 0; i < listeners.length; i++) {
        listeners[i].rowSelected(event);
      }
    }
  }

  /**
   * @invar ($collection == null) ? ($rowIndex == -1);
   * @invar $rowIndex >= -1;
   */
  private int $rowIndex;

  /*</property>*/



  /*<property name="sortedCollection">*/
  //------------------------------------------------------------------

  /**
   * The sorted version of {@link #getCollection()}. Created
   * lazily on first request.
   *
   * @result (getCollection() == null) ? null;
   * @result (getCollection() != null) ? c:hasSameContents(result. getCollection());
   * @mudo (jand) result is sorted
   */
  public final List getSortedCollection() {
    if (getCollection() != null) {
      LOG.debug("request for sorted collection without wrapped data; returning null");
      return null;
    }
    if ($list == null) {
      LOG.debug("first request for sorted collection data; this means data is going to be accessed soon");
      // MUDO (jand) CREATE SORTED
      $list = new ArrayList(getCollection());
    }
    else {
      LOG.debug("returning cached sorted collection data");
    }
    return $list;
  }

  private List $list;


  /**
   * @return (getCollection() != null) ? getSortedCollection().get(getRowIndex()) : null;
   * @throws IllegalArgumentException
   *         ! isRowAvailable();
   */
  public final Object getRowData() throws IllegalArgumentException {
    LOG.debug("request for data at row " + getRowIndex());
    if (getCollection() == null) {
      return null;
    }
    if (! isRowAvailable()) {
      throw new IllegalArgumentException("row " + getRowIndex() + " is not available");
    }
    return getSortedCollection().get(getRowIndex());
  }

}
