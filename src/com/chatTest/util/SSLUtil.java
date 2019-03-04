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
	 * 创建一个服务器的SSLContext
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
		// 当要求客户端提供安全证书时，服务器端可创建TrustManagerFactory，
		// 并由它创建TrustManager，TrustManger根据与之关联的KeyStore中的信息，
		// 来决定是否相信客户提供的安全证书。
		// String trustStoreFile = "client.keys";
		// KeyStore ts = KeyStore.getInstance("JKS");
		// ts.load(new FileInputStream(trustStoreFile), password);
		// TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
		// tmf.init(ts);
		// sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
		
	}
	
	/**
	 * 创建一个客户端的SSLContext
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
