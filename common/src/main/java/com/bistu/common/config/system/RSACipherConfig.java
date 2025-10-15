package com.bistu.common.config.system;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Getter
@Setter
@Configuration
public class RSACipherConfig {
	private PublicKey publicKey;
	private PrivateKey privateKey;
	private int keySize;

	@Value("${path.workPath}")
	private String workPath;

	private final String DEFAULT_PUBLICKEY = "-----BEGIN PUBLIC KEY-----\n" +
			"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAs2mSB3HhxQP8T4hQ4fZQ\n" +
			"in1NoA2a0vreHgawZsvtqyvUq/oQMKKxUwTJ8oqvmEDd4JWwygeKpOOBHmM29KXs\n" +
			"ldtKclPJPvuGRoX2d9bLSjmQQGWhqmTjdzuGbfhP5cpcF5P8AEUBmURev33CftIV\n" +
			"yG5aUmRtuuxj6JjOC9JAN0sdqIfEltcHwseTerYmtbQQFjPe8A5odlwjh61WqhuM\n" +
			"g8ico2TD5H9Vb3ASyrbVLQOAn6VlvxJHiQNDPGYyiVdJcivijFAnPxyelqqOGP2F\n" +
			"KIdqMkfUpw9hVN+kQZ9fp1Mncc12M2UcQWX6rWHoeNpcY2JxqcZrUsa82X6mqlCb\n" +
			"vwIDAQAB\n" +
			"-----END PUBLIC KEY-----";
	private final String DEFAULT_PRIVATEKEY = "-----BEGIN RSA PRIVATE KEY-----\n" +
			"MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCzaZIHceHFA/xP\n" +
			"iFDh9lCKfU2gDZrS+t4eBrBmy+2rK9Sr+hAworFTBMnyiq+YQN3glbDKB4qk44Ee\n" +
			"Yzb0peyV20pyU8k++4ZGhfZ31stKOZBAZaGqZON3O4Zt+E/lylwXk/wARQGZRF6/\n" +
			"fcJ+0hXIblpSZG267GPomM4L0kA3Sx2oh8SW1wfCx5N6tia1tBAWM97wDmh2XCOH\n" +
			"rVaqG4yDyJyjZMPkf1VvcBLKttUtA4CfpWW/EkeJA0M8ZjKJV0lyK+KMUCc/HJ6W\n" +
			"qo4Y/YUoh2oyR9SnD2FU36RBn1+nUydxzXYzZRxBZfqtYeh42lxjYnGpxmtSxrzZ\n" +
			"fqaqUJu/AgMBAAECggEAaokR0aHqMpc/R+rmPIeLI/FsGtJ5UTbull3aefA2XSJV\n" +
			"0xlWa8rwCSAa2w9HXd2G40mABTedOHRvw5+ejn/IkOP6vRDLu76X6WC7DkZZyOWN\n" +
			"pu+yo//somySUtLUfts+/DE/vtk7LcvPh14iFDSkArSkpbBFCG4+lWvHTnW29B2D\n" +
			"ck/4popS5LxTyc09zS7pImiAa3/m7DRh34uqs70dZpSW2swWSWBhSrnA9LvqL/ft\n" +
			"xA5zTQrc6UR/sKCwbmzkpyg8sRwBCnjBi8Qf4ekiWJk7Pu4iA42kMwEdt18vL1R+\n" +
			"7fTWp5FfelXlcVUBuAWpOwZUYX5r6SLcpoYNw/lWAQKBgQDmzouz5iQ9dlTdRwlJ\n" +
			"fv32TME6GysptiDTo4OYqWozbFsyOsmQYW+wXiaBFzai6r0PQBMrxYd06gZI1vsE\n" +
			"RotM3o3sDanopwDITTyovGQWLEkqTFAXEMJ9XgSGjuvRYMO7pFZsHBreu6nnYW7n\n" +
			"4NbXHtm6s7fDaAZRlZIvfaEw/wKBgQDG/ufdEuEXgeUk1ZllIZPF8dqdVRU5WyQy\n" +
			"DX2jzH0iptjQ9GolgPb/ia0PnoYEw6tDYM52Gn+S/sIwOa8Pvlq3AScOFrVIDPQN\n" +
			"MuTwS2kECZzs06vLeCXX71kZK0MTqprMwAv+Z8FW8VidBkGNmU6i3YwqZdfWJulS\n" +
			"+B/M6mHVQQKBgQDQazskHHIlPjGsD3WHdLGINA9YlSgI5noGIuW2eWV5QiOn3AYf\n" +
			"5pyguqQyde/Udfx7RPUK1bhYDuvV4quRVNJ9e4rU2hHQEN5YAccA5/3JN/xpwbeg\n" +
			"3HWEF94mU/EGhUa6rs+LmGV7NCNc2VL4+MOIRkd+nMMYJVtvWI1FbUCdqQKBgQCq\n" +
			"XyfIoRdSdwXmqXHxofPQ76aKOYQ4XQ36RI6jJLqmZk8PZ5NRlL7kPc2Lp996SMRi\n" +
			"pYlemIVNJwy/GUQGqKUmNbhoWYzPqTdx7XcRPJ8ms8xE/10AkVdlLykLYXzGY7Sx\n" +
			"RGOAZvijJXMGzYpmXzFyQ1h0HEnUse/l90XSvYERgQKBgQDK/W9jApQD8WdWelzT\n" +
			"BoNhAQbQ63RnjWgmK4Txpm6bcxFpkK7AfqOBF19thnoVE6hbgIjAQ8jVG3CHbKCw\n" +
			"XmrhEcBYlUZqEF68wS3cfXfIbJPhwH++xCCsveSPz4KSn84FhmafBb0CCuRu+l/s\n" +
			"v/N/gbNCrNP7eA4N5V9ruDPJ1g==\n" +
			"-----END RSA PRIVATE KEY-----";

