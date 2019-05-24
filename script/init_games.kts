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

val games = ArrayList<org.bson.Document>()

val zeroX = org.bson.Document("name", "0x Universe")
zeroX["url"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/0x-universe.jpg"
zeroX["_id"] = "3bb55a99-2262-4c8d-bad8-ba9f2bf200a7"
games.add(zeroX)
val apexLegends = org.bson.Document("name", "Apex Legends")
apexLegends["url"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/apex-legends.jpg"
apexLegends["_id"] = "481cad39-500f-4e43-b107-335ac4192794"
games.add(apexLegends)
val beyondTheVoid = org.bson.Document("name", "Beyond The Void")
beyondTheVoid["url"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/beyond-the-void.jpeg"
beyondTheVoid["_id"] = "2e214103-5071-4f06-b66e-a8d3c9e8696c"
games.add(beyondTheVoid)
val csgo = org.bson.Document("name", "CSGO")
csgo["url"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/counter-strike-global-offensive.jpg"
csgo["_id"] = "dbc397d5-0e4c-4071-998a-fa2f26e5cc5a"
games.add(csgo)
val dota_two = org.bson.Document("name", "DOTA 2")
dota_two["url"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/dota-2.jpg"
dota_two["_id"] = "0b07301d-9e60-47f6-8686-623c59681b5e"
games.add(dota_two)
val dota_auto_chess = org.bson.Document("name", "DOTA Auto Chess")
dota_auto_chess["url"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/dota-auto-chess.png"
dota_auto_chess["_id"] = "77753c6f-5a08-4df1-b631-86204c7abf54"
games.add(dota_auto_chess)
val fortnite = org.bson.Document("name", "Fortnite")
fortnite["url"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/fortnite.jpg"
fortnite["_id"] = "882065d8-55b6-4524-9908-daccd6e9b461"
games.add(fortnite)
val godsUnchained = org.bson.Document("name", "Gods Unchained")
godsUnchained["url"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/gods-unchained.jpg"
godsUnchained["_id"] = "43b1dd51-8b1a-436f-8d48-5d9e131d5fe3"
games.add(godsUnchained)
val LoL = org.bson.Document("name", "League of Legends")
LoL["url"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/league-of-legends.jpg"
LoL["_id"] = "d8b33c59-60c5-4870-82a7-a340e5a0b963"
games.add(LoL)
val PUBG = org.bson.Document("name", "PUBG")
PUBG["url"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/pubg.jpg"
PUBG["_id"] = "396fd7a1-da65-48f0-ab0e-b043170a9d27"
games.add(PUBG)
val PUBGm = org.bson.Document("name", "PUBG Mobile")
PUBGm["url"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/pubg.jpg"
PUBGm["_id"] = "27529c86-70a5-4b72-af49-9d868b44b950"
games.add(PUBGm)
val r6 = org.bson.Document("name", "Rainbow Six: Siege")
r6["url"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/rainbow-six.jpg"
r6["_id"] = "34299aef-9c19-46a0-b5ca-57fdeaa6c026"
games.add(r6)
val rocket_league = org.bson.Document("name", "Rocket League")
rocket_league["url"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/rocket-league.jpg"
rocket_league["_id"] = "1200640a-1570-4b68-82b2-3ef36522e62e"
games.add(rocket_league)
val SoG = org.bson.Document("name", "Spells of Genesis")
SoG["url"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/spells-of-genesis.jpg"
SoG["_id"] = "0aa80d85-8558-4a73-8c12-f11a8a29b634"
games.add(SoG)
val WoW = org.bson.Document("name", "World of Warcraft")
WoW["url"] = "https://s3.us-east-2.amazonaws.com/eggegg-images/games/world-of-warcraft.jpg"
WoW["_id"] = "dd0dfa0a-f0e2-4aa1-a05c-b61fe6d6b12a"
games.add(WoW)

games.forEach{ game ->
    game.append("createdAt", Date())
    game.append("updatedAt", Date())
    gamesCollection.insertOne(game)
}
