package hu.bme.aut.android.hataridonaploeh7k1k

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import hu.bme.aut.android.hataridonaploeh7k1k.extension.validateNonEmpty
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private val REG_REQUEST = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()
        btnRegister.setOnClickListener { registerClick() }
        btnLogin.setOnClickListener { loginClick() }
    }

    private fun validateForm() = etEmail.validateNonEmpty() && etPassword.validateNonEmpty()

    private fun registerClick() {
        val registrationIntent = Intent(this, RegistrationActivity::class.java)
        startActivityForResult(registrationIntent, REG_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REG_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val e_mail: String = data?.getStringExtra("e-mail") as String
                etEmail.text = Editable.Factory.getInstance().newEditable(e_mail)

                val password: String = data.getStringExtra("password") as String
                etPassword.text = Editable.Factory.getInstance().newEditable(password)
                if(validateForm()){
                    signingIn()
                }
            }
        }
    }

    private fun loginClick() {
        if (!validateForm()) {
            return
        }
        signingIn()
    }

    private fun signingIn(){
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


