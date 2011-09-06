package coreref.common

class DomainUtils {

	static boolean coerceBoolean(val, defaultVal = false) {
		val == null ? defaultVal : (val == true || val == 'true' || val == 'on')
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
		def val = instance."$field"
		(val != null && !"".equals(val.trim()))
	}

	static boolean isUnique(instance, field, collection) {
		def val = instance."$field"
		def list = collection.findAll((field): val)
		!list.find { it['_id']?.toString() != instance?.id?.toString() }
	}
}