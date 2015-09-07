package org.jboss.tools.teste.mail;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/sendEmail.do")
public class SendEmail extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String MSG_MIME_TYPE = "text/plain";

    private String host;
    private String port;
    private String login;
    private String password;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        host = context.getInitParameter("host");
        port = context.getInitParameter("port");
        login = context.getInitParameter("login");
        password = context.getInitParameter("password");
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String resultMsg = "E-Mail enviado com sucesso!";
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String subject = request.getParameter("subject");
        String message = request.getParameter("message");

        try {
            Properties props = getSMTPProperties();

            Authenticator auth = new SMTPAuthenticator(login, password);

            Session session = Session.getInstance(props, auth);

            MimeMessage msg = createMessage(from, to, subject, message, session);

            Transport.send(msg);
        } catch (AuthenticationFailedException ex) {
            resultMsg = "Falha na autenticação";
        } catch (AddressException ex) {
            resultMsg = "E-Mail de destino inválido!";
        } catch (MessagingException ex) {
            resultMsg = ex.getMessage();
            //ex.printStackTrace();//descomentar para debug
        }

        request.setAttribute("message", resultMsg);

        RequestDispatcher dispatcher = request
                .getRequestDispatcher("result.jsp");
        dispatcher.forward(request, response);
    }

    private MimeMessage createMessage(String from, String to, String subject,
            String message, Session session) throws MessagingException {
        MimeMessage msg = new MimeMessage(session);
        msg.setText(message);
        msg.setSubject(subject);
        msg.setFrom(new InternetAddress(from));
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setContent(message, MSG_MIME_TYPE);
        msg.setSentDate(new Date());

        return msg;
    }

    private Properties getSMTPProperties() {
        Properties props = new Properties();
        props.setProperty("mail.host", host);
        props.setProperty("mail.transport.protocol", "smtp");
        // Comente essas 4 linhas abaixo se deseja usar o SSL
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.port", port);
        props.setProperty("mail.smtp.ssl.trust", host);
        props.setProperty("mail.smtp.starttls.enable", "true");
        // Descomente essas tres linhas abaixo se deseja usar SSL
//         props.setProperty("mail.smtps.auth", "true");
//         props.setProperty("mail.smtps.port", port);
//         props.setProperty("mail.smtps.ssl.trust", host);
        //Descomente a linha abaixo se deseja informações de debug
//        props.put("mail.debug", "true");

        return props;
    }

    private class SMTPAuthenticator extends Authenticator {
        private String login;
        private String password;

        public SMTPAuthenticator(String login, String password) {
            this.login = login;
            this.password = password;
        }

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(login, password);
        }
    }
}