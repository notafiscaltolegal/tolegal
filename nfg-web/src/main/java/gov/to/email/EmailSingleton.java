package gov.to.email;

import java.util.Date;

import javax.ejb.Asynchronous;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.http.MediaType;

@Singleton
public class EmailSingleton {
	
	@Inject
	private Session mailSession;
	
	@Asynchronous
	@Lock(LockType.READ)
	public void sendMail(@Observes(during = TransactionPhase.AFTER_SUCCESS) EmailDTO email) {

		try {
            MimeMessage m = new MimeMessage(mailSession);
            Address[] remetente = new InternetAddress[] {new InternetAddress(email.getPara())};

            m.setRecipients(Message.RecipientType.TO, remetente);
            m.setSubject(email.getAssunto());
            m.setSentDate(new Date());
            m.setContent(email.getMensagem(), MediaType.TEXT_HTML_VALUE); 
            
            Transport.send(m);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
	}
}
