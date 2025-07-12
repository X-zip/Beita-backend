package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class CheckUtils {
	 
	private static String token = "mote"; // 定义Token 务必与服务器保持一致
 
	/**
	 * 验证签名
	 * 
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static boolean checkSignature(String signature, String timestamp,
			String nonce) {
 
		// 将token、timestamp、nonce三个参数进行字典排序  
		String[] arr = new String[] { token, timestamp, nonce };
		Arrays.sort(arr);
 
		// 将三个参数字符串拼接成一个字符串
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		try {
			//获取加密工具
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			// 对拼接好的字符串进行sha1加密
			byte[] digest = md.digest(content.toString().getBytes());
			String tmpStr = byteToStr(digest);
			//获得加密后的字符串与signature对比
			return tmpStr != null ? tmpStr.equals(signature.toUpperCase()): false;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return false;
	}
 
	private static String byteToStr(byte[] byteArray) {
		String strDigest = "";
		for (int i = 0; i < byteArray.length; i++) {
			strDigest += byteToHexStr(byteArray[i]);
		}
		return strDigest;
	}
 
	private static String byteToHexStr(byte mByte) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = Digit[mByte & 0X0F];
		String s = new String(tempArr);
		return s;
	}
 
}
