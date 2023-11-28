package com.example.bilsem
import android.os.Bundle
import android.app.Activity
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.*
import com.google.firebase.database.ValueEventListener
import android.widget.LinearLayout
import android.graphics.Color
import android.graphics.Typeface


class AnnouncementsActivity : Activity() {

    private lateinit var textViewAnnouncements: TextView
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.duyuruoku)
        database = FirebaseDatabase.getInstance().reference.child("announcements")

        textViewAnnouncements = findViewById(R.id.textViewAnnouncements)
        val announcementsContainer = findViewById<LinearLayout>(R.id.announcementsContainer)

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                announcementsContainer.removeAllViews() // Önceki duyuruları temizle

                for (snapshot in dataSnapshot.children) {
                    val konum = snapshot.child("konum").value.toString()
                    val content = snapshot.child("content").value.toString()
                    val kanGrubu = snapshot.child("kanGrubu").value.toString()
                    val title = snapshot.child("title").value.toString()

                    val textView = TextView(this@AnnouncementsActivity)
                    textView.text = "$konum $content $kanGrubu $title"
                    textView.textSize = 24f
                    textView.setTextColor(Color.parseColor("#D39D3F"))
                    textView.setTypeface(null, Typeface.BOLD_ITALIC)

                    announcementsContainer.addView(textView)
                }

                textViewAnnouncements.text = dataSnapshot.childrenCount.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Hata durumunda yapılacak işlemler
            }
        })

        val buttonGeri = findViewById<Button>(R.id.buttonGeri)

        buttonGeri.setOnClickListener {
            finish() // Geri tuşuna basılmış gibi davranır
        }
    }
}