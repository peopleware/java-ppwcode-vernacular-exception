package be.peopleware.bean_I;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Set;




/**
 * Static utility methods for associations.
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public class Associations {

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
   * This method gathers the type invariants for a reference that implements
   * the to-one part of a bidirectional one-to-many association. The reference
   * is optional or mandatory (weak type).
   *
   * @param     reference
   *            The object that <code>manyObject</code> refers to, that is on
   *            the one-side of the many-to-one association.
   * @param     toManySetPropertyName
   *            The name of the property that holds the to-many reference
   *            on <code>reference</code>.
   *            This property should contain <code>manyObject</code>.
   *            <code>reference</code> must have a getter for this property
   *            that returns a <code>Set</code>.
   * @param     manyObject
   *            The instance on the many-side of the many-to-one association,
   *            that owns <code>reference</code>.
   * @param     mandatory
   *            Is participation of <code>manyObject</code> into the many-to-one
   *            association mandatory? I.e., is <code>manyObject</code> of a
   *            weak type? In other words, must <code>reference</code> be
   *            non-<code>null</code>?
   * @pre       Beans.hasPropertyReadMethod(reference.getClass(),
   *                                        toManySetPropertyName);
   * @pre       manyObject != null;
   * @return    (mandatory ==> (reference != null))
   *            && ((reference != null) ==>
   *                    reference[toManySetPropertyName].contains(manyObject));
   */
  public static final boolean bidirectionalManyToOneReference(
      final Object reference,
      final String toManySetPropertyName,
      final Object manyObject,
      final boolean mandatory) {
    assert manyObject != null;
    boolean result;
    if (reference == null) {
      result = !mandatory;
    }
    else { // reference != null
      try {
        assert Beans.hasPropertyReadMethod(reference.getClass(),
                                           toManySetPropertyName);
      }
      catch (IntrospectionException iExc) {
        assert false : "IntrospectionExceptionshould not happen: " + iExc; //$NON-NLS-1$
      }
      Set manySet = null;
      try {
        manySet = (Set)Beans.getPropertyValue(reference, toManySetPropertyName);
      }
      catch (NullPointerException npExc) {
        assert false : "NullPointerExceptionshould not happen: " + npExc; //$NON-NLS-1$
      }
      catch (IntrospectionException iExc) {
        assert false : "IntrospectionExceptionshould not happen: " + iExc; //$NON-NLS-1$
      }
      catch (NoSuchMethodException nsmExc) {
        assert false : "NoSuchMethodExceptionshould not happen: " + nsmExc; //$NON-NLS-1$
      }
      catch (IllegalAccessException iaExc) {
        assert false : "IllegalAccessExceptionshould not happen: " + iaExc; //$NON-NLS-1$
      }
      catch (InvocationTargetException itExc) {
        assert false : "InvocationTargetExceptionshould not happen: " + itExc; //$NON-NLS-1$
      }
      result = manySet.contains(manyObject);
    }
    return result;
  }

  /**
   * This method gathers the type invariants for a set that implements
   * the to-many part of a bidirectional one-to-many association.
   *
   * @param     manySet
   *            The set that is to contain many references to instances of
   *            type <code>manyType</code>. This set is owned by
   *            <code>oneObject</code>.
   * @param     manyType
   *            The type on the many side of the one-to-many association.
   * @param     toOneReferencePropertyName
   *            The name of the property that holds the to-one reference
   *            on instances of <code>manyType</code> in <code>manySet</code>.
   *            This property should refer to <code>oneObject</code>.
   *            <code>manyType</code> must have a getter for this property
   *            that returns a reference to an instance of the type of
   *            <code>oneObject</code>.
   * @param     oneObject
   *            The instance on the one-side of the one-to-many association,
   *            that owns <code>manySet</code>.
   * @pre       manyType != null;
   * @pre       Beans.hasPropertyReadMethod(manyType, toOneReferenceProperty);
   * @return    (manySet != null)
   *            && (!manySet.contains(null))
   *            && (forall Object o; manySet.contains(o); o instanceof manyType)
   *            && (forall Object o; manySet.contains(o);
   *                    o[toOneReferencePropertyName] == oneObject);
   */
  public static final boolean bidirectionalOneToManySet(
      final Set manySet,
      final Class manyType,
      final String toOneReferencePropertyName,
      final Object oneObject) {
    assert manyType != null;
    boolean result = (manySet != null) && (!manySet.contains(null));
    if (result) {
      Iterator iter = manySet.iterator();
      while (iter.hasNext()) {
        Object manyObject = iter.next();
        try {
          if ((!manyType.isInstance(manyObject))
              || (Beans.getPropertyValue(manyObject, toOneReferencePropertyName)
                      != oneObject)) {
            return false; // break
          }
          // else continue
        }
        catch (NullPointerException npExc) {
          assert false : "NullPointerExceptionshould not happen: " + npExc; //$NON-NLS-1$
        }
        catch (IntrospectionException iExc) {
          assert false : "IntrospectionExceptionshould not happen: " + iExc; //$NON-NLS-1$
        }
        catch (NoSuchMethodException nsmExc) {
          assert false : "NoSuchMethodExceptionshould not happen: " + nsmExc; //$NON-NLS-1$
        }
        catch (IllegalArgumentException iaExc) {
          assert false : "IllegalArgumentExceptionshould not happen: " + iaExc; //$NON-NLS-1$
        }
        catch (IllegalAccessException iaExc) {
          assert false : "IllegalAccessExceptionshould not happen: " + iaExc; //$NON-NLS-1$
        }
        catch (InvocationTargetException itExc) {
          assert false : "InvocationTargetExceptionshould not happen: " + itExc; //$NON-NLS-1$
        }
      }
      // else result is still true
    }
    return result;
  }

}
