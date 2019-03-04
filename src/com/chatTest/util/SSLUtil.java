package com.chatTest.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class SSLUtil {
	
	/**
	 * ����һ����������SSLContext
	 * @return
	 * @throws Exception
	 */
	public static SSLContext createServerSSLContext() {
		String keyStoreFile = "test.keys";
		String passphrase = "123456";
		SSLContext sslContext = null;
		try {
			KeyStore ks = KeyStore.getInstance("JKS");
			char[] password = passphrase.toCharArray();
			ks.load(new FileInputStream(keyStoreFile), password);
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks, password);

			sslContext = SSLContext.getInstance("TLS");
			sslContext.init(kmf.getKeyManagers(), null, null);
		} catch (UnrecoverableKeyException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException
				| CertificateException | IOException e) {
			e.printStackTrace();
		}

		return sslContext;
		// ��Ҫ��ͻ����ṩ��ȫ֤��ʱ���������˿ɴ���TrustManagerFactory��
		// ����������TrustManager��TrustManger������֮������KeyStore�е���Ϣ��
		// �������Ƿ����ſͻ��ṩ�İ�ȫ֤�顣
		// String trustStoreFile = "client.keys";
		// KeyStore ts = KeyStore.getInstance("JKS");
		// ts.load(new FileInputStream(trustStoreFile), password);
		// TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
		// tmf.init(ts);
		// sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
		
	}
	
	/**
	 * ����һ���ͻ��˵�SSLContext
	 * @return
	 * @throws Exception
	 */
	public static SSLContext createClienSSLContext() {
		String keyStoreFile = "test.keys";
		String passphrase = "123456";
		SSLContext sslContext = null;
		try {
			KeyStore ks = KeyStore.getInstance("JKS");
			char[] password = passphrase.toCharArray();
			ks.load(new FileInputStream(keyStoreFile), password);
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
			tmf.init(ks);
			
			sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, tmf.getTrustManagers(), null);
		} catch (KeyManagementException | KeyStoreException | NoSuchAlgorithmException | CertificateException
				| IOException e) {
			e.printStackTrace();
		}
		return sslContext;
		
	}

}
