package com.example.bilsem
import android.content.Intent
import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Girisyap : Activity() {

    private lateinit var etEmail: EditText
    private lateinit var etParola: EditText
    private lateinit var btnGirisYap: Button

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.girisyap)

        etEmail = findViewById(R.id.et_email)
        etParola = findViewById(R.id.et_parola)
        btnGirisYap = findViewById(R.id.btn_giris_yap)

        firebaseAuth = FirebaseAuth.getInstance()

        btnGirisYap.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val parola = etParola.text.toString().trim()

            if (email.isNotEmpty() && parola.isNotEmpty()) {
                girisYap(email, parola)
            } else {
                Toast.makeText(this, "E-posta ve parola boş olamaz", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun girisYap(email: String, parola: String) {
        firebaseAuth.signInWithEmailAndPassword(email, parola)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Giriş başarılı olduğunda MainActivity'e yönlendir
                    val intent = Intent(this, AnnouncementsActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Giriş yapılamadı. E-posta veya parola hatalı.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}