/*
 * <license> Copyright 2004-2005, PeopleWare n.v. NO RIGHTS ARE GRANTED FOR THE
 * USE OF THIS SOFTWARE, EXCEPT, IN WRITING, TO SELECTED PARTIES. </license>
 */
package be.peopleware.jsf_II.convert;


import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import net.sf.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.peopleware.exception_I.TechnicalException;
import be.peopleware.jsf_II.RobustCurrent;
import be.peopleware.persistence_I.DaoFactory;
import be.peopleware.persistence_I.IdNotFoundException;
import be.peopleware.persistence_I.PersistentBean;
import be.peopleware.persistence_I.hibernate.HibernateAsyncCrudDao;
import be.peopleware.persistence_I.hibernate.HibernateDaoFactory;
import be.peopleware.servlet_I.hibernate.SessionInView;


/**
 * Converter for {@link PersistentBean}. The value must be the ID of
 * the class. To activate this, the following entry has to appear in
 * <kbd>faces-config.xml </kbd>:
 *
 * <pre>
 *
 *  &lt;converter&gt;
 *    &lt;converter-for-class&gt;be.peopleware.persistence_I.PersistentBean&lt;/converter-for-class&gt;
 *    &lt;converter-class&gt;be.peopleware.jsf_II.convertor.PersistentBeanConverter&lt;/converter-class&gt;
 *  &lt;/converter&gt;
 *
 * </pre>
 *
 * @author David Van Keer
 * @author Peopleware n.v.
 */
public class PersistentBeanConverter implements Converter {

  /* <section name="Meta Information"> */
  //------------------------------------------------------------------
  /** {@value} */
  public static final String CVS_REVISION = "$Revision$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_DATE = "$Date$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_STATE = "$State$"; //$NON-NLS-1$
  /** {@value} */
  public static final String CVS_TAG = "$Name$"; //$NON-NLS-1$
  /* </section> */

  private static final Log LOG = LogFactory.getLog(PersistentBeanConverter.class);


  /**
   * @return    (value != null)
   *              ? result.getId().equals((Long)value))
   *              : null;
   * @throws    ConverterException
   *            No PersistentBean was found with a ID of <code>value</code> or
   *            technical problems occured while trying to retrieve the correct
   *            PersistentBean
   */
  public Object getAsObject(final FacesContext context,
                            final UIComponent component,
                            final String value)
      throws ConverterException {
    // First determine which type we are using. The jsf page designer, will
    // have to pass a attribute with name 'typeName' with a a fqClassName
    // of the type to retrieve.
    String typeName = (String)component.getAttributes().get("typeName");
    Class type = PersistentBean.class;
    if (typeName != null || typeName.equals(EMPTY)) {
      try {
        type = Class.forName(typeName);
      }
      catch (LinkageError lErr) {
        throw new FacesException("Cannot convert String to Class", lErr);
      }
      catch (ClassNotFoundException cnfExc) {
        throw new FacesException("Cannot convert String to Class", cnfExc);
      }
    }
    
    Object result = null;
    if (value != null && !value.equals(EMPTY)) {
      Long id = Long.valueOf(value);
      LOG.debug("Recieving a request for a \"" + type + "\" with id \"" + id + "\".");
      HibernateDaoFactory daoFactory = new HibernateDaoFactory();
      HibernateAsyncCrudDao dao = (HibernateAsyncCrudDao)daoFactory.getDao(DaoFactory.ASYNC_CRUD);
      try {
        Session session = SessionInView.getSession(RobustCurrent.httpServletRequest());
        dao.setSession(session);
        result = dao.retrievePersistentBean(id, type);
        LOG.debug("\"" + type + "\" retrieved with id \"" + id + "\"");
      }
      catch (IdNotFoundException infExc) {
        LOG.error("No \"" + type + "\" found with id \"" + id + "\"");
        throw new ConverterException("No PersistentBean bean found with id " + id);
      }
      catch (TechnicalException tExc) {
        LOG.error("Failed to retrieve \"" + type + "\" due to technical problems", tExc);
        throw new ConverterException("Unable to retrieve PersistentBean due to technical problems.");
      }
    }
    return result;
  }

  public final static String EMPTY = "";

  /**
   * @return    (value != null)
   *                ? (((PersistentBean)value).getId() != null)
   *                      ? ((PersistentBean)value).getId().toString
   *                      : ""
   *                : "";
   * @throws    ConverterException
   *            !(value instanceof PersistentBean))
   */
  public String getAsString(final FacesContext context,
                            final UIComponent component,
                            final Object value) throws ConverterException {
    if (value == null) {
      return EMPTY;
    }
    if (value.equals(EMPTY)) {
      return EMPTY;
    }
    if (!(value instanceof PersistentBean)) {
      LOG.error("Unable to convert \"" + value + "\" because it is not a subclass of PersistentBean.");
      throw new ConverterException("Value is not subclass of PersistentBean Class");
    }
    PersistentBean persistentBean = ((PersistentBean)value);
    return (persistentBean.getId() != null) ? persistentBean.getId().toString() : EMPTY;
  }

}