package com.bistu.common.util;

import javax.crypto.Cipher;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author: zxh
 * @date: 2024/4/10 11:54
 * @description: 加解密
 */
public class EncryptionUtil {
	private static final String PRIVATE_KEY_FILE_NAME = "private.key";  // 私钥名称
	private static final String PUBLIC_KEY_FILE_NAME = "public.key";    // 公钥名称
	private static final String DEFAULT_URL_ENCODING = "UTF-8";
	private static PrivateKey privateKey;   // 私钥
	private static PublicKey publicKey;     // 公钥

	private static boolean initSuccess = false; // 初始化状态

	/**
	 * 初始化密钥对
	 *
	 * @param keyDir 秘钥保存目录
	 * @throws Exception 初始化时出现的异常，异常不为 EncryptException 时为初始化失败
	 */
	public static void initKey(String keyDir) throws Exception {
		String privateKeyPath = keyDir + PRIVATE_KEY_FILE_NAME;
		String publicKeyPath = keyDir + PUBLIC_KEY_FILE_NAME;
		try {
			privateKey = readPrivateKeyFromFile(privateKeyPath);
			publicKey = readPublicKeyFromFile(publicKeyPath);
		} catch (EncryptException e) {
			try {
				// 创建密钥对
				KeyPair keyPair = generateKeyPair("RSA");
				privateKey = keyPair.getPrivate();
				publicKey = keyPair.getPublic();
				saveKeyPair(privateKeyPath, privateKey, publicKeyPath, publicKey);
				initSuccess = true;
			} catch (NoSuchAlgorithmException ex) {
				initSuccess = false;
				throw new NoSuchAlgorithmException("生成秘钥失败", ex);
			} catch (IOException ioe) {
				initSuccess = false;
				throw new IOException("保存秘钥失败", ioe);
			}
			throw new EncryptException("读取秘钥对失败：" + e.getMessage() + "，重新生成秘钥", e);
		}
		initSuccess = true;
	}

