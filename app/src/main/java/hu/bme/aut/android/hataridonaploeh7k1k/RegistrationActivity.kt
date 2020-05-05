package hu.bme.aut.android.hataridonaploeh7k1k

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import hu.bme.aut.android.hataridonaploeh7k1k.extension.validateNonEmpty
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        firebaseAuth = FirebaseAuth.getInstance()
        btnReg.setOnClickListener { registerClick() }
    }

    private fun validateForm() = etRegName.validateNonEmpty() && etRegEmail.validateNonEmpty() && etRegPassword.validateNonEmpty()

    fun registerClick() {
        if (!validateForm()) {
            return
        }

        firebaseAuth
            .createUserWithEmailAndPassword(etRegEmail.text.toString(), etRegPassword.text.toString())
            .addOnSuccessListener { result ->
                val firebaseUser = result.user
                val profileChangeRequest = UserProfileChangeRequest.Builder()
                    .setDisplayName(etRegName.text.toString())
                    .build()
                firebaseUser?.updateProfile(profileChangeRequest)

                Toast.makeText(this, "Regisztráció sikeres", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }

        val returnIntent = Intent()
        returnIntent.putExtra("e-mail", etRegEmail.text.toString())
        returnIntent.putExtra("password", etRegPassword.text.toString())
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }
}
