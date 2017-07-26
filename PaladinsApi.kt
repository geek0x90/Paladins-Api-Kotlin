package com.example.localghost.paladinsstats

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONObject
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray


enum class HiRezPlatform {PC, XBOX, PS4}

enum class HiRezGame {SMITE, PALADINS}

class PaladinsApi {
    var devID: String = ""
    var authKey: String = ""
    var game: String = ""
    var endPoint: String = ""
    var hirezplatform: HiRezPlatform = HiRezPlatform.PC
    var hirezgame: HiRezGame = HiRezGame.PALADINS
    var time: String = ""
    var queue: RequestQueue? = null
    var context: Context? = null
    var session: String = ""

    constructor(context: Context, devID: String, authKey: String) {
        this.context = context
        this.queue = Volley.newRequestQueue(context)
        this.devID = devID
        this.authKey = authKey
        //createSession()
        setGame(this.hirezgame)
    }

    fun setGame(game: HiRezGame) {
        when(game) {
            HiRezGame.PALADINS -> {
                this.game = "paladins"
            }
            HiRezGame.SMITE -> {
                this.game = "smiteS"
            }
        }

        this.hirezgame = game

        setEndPoint(this.hirezplatform)
    }

    fun setEndPoint(endpoint: HiRezPlatform) {
        when(endpoint) {
            HiRezPlatform.PC -> {
                this.endPoint = "http://api." + game + ".com/paladinsapi.svc"
            }
            HiRezPlatform.XBOX -> {
                this.endPoint = "http://api.xbox." + game + ".com/paladinsapi.svc"
            }
            HiRezPlatform.PS4 -> {
                this.endPoint = "http://api.ps4." + game + ".com/paladinsapi.svc"
            }
        }

        this.hirezplatform = endpoint
    }

