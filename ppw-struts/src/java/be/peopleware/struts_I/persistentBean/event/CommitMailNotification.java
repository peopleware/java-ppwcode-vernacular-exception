package be.peopleware.struts_I.persistentBean.event;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * A handler for processing a CommittedEvent. This handler generates
 * and sends a email to the parties which need to be notified about the event.
 *
 * @author    David Van Keer
 * @author    Peopleware n.v.
 * 
 * @invar getSubjectPrefix() != null;
 */
public class CommitMailNotification
    implements CommittedEventListener {

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
   * @pre subjectPrefix != null;
   * 
   * @post new.getSubjectPrefix().equals(subjectPrefix):
   */
  public CommitMailNotification(String subjectPrefix) {
    assert subjectPrefix != null;
    $subjectPrefix = subjectPrefix;
  }

  
  
  /*<property name="subjectPrefix">*/
  //------------------------------------------------------------------
  
  /**
   *  @basic
   */
  public final String getSubjectPrefix() {
    return $subjectPrefix;
  }
  
  private String $subjectPrefix =
      "Web Application Commit Notification: "; //$NON-NLS-1$
  
  /*</property>*/
  
  

  private static final Log LOG
      = LogFactory.getLog(CommitMailNotification.class);

  private Properties $properties = new Properties();

  /**
   * Process the CommittedEvent. In this case we send a email.
   *
   * If mail cannot be sent for a technical reason, this is logged,
   * but it does not stop the process.
   * @param     pbEvent
   *            The CommittedEvent to process.
   */
  public void committed(final CommittedEvent pbEvent) {
    Session session = null;
    try {
      loadProperties();
      // NamingException can be thrown
      Context initCtx = new InitialContext();
      Context envCtx = (Context)initCtx.lookup("java:comp/env"); //$NON-NLS-1$
      // NoClassDefFoundError can be thrown if configuration is not done.
      session = (Session)envCtx.lookup("mail/session"); //$NON-NLS-1$
      MimeMessage message = new MimeMessage(session);
      { // MessagingException can be thrown
        message.setContent(null, "text/plain");  //$NON-NLS-1$
        if (pbEvent instanceof CreatedEvent) {
          addMailContent(message, (CreatedEvent)pbEvent);
        }
        else if (pbEvent instanceof UpdatedEvent) {
          addMailContent(message, (UpdatedEvent)pbEvent);
        }
        else if (pbEvent instanceof DeletedEvent) {
          addMailContent(message, (DeletedEvent)pbEvent);
        }
        else {
          LOG.error("Incorrect CommittedEvent recieved."); //$NON-NLS-1$
        }
        addRecepients(message, envCtx);
        message.saveChanges();
        Transport.send(message);
      }
    }
    catch (NoClassDefFoundError ncdfExc) {
      // Log configuration error and re-throw the exception.
      LOG.error("JavaMail not configured, unable to find mail.jar and/or " //$NON-NLS-1$
                 + "activation.jar. For more details please read README.html", //$NON-NLS-1$
                 ncdfExc);
      throw ncdfExc;
    }
    catch (IOException ioExc) {
      LOG.error("unable to load properties for mail notification; " //$NON-NLS-1$
                 + "mail cannot be sent", //$NON-NLS-1$
                 ioExc);
    }
    catch (NamingException nExc) {
      LOG.error("trying to access mail session via JNDI; " //$NON-NLS-1$
                 + "mail cannot be sent", //$NON-NLS-1$
                 nExc);
    }
    catch (MessagingException mExc) {
      LOG.error("trying to create and send mail; " //$NON-NLS-1$
                 + "mail cannot be sent", //$NON-NLS-1$
                 mExc);
    }
    catch (Throwable t) {
      LOG.error("throwable occured when trying to send mail; " //$NON-NLS-1$
                 + "mail cannot be sent", //$NON-NLS-1$
                 t);
    }
  }

  private void addMailContent(final MimeMessage message,
                              final CreatedEvent pbEvent)
      throws MessagingException {
    message.setSubject(getSubjectPrefix()
                       + $properties.getProperty("mail.subject.new")); //$NON-NLS-1$
    message.setText($properties.getProperty("mail.content.new.intro")//$NON-NLS-1$
                    + pbEvent.getBean().toString()); //$NON-NLS-1$
  }

  private void addMailContent(final MimeMessage message,
                              final UpdatedEvent pbEvent)
      throws MessagingException {
    message.setSubject(getSubjectPrefix()
                       + $properties.getProperty("mail.subject.update")); //$NON-NLS-1$
    message.setText($properties.getProperty("mail.content.update.intro") //$NON-NLS-1$
                    + $properties.getProperty("mail.content.update.oldPrefix") //$NON-NLS-1$
                    + pbEvent.getOldBeanString()
                    + $properties.getProperty("mail.content.update.newPrefix") //$NON-NLS-1$
                    + pbEvent.getNewBean().toString());
  }
  
  private void addMailContent(final MimeMessage message,
                              final DeletedEvent pbEvent)
      throws MessagingException {
    message.setSubject(getSubjectPrefix()
                       + $properties.getProperty("mail.subject.delete")); //$NON-NLS-1$
    message.setText($properties.getProperty("mail.content.delete.intro") //$NON-NLS-1$
                    + pbEvent.getBean().toString());
  }

  private void addRecepients(final MimeMessage message,
                             final Context context)
      throws MessagingException {
    try {
      InternetAddress[] to = InternetAddress.parse(
          (String)context.lookup("mail/notification/change/to")); //$NON-NLS-1$
      message.addRecipients(Message.RecipientType.TO, to);
      InternetAddress[] cc = InternetAddress.parse(
          (String)context.lookup("mail/notification/change/cc")); //$NON-NLS-1$
      message.addRecipients(Message.RecipientType.CC, cc);
      InternetAddress[] bcc = InternetAddress.parse(
          (String)context.lookup("mail/notification/change/bcc")); //$NON-NLS-1$
      message.addRecipients(Message.RecipientType.BCC, bcc);
    }
    catch (NamingException nExc) {
      LOG.error("failure to retrieve email address(es) for notification; " //$NON-NLS-1$
                 + "mail cannot be sent", //$NON-NLS-1$
                 nExc);
    }
    catch (AddressException aExc) {
      LOG.error("invalid email address(es) detected for notification; " //$NON-NLS-1$
                 + "mail cannot be sent", //$NON-NLS-1$
                 aExc);
    }
  }

  /**
   * @mudo dirty code
   */
  private void loadProperties() throws IOException {
    InputStream propStream = getClass().getResourceAsStream(
        "CommitMailNotification.properties"); //$NON-NLS-1$
    $properties.load(propStream);
  }

}
