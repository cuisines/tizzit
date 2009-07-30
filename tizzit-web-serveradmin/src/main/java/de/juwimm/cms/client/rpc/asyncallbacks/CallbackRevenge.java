package de.juwimm.cms.client.rpc.asyncallbacks;


public interface CallbackRevenge <T>{
	public void callback(T result, String clazzName);
}
