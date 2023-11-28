package com.example.bilsem


import android.content.Intent
import android.app.Activity
import android.os.Bundle
import android.widget.Button

class Anasayfa : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.anasayfa)

        val btnGirisYap = findViewById<Button>(R.id.btnGirisYap)
        val btnKayitOl = findViewById<Button>(R.id.btnKayitOl)

        btnGirisYap.setOnClickListener {
            val intent = Intent(this, Girisyap::class.java)
            startActivity(intent)
        }

        btnKayitOl.setOnClickListener {
            val intent = Intent(this, Kayitol::class.java)
            startActivity(intent)
        }
        val tweetButton = findViewById<Button>(R.id.tweetButton)
        tweetButton.setOnClickListener {
            val twitterHelper = TwitterHelper()
            twitterHelper.sendTweet()
        }
    }
}