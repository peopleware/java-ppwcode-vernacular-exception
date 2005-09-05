package be.peopleware.bean_V;


/**
 * <p>Delegates are objects used internally in a <dfn>delegating bean</dfn>
 *   to do work for the bean. Because different instances of a delegate type
 *   can be used in different types of delegating beans, this is a potent
 *   form of reuse. Delegating beans keep <code>private</code> the fact that
 *   they are using a delegate for certain tasks internally. When delegation
 *   is acknowledged to public users, please use an <code>ActiveProperty</code>
 *   (see <a href="http://www.beedra.org/">Beedra</a>).</p>
 * <p>Delegates know which bean and which property they work for.
 *   This knowledge can, e.g., be used to throw
 *   {@link be.peopleware.bean_V.PropertyException}s.
 *   Delegates are not beans themselves (there is no default
 *   constructor). The properties introduced in this class
 *   are immutable.</p>
 * <p>Delegates cannot be made {@link Cloneable} or
 *   {@link java.io.Serializable}.
 *   The {@link Object#equals(Object)} should not be overridden.
 *   Instead, a {@link #hasSameValues(Object)} method is offered.</p>
 * <p>Here is the delegate use idiom:</p>
 * <pre>
 * &hellip;
 * public class <var>MyDelegatingBean</var> &hellip; {
 *
 *   /&#x2A;&lt;constructors&gt;&#x2A;/
 *   //------------------------------------------------------------------
 *
 *   public <var>MyDelegatingBean</var>(&hellip;) {
 *     &hellip;
 *   }
 *
 *   /&#x2A;&lt;/constructors&gt;&#x2A;/
 *
 *   &hellip;
 *
 *   /&#x2A;&lt;property name=&quot;<var>propertyName</var>&quot;&gt;&#x2A;/
 *   //------------------------------------------------------------------
 *
 *   &hellip;
 *   <var>public delegation methods to access delegate functionality</var>
 *   &hellip;
 *
 *   /&#x2A;*
 *    * &hellip;
 *    * &#x40;invar $<var>myDelegate</var> != null;
 *    * &#x40;invar $<var>myDelegate</var>.getDelegatingBean() == this;
 *    &#x2A;/
 *   private <var>MyDelegate</var> $<var>myDelegate</var> = new <var>MyDelegate</var>(this, &quot;<var>propertyName</var>&quot;);
 *
 *   /&#x2A;&lt;/property&gt;&#x2A;/
 *
 *   &hellip;
 * }
 * </pre>
 * <p>It often makes sense to specify the actual <var>MyDelegate</var> class
 *   as a private or protected inner class or even anonymous inner class of
 *   <var>MyDelegatingBean</var>.</p>
 * <p>From the above idiom it should be clear that the constructor of the
 *   delegate is called <em>during</em> the initialization phase of the
 *   delegating instance. Furthermore, a reference to this
 *   <em>delegating-instance-under-construction</em> is passed into the
 *   delegate constructor at this time. Since the initialization phase
 *   of the delegating instance is not necessarily completed yet (it
 *   probably isn't), the delegating instance does not yet necessarily
 *   adhere to its type invariants. <em>This means that you can create
 *   giant problems if the delegate constructor accesses the delegating
 *   instance in any way. <strong>The constructor of a delegate should
 *   only store the reference to the delegating bean, and never ever
 *   access that instance in any way.</strong></em> Storing the reference
 *   to the delegating instance is done by the constructor in this class.</p>
 * <h2>Note for use in the context of Hibernate</h2>
 * <p>When used in the context of Hibernate, delegates are often
 *   treated as Hibernate <dfn>components</dfn>. When a delegating
 *   bean is initialised from persistent storage, it is created
 *   with a default constructor. Then, components are created with a default
 *   constructor, and set on the delegating bean. In essence, it
 *   would be advisable to change Hibernate not to do this. Normal behavior
 *   with delegates is that they are constructed, initialized and set
 *   during initialization (construction) of the delegating bean, and
 *   that they are stored in final instance variables. Hibernate should
 *   be aware of this, and be coerced not to create new delegate instances,
 *   but fill the values of the existing delegates. This is considered
 *   too difficult for now.<br />
 *   To accomodate the use of delegates as components in a Hibernate
 *   context, methods are offered that make it possible
 *   to create a instance of the delegate without the delegating bean
 *   and property name set.
 *   <strong>The delegating bean and property name should then be set through
 *   {@link #setDelegatingBean(Object)} and {@link #setPropertyName(String)}
 *   immediately after construction.</strong> To achieve this, the delegating
 *   bean needs to apply delegation with the following idiom:</p>
 * <pre>
 * &hellip;
 * public class <var>MyDelegatingBean</var> &hellip; {
 *
 *   /&#x2A;&lt;constructors&gt;&#x2A;/
 *   //------------------------------------------------------------------
 *
 *   public <var>MyDelegatingBean</var>(&hellip;) {
 *     &hellip;
 *     set<var>MyDelegate</var>(new <var>MyDelegate</var>());
 *       /&#x2A; this new <var>MyDelegate</var> will be overwritten if it is
 *        * Hibernate that is initializing this new <var>MyDelegatingBean</var>.
 *        * There is no way to work around this inefficiency.
 *        &#x2A;/
 *     &hellip;
 *   }
 *
 *   /&#x2A;&lt;/constructors&gt;&#x2A;/
 *
 *   &hellip;
 *
 *   /&#x2A;&lt;property name=&quot;<var>propertyName</var>&quot;&gt;&#x2A;/
 *   //------------------------------------------------------------------
 *
 *   &hellip;
 *   <var>public delegation methods to access delegate functionality</var>
 *   &hellip;
 *
 *   /&#x2A;*
 *    * This method is package accessible, because it will be used
 *    * to read the delegate by Hibernate.
 *    *
 *    * &#x40;basic
 *    &#x2A;/
 *   <var>MyDelegate</var> get<var>MyDelegate</var>() {
 *     return $<var>myDelegate</var>;
 *   }
 *
 *   /&#x2A;*
 *    * This method is package accessible, because it will be used
 *    * to set the delegate by Hibernate.
 *    *
 *    * &#x40;param <var>myDelegate</var>
 *    *        The new delegate for this delegating bean.
 *    * &#x40;pre <var>myDelegate</var> != null;
 *    * &#x40;pre <var>myDelegate</var>.getDelegatingBean() == null;
 *    * &#x40;post new.get<var>MyDelegate</var>() == <var>myDelegate</var>;
 *    * &#x40;post new.get<var>MyDelegate</var>().getDelegatingBean() == this;
 *    * &#x40;post new.get<var>MyDelegate</var>().getPropertyName().equals(&quot;<var>propertyName</var>&quot;);
 *    &#x2A;/
 *   void set<var>MyDelegate</var>(<var>MyDelegate</var> <var>myDelegate</var>) {
 *     assert <var>myDelegate</var> != null;
 *     assert <var>myDelegate</var>.getDelegatingBean() == null;
 *     $<var>myDelegate</var> = <var>myDelegate</var>;
 *     $<var>myDelegate</var>.setDelegatingBean(this);
 *     $<var>myDelegate</var>.setPropertyName(&quot;<var>propertyName</var>&quot;);
 *   }
 *
 *   /&#x2A;*
 *    * &hellip;
 *    * This delegate variable cannot be made final, because when
 *    * used as a component in a Hibernate context, it will be
 *    * overwritten immediately after construction.
 *    *
 *    * &#x40;invar $<var>myDelegate</var> != null;
 *    * &#x40;invar $<var>myDelegate</var>.getDelegatingBean() == this;
 *    &#x2A;/
 *   private <var>MyDelegate</var> $<var>myDelegate</var>;
 *
 *   /&#x2A;&lt;/property&gt;&#x2A;/
 *
 *   &hellip;
 * }
 * </pre>
 * <p>The Hibernate <kbd>hbm</kbd> file then should have an entry based
 *   on the following idiom:</p>
 * <pre>
 * &lt;?xml version=&quot;1.0&quot;?&gt;
 * &lt;!DOCTYPE hibernate-mapping PUBLIC &quot;-//Hibernate/Hibernate Mapping DTD 2.0//EN&quot; &quot;http://hibernate.sourceforge.net/hibernate-mapping-2.0.dtd&quot;&gt;
 * &lt;hibernate-mapping&gt;
 *  &lt;&hellip;class name=&quot;<var>&hellip;.MyDelegatingBean</var>&quot; &hellip;&gt;
 *    &hellip;
 *    &lt;component name=&quot;<var>myDelegate</var>&quot;
 *               class=&quot;<var>&hellip;.MyDelegate</var>&quot;
 *               access=&quot;method&quot;&gt;
 *      <var>field and collection mappings of the delegate</var>
 *    &lt;/component&gt;
 *    &hellip;
 *  &lt;/&hellip;class&gt;
 * &lt;/hibernate-mapping&gt;
 * </pre>
 * <p><strong>This setup to accomodate Hibernate should be considered a
 *   workaround until a fundamental solution is found or
 *   created. These methods are labelled deprecated for easy trace
 *   later.</strong></p>
 *
 * @idea (jand): Change Hibernate to accomodate for delegates in the way 
 *       described above. Investigate whether the current extension mechanisms
 *       can be used (doubtful). Otherwise, contact the Hibernate developers to
 *       see what the scope of the change would be. It is highly likely that
 *       this requires a new kind of element in the <kbd>hbm</kbd> files.
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 *
 * @invar     getDelegatingBean() != null;
 * @invar     (getPropertyName() != null) && (getPropertyName().length() > 0);
 * @invar     !Cloneable.class.isAssignableFrom(getClass());
 * @invar     !java.io.Serializable.class.isAssignableFrom(getClass());
 * @invar     (forall Object o; ; equals(o) == (this == o));
 *                do not override the <code>equals</code> method
 */
