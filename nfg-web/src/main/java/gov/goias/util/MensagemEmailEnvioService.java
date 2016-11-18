package gov.goias.util;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MensagemEmailEnvioService {

//	@Autowired
	private JavaMailSender mailSender;

	private String senderAddress;
	private Logger logger = Logger.getLogger(MensagemEmailEnvioService.class);


	public void sendMail(final String recipient, final String subject, final String content, final File attachment) throws Exception {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
				mimeMessage.setFrom(new InternetAddress("naoresponda-nfgoiana@sefaz.go.gov.br"));
				mimeMessage.setSubject(subject);
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
				if(attachment != null) {
					helper.addAttachment(attachment.getName(), new FileSystemResource(attachment));
				}
				helper.setText(content);
			}
		};
		try {
			this.mailSender.send(preparator);
			logger.info("Email de aviso de novas mensagens enviado com sucesso para destinat�rio: " + recipient);
		} catch(MailException ex) {
			String msgErro = "Falha ao enviar email para destinat�rio: " + recipient  ;
			logger.error(msgErro);
			throw new Exception(msgErro, ex);
		}
	}

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public String getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}
}