package coreref.mongo

import org.codehaus.groovy.grails.commons.ApplicationHolder

import com.mongodb.*
import org.bson.types.ObjectId

/**
 * A simple service for accessing a MongoDB instance.
 */
class MongoService {
	private def _mongo
	boolean transactional = false

	static {
		// some convenience methods
		DBCollection.metaClass {
			count << { LinkedHashMap query -> delegate.count(query as BasicDBObject) }
			get << { String id ->
				try {
					return delegate.find([_id: new ObjectId(id)] as BasicDBObject).find { true }
				} catch (e) {
					return null
				}
			}
			list << {  -> delegate.find() }
			findAll << { LinkedHashMap query -> delegate.find(query as BasicDBObject) }
			findAll << { LinkedHashMap query, LinkedHashMap filter -> delegate.find(query as BasicDBObject, filter as BasicDBObject) }
			find << { LinkedHashMap query -> delegate.findOne(query as BasicDBObject) }
			find << { LinkedHashMap query, LinkedHashMap filter -> delegate.findOne(query as BasicDBObject, filter as BasicDBObject) }
			add << { LinkedHashMap doc -> delegate.insert(doc as BasicDBObject) }
			add << { Object obj -> delegate.insert(obj.save() as BasicDBObject) }
			update << { BasicDBObject doc, LinkedHashMap op -> delegate.update(doc, op as BasicDBObject) }
			update << { BasicDBObject doc, Object obj -> delegate.update(doc, obj.save() as BasicDBObject) }
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

	def getMongo() {
		if (!_mongo) {
			// create our Mongo instance if needed
			def host = ApplicationHolder?.application?.config?.mongo?.host ?: 'localhost'
			def servers = host.split(',').collect { new ServerAddress(it) }
			_mongo = new Mongo(servers)
		}
		_mongo
	}

	/**
	 * Get a collection by name.  If the collection doesn't exist, this method returns null.
	 */
	def getCollection(name) {
		if (!name) { return null }

		// get our database and collection
		def db = mongo.getDB(ApplicationHolder?.application?.config?.mongo?.db ?: 'coreref')
		if (db.collectionExists(name)) {
			return db.getCollection(name)
		} else {
			return null
		}
	}

	/**
	 * Map the class to Mongo.
	 */
	def map(Class clazz) {
		def _mongoService = this

		// decorate the class
		clazz.metaClass {
			getMongoService << { -> _mongoService }
			getMongoCollection << { -> return _mongoService.getCollection(delegate?.mongo?.collection) }
			getMongoObject << { ->
				def c = delegate.getMongoCollection()
				if (c && delegate.id) {
					return c.get(delegate.id)
				}
				return null;
			}
		}
		clazz.metaClass.static.getMongoService = { -> return _mongoService }
		clazz.metaClass.static.getMongoCollection = { -> return _mongoService.getCollection(delegate?.mongo?.collection) }
		clazz.metaClass.static.getMongoObject = { String id ->
			try {
				return getMongoCollection().find([_id: new ObjectId(id)] as BasicDBObject).find { true }
			} catch (e) {
				return null
			}
		}
		clazz.metaClass.static.getInstance << { String id ->
			def instance = getMongoObject(id)
			if (instance) {
				clazz.newInstance(instance)
			} else {
				return null
			}
		}

		// map the class to the database
		def db = mongo.getDB(ApplicationHolder?.application?.config?.mongo?.db ?: 'coreref')
		def settings = clazz.mongo
		if (db && settings && settings.collection) {
			def name = settings.collection
			if (!db.collectionExists(name)) {
				def collection = db.createCollection(name, [:] as BasicDBObject)
				if (settings.index) {
					settings.index.each { f ->
						collection.ensureIndex([(f): true] as BasicDBObject)
					}
				}
			}
		}
	}
}