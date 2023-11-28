package com.example.bilsem
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.app.Activity


class Kayitol : Activity() {
    private lateinit var etParola: EditText
    private lateinit var etKullaniciAdi: EditText
    private lateinit var etSehir: EditText
    private lateinit var etKanGrubu: EditText
    private lateinit var etEmail: EditText

    private lateinit var btnKayitOl: Button

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kayit_ol)

        etParola = findViewById(R.id.et_parola)
        etKullaniciAdi = findViewById(R.id.et_kullanici_adi)
        etSehir = findViewById(R.id.et_sehir)
        etKanGrubu = findViewById(R.id.et_kan_grubu)
        etEmail = findViewById(R.id.et_email)

        btnKayitOl = findViewById(R.id.btn_kayit_ol)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()

        btnKayitOl.setOnClickListener {
            val parola = etParola.text.toString()
            val kullaniciAdi = etKullaniciAdi.text.toString()
            val sehir = etSehir.text.toString()
            val kanGrubu = etKanGrubu.text.toString()
            val email = etEmail.text.toString()

            if (parola.isEmpty()) {
                Toast.makeText(this, "Lütfen bir şifre girin.", Toast.LENGTH_SHORT).show()
            } else {
                kayitOlFirebase(parola, kullaniciAdi, sehir, kanGrubu, email)
            }
        }
    }

    private fun kayitOlFirebase(parola: String, kullaniciAdi: String, sehir: String, kanGrubu: String, email: String) {
        mAuth.createUserWithEmailAndPassword(email, parola)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = mAuth.currentUser?.uid

                    val kullaniciBilgileri = HashMap<String, Any>()
                    kullaniciBilgileri["kullaniciAdi"] = kullaniciAdi
                    kullaniciBilgileri["sehir"] = sehir
                    kullaniciBilgileri["kanGrubu"] = kanGrubu
                    kullaniciBilgileri["email"] = email

                    mDatabase.reference.child("kullanicilar").child(userId ?: "").setValue(kullaniciBilgileri)
                        .addOnCompleteListener { databaseTask ->
                            if (databaseTask.isSuccessful) {
                                Toast.makeText(this, "Kaydınız başarıyla tamamlandı.", Toast.LENGTH_SHORT).show()

                                val intent = Intent(this, Anasayfa::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, "Kayıt sırasında bir hata oluştu.", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Kayıt sırasında bir hata oluştu.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}