public class Delegate {

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



  /*<construction>*/
  //------------------------------------------------------------------

  /**
   * @param     delegatingBean
   *            The bean that delegates to this.
   * @param     propertyName
   *            The name of the property for which <code>delegatingBean</code>
   *            uses this.
   *
   * @pre       delegatingBean != null;
   * @pre       (propertyName != null) && (propertyName.length() > 0);
   * @post      new.getDelegatingBean() == delegatingBean;
   * @post      new.getpropertyName().equals(propertyName);
   */
  public Delegate(final Object delegatingBean, final String propertyName) {
    assert delegatingBean != null : "delegatingBean != null"; //$NON-NLS-1$
    assert (propertyName != null) && propertyName.length() > 0
           : "(propertyName != null) && getPropertyName().length() > 0"; //$NON-NLS-1$
    $delegatingBean = delegatingBean;
    $propertyName = propertyName;
  }

  /**
   * This method is here only to accomodate Hibernate. See
   * the class description above. Type invariants are not ok
   * when this constructor is used. The user should call
   * {@link #setDelegatingBean(Object)}
   * immediately after calling this constructor. Users should
   * create a subtype that does feature a default constructor,
   * where this constructor is called. This migh be possible
   * in many circumstances, since the property name is dependent
   * on the type of the delegate (static) and not on a particular
   * instance, as {@link #getDelegatingBean()} is.
   *
   * @deprecated
   *
   * @param     propertyName
   *            The name of the property for which <code>delegatingBean</code>
   *            uses this.
   *
   * @pre       (propertyName != null) && (propertyName.length() > 0);
   * @post      new.getDelegatingBean() == null;
   * @post      new.getpropertyName().equals(propertyName);
   */
  public Delegate(final String propertyName) {
    assert (propertyName != null) && propertyName.length() > 0
            : "(propertyName != null) && getPropertyName().length() > 0"; //$NON-NLS-1$
    $propertyName = propertyName;
  }

