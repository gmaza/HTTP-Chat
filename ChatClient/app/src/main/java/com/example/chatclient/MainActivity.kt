package com.example.chatclient

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.chatclient.login.LoginPresenter
import com.example.chatclient.login.LoginView
import com.example.chatclient.utils.SoftInputAssist
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity(), LoginView {

    private val presenter = LoginPresenter(this)
    private lateinit var softInputAssist: SoftInputAssist

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_main)
        softInputAssist = SoftInputAssist(this)

        button_save.setOnClickListener {
            presenter.register(txt_register_name.text.toString(), txt_register_profession.text.toString())
        }

        presenter.checkConnection()
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