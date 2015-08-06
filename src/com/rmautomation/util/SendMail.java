package com.rmautomation.util;


//set CLASSPATH=%CLASSPATH%;activation.jar;mail.jar

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class SendMail

{
	public static void execute(String reportFileName) throws Exception

	{
		String path=System.getProperty("user.dir")+"//"+reportFileName;
		
		Zip.zip(System.getProperty("user.dir")+"//"+ReportUtil.result_FolderName,reportFileName);  //This zip will be created and will be mailed.


		String[] to={"sachin.shanbag@tieto.com"};

		String[] cc={"sachinshanbhag189@gmail.com"};
		String[] bcc={};

		//This is for google mail for now

		SendMail.sendMail("sachinshanbhag189@gmail.com",
				"Disney1986",
				"smtp.gmail.com",
				"465",
				"true",
				"true",
				true,
				"javax.net.ssl.SSLSocketFactory",
				"false",
				to,
				cc,
				bcc,
				"RM Automation Test Reports",
				"Hi Team,\n\nPlease find the reports of the RM Automation Tests attached herewith.\nThank you!\n\nBest Regards,\nTeam RMAutomation",
				path,
				reportFileName);
	}

	public  static boolean sendMail(String userName,
			String passWord,
			String host,
			String port,
			String starttls,
			String auth,
			boolean debug,
			String socketFactoryClass,
			String fallback,
			String[] to,
			String[] cc,
			String[] bcc,
			String subject,
			String text,
			String attachmentPath,
			String attachmentName){


		Properties props = new Properties();

		//Properties props=System.getProperties();

		props.put("mail.smtp.user", userName);

		props.put("mail.smtp.host", host);

		if(!"".equals(port))

			props.put("mail.smtp.port", port);

		if(!"".equals(starttls))

			props.put("mail.smtp.starttls.enable",starttls);

		props.put("mail.smtp.auth", auth);
		// props.put("mail.smtps.auth", "true");

		if(debug){
			props.put("mail.smtp.debug", "true");
		}else{
			props.put("mail.smtp.debug", "false");         
		}
		
		if(!"".equals(port))
			props.put("mail.smtp.socketFactory.port", port);

		if(!"".equals(socketFactoryClass))
			props.put("mail.smtp.socketFactory.class",socketFactoryClass);

		if(!"".equals(fallback))
			props.put("mail.smtp.socketFactory.fallback", fallback);
		
		try
		{
			Session session = Session.getDefaultInstance(props, null);

			session.setDebug(debug);

			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("sachinshanbhag189@gmail.com"));  ///TEST
			msg.setSubject(subject);

			//create MimeBodyPart object and set your message text   
			BodyPart messageBodyPart1 = new MimeBodyPart();
			messageBodyPart1.setText(text);
						
			//create new MimeBodyPart object and set DataHandler object to this object 
			MimeBodyPart messageBodyPart2 = new MimeBodyPart();
			DataSource source = new FileDataSource(attachmentPath);
			messageBodyPart2.setDataHandler(new DataHandler(source));
			messageBodyPart2.setFileName(attachmentName);
			
			//create Multipart object and add MimeBodyPart objects to this object    
			Multipart multipart = new MimeMultipart();			
			multipart.addBodyPart(messageBodyPart1);
			multipart.addBodyPart(messageBodyPart2);  

			// Put parts in message  //set the Multipart object to the message object  
			msg.setContent(multipart);
			
			for(int i=0;i<to.length;i++){
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
			}

			for(int i=0;i<cc.length;i++){
				msg.addRecipient(Message.RecipientType.CC, new InternetAddress(cc[i]));
			}

			for(int i=0;i<bcc.length;i++){
				msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc[i]));
			}

			msg.saveChanges();

			Transport transport = session.getTransport("smtp");

			transport.connect(host, userName, passWord);

			transport.sendMessage(msg, msg.getAllRecipients());

			transport.close();

			return true;

		}

		catch (Exception mex)

		{

			mex.printStackTrace();

			return false;

		}

	}



}