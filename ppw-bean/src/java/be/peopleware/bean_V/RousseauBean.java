/*<license>
  Copyright 2004, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package be.peopleware.bean_V;


/**
 * <p>A type that formalizes how to work with JavaBeans that represent
 *   real-world objects during only a part of there life cycle.</p>
 * <a href="http://en.wikipedia.org/wiki/Jean-Jacques_Rousseau">
 * <img src="http://upload.wikimedia.org/wikipedia/en/thumb/0/0e/180px-Jean-JacquesRousseau.jpg"
 *      align="right" /></a>
 * <p>Since JavaBeans are required to have a default constructor, often
 *   some properties cannot be given a semantically acceptable value
 *   at instantiation. This triggers a pattern where instances of the
 *   bean class can exist in a state where it does not represent a
 *   real-world object of the type the class is intended for, i.e.,
 *   the instances do not conform to type invariants that would apply
 *   direct representations of the real-world objects. Such beans
 *   are created in a <dfn>wild</dfn> state, then
 *   go through a setup phase where a number of properties are set,
 *   and then enter a lifecycle phase where they do represent a
 *   real-world object of the type the class is intended for (they become
 *   <dfn>civilized</dfn>).
 *   Typically, by changing one or more properties, such objects
 *   can also leave the civilized state, which typically happens before
 *   the object is terminated.</p>
 * <p>This type offers a number of methods to support this pattern.</p>
 * <p>Normally, invariants are specified and enforced as much as possible.
 *   This is possible for all properties for which there exists a
 *   <dfn>civilized</dfn> default value that can be set in the default
 *   constructor. Typically, this is at least not possible with
 *   associations, if the association is mandatory.</p>
 * <p>The extra rules that should apply in a <dfn>civilized</dfn> state
 *   can be checked by calling {@link #getWildExceptions()}.
 *   {@link #isCivilized()} gives a simple boolean answer about the state
 *   of the Rousseau bean. It is possible that certain subtypes are able
 *   to change their properties from a <dfn>wild</dfn> state to a
 *   <dfn>civilized</dfn> state, without actually changing the semantics
 *   of the stored information. This is called <dfn>normalization</dfn>
 *   ({@link #normalize()}). Normalization keeps
 *   {@link #hasSameValues(RousseauBean)}.</p>
 * <p>Since <code>RousseauBean</code>s are semantic classes, i.e., they
 *   are intented for their instances to represent real-world objects,
 *   they should always be used <em>by reference</em>. For this reason,
 *   the contracts of {@link Object#equals(Object)} and {@link Object#hashCode()},
 *   inherited from {@link Object}, are frozen here.</p>
 * <p>Subclasses should take care to override the following methods diligently:
 *   <ul>
 *     <li>{@link #hasSameValues(RousseauBean)}, to take into account
 *         the values of properties added in the subclass;</li>
 *     <li>{@link #getWildExceptions()}, to add validation concerning
 *         properties and type invariants added in the subclass.</li>
 *   </ul>
 * </p>
 *
 * @author    Jan Dockx
 * @author    PeopleWare n.v.
 */
public interface RousseauBean {

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
   * <p>Build a set of {@link CompoundPropertyException}s that tell what is
   *   wrong with this instance, with respect to <dfn>being civilized</dfn>.
   *   The result comes in the form of an <strong>unclosed</strong>
   *   {@link CompoundPropertyException}, of
   *   which the set of element exceptions might be empty.</p>
   * <p>This method should work in any state of the object,
   *   also if it is not yet {@link #normalize() normalized}.</p>
   * <p>This method is public instead of
   *   protected to make it more easy to describe to users what the business
   *   rules for this type are.</p>
   *
   * @result    result != null;
   * @result    !result.isClosed();
   * @result    result.getOrigin() == this;
   * @result    result.getPropertyName() == null;
   * @result    result.getMessage() == null;
   * @result    result.getCause() == null;
   */
  CompoundPropertyException getWildExceptions();

  /**
   * @return getWildExceptions().isEmpty();
   */
  boolean isCivilized();

  /**
   * This method does nothing, but will throw the wild exceptions
   * if this bean is not civilized.
   *
   * @post hasSameValues(new);
   * @post ! isCivilized() ==> false;
   *       If this bean is not civilized, nothing can make this
   *       postcondition true, and thus an exception must be thrown.
   * @throws CompoundPropertyException cpExc
   *         ! isCivilized();
   *         cpExc.hasSameValues(getWildException());
   *         cpExc.isClosed();
   */
  void checkCivility() throws CompoundPropertyException;

  /**
   * <p>Change the value of properties or the internal state of this
   *   object without changing its semantics, with the intent of
   *   making the instance more <dfn>civilized</dfn>. This method
   *   should respect the result of {@link #hasSameValues(RousseauBean)}.
   * <p>This method is typically called immediately prior to
   *   checking civilization with {@link #getWildExceptions()}.</p>
   * <p>This method can be called at any time, so it should never
   *   make unexpected changes to the state.</p>
   *
   * @post (foreach RousseauBean rb;
   *          old.hasSameValues(rb) <==> (old.normalize()).hasSameValues(rb));
   */
  void normalize();

  /**
   * This instance has the same values as the instance <code>rb</code>.
   * This comparison does not take into account to-many associations,
   * to avoid infinite loops. Comparison is independent of normalization,
   * i.e., it should return the same result when the participants in the
   * comparison are normalized or not. To accomplish that, this method
   * may call {@link #normalize()} itself.
   *
   * @param     rb
   *            The object to compare values with.
   * @result   result ==> (rb != null) && (getClass() == rb.getClass());
   */
  boolean hasSameValues(final RousseauBean rb);

  /**
   * Overridden to make final.
   *
   * @see Object
   */
  boolean equals(final Object other);

  /**
   * Overridden to make final.
   *
   * @see Object
   */
  int hashCode();

}
