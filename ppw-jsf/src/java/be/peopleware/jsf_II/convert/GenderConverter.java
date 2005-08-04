package be.peopleware.jsf_II.convert;


import java.util.Map;

import be.peopleware.value_I.Gender;


/**
 * Converter for {@link Gender}.
 * The value must be the string representation of the class.
 *
 * To activate this converter, the following entry has to appear in <kbd>faces-config.xml</kbd>:
 * <pre>
 * &lt;converter&gt;
 *   &lt;converter-for-class&gt;be.peopleware.value_I.Gender&lt;/converter-for-class&gt;
 *   &lt;converter-class&gt;be.peopleware.jsf_II.convert.GenderConverter&lt;/converter-class&gt;
 * &lt;/converter&gt;
 * </pre>
 *
 * @author nsmeets
 * @author Peopleware n.v.
 */
public class GenderConverter extends EnumerationValueConverter {

  public Map getValuesMap() {
    return Gender.VALUES;
  }

  public Class getType() {
    return Gender.class;
  }
}

