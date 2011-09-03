package coreref.security

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

import coreref.common.User

class SecurityService {
    static transactional = false
	def mongoService

    String encodePassword(String password) {
		return md5Hex(password)
    }

	User authenticate(String email, String password) {
		mongoService.getCollection(User.mongo.collection).find(email: email, password: encodePassword(password), enabled: true)
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
