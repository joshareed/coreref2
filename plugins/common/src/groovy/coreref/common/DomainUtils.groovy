package coreref.common

class DomainUtils {

	static boolean coerceBoolean(val, defaultVal = false) {
		val == null ? defaultVal : (val == true || val == 'true' || val == 'on')
	}

	static boolean isSet(instance, field) {
		def val = instance."$field"
		(val != null && !"".equals(val.trim()))
	}
}