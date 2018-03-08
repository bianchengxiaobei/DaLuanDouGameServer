package com.chen.db.bean;

import java.io.IOException;

import org.msgpack.core.MessageBufferPacker;

import com.chen.message.Bean;

public class Mail
{
	private long mailId;
	private long receiverId;
	private long senderId;
	private long sendTime;
	private long endTime;
	private byte mailState;
	private byte mailType;
	private String title;
	private String content;
	private String gift;
	private transient int saveType;
	
	public long getMailId() {
		return mailId;
	}
	public void setMailId(long mailId) {
		this.mailId = mailId;
	}
	public long getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(long receiverId) {
		this.receiverId = receiverId;
	}
	public long getSendTime() {
		return sendTime;
	}
	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public byte getMailState() {
		return mailState;
	}
	public void setMailState(byte mailState) {
		this.mailState = mailState;
	}
	public byte getMailType() {
		return mailType;
	}
	public void setMailType(byte mailType) {
		this.mailType = mailType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getGift() {
		return gift;
	}
	public void setGift(String gift) {
		this.gift = gift;
	}
	public int getSaveType() {
		return saveType;
	}
	public void setSaveType(int saveType) {
		this.saveType = saveType;
	}
//	public void write(MessageBufferPacker packer)
//	{
//		writeLong(packer, mailId);
//		writeLong(packer, sendTime);
//		writeLong(packer, endTime);
//		writeByte(packer, mailState);
//		writeByte(packer, mailType);
//		writeString(packer, title);
//		writeString(packer, content);
//		writeString(packer, gift);
//	}
	public void write(MessageBufferPacker packer)
	{
		try {
			packer.packLong(mailId);
			packer.packLong(sendTime);
			packer.packLong(endTime);
			packer.packByte(mailState);
			packer.packByte(mailType);
			packer.packString(title);
			packer.packString(content);
			packer.packString(gift);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public long getSenderId() {
		return senderId;
	}
	public void setSenderId(long senderId) {
		this.senderId = senderId;
	}
}
