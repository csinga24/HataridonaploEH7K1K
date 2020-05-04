package hu.bme.aut.android.hataridonaploeh7k1k.menu

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import hu.bme.aut.android.hataridonaploeh7k1k.R

class ProfilActivity : AppCompatActivity() {

    private val firebaseUser: FirebaseUser? = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        val user_name = findViewById<TextView>(R.id.profil_name)
        user_name.setText(firebaseUser?.displayName)
        val user_email = findViewById<TextView>(R.id.profil_email)
        user_email.setText(firebaseUser?.email)

    }
}