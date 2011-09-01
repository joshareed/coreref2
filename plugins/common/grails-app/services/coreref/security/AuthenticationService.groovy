package coreref.security

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class AuthenticationService {
    static transactional = false

    String encodePassword(String password) {
		return md5Hex(password)
    }

    private String md5Hex(final String s) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5")
			digest.update(s.getBytes())
			return new BigInteger(1, digest.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("No MD5 algorithm available!")
		}
    }
}
