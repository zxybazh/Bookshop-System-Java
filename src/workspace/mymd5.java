package workspace;

import java.security.MessageDigest;

/**
 * Created by zxybazh on 5/14/15.
 */
public class mymd5 {
	public static void main(String[] args) {
		String result = getMD5("tiny");
		System.out.println(result);
	}

	/**
	 * @param message
	 * @return
	 */
	public static String getMD5(String message) {
		String md5str = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");

			byte[] input = message.getBytes();

			byte[] buff = md.digest(input);

			md5str = bytesToHex(buff);

		} catch (Exception e) {
			System.out.println(">_< MD5 Security hashing error!!! Detailed information below:");
			e.printStackTrace();
		}
		return md5str;
	}

	/**
	 * @param bytes
	 * @return
	 */
	public static String bytesToHex(byte[] bytes) {
		StringBuffer md5str = new StringBuffer();
		int digital;
		for (int i = 0; i < bytes.length; i++) {
			 digital = bytes[i];

			if(digital < 0) {
				digital += 256;
			}
			if(digital < 16){
				md5str.append("0");
			}
			md5str.append(Integer.toHexString(digital));
		}
		return md5str.toString().toLowerCase();
	}
}