  /**
   * This method is here only to accomodate Hibernate. See
   * the class description above. Type invariants are not ok
   * when this constructor is used. The user should call
   * {@link #setDelegatingBean(Object)} and {@link #setPropertyName(String)}
   * immediately after calling this constructor.
   *
   * @deprecated
   *
   * @post  new.getDelegatingBean() == null;
   * @post  new.getPropertyName() == null;
   */
  public Delegate() {
    // NOP
  }

  /*</construction>*/



  /*<property name="delegatingBean">*/
  //------------------------------------------------------------------

  /**
   * The object we are a delegate for.
   *
   * @basic
   */
  public final Object getDelegatingBean() {
    return $delegatingBean;
  }

  /**
   * This method is here only to accomodate Hibernate. See
   * the class description above.
   *
   * @deprecated
   *
   * @param delegatingBean
   *        The new delegating bean
   * @pre   delegatingBean != null;
   * @post  new.getDelegatingBean() == delegatingBean;
   */
  public final void setDelegatingBean(final Object delegatingBean) {
    $delegatingBean = delegatingBean;
  }

  /**
   * @invar     $delegatingBean != null;
   */
  private Object $delegatingBean;

  /*</property>*/



  /*<property name="propertyName">*/
  //------------------------------------------------------------------

  /**
   * The name of the property we are the delegate for.
   *
   * @basic
   */
  public final String getPropertyName() {
    return $propertyName;
  }

  /**
   * This method is here only to accomodate Hibernate. See
   * the class description above.
   *
   * @deprecated
   *
   * @param propertyName
   *        The new property name
   * @pre   propertyName != null;
   * @pre   propertyName.length() > 0;
   * @post  new.getPropertyName().equals(propertyName);
   *
   */
  public final void setPropertyName(final String propertyName) {
    assert propertyName != null;
    assert propertyName.length() > 0;
    $propertyName = propertyName;
  }

  /**
   * @invar     $propertyName != null;
   * @invar     $propertyName.length() > 0;
   */
  private String $propertyName;

  /*</property>*/


  /**
   * This instance has the same values as the instance <code>o</code>.
   * In contrast to {@link #equals(Object)} compares referential
   * equality. The delegating bean and the property name are not
   * taken into account.
   *
   * @param     o
   *            The object to compare values with.
   * @return    result ==>  (o != null) && (getClass() == o.getClass());
   */
  public boolean hasSameValues(final Object o) {
    return (o != null) && (getClass() == o.getClass());
  }

  /**
   * A String that lists all data in this object. This string does not
   * contain the delegating bean, but it does list the property name.
   *
   * @return ", " + getPropertyName() + ": ";
   */
  protected String unclosedPropertiesString() {
     return ", " + getPropertyName() + ": "; //$NON-NLS-1$ //$NON-NLS-2$
  }

}