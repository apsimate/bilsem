package com.example.bilsem
import com.google.firebase.Firebase
import com.google.firebase.database.database
import twitter4j.Status
import twitter4j.TwitterException
import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder

class TwitterHelper {
    private val consumerKey = ""
    private val consumerSecret = ""
    private val accessToken = "-"
    private val accessTokenSecret = ""

    fun sendTweet() {
        val cb = ConfigurationBuilder()
        cb.setDebugEnabled(true)
            .setOAuthConsumerKey(consumerKey)
            .setOAuthConsumerSecret(consumerSecret)
            .setOAuthAccessToken(accessToken)
            .setOAuthAccessTokenSecret(accessTokenSecret)

        val tf = TwitterFactory(cb.build())
        val twitter = tf.instance

        val database = Firebase.database
        val duyurularRef = database.getReference("announcements")

        duyurularRef.get().addOnSuccessListener { dataSnapshot ->
            val duyurularData = dataSnapshot.value as? Map<String, Any>
            val indeksSirasi = duyurularData?.keys?.sortedDescending()

            val sonDuyuruId = indeksSirasi?.get(0)
            val sonDuyuru = duyurularData?.get(sonDuyuruId) as? Map<String, Any>

            val title = sonDuyuru?.get("title") as? String ?: ""
            val content = sonDuyuru?.get("content") as? String ?: ""
            val konum = sonDuyuru?.get("Konum") as? String ?: ""
            val kanGrubu = sonDuyuru?.get("kanGrubu") as? String ?: ""
            val tweetIcerigi = "$konum: $content: $kanGrubu: $title"

            try {
                val status: Status = twitter.updateStatus(tweetIcerigi)
                println("Tweet başarıyla gönderildi: ${status.text}")
            } catch (e: TwitterException) {
                e.printStackTrace()
                println("Tweet gönderilirken bir hata oluştu: ${e.message}")
            }

            println("Duyurular:")
            println(tweetIcerigi)
            println("Tweetler başarıyla gönderildi.")
        }
    }
}