	public void setPublicKey(InputStream publicKeyStream) throws Exception {
		try {
			ObjectInputStream publicKeyIn = new ObjectInputStream(publicKeyStream);
			this.publicKey = (PublicKey) publicKeyIn.readObject();
		} catch (Exception e) {
			throw new Exception("读取公钥失败", e);
		}
	}

	public void setPrivateKey(InputStream privateKeyStream) throws Exception {
		try {
			ObjectInputStream privateKeyIn = new ObjectInputStream(privateKeyStream);
			this.privateKey = (PrivateKey) privateKeyIn.readObject();
		} catch (Exception e) {
			throw new Exception("读取公钥失败", e);
		}
	}

	public void setPublicKey(String publicKeyPEM) throws NoSuchAlgorithmException {
		String formatKey = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "")
				.replace("-----END PUBLIC KEY-----", "")
				.replaceAll("\\s", "")
				.trim();
		byte[] keyBytes = Base64.getDecoder().decode(formatKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		try {
			this.publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setPrivateKey(String privateKeyPEM) throws NoSuchAlgorithmException {
		String formatKey = privateKeyPEM.replace("-----BEGIN RSA PRIVATE KEY-----", "")
				.replace("-----END RSA PRIVATE KEY-----", "")
				.replaceAll("\\s", "")
				.trim();
		byte[] keyBytes = Base64.getDecoder().decode(formatKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		try {
			this.privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Bean("defaultRSAKeyPair")
	public RSACipherConfig defaultRSAKeyPair() throws NoSuchAlgorithmException {
		RSACipherConfig rsaCipherConfig = new RSACipherConfig();
		rsaCipherConfig.setPrivateKey(getDEFAULT_PRIVATEKEY());
		rsaCipherConfig.setPublicKey(getDEFAULT_PUBLICKEY());
		return rsaCipherConfig;
	}

	@Bean("RSAKeyPairWithFile")
	public RSACipherConfig RSAKeyPairWithFile() throws Exception {
		RSACipherConfig rsaCipherConfig = new RSACipherConfig();
		String privateKeyPath = workPath + "private.key";
		String publicKeyPath = workPath + "public.key";
		if (!new File(privateKeyPath).exists() || !new File(publicKeyPath).exists()) {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(1024);
			KeyPair keyPair = generator.generateKeyPair();
			File privateKey = new File(privateKeyPath);
			File publicKey = new File(publicKeyPath);
			if (privateKey.exists()) {
				privateKey.delete();
			}
			if (publicKey.exists()) {
				publicKey.delete();
			}
			ObjectOutputStream privateOs = new ObjectOutputStream(Files.newOutputStream(Paths.get(privateKeyPath)));
			privateOs.writeObject(keyPair.getPrivate());
			ObjectOutputStream publicOs = new ObjectOutputStream(Files.newOutputStream(Paths.get(publicKeyPath)));
			publicOs.writeObject(keyPair.getPublic());
		}
		InputStream privateKeyStream = Files.newInputStream(Paths.get(privateKeyPath));
		InputStream publicKeyStream = Files.newInputStream(Paths.get(publicKeyPath));
		rsaCipherConfig.setPrivateKey(privateKeyStream);
		rsaCipherConfig.setPublicKey(publicKeyStream);
		return rsaCipherConfig;
	}
}
