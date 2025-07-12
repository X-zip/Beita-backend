package utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 采用MD5算法加密的工具类
 */
public final class Md5Util {
	private static final Log _log = LogFactory.getLog(Md5Util.class);
	/**
	 * 盐/料，给传入的密码加点料， 目的是让它无法破解。
	 */
	private static String SALT = "Twitter\u63A8\u51FA\u7684\u5F00\u6E90web\u5F00\u53D1\u5DE5\u5177\u5305\u9664\u4E86\u767B\u5F55\u6A21\u5757\u4E4B\u5916\u7684\u9875\u9762\u4E8B\u4EF6\u90FD\u5728\u8FD9\u4E2Ajs\u91CC\u5B9A\u4E49";
	private static MessageDigest md5;
	static{ try{ md5=MessageDigest.getInstance("MD5"); } catch(NoSuchAlgorithmException ex){ ex.printStackTrace(); } }
	
	public static String encodePasswd(String passwd) {
		passwd = passwd + SALT;
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			byte[] digest = md.digest(passwd.getBytes("UTF-8"));
			return toHexString(digest);
		} catch (NoSuchAlgorithmException e) {
			_log.error(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			_log.error(e.getMessage(), e);
		}
		return passwd;
	}

	public static String md5Encode(String str, String charset)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance("md5");
		byte[] digest = md.digest(str.getBytes(charset));
		return toHexString(digest);
	}

	public static String toHexString(byte[] byteArr) {
		StringBuilder buf = new StringBuilder();
		char[] cs = new char[2];
		for (byte b : byteArr) {
			Arrays.fill(cs, '0');
			String hex = Integer.toHexString(b & 0xff);
			System.arraycopy(hex.toCharArray(), 0, cs,
					cs.length - hex.length(), hex.length());
			buf.append(new String(cs));
		}
		return buf.toString();
	}
	
	/** * MD5加密 * @param primitiveValue 原始值 * @return 加密值 */
	public static byte[] encrypt(byte[] primitiveValue) { 
		md5.update(primitiveValue); 
		return md5.digest(); 
		}

}