	/**
	 * 初始化公钥
	 *
	 * @param keyPem pem格式的公钥
	 * @throws Exception 初始化时出现的异常，异常不为 EncryptException 时为初始化失败
	 */
	public static void initPubKey(String keyPem) throws Exception {
		// 读取 PEM 文件内容
		// 去除 PEM 格式的头部和尾部，只保留 Base64 编码的部分
		String keyString = keyPem.replace("-----BEGIN PUBLIC KEY-----", "")
				.replace("-----END PUBLIC KEY-----", "")
				.replaceAll("\\s", "")
				.trim();

		// Base64 解码
		byte[] keyBytes = Base64.getDecoder().decode(keyString);

		// 使用 KeyFactory 初始化公钥
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		try {
			initSuccess = true;
			publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成密钥对
	 *
	 * @param keyType 秘钥类型
	 * @return 返回秘钥对
	 * @throws NoSuchAlgorithmException 异常
	 */
	private static KeyPair generateKeyPair(String keyType) throws NoSuchAlgorithmException {
		KeyPairGenerator generator = KeyPairGenerator.getInstance(keyType);
		generator.initialize(1024);
		return generator.generateKeyPair();
	}

	/**
	 * 保存秘钥到文件中
	 *
	 * @param privateKeyPath 私钥path
	 * @param privateKey     私钥对象
	 * @param publicKeyPath  公钥path
	 * @param publicKey      公钥对象
	 * @throws IOException IO异常
	 */
	private static void saveKeyPair(String privateKeyPath, PrivateKey privateKey, String publicKeyPath, PublicKey publicKey) throws IOException {
		// 保存私钥
		File privateKeyFile = new File(privateKeyPath);
		if (privateKeyFile.exists()) {
			privateKeyFile.delete();
		}
		ObjectOutputStream privateOut = new ObjectOutputStream(Files.newOutputStream(Paths.get(privateKeyPath)));
		privateOut.writeObject(privateKey);

		// 保存公钥
		File publicKeyFile = new File(publicKeyPath);
		if (publicKeyFile.exists()) {
			publicKeyFile.delete();
		}
		ObjectOutputStream publicOut = new ObjectOutputStream(Files.newOutputStream(Paths.get(publicKeyPath)));
		publicOut.writeObject(publicKey);
	}

	/**
	 * 从文件读取私钥
	 *
	 * @param filePath 私钥path
	 * @return 返回私钥对象
	 * @throws EncryptException 读取失败异常
	 */
	private static PrivateKey readPrivateKeyFromFile(String filePath) throws EncryptException {
		try {
//			ObjectInputStream privateIn = new ObjectInputStream(Files.newInputStream(Paths.get(filePath)));
//			return (PrivateKey) privateIn.readObject();
			String keyString = new String(Files.readAllBytes(Paths.get(filePath)));

			// 去除 PEM 格式的头部和尾部，只保留 Base64 编码的部分
			keyString = keyString.replace("-----BEGIN PRIVATE KEY-----", "")
					.replace("-----END PRIVATE KEY-----", "")
					.replaceAll("\\s", "")
					.trim();

			// Base64 解码
			byte[] keyBytes = Base64.getDecoder().decode(keyString);

			// 使用 KeyFactory 初始化公钥
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
		} catch (Exception e) {
			throw new EncryptException("读取私钥失败", e);
		}
	}

	/**
	 * 从文件读取公钥
	 *
	 * @param filePath 公钥path
	 * @return 返回公钥对象
	 * @throws EncryptException 读取失败异常
	 */
	private static PublicKey readPublicKeyFromFile(String filePath) throws EncryptException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
//		try {
//			ObjectInputStream publicIn = new ObjectInputStream(Files.newInputStream(Paths.get(filePath)));
//			return (PublicKey) publicIn.readObject();
//		} catch (Exception e) {
//			throw new EncryptException("读取公钥失败", e);
//		}
		// 读取 PEM 文件内容
		String keyString = new String(Files.readAllBytes(Paths.get(filePath)));

		// 去除 PEM 格式的头部和尾部，只保留 Base64 编码的部分
		keyString = keyString.replace("-----BEGIN PUBLIC KEY-----", "")
				.replace("-----END PUBLIC KEY-----", "")
				.replaceAll("\\s", "")
				.trim();

		// Base64 解码
		byte[] keyBytes = Base64.getDecoder().decode(keyString);

		// 使用 KeyFactory 初始化公钥
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
	}

	/**
	 * 对数据进行数字签名
	 *
	 * @param data 要签名的数据
	 * @return 返回数字签名
	 */
	public static String signData(String data) throws EncryptException {
		if (!initSuccess) {
			throw new EncryptException("秘钥初始化失败");
		}
		try {
			// 创建签名对象
			Signature signature = Signature.getInstance("SHA256withRSA");
			// 初始化私钥
			signature.initSign(privateKey);
			// 设置数据
			signature.update(data.getBytes());
			// 创建数字签名
			byte[] digitalSignature = signature.sign();
			// 返回base64字符串
			return Base64.getEncoder().encodeToString(digitalSignature);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 验证一段数据的数字签名
	 *
	 * @param data             要验证的数据
	 * @param digitalSignature 数字签名
	 * @return 返回数字签名是否验证通过
	 */
	public static boolean verifySignature(String data, String digitalSignature) throws EncryptException {
		if (!initSuccess) {
			throw new EncryptException("秘钥初始化失败");
		}
		try {
			// 创建签名对象
			Signature signature = Signature.getInstance("SHA256withRSA");
			// 初始化公钥
			signature.initVerify(publicKey);
			// 设置数据
			signature.update(data.getBytes());
			// 将数字签名通过base64反编码回byte数组
			byte[] decodedSignature = Base64.getDecoder().decode(digitalSignature);
//			byte[] decodedSignature = digitalSignature.getBytes();
			// 对数字签名进行验证
			return signature.verify(decodedSignature);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 解密字符串
	 *
	 * @param encryptedData 加密后的字符串
	 * @return 解密完的字符串
	 */
	public static String decrypt(String encryptedData, PrivateKey privateKey) throws EncryptException {
		try {
			// 创建Cipher对象
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			// 初始化Cipher对象为解密模式，并加载私钥
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			// 解密字符串
			byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
//			return ZipUtil.unGzip(decryptedBytes, "UTF8");
			return new String(decryptedBytes, "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// 加解密自定义异常类
	public static class EncryptException extends Exception {
		public EncryptException(String message) {
			super(message);
		}

		public EncryptException(String message, Throwable cause) {
			super(message, cause);
		}
	}

	public static String urlDecode(String part) throws Exception {
		try {
			return URLDecoder.decode(part, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new Exception(part);
		}
	}
}
