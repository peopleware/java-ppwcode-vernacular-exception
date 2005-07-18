/*<license>
  Copyright 2004-2005, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/


package be.peopleware.jsf_II.convert;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;


/**
 * Converter for {@link Class}. The value must be the FQCN of the class.
 * 
 * To activate this, the following entry has to appear in <kbd>faces-config.xml</kbd>:
 * <pre>
 * &lt;converter&gt;
 *   &lt;converter-for-class&gt;java.lang.Class&lt;/converter-for-class&gt;
 *   &lt;converter-class&gt;be.peopleware.jsf_I.convert.ClassConverter&lt;/converter-class&gt;
 * &lt;/converter&gt;
 * </pre>
 * 
 * @author Wim Lambrechts
 * @author René Clerckx
 * @author Jan Dockx
 * @author Peopleware n.v.
 * 
 * @todo (jand) Toryt
 */
public class ClassConverter implements Converter {
  
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
   * @return (value != null) ? Class.forName(value) : null;
   * @throws ConverterException
   *         ; the class with FQCN <code>value</code> could not be loaded.
   */
  public Object getAsObject(FacesContext context, UIComponent component, String value)
      throws ConverterException {
    if (value == null) {
      return null;
    }
    else {
      try {
        return Class.forName(value);
      }
      catch (Throwable e) {
        throw new ConverterException("class with name " + value + "could not be loaded");
      }
    }
  }
  
  public final static String EMPTY = "";
  
  /**
   * @return (value != null) ? value.getName() : EMPTY;
   * @throws ConverterException
   *         ! value instanceof Class
   */
  public String getAsString(FacesContext context, UIComponent component, Object value)
      throws ConverterException {
    if (value == null) {
      return EMPTY;
    }
    if (! (value instanceof Class)) {
      throw new ConverterException("value is not a Class");
    }
    return ((Class)value).getName();
  }
  
}

