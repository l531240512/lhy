package com.lhy.pro.common.utils;

import java.io.File;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import com.lhy.pro.common.UsualConstants;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;

/**
* @author lhy
*
*/
@Slf4j
public class MailUtil {
		/**设置发件人的邮箱信息，邮件服务器地址*/
		private static  String MAIL_ADDR ="";
		private static String mailCount ="";
		private static String MAIL_P ="";
		private static String MAIL_SER = "";
		public static final String CONTENT_TYPE_HTML = "HTML";
		public static final String CONTENT_TYPE_TEXT = "TEXT";
		public static final String CONTENT_TYPE = "text/html;charset=UTF-8";

		/**
		 *  发送邮件，可同时发给多人 。默认是立即发送，使用文本格式。
		 * @param internetAddr 邮件地址列表，String数组
		 * @param title  邮件标题
		 * @param content  邮件正文
		 */
		public static void sendMail(String internetAddr , String title,String content){
			sendMail(internetAddr ,new Date() , title,content,CONTENT_TYPE_TEXT);
		}
		/**
		 * 发送邮件，可同时发给多人
		 * @param internetAddr 邮件地址列表，String数组
		 * @param sendTime 发送时间
		 * @param title 邮件标题
		 * @param content 邮件正文
		 * @param contentType 正文格式：HTML或Text，MailUtil.CONTENT_TYPE_HTML ：发送内容使用HTML格式；MailUtil.CONTENT_TYPE_TEXT : 发送内容使用TEXT格式
		 */
		public static void sendMail(String internetAddr ,Date sendTime , String title,String content,String contentType){
			try {
				//用来在一个文件中存储键-值对的，其中键和值是用等号分隔的，
				Properties props=new Properties();
				//存储发送邮件服务器的信息
				props.put("mail.smtp.host",MAIL_SER);
				//同时通过验证
				props.put("mail.smtp.auth","true");
				//根据属性新建一个邮件会话
				Session s=Session.getInstance(props);
//				//有它会打印一些调试信息。
//				s.setDebug(true);

				//由邮件会话新建一个消息对象
				MimeMessage message=new MimeMessage(s);

				//设置邮件
				InternetAddress from= new InternetAddress(MAIL_ADDR);
				//设置发件人的地址
				message.setFrom(from);

				message.setRecipient(Message.RecipientType.TO,  new InternetAddress(internetAddr, UsualConstants.EN_CODE_UTF8));
				//设置标题
				message.setSubject(title);

				//设置信件内容
				if(contentType!=null && contentType.equals(CONTENT_TYPE_HTML)){
					 //发送HTML邮件   //<b>你好</b><br><p>大家好</p>
					message.setContent(content, CONTENT_TYPE);
				}else if(contentType!=null && contentType.equals(CONTENT_TYPE_TEXT)){
					//发送普通文本
					message.setText(content);
				}else{
					//发送普通文本
					message.setText(content);
				}
				//设置发信时间
				message.setSentDate(new Date());

				//存储邮件信息
				message.saveChanges();

				//发送邮件
				Transport transport=s.getTransport("smtp");
				//以smtp方式登录邮箱,第一个参数是发送邮件用的邮件服务器SMTP地址,第二个参数为用户名,第三个参数为密码
				transport.connect(MAIL_SER,mailCount,MAIL_P);
				//发送邮件,其中第二个参数是所有已设好的收件人地址
				transport.sendMessage(message,message.getAllRecipients());
				log.info("邮件已发送！");
				transport.close();
			} catch (Exception e) {
//				e.printStackTrace();
				log.error("邮件发送失败！");
			}
		}

	/**
	 * 发送带有附件的邮件 ，图片放在附件中
	 * @param internetAddr 发送邮箱
	 * @param file 附件
	 * @param title 标题
	 * @param content 正文
	 * @param images 图片附件
	 * @throws Exception
	 */
		public static void sendWithFile(Object[] internetAddr ,File file, String title,String content, String[] images) throws Exception {
			//用来在一个文件中存储键-值对的，其中键和值是用等号分隔的，
			Properties props=new Properties();
			//存储发送邮件服务器的信息
			props.put("mail.smtp.host",MAIL_SER);
			//同时通过验证
			props.put("mail.smtp.auth","true");
			//根据属性新建一个邮件会话
			Session s=Session.getInstance(props);
//				//有它会打印一些调试信息。
//				s.setDebug(true);

			//由邮件会话新建一个消息对象
			MimeMessage message=new MimeMessage(s);

			//设置邮件
			InternetAddress from= new InternetAddress(MAIL_ADDR);
			//设置发件人的地址
			message.setFrom(from);

			InternetAddress[] internetAddrs = new InternetAddress[internetAddr.length];
			for(int i=0;i<internetAddr.length;i++) {
				internetAddrs[i]= new InternetAddress(internetAddr[i].toString());
			}
			message.setRecipients(Message.RecipientType.TO, internetAddrs);

			//设置标题
			message.setSubject(title);

			MimeMultipart msgMultipart = new MimeMultipart("mixed");
			message.setContent(msgMultipart);

			// 7，设置消息正文
			MimeBodyPart c = new MimeBodyPart();
			msgMultipart.addBodyPart(c);

			// 8，设置正文格式
			MimeMultipart bodyMultipart = new MimeMultipart("related");
			c.setContent(bodyMultipart);

			// 9，设置正文内容
			MimeBodyPart htmlPart = new MimeBodyPart();
			bodyMultipart.addBodyPart(htmlPart);

			MimeBodyPart filePart = new MimeBodyPart();
			FileDataSource dataSource = new FileDataSource(file);
			DataHandler dataHandler = new DataHandler(dataSource);
			// 文件处理
			filePart.setDataHandler(dataHandler);
			BASE64Encoder enc = new BASE64Encoder();
			String fileName="=?UTF-8?B?"+enc.encode(file.getName().getBytes("utf-8"))+"?=";
			filePart.setFileName(fileName);
			// 附件名称
//			filePart.setFileName(file.getName());
			// 放入正文（有先后顺序，所以在正文后面）
			msgMultipart.addBodyPart(filePart);

			if(images != null && images.length>0) {
				for (String filename : images) {
					MimeBodyPart attachPart = new MimeBodyPart();
					attachPart.attachFile(filename);
					bodyMultipart.addBodyPart(attachPart);
				}
			}
			htmlPart.setContent(content,CONTENT_TYPE);

			//设置发信时间
			message.setSentDate(new Date());
			//存储邮件信息
			message.saveChanges();

			//发送邮件
			Transport transport=s.getTransport("smtp");
			//以smtp方式登录邮箱,第一个参数是发送邮件用的邮件服务器SMTP地址,第二个参数为用户名,第三个参数为密码
			transport.connect(MAIL_SER,mailCount,MAIL_P);
			//发送邮件,其中第二个参数是所有已设好的收件人地址
			transport.sendMessage(message,message.getAllRecipients());
			log.info("邮件已发送！");
			transport.close();
		}
}
