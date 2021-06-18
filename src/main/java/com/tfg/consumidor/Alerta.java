package com.tfg.consumidor;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
 
public class Alerta	 {
	static Properties props = new Properties();  
	private static Logger logger = LogManager.getLogger(Alerta.class);
    public Alerta(){
          props.put("mail.smtp.host", "smtp.gmail.com");    
          props.put("mail.smtp.socketFactory.port", "465");    
          props.put("mail.smtp.socketFactory.class",    
                    "javax.net.ssl.SSLSocketFactory");    
          props.put("mail.smtp.auth", "true");    
          props.put("mail.smtp.port", "465");
    }

    public static void send(String nombre_analizador, double temperatura){  
          //get Session   
    	final String from = "mrrtrece2@gmail.com";
    	final String password = "Trece.13";
    	String to="mrrtrece2@gmail.com";
    	logger.info("email enviado a "+to+".");
    	String sub ="Alerta " + nombre_analizador;
    	logger.info("El asunto del email enviado es: "+sub+".");
    	String msg ="La temperatura dentro de 30 minutos será de "+temperatura;
    	logger.info("El cuerpo del email enviado es: "+msg+".");
    	
          Session session = Session.getDefaultInstance(props,    
           new javax.mail.Authenticator() {    
           protected PasswordAuthentication getPasswordAuthentication() {    
           return new PasswordAuthentication(from,password);  
           }    
          });    
          //compose message    
          try {    
           MimeMessage message = new MimeMessage(session);    
           message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));    
           message.setSubject(sub);    
           message.setText(msg);    
           //send message  
           Transport.send(message); 
           logger.info("Correo electrónico enviado correctamente.");
           System.out.println("Mensaje ENVIADO");    
          } catch (MessagingException e) {
        	  logger.error("Error al enviar el correo electrónico.");
        	  throw new RuntimeException(e);
          }    
             
    }  	
 
}