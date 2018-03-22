package com.vb.tracker.free.lock;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.util.Locale;

class Encryptor {

	private static String bytes2Hex(byte[] bytes) {

		StringBuilder hs = new StringBuilder();
		for ( byte element: bytes) {
			String stmp = (Integer.toHexString(element & 0XFF));
			if (stmp.length() == 1) {
				hs.append("0").append(stmp);
			} else {
				hs.append(stmp);
			}
		}

		return hs.toString().toLowerCase(Locale.ENGLISH);
	}

	static String getSHA1(String text) {

		if (TextUtils.isEmpty(text)) {
			return null;
		}
		MessageDigest sha1Digest;
		try {
			sha1Digest = MessageDigest.getInstance("SHA-1");
		} catch (Exception e) {
			return null;
		}
		byte[] textBytes = text.getBytes();
		sha1Digest.update(textBytes, 0, text.length());
		byte[] sha1hash = sha1Digest.digest();
		return bytes2Hex(sha1hash);
	}
}
