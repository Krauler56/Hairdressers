package org.korek.spring.services.common.interfaces;

public interface IPassEncoder
{
	String encode(String rawPass);
	
	boolean maches(String rawPass, String encodedPass);
}
