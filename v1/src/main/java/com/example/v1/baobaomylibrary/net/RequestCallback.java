package com.example.v1.baobaomylibrary.net;

public interface RequestCallback
{
	public void onSuccess(String content);

	public void onFail(String errorMessage);
}
