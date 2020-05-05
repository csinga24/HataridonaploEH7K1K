package hu.bme.aut.android.hataridonaploeh7k1k.menu

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import hu.bme.aut.android.hataridonaploeh7k1k.R

class ProfilActivity : AppCompatActivity() {

    private val firebaseUser: FirebaseUser? = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        val userName = findViewById<TextView>(R.id.profil_name)
        userName.text = "Név:  " + firebaseUser?.displayName
        val userEmail = findViewById<TextView>(R.id.profil_email)
        userEmail.text = "E-mail: " + firebaseUser?.email

    }
}