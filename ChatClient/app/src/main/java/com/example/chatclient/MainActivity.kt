package com.example.chatclient

import android.Manifest
import android.R.attr
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatclient.login.LoginPresenter
import com.example.chatclient.login.LoginView
import com.example.chatclient.utils.SoftInputAssist
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity(), LoginView {

    private val presenter = LoginPresenter(this)
    private lateinit var softInputAssist: SoftInputAssist
    private var imgBase64=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_main)
        softInputAssist = SoftInputAssist(this)

        button_save.setOnClickListener {
            presenter.register(txt_register_name.text.toString(), txt_register_profession.text.toString(), imgBase64)
        }

        presenter.checkConnection()

        user_photo.setOnClickListener {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else{
                    //permission already granted
                    pickImageFromGallery();
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            user_photo.setImageURI(data?.data)

            val source = ImageDecoder.createSource(this.contentResolver, data?.data!!)
            val v = ImageDecoder.decodeBitmap(source)
            val byteArrayOutputStream = ByteArrayOutputStream()
            v.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
            imgBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun showLoginForm() {
        loading.visibility = View.GONE
        mainContainer.visibility = View.VISIBLE
    }

    override fun openHistoryView(name: String) {
        val intent = Intent(this, ChatsHistory::class.java).apply {
            putExtra("name", name)
        }
        startActivity(intent)
    }

    override fun showToast(message: String) {
        runOnUiThread {
            val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_LONG)
            toast.show()
        }
    }

    override fun onResume() {
        softInputAssist.onResume()
        super.onResume()
    }

    override fun onPause() {
        softInputAssist.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        softInputAssist.onDestroy()
        super.onDestroy()
    }
}