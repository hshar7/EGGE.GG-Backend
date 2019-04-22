#!/usr/bin/env kscript
@file:DependsOn("org.mongodb:mongo-java-driver:3.8.2")
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoClient
import com.mongodb.MongoClientSettings
import com.mongodb.ConnectionString
import com.mongodb.ServerAddress
import com.mongodb.MongoCredential
import com.mongodb.MongoClientOptions
import com.mongodb.client.model.IndexOptions
import java.util.*

var mongoUrl: String = "mongodb://localhost:27017/eggegg"
val client = MongoClients.create(mongoUrl)
val database = client.getDatabase("eggegg")
val tokens = database.getCollection("tokens")

var index = org.bson.Document("symbol", 1)
tokens.createIndex(index, IndexOptions().unique(true));
index = org.bson.Document("name", 1)
tokens.createIndex(index, IndexOptions().unique(true));
index = org.bson.Document("address", 1)
tokens.createIndex(index, IndexOptions().unique(true));

val eth = org.bson.Document()
eth.append("_id", UUID.randomUUID().toString())
eth.append("symbol", "ETH")
eth.append("name", "Ether")
eth.append("address", "0x0000000000000000000000000000000000000000")
eth.append("usdPrice", 160.toFloat())
eth.append("tokenVersion", 0)
eth.append("createdAt", Date())
eth.append("updatedAt", Date())
tokens.insertOne(eth)

val dai = org.bson.Document()
dai.append("_id", UUID.randomUUID().toString())
dai.append("symbol", "DAI")
dai.append("name", "DAI")
dai.append("address", "0x89d24a6b4ccb1b6faa2625fe562bdd9a23260359")
dai.append("usdPrice", 1.toFloat())
dai.append("tokenVersion", 20)
dai.append("createdAt", Date())
dai.append("updatedAt", Date())
tokens.insertOne(dai)

val eggToken = org.bson.Document()
eggToken.append("_id", UUID.randomUUID().toString())
eggToken.append("symbol", "EGG")
eggToken.append("name", "Egg Test Token")
eggToken.append("address", "0xccb006c729d74c5bd87de94955166947cecf9f47")
eggToken.append("usdPrice", 1.toFloat())
eggToken.append("tokenVersion", 20)
eggToken.append("createdAt", Date())
eggToken.append("updatedAt", Date())
tokens.insertOne(eggToken)
