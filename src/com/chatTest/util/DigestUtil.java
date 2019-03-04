package com.chatTest.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

/**
 * ��������SHA-1ɢ��ֵ
 * @author jpgong
 *
 */

public class DigestUtil {
	
	/**
	 * ����SHA-1��ɢ��ֵ
	 * @param message
	 * @return
	 */
	public static byte[] getSHA1Digest(String message) {
		byte[] resultbyte = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] messageByte = message.getBytes();
			md.update(messageByte);
			resultbyte = md.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return resultbyte;
	}
	
	public static void main(String[] args) {
		System.out.println(new HexBinaryAdapter().marshal(getSHA1Digest("dddtm5q4g")));
	}

}
