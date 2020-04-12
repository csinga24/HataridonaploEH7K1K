package hu.bme.aut.android.hataridonaploeh7k1k

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import hu.bme.aut.android.hataridonaploeh7k1k.extension.validateNonEmpty
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()
        btnRegister.setOnClickListener { registerClick() }
        btnLogin.setOnClickListener { loginClick() }
    }

    private fun validateForm() = etEmail.validateNonEmpty() && etPassword.validateNonEmpty()

    @SuppressLint("ShowToast")
    private fun registerClick() {
        if (!validateForm()) {
            return
        }

        firebaseAuth
            .createUserWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
            .addOnSuccessListener { result ->
                val firebaseUser = result.user
                val profileChangeRequest = UserProfileChangeRequest.Builder()
                    .setDisplayName(firebaseUser?.email?.substringBefore('@'))
                    .build()
                firebaseUser?.updateProfile(profileChangeRequest)

                Toast.makeText(this,"Registration successful", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        signingIn()
    }

    private fun loginClick() {
        if (!validateForm()) {
            return
        }
        signingIn()
    }

    fun signingIn(){
        firebaseAuth
            .signInWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
            .addOnSuccessListener {
                startActivity(Intent(this@LoginActivity, MenuActivity::class.java))
                finish()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }
}


