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
import kotlin.collections.HashMap

var mongoUrl: String = "mongodb://localhost:27017/eggegg"
val client = MongoClients.create(mongoUrl)
val database = client.getDatabase("eggegg")
val gamesCollection = database.getCollection("games")

val index = org.bson.Document("name", 1)
gamesCollection.createIndex(index, IndexOptions().unique(true));

val games = HashMap<String, String>()
games["0x Universe"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/0x-universe.jpg"
games["Apex Legends"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/apex-legends.jpg"
games["Beyond The Void"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/beyond-the-void.jpeg"
games["CSGO"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/counter-strike-global-offensive.jpg"
games["DOTA 2"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/dota-2.jpg"
games["DOTA Auto Chess"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/dota-auto-chess.png"
games["Fortnite"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/fortnite.jpg"
games["Gods Unchained"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/gods-unchained.jpg"
games["League of Legends"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/league-of-legends.jpg"
games["PUBG"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/pubg.jpg"
games["PUBG Mobile"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/pubg.jpg"
games["Rainbow Six: Siege"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/rainbow-six.jpg"
games["Rocket League"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/rocket-league.jpg"
games["Spells of Genesis"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/spells-of-genesis.jpg"
games["World of Warcraft"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/world-of-warcraft.jpg"

games.forEach{ (key, value) ->
    val game = org.bson.Document()
    game.append("_id", UUID.randomUUID().toString())
    game.append("name", key)
    game.append("url", value)
    game.append("createdAt", Date())
    game.append("updatedAt", Date())
    gamesCollection.insertOne(game)
}
