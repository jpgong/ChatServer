package com.chatTest.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

/**
 * 计算口令的SHA-1散列值
 * @author jpgong
 *
 */

public class DigestUtil {
	
	/**
	 * 计算SHA-1的散列值
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
