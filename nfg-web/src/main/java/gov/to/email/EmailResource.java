package gov.to.email;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.mail.Session;

@ApplicationScoped
public class EmailResource {
	
	@Produces
	@Resource(mappedName = "java:jboss/mail/gmail")
	private Session mailSession;

}
