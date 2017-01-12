package org.pplm.framework.cas.client;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author OracleGao
 *
 */
public final class Utils {
	public static String getSha1(byte[] bytes) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
		messageDigest.update(bytes);  
        byte[] encodes = messageDigest.digest();  
 
        StringBuffer stringBuffer = new StringBuffer();  
        for (int i = 0; i < encodes.length; i++) {  
            String shaHex = Integer.toHexString(encodes[i] & 0xFF);  
            stringBuffer.append(shaHex);  
        }  
        return stringBuffer.toString();
	}
	
	@SafeVarargs
	public static <T> String joinWithoutNull(T... elements) {
		for (T element : elements) {
			if (element == null) {
				return null;
			}
		}
		return StringUtils.join(elements);
	}
	
}
