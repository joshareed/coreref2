package coreref.common

class DomainUtils {

	static boolean coerceBoolean(val, defaultVal = false) {
		val == null ? defaultVal : (val == true || val == 'true' || val == 'on')
	}

	static int coerceInt(val, defaultVal = 1) {
		val == null ? defaultVal : val as int
	}

	static List coerceList(val, defaultVal = []) {
		if (val == null) {
			return defaultVal
		} else if (val instanceof List) {
			return val
		} else {
			return val.toString().split(',').collect { it.trim() }
		}
	}

	static boolean isSet(instance, field) {
		if (instance.hasProperty(field)) {
			def val = instance."$field"
			return (val != null && !"".equals(val.trim()))
		} else {
			return false
		}
	}

	static boolean isUnique(instance, field, collection) {
		def val = instance."$field"
		def list = collection.findAll((field): val)
		!list.find { it['_id']?.toString() != instance?.id?.toString() }
	}
}