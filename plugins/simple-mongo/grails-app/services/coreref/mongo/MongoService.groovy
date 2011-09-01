package coreref.mongo

import org.codehaus.groovy.grails.commons.ApplicationHolder

import com.mongodb.*

/**
 * A simple service for accessing a MongoDB instance.
 */
class MongoService {
	private def mongo
	boolean transactional = false

	static {
		// some convenience methods
		DBCollection.metaClass {
			count << { LinkedHashMap query -> delegate.count(query as BasicDBObject) }
			list << {  -> delegate.find() }
			findAll << { LinkedHashMap query -> delegate.find(query as BasicDBObject) }
			findAll << { LinkedHashMap query, LinkedHashMap filter -> delegate.find(query as BasicDBObject, filter as BasicDBObject) }
			find << { LinkedHashMap query -> delegate.find(query as BasicDBObject).find {true} }
			find << { LinkedHashMap query, LinkedHashMap filter -> delegate.find(query as BasicDBObject, filter as BasicDBObject).find {true} }
			add << { LinkedHashMap doc -> delegate.insert(doc as BasicDBObject) }
			add << { Object obj -> delegate.insert(obj.save() as BasicDBObject) }
			update << { BasicDBObject doc, LinkedHashMap op -> delegate.update(doc, op as BasicDBObject) }
			methodMissing { String name, args ->
				if (name.startsWith('findBy')) {
					def p =	 name - 'findBy'
					p = p[0].toLowerCase() + p[1..-1]
					def query = [:]
					query[p] = args[0]
					return delegate.find(query as BasicDBObject).find { true }
				} else if (name.startsWith('findAllBy')) {
					def p =	 name - 'findAllBy'
					p = p[0].toLowerCase() + p[1..-1]
					def query = [:]
					query[p] = args[0]
					return delegate.find(query as BasicDBObject)
				}
				throw new MissingMethodException(name, delegate.getClass(), args)
			}
		}
		DBCursor.metaClass {
			sort << { LinkedHashMap keys -> delegate.sort(keys as BasicDBObject) }
			first << { -> delegate.find { true } }
		}
	}

	/**
	 * Allow accessing collections like properties.
	 */
	def propertyMissing(name) {
		def collection = getCollection(name)
		if (collection) {
			MongoService.metaClass."$name" = collection
		}
		return collection
	}

	/**
	 * Get a collection by name.  If the collection doesn't exist, this method returns null.
	 */
	def getCollection(name) {
		if (!name) { return null }
		if (!mongo) {
			// create our Mongo instance if needed
			def host = ApplicationHolder?.application?.config?.mongo?.host ?: 'localhost'
			def servers = host.split(',').collect { new ServerAddress(it) }
			mongo = new Mongo(servers)
		}

		// get our database and collection
		def db = mongo.getDB(ApplicationHolder?.application?.config?.mongo?.db ?: 'coreref')
		if (name in db.collectionNames) {
			return db.getCollection(name)
		} else {
			return null
		}
	}

	def map(Class clazz) {
		println clazz.mongo
	}
}
