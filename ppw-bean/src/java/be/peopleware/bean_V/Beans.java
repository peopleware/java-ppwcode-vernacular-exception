/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.bean_V;


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.taglibs.standard.lang.jstl.Coercions;
import org.apache.taglibs.standard.lang.jstl.ELException;
import org.apache.taglibs.standard.lang.jstl.Logger;


/**
 * Convenience methods for working with JavaBeans.
 *
 * @mudo (jand) most methods are also in Toryt.support.Reflection; consolidate
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public class Beans {

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
   * Returns the {@link PropertyDescriptor} of the property with
   * name <code>propertyName</code> of <code>beanClass</code>. If such a
   * property or such an descriptor does not exist, an exception is thrown.
   *
   * @param     beanClass
   *            The bean class to get the property descriptor of
   * @param     propertyName
   *            The programmatic name of the property we want the descriptor for
   * @return    PropertyDescription
   *            result != null;
   * @throws    IntrospectionException
   *            Cannot get the <code>BeanInfo</code> of <code>beanClass</code>,
   *            cannot find a property descriptor.
   *
   * @pre       beanClass != null;
   *
   * @deprecated Use <a href="http://jakarta.apache.org/commons/beanutils/api/org/apache/commons/beanutils/PropertyUtils.html"
   *               target="extern">Apache Jakarta Commons beanutils PropertyUtil.getPropertyDescriptor()</a> instead.
   */
  public static PropertyDescriptor
      getPropertyDescriptor(final Class beanClass, final String propertyName)
      throws IntrospectionException {
    assert beanClass != null;
    BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
                                 // throws IntrospectionException
    PropertyDescriptor[] propertyDescriptors =
        beanInfo.getPropertyDescriptors();
                                 // entries in the array are never null
    PropertyDescriptor descriptor = null;
    for (int i = 0; i < propertyDescriptors.length; i++) {
      if (propertyDescriptors[i].getName().equals(propertyName)) {
                // PropertyDescriptors always return a non-null name
        descriptor = propertyDescriptors[i];
        break;
      }
    }
    if (descriptor == null) {
      throw new IntrospectionException("No property descriptor found for " //$NON-NLS-1$
                                       + propertyName
                                       + " in " //$NON-NLS-1$
                                       + beanClass.getName());
    }
    return descriptor;
  }

  /**
   * Returns the {@link PropertyEditor} of the property with
   * name <code>propertyName</code> of <code>beanClass</code>. If such a
   * property does not exist, an exception is thrown. If no editor is found,
   * <code>null</code> is returned.
   *
   * @param     beanClass
   *            The bean class to get the property editor of
   * @param     propertyName
   *            The programmatic name of the property we want the editor for
   * @return    PropertyEditor
   *            Returns the {@link PropertyEditor} of the property with
   *            name <code>propertyName</code> of <code>beanClass</code>.
   * @throws    IntrospectionException
   *            Cannot get the <code>BeanInfo</code> of <code>beanClass</code>,
   *            cannot find a property descriptor.
   * @throws    InstantiationException
   *            Could not instantiate the editor according to bean info.
   * @throws    IllegalAccessException
   *            Could not instantiate the editor according to bean info.
   * @throws    ClassCastException
   *            Could not instantiate the editor according to bean info.
   *
   * @pre       beanClass != null;
   */
  public static PropertyEditor
      getPropertyEditorInstance(final Class beanClass,
                                final String propertyName)
      throws IntrospectionException,
             InstantiationException,
             IllegalAccessException,
             ClassCastException {
    assert beanClass != null;
    PropertyEditor result = null;
    PropertyDescriptor descriptor = getPropertyDescriptor(beanClass, propertyName);
    Class editorClass = descriptor.getPropertyEditorClass();
    // probable not found
    if (editorClass != null) {
      result = (PropertyEditor)editorClass.newInstance();
      // InstantiationException, IllegalAccessException, ClassCastException
    }
    else {
      result = PropertyEditorManager.findEditor(descriptor.getPropertyType());
    }
    return result;
  }

  /**
   * Returns the method object of the inspector of the property with
   * name <code>propertyName</code> of <code>beanClass</code>. If such a
   * property or such an inspector does not exist, an exception is thrown.
   * This method only finds implemented methods,
   * thus not methods in interfaces or abstract methods.
   *
   * @todo check that This method only finds implemented methods,
   * thus not methods in interfaces or abstract methods, and fix.
   *
   * @param     beanClass
   *            The bean class to get the property read method of
   * @param     propertyName
   *            The programmatic name of the property we want to read
   * @pre       beanClass != null;
   * @return    result != null;
   * @throws    IntrospectionException
   *            Cannot get the <code>BeanInfo</code> of <code>beanClass</code>.
   * @throws    NoSuchMethodException
   *            There is no read method for this property.
   */
  public static Method getPropertyReadMethod(final Class beanClass,
                                             final String propertyName)
      throws IntrospectionException, NoSuchMethodException {
    assert beanClass != null;
    Method inspector = getPropertyDescriptor(beanClass, propertyName).getReadMethod();
        // this can be null for an read-protected property
    if (inspector == null) {
      throw new NoSuchMethodException("No read method for property " //$NON-NLS-1$
                                      + propertyName);
    }
    return inspector;
  }

  /**
   * Check whether <code>beanClass</code> has a no-arguments getter for
   * <code>propertyName</code>. This method only finds implemented methods,
   * thus not methods in interfaces or abstract methods.
   *
   * @todo check that This method only finds implemented methods,
   * thus not methods in interfaces or abstract methods, and fix.
   *
   * @param     beanClass
   *            The bean class to get the property read method of
   * @param     propertyName
   *            The programmatic name of the property we want to read
   * @pre       beanClass != null;
   * @throws    IntrospectionException
   *            Cannot get the <code>BeanInfo</code> of <code>beanClass</code>,
   *            cannot find a property descriptor.
   */
  public static boolean hasPropertyReadMethod(final Class beanClass,
                                              final String propertyName)
      throws IntrospectionException {
    assert beanClass != null;
    return getPropertyDescriptor(beanClass, propertyName).getReadMethod() != null;
  }

  /**
   * Returns the method object of the mutator of the property with
   * name <code>propertyName</code> of <code>beanClass</code>. If such a
   * property or such an mutator does not exist, an exception is thrown.
   *
   * @param     beanClass
   *            The bean class to get the property write method of
   * @param     propertyName
   *            The programmatic name of the property we want to write
   * @pre       beanClass != null;
   * @return    result != null;
   * @throws    IntrospectionException
   *            Cannot get the <code>BeanInfo</code> of <code>beanClass</code>.
   * @throws    NoSuchMethodException
   *            There is no write method for this property.
   */
  public static Method getPropertyWriteMethod(final Class beanClass,
                                              final String propertyName)
      throws IntrospectionException, NoSuchMethodException {
    assert beanClass != null;
    Method mutator = getPropertyDescriptor(beanClass, propertyName).getWriteMethod();
        // this can be null for a write-protected property
    if (mutator == null) {
      throw new NoSuchMethodException("No write method for property " //$NON-NLS-1$
                                      + propertyName);
    }
    return mutator;
  }

  /**
   * The value of the property <code>propertyExpression</code>
   * of object <code>bean</code>. <code>propertyExpression</code>
   * can be an expression to navigate through an object graph,
   * of the form <code><var>property1</var>.<var>property2</var>.<var>property3</var></code>.
   *
   * @param     bean
   *            The bean to get the property value of
   * @param     propertyExpression
   *            A dot-separated chain of programmatic property names
   *            to navigate.
   * @throws    NullPointerException
   *            bean == null; or an intermediate property is null
   * @throws    IntrospectionException
   *            Cannot get the <code>BeanInfo</code> of <code>bean</code> class.
   * @throws    NoSuchMethodException
   *            There is no read method for this property in the bean.
   * @throws    IllegalAccessException
   *            This user is not allowed to access the read method of
   *            the <code>propertyName()</code>-property of <code>bean</code>.
   * @throws    InvocationTargetException
   *            The read method of the property <code>propertyName</code>,
   *            applied to <code>bean</code>, has thrown an exception.
   *
   * @deprecated Use <a href="http://jakarta.apache.org/commons/beanutils/api/org/apache/commons/beanutils/PropertyUtils.html"
   *               target="extern">Apache Jakarta Commons beanutils PropertyUtil.getProperty()</a> instead.
   */
  public static Object getNavigatedPropertyValue(final Object bean,
                                        final String propertyExpression)
      throws NullPointerException,
             IntrospectionException,
             NoSuchMethodException,
             IllegalAccessException,
             InvocationTargetException {
    String[] parts = propertyExpression.split("\\.");
    Object cursor = bean;
    for (int lcv = 0; lcv < parts.length; lcv++) {
      cursor = getPropertyValue(cursor, parts[lcv]);
    }
    return cursor;
  }

  /**
   * The value to be written to the text file, as String.
   *
   * @param     bean
   *            The bean to get the property value of
   * @param     propertyName
   *            The programmatic name of the property we want to read
   * @return    Object
   *            @todo (dvankeer): (JAVADOC) Write description.
   * @throws    NullPointerException
   *            bean == null;
   * @throws    IntrospectionException
   *            Cannot get the <code>BeanInfo</code> of <code>bean</code> class.
   * @throws    NoSuchMethodException
   *            There is no read method for this property in the bean.
   * @throws    IllegalAccessException
   *            This user is not allowed to access the read method of
   *            the <code>propertyName()</code>-property of <code>bean</code>.
   * @throws    InvocationTargetException
   *            The read method of the property <code>propertyName</code>,
   *            applied to <code>bean</code>, has thrown an exception.
   *
   * @deprecated Use <a href="http://jakarta.apache.org/commons/beanutils/api/org/apache/commons/beanutils/PropertyUtils.html"
   *               target="extern">Apache Jakarta Commons beanutils PropertyUtil.getProperty()</a> instead.
   */
  public static Object getPropertyValue(final Object bean,
                                        final String propertyName)
      throws NullPointerException,
             IntrospectionException,
             NoSuchMethodException,
             IllegalAccessException,
             InvocationTargetException {
    Method inspector = getPropertyReadMethod(bean.getClass(), propertyName);
    // != null; throws loads of exceptions
    Object result = null;
    try {
      result = inspector.invoke(bean, null);
    }
    catch (IllegalArgumentException iaExc) {
      assert false : "Should not happen, since there are no " //$NON-NLS-1$
                     + "arguments, and the implicit argument is " //$NON-NLS-1$
                     + "not null and of the correct type"; //$NON-NLS-1$
    }
    /* ExceptionInInitializerError can occur with invoke, but we do not
        take into account errors */
    return result;
  }

  /**
   * Set the value of the property with name <code>propertyName</code>
   * of the bean <code>bean</code> to <code>value</code>.
   *
   * @param     bean
   *            The bean to set the property value of
   * @param     propertyName
   *            The programmatic name of the property we want to write
   * @param     value
   *            The value to be stored in the property
   * @throws    NullPointerException
   *            bean == null;
   * @throws    IntrospectionException
   *            Cannot get the <code>BeanInfo</code> of <code>bean</code> class.
   * @throws    NoSuchMethodException
   *            There is no write method for this property in the bean.
   * @throws    IllegalAccessException
   *            This user is not allowed to access the write method of
   *            the <code>propertyName()</code>-property of <code>bean</code>.
   * @throws    InvocationTargetException
   *            The write method of the property <code>propertyName</code>,
   *            applied to <code>bean</code>, has thrown an exception.
   * @throws    IllegalArgumentException
   *            <code>value</code> cannot be coerced in an value of the type
   *            expected by the property write method.
   *
   * @deprecated Use <a href="http://jakarta.apache.org/commons/beanutils/api/org/apache/commons/beanutils/PropertyUtils.html"
   *               target="extern">Apache Jakarta Commons beanutils PropertyUtil.setProperty()</a> instead.
   */
  public static void setPropertyValue(final Object bean,
                                      final String propertyName,
                                      final Object value)
      throws NullPointerException,
             IntrospectionException,
             NoSuchMethodException,
             IllegalAccessException,
             InvocationTargetException,
             IllegalArgumentException {
    Method mutator = getPropertyWriteMethod(bean.getClass(), propertyName);
    // != null; throws loads of exceptions
    Object[] args = {value};
    mutator.invoke(bean, args);
    /* ExceptionInInitializerError can occur with invoke, but we do not
        take into account errors */
  }

  /**
   * Set the value of the property with name <code>propertyName</code>
   * of the bean <code>bean</code> to <code>value</code>, after coercing
   * it according to EL rules.
   *
   * @param     bean
   *            The bean to set the property value of
   * @param     propertyName
   *            The programmatic name of the property we want to write
   * @param     value
   *            The value to be stored in the property
   * @throws    IntrospectionException
   *            Cannot get the <code>BeanInfo</code> of <code>bean</code> class.
   * @throws    NoSuchMethodException
   *            There is no write method for this property in the bean.
   * @throws    IllegalAccessException
   *            This user is not allowed to access the write method of
   *            the <code>propertyName()</code>-property of <code>bean</code>.
   * @throws    InvocationTargetException
   *            The write method of the property <code>propertyName</code>,
   *            applied to <code>bean</code>, has thrown an exception.
   *
   * @idea (jand): Candidate for more broad use
   */
  public static void setPropertyValueCoerced(final Object bean,
                                             final String propertyName,
                                             final Object value)
    throws IntrospectionException,
           NoSuchMethodException,
           InvocationTargetException,
           IllegalAccessException {
    Method mutator = Beans.getPropertyWriteMethod(bean.getClass(),
                                                  propertyName);
    // != null; throws loads of exceptions
    if (mutator.getParameterTypes().length != 1) {
      throw new IllegalArgumentException("property setter \"" //$NON-NLS-1$
                                         + mutator
                                         + "\" expects more or less " //$NON-NLS-1$
                                         + "than 1 argument"); //$NON-NLS-1$
    }
    Class expectedType = mutator.getParameterTypes()[0];
    // array length is checked already
    try {
      Object coercedValue = Coercions.coerce(value,
                                             expectedType,
                                             new Logger(System.err));
      Object[] args = {coercedValue};
      mutator.invoke(bean, args);
      /* ExceptionInInitializerError can occur with invoke, but we do not
          take into account errors */
    }
    catch (ELException elExc) {
      throw new IllegalArgumentException("unable to coerce \"" //$NON-NLS-1$
                                          + value
                                          + "\" into type \"" //$NON-NLS-1$
                                          + expectedType
                                          + "\""); //$NON-NLS-1$
    }
  }

  private static final String PREFIXED_FQCN_PATTERN = "\\."; //$NON-NLS-1$

  private static final String EMPTY = ""; //$NON-NLS-1$

  private static final String DOT = "."; //$NON-NLS-1$

  /**
   * Return a fully qualified class name that is in the same package
   * as <code>fqcn</code>, and has as class name
   * <code>prefix + <var>ClassName</var></code>.
   *
   * @param prefix
   *        The prefix to add before the class name.
   * @param fqcn
   *        The fully qualified class name to start from.
   */
  public static String prefixedFqcn(final String prefix,
                                    final String fqcn) {
    String[] parts = fqcn.split(PREFIXED_FQCN_PATTERN);
    String prefixedName = prefix + parts[parts.length - 1];
    String result = EMPTY;
    for (int i = 0; i < parts.length - 1; i++) {
      result = result + parts[i] + DOT;
    }
    result = result + prefixedName;
    return result;
  }

  /**
   * Load the class with name
   * <code>prefixedFqcn(prefix, fqcn)</code>.
   *
   * @param prefix
   *        The prefix to add before the class name.
   * @param fqcn
   *        The original fully qualified class name to derive
   *        the prefixed class name from.
   * @throws ClassNotFoundException
   *         true;
   */
  public static Class loadPrefixedClass(final String prefix,
                                        final String fqcn)
      throws ClassNotFoundException {
    return Class.forName(prefixedFqcn(prefix, fqcn));
  }

  /**
   * Instantiate an object of a type
   * <code>prefixedFqcn(prefix, fqcn)</code>.
   *
   * @param cl
   *        The class-loader from which we should create
   *        the bean. If this is null, then the system class-loader
   *        is used.
   * @param prefix
   *        The prefix to add before the class name.
   * @param fqcn
   *        The original fully qualified class name to derive
   *        the prefixed class name from.
   * @throws ClassNotFoundException
   * @throws IOException
   */
  public static Object instantiatePrefixed(final ClassLoader cl,
                                           final String prefix,
                                           final String fqcn)
      throws IOException, ClassNotFoundException {
    return java.beans.Beans.instantiate(cl, prefixedFqcn(prefix, fqcn));
  }

  /**
   * Returns the constant(public final static) with the given fully qualified
   * name.
   *
   * @param     fqClassName
   *            The fully qualified class name of the type to look in
   *            for the constant.
   * @param     constantName
   *            The name of the constant whose value to return.
   * @return    Object
   *            The value of the field named <code>constantName</code>
   *            in class <code>fqClassName</code>.
   * @throws    LinkageError
   *            Error retrieving value.
   * @throws    ClassNotFoundException
   *            Could not find class <code>fqClassName</code>.
   * @throws    NoSuchFieldException
   *            Could not find a field named <code>constantName</code>
   *            in class <code>fqClassName</code>.
   * @throws    NullPointerException
   *            Error retrieving value.
   * @throws    SecurityException
   *            Not allowed to read the value of the field named
   *            <code>constantName</code>
   *            in class <code>fqClassName</code>.
   * @throws    IllegalAccessException
   *            The field named
   *            <code>constantName</code>
   *            in class <code>fqClassName</code> is not public.
   * @throws    IllegalArgumentException
   *            Error retrieving value.
   */
  public static Object constant(final String fqClassName,
                                final String constantName)
      throws LinkageError,
             ClassNotFoundException,
             NoSuchFieldException,
             NullPointerException,
             SecurityException,
             IllegalAccessException,
             IllegalArgumentException {
    Class clazz = Class.forName(fqClassName); // LinkageError,
                                              // ClassNotFoundException
    Field field = clazz.getField(constantName); // NoSuchFieldException
                                                // NullPointerException
                                                // SecurityException
    return field.get(null); // IllegalAccessException
                            // IllegalArgumentException
                            // NullPointerException
                            // ExceptionInInitializerError
  }

}
