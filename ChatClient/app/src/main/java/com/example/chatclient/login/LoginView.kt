package com.example.chatclient.login

interface LoginView {
    fun showLoginForm()

    fun openHistoryView(name: String)

    fun showToast(message: String)
}