    fun createSession(successListener: () ->  Unit = {}) {
        updateTime()
        var uri: String = this.endPoint + "/createsessionJson/" + this.devID + "/" + getSignature("createsession") + "/" + this.time

        Log.d("URI", uri)

        var request = JsonObjectRequest(Request.Method.GET, uri, null, Response.Listener { response ->
            this.session = response.getString("session_id")
            Log.d("SESSIONID", response.getString("session_id"))
            successListener()
        }, Response.ErrorListener { error ->
            Log.d("Api Error", "createSession: " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun testSession(successListener: Response.Listener<JSONObject>) {
        updateTime()
        var uri: String = this.endPoint + "/testsessionJson/" + this.devID + "/" + getSignature("testsession") + "/" + this.time

        Log.d("URI", uri)

        var request = JsonObjectRequest(Request.Method.GET, uri, null, successListener, Response.ErrorListener { error ->
            Log.d("Api Error", "testSession: " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun ping() {
        updateTime()
        var uri: String = this.endPoint + "/ping"

        Log.d("URI", uri)

        var request = JsonArrayRequest(Request.Method.GET, uri, null, Response.Listener { response ->
            Log.d("Ping", "Success")
        } , Response.ErrorListener { error ->
            Log.d("Api Error", "getMotd " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getHiRezServerStatus(successListener: Response.Listener<JSONObject>) {
        updateTime()
        var uri: String = this.endPoint + "/gethirezserverstatusJson/" + this.devID + "/" + getSignature("gethirezserverstatus") + "/" + this.time

        Log.d("URI", uri)

        var request = JsonObjectRequest(Request.Method.GET, uri, null, successListener, Response.ErrorListener { error ->
            Log.d("Api Error", "getHiRezServerStatus: " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getMotd(successListener: Response.Listener<JSONArray>) {
        updateTime()
        var uri: String = this.endPoint + "/getmotdJson/" + this.devID + "/" + getSignature("getmotd") + "/" + this.session + "/" + this.time

        Log.d("URI", uri)

        var request = JsonArrayRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getMotd " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getDataUsed(successListener: Response.Listener<JSONArray>) {
        updateTime()
        var uri: String = this.endPoint + "/getdatausedJson/" + this.devID + "/" + getSignature("getmotd") + "/" + this.session + "/" + this.time

        Log.d("URI", uri)

        var request = JsonArrayRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getDataUsed " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getPlayer(successListener: Response.Listener<JSONArray>, player: String) {
        updateTime()
        var uri: String = this.endPoint + "/getplayerJson/" + this.devID + "/" + getSignature("getplayer") + "/" + this.session + "/" + this.time + "/" + player

        Log.d("URI", uri)

        var request = JsonArrayRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getPlayer " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getESportsProLeagueDetails(successListener: Response.Listener<JSONArray>) {
        updateTime()
        var uri: String = this.endPoint + "/getesportsproleaguedetailsJson/" + this.devID + "/" + getSignature("getesportsproleaguedetails") + "/" + this.session + "/" + this.time

        Log.d("URI", uri)

        var request = JsonArrayRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getESportsProLeagueDetails " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getFriends(successListener: Response.Listener<JSONArray>, player: String) {
        updateTime()
        var uri: String = this.endPoint + "/getfriendsJson/" + this.devID + "/" + getSignature("getfriends") + "/" + this.session + "/" + this.time + "/" + player

        Log.d("URI", uri)

        var request = JsonArrayRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getFriends " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getGodRank(successListener: Response.Listener<JSONArray>, player: String) {
        updateTime()
        var uri: String = this.endPoint + "/getgodranksJson/" + this.devID + "/" + getSignature("getgodranks") + "/" + this.session + "/" + this.time + "/" + player

        Log.d("URI", uri)

        var request = JsonArrayRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getGodRank " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getChampionRank(successListener: Response.Listener<JSONArray>, player: String) {
        updateTime()
        var uri: String = this.endPoint + "/getchampionranksJson/" + this.devID + "/" + getSignature("getchampionranks") + "/" + this.session + "/" + this.time + "/" + player

        Log.d("URI", uri)

        var request = JsonArrayRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getChampionRank " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getGods(successListener: Response.Listener<JSONArray>, languageCode: String) {
        updateTime()
        var uri: String = this.endPoint + "/getgodsJson/" + this.devID + "/" + getSignature("getgods") + "/" + this.session + "/" + this.time + "/" + languageCode

        Log.d("URI", uri)

        var request = JsonArrayRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getGods " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getChampions(successListener: Response.Listener<JSONArray>, languageCode: String) {
        updateTime()
        var uri: String = this.endPoint + "/getchampionsJson/" + this.devID + "/" + getSignature("getchampions") + "/" + this.session + "/" + this.time + "/" + languageCode

        Log.d("URI", uri)

        var request = JsonArrayRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getChampions " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getGodLeaderboard(successListener: Response.Listener<JSONArray>, godID: String, queue: String) {
        updateTime()
        var uri: String = this.endPoint + "/getgodleaderboardJson/" + this.devID + "/" + getSignature("getgodleaderboard") + "/" + this.session + "/" + this.time + "/" + godID + "/" + queue

        Log.d("URI", uri)

        var request = JsonArrayRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getGodLeaderboard " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getGodSkins(successListener: Response.Listener<JSONArray>, godID: String, languageCode: String) {
        updateTime()
        var uri: String = this.endPoint + "/getgodskinsJson/" + this.devID + "/" + getSignature("getgodskins") + "/" + this.session + "/" + this.time + "/" + godID + "/" + languageCode

        Log.d("URI", uri)

        var request = JsonArrayRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getGodSkins " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getChampionSkins(successListener: Response.Listener<JSONArray>, godID: String, languageCode: String) {
        updateTime()
        var uri: String = this.endPoint + "/getchampionskinsJson/" + this.devID + "/" + getSignature("getchampionskins") + "/" + this.session + "/" + this.time + "/" + godID + "/" + languageCode

        Log.d("URI", uri)

        var request = JsonArrayRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getChampionSkins " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getItems(successListener: Response.Listener<JSONArray>, leagueCode: String) {
        updateTime()
        var uri: String = this.endPoint + "/getitemsJson/" + this.devID + "/" + getSignature("getitems") + "/" + this.session + "/" + this.time + "/" + leagueCode

        Log.d("URI", uri)

        var request = JsonArrayRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getItems " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getPlayerLoadouts(successListener: Response.Listener<JSONArray>, player: String) {
        updateTime()
        var uri: String = this.endPoint + "/getplayerloadoutsJson/" + this.devID + "/" + getSignature("getplayerloadouts") + "/" + this.session + "/" + this.time + "/" + player

        Log.d("URI", uri)

        var request = JsonArrayRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getPlayerLoadouts " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getDemoDetails(successListener: Response.Listener<JSONArray>, matchID: Int) {
        updateTime()
        var uri: String = this.endPoint + "/getdemodetailsJson/" + this.devID + "/" + getSignature("getdemodetails") + "/" + this.session + "/" + this.time + "/" + matchID.toString()

        Log.d("URI", uri)

        var request = JsonArrayRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getDemoDetails " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getMatchDetails(successListener: Response.Listener<JSONArray>, matchID: Int) {
        updateTime()
        var uri: String = this.endPoint + "/getmatchdetailsJson/" + this.devID + "/" + getSignature("getmatchdetails") + "/" + this.session + "/" + this.time + "/" + matchID.toString()

        Log.d("URI", uri)

        var request = JsonArrayRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getMatchDetails " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getMatchPlayerDetails(successListener: Response.Listener<JSONArray>, matchID: Int) {
        updateTime()
        var uri: String = this.endPoint + "/getmatchplayerdetailsJson/" + this.devID + "/" + getSignature("getmatchplayerdetails") + "/" + this.session + "/" + this.time + "/" + matchID.toString()

        Log.d("URI", uri)

        var request = JsonArrayRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getMatchPlayerDetails " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getPlayerStatus(successListener: Response.Listener<JSONArray>, player: String) {
        updateTime()
        var uri: String = this.endPoint + "/getplayerstatusJson/" + this.devID + "/" + getSignature("getplayerstatus") + "/" + this.session + "/" + this.time + "/" + player

        Log.d("URI", uri)

        var request = JsonArrayRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getPlayerStatus " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getMatchHistory(successListener: Response.Listener<JSONArray>, player: String) {
        updateTime()
        var uri: String = this.endPoint + "/getmatchhistoryJson/" + this.devID + "/" + getSignature("getmatchhistory") + "/" + this.session + "/" + this.time + "/" + player

        Log.d("URI", uri)

        var request = JsonArrayRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getMatchHistory " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getLeagueSeasons(successListener: Response.Listener<JSONArray>, queue: String) {
        updateTime()
        var uri: String = this.endPoint + "/getleagueseasonsJson/" + this.devID + "/" + getSignature("getleagueseasons") + "/" + this.session + "/" + this.time + "/" + queue

        Log.d("URI", uri)

        var request = JsonArrayRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getLeagueSeasons " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getLeagueLeaderboard(successListener: Response.Listener<JSONArray>, queue: String, tier: String, season: String) {
        updateTime()
        var uri: String = this.endPoint + "/getleagueleaderboardJson/" + this.devID + "/" + getSignature("getleagueleaderboard") + "/" + this.session + "/" + this.time + "/" + queue + "/" + tier + "/" + season

        Log.d("URI", uri)

        var request = JsonArrayRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getLeagueLeaderboard " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getSearchTeams(successListener: Response.Listener<JSONArray>, team: String) {
        updateTime()
        var uri: String = this.endPoint + "/searchteamsJson/" + this.devID + "/" + getSignature("searchteams") + "/" + this.session + "/" + this.time + "/" + team

        Log.d("URI", uri)

        var request = JsonArrayRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getSearchTeams " + error.toString())
        })

        queue!!.add(request)
    }

    fun getTopMatches(successListener: Response.Listener<JSONArray>) {
        updateTime()
        var uri: String = this.endPoint + "/gettopmatchesJson/" + this.devID + "/" + getSignature("gettopmatches") + "/" + this.session + "/" + this.time

        Log.d("URI", uri)

        var request = JsonArrayRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getTopMatches " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getPatchInfo(successListener: Response.Listener<JSONObject>) {
        updateTime()
        var uri: String = this.endPoint + "/getpatchinfoJson/" + this.devID + "/" + getSignature("getpatchinfo") + "/" + this.session + "/" + this.time

        Log.d("URI", uri)

        var request = JsonObjectRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getPatchInfo " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getTeamPlayers(successListener: Response.Listener<JSONArray>, clanID: Int) {
        updateTime()
        var uri: String = this.endPoint + "/getteamplayersJson/" + this.devID + "/" + getSignature("getteamplayers") + "/" + this.session + "/" + this.time + "/" + clanID.toString()

        Log.d("URI", uri)

        var request = JsonArrayRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getTeamPlayers " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getQueueStats(successListener: Response.Listener<JSONArray>, player: String, queue: String) {
        updateTime()
        var uri: String = this.endPoint + "/getqueuestatsJson/" + this.devID + "/" + getSignature("getqueuestats") + "/" + this.session + "/" + this.time + "/" + player + "/" + queue

        Log.d("URI", uri)

        var request = JsonArrayRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getQueueStats " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getTeamDetails(successListener: Response.Listener<JSONArray>, clanID: Int) {
        updateTime()
        var uri: String = this.endPoint + "/getteamdetailsJson/" + this.devID + "/" + getSignature("getteamdetails") + "/" + this.session + "/" + this.time + "/" + clanID.toString()

        Log.d("URI", uri)

        var request = JsonArrayRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getTeamDetails " + error.toString())
        })

        queue!!.add(request)
    }

    fun getPlayerAchievements(successListener: Response.Listener<JSONObject>, playerID: Int) {
        updateTime()
        var uri: String = this.endPoint + "/getplayerachievementsJson/" + this.devID + "/" + getSignature("getplayerachievements") + "/" + this.session + "/" + this.time + "/" + playerID.toString()

        Log.d("URI", uri)

        var request = JsonObjectRequest(Request.Method.GET, uri, null, successListener , Response.ErrorListener { error ->
            Log.d("Api Error", "getPlayerAchievements " + error.toString())
        })

        this.queue!!.add(request)
    }

    fun getSignature(api: String): String {
        var string: String = this.devID + api + this.authKey + this.time;
        var hash: String = md5(string)

        return hash
    }

    fun updateTime() {
        var formatter: SimpleDateFormat = SimpleDateFormat("yyyyMMddHHmmss")
        formatter.timeZone = TimeZone.getTimeZone("UTC")

        var gtmTime: String = formatter.format(System.currentTimeMillis())

        this.time = gtmTime
        //this.time = formatter.format( df )
    }

    private fun md5(input: String): String {
        val digest: MessageDigest

        try {
            digest = MessageDigest.getInstance("MD5")
            digest.reset()
            digest.update(input.toByteArray())

            val a = digest.digest()
            val len = a.size
            val sb = StringBuilder(len shl 1)

            for (i in 0..len - 1) {
                sb.append(Character.forDigit(a[i].toInt() and 0xf0 shr 4, 16))
                sb.append(Character.forDigit(a[i].toInt() and 0x0f, 16))
            }

            return sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return ""
    }
}