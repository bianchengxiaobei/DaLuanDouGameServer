package com.chen.mail.message;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.db.bean.Mail;
import com.chen.message.Message;

public class ResNotifyGetNewMailMessage extends Message
{	
	public Mail mail;
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 1073;
	}

	@Override
	public String getQueue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServer() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void write(IoBuffer buffer)
	{
		if (mail != null)
		{
			mail.write(messagePack);
		}
		super.write(buffer);
	}
}
