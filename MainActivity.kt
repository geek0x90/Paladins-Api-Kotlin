package com.example.localghost.paladinsstats

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.android.volley.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var debugText = findViewById<TextView>(R.id.debugText)
        var button = findViewById<Button>(R.id.button)

        var papi = PaladinsApi(this, getString(R.string.devID), getString(R.string.authKey))
        papi.setEndPoint(HiRezPlatform.PC)
        papi.setGame(HiRezGame.PALADINS)

        papi.createSession({
            Log.d("Session", "Created")

            papi.getPlayer(Response.Listener { response ->
                var playerID = response.getJSONObject(0).getInt("Id")

                papi.getPlayerAchievements(Response.Listener { response ->
                    debugText.text = response.toString()
                }, playerID)
            }, "divby0")
        })

        button.setOnClickListener {
            papi.getChampions(Response.Listener { response ->
                debugText.text = response.toString()
            }, "1")
        }
    }
}
