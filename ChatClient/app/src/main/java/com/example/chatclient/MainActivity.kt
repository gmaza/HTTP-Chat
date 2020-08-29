package com.example.chatclient

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.chatclient.login.LoginPresenter
import com.example.chatclient.login.LoginView
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity(), LoginView {

    private val presenter = LoginPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_main)

        button_save.setOnClickListener {
            presenter.register(txt_register_name.text.toString(), txt_register_profession.text.toString())
        }

        presenter.checkConnection()
    }





    override fun showLoginForm() {
        loading.visibility = View.GONE
        mainContainer.visibility = View.VISIBLE
    }
}
