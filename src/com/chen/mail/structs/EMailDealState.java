package com.chen.mail.structs;

public enum EMailDealState
{
	Mail_Update(0),
	Mail_Insert(1),
	Mail_Delete(2),
	Mail_DeleteAllById(3);
	public int value;
	private EMailDealState(int value)
	{
		this.value = value;
	}
}
