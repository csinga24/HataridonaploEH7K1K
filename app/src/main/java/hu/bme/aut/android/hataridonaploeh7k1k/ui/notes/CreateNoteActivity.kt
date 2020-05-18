package hu.bme.aut.android.hataridonaploeh7k1k.ui.notes

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import hu.bme.aut.android.hataridonaploeh7k1k.R
import hu.bme.aut.android.hataridonaploeh7k1k.data.Note
import hu.bme.aut.android.hataridonaploeh7k1k.extension.showText
import hu.bme.aut.android.hataridonaploeh7k1k.extension.validateNonEmpty
import kotlinx.android.synthetic.main.activity_create_note.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.URLEncoder
import java.util.*

class CreateNoteActivity : AppCompatActivity() {

    companion object {
        private const val MAKE_IMAGE = 1
        private const val PICK_IMAGE = 2
    }

    private var readyText:String = "Új jegyzet hozzáadva!"
    private var userId: String = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser!!.uid
    private var keyOfNoteToModify: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

        btnAddNewNote.setOnClickListener { sendClick() }
        btnPicture.setOnClickListener { attachPicClick() }
        btnCamera.setOnClickListener { makePicClick() }

        if(intent.getStringExtra("note title") != null) {
            getExtras(intent)
        }
        rgPriority.checkedRadioButtonId
    }

    @SuppressLint("SetTextI18n")
    private fun getExtras(intent: Intent){
        keyOfNoteToModify = intent.getStringExtra("note key")
        val title: String? = intent.getStringExtra("note title")
        note_title.text = Editable.Factory.getInstance().newEditable(title)
        val desc: String? = intent.getStringExtra("note desc")
        note_desc.text = Editable.Factory.getInstance().newEditable(desc)

        val img: String? = intent.getStringExtra("note img")
        if(img != null) {
            if (img.isNullOrBlank()) {
                imvPicture.visibility = View.GONE
            } else {
                Glide.with(this).load(img).into(imvPicture)
                imvPicture.visibility = View.VISIBLE
            }
        }

        when (intent.getStringExtra("note priority")){
            Note.Priority.HIGH.name -> radioButtonHigh.isChecked = true
            Note.Priority.MEDIUM.name -> radioButtonMedium.isChecked = true
            Note.Priority.LOW.name -> radioButtonLow.isChecked = true
            else -> radioButtonLow.isChecked = true
        }

        tvAddNote.text = "Jegyzet módosítása"
        btnAddNewNote.text = "Módosítás!"
        readyText = "Jegyzet módosítva!"

    }

    private  fun makePicClick(){
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, MAKE_IMAGE)
    }

    private fun attachPicClick() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK || data == null) {
            return
        }

        if (requestCode == MAKE_IMAGE) {
            val imageBitmap = data.extras?.get("data") as? Bitmap ?: return
            imvPicture.setImageBitmap(imageBitmap)
            imvPicture.visibility = View.VISIBLE
        }
        if (requestCode == PICK_IMAGE) {
            var selectedImageUri = data.getData();
            val path: String? = selectedImageUri?.let { getPathFromURI(it) };
            if (path != null) {
                val f: File = File(path);
                selectedImageUri = Uri.fromFile(f);
            }
            imvPicture.setImageURI(selectedImageUri);
            imvPicture.visibility = View.VISIBLE
        }
    }

    fun getPathFromURI(contentUri: Uri): String? {
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor =
            contentResolver.query(contentUri, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            res = cursor.getString(column_index)
        }
        cursor.close()
        return res
    }

    private fun validateForm() = note_title.validateNonEmpty()

    private fun sendClick() {
        if (!validateForm()) {
            return
        }
        if(imvPicture.visibility != View.VISIBLE) {
            uploadNote(null)
        }
        else {
            val bitmap: Bitmap = (imvPicture.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageInBytes = baos.toByteArray()

            val storageReference = FirebaseStorage.getInstance().reference
            val newImageName = URLEncoder.encode(UUID.randomUUID().toString(), "UTF-8") + ".jpg"
            val newImageRef = storageReference.child("images/$newImageName")

            newImageRef.putBytes(imageInBytes)
                .addOnFailureListener { }
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let { throw it }
                    }
                    newImageRef.downloadUrl
                }
                .addOnSuccessListener { downloadUri ->
                    uploadNote(downloadUri.toString())
                }
        }
    }

    private fun uploadNote(imageUrl: String?){
        if(keyOfNoteToModify == null) {
            val key = FirebaseDatabase.getInstance().reference.child("notes").push().key ?: return
            val newNote = Note(key, userId, note_title.text.toString(), checkPriority(), note_desc.text.toString(), imageUrl)
            FirebaseDatabase.getInstance().reference
                .child("notes")
                .child(key)
                .setValue(newNote)
                .addOnCompleteListener {
                   readyText.showText(this)
                    setResult(Activity.RESULT_OK)
                    finish()
                }
        }
        else{
            val modifiedNote = Note(keyOfNoteToModify, userId, note_title.text.toString(), checkPriority(), note_desc.text.toString(), imageUrl)
            FirebaseDatabase.getInstance().reference
                .child("notes")
                .child(keyOfNoteToModify!!)
                .setValue(modifiedNote)
                .addOnCompleteListener {
                    readyText.showText(this)
                    finish()
                }

        }
    }

    private fun checkPriority(): Note.Priority {
        val priority: Note.Priority
        when (rgPriority.checkedRadioButtonId) {
            radioButtonHigh.id -> priority = Note.Priority.HIGH
            radioButtonMedium.id -> priority = Note.Priority.MEDIUM
            radioButtonLow.id -> priority = Note.Priority.LOW
            else -> priority = Note.Priority.LOW
        }
        return priority
    }

    override fun onBackPressed() {
        if(keyOfNoteToModify != null) {
            sendClick()
        }
        else{
            super.onBackPressed()
        }
    }

}
