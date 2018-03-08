package com.chen.login.bean;

import org.msgpack.core.MessageBufferPacker;

import com.chen.db.bean.Mail;
import com.chen.message.Bean;

public class EmailInfo extends Bean
{
	public Mail[] mails;
	@Override
	public void write(MessageBufferPacker packer)
	{
		if (mails == null)
		{
			log.error("Mails == null");
		}
		writeArray(packer, mails);
	}
}
