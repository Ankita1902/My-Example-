package com.example.jiexunxu.tinderapp


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class ErrorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)
        initUI()
    }

    override fun onBackPressed() {
        goBackToMain()
    }

    private fun initUI() {
        title = "Oops..."
        val msg = intent.getStringExtra("message")
        val errorText = findViewById(R.id.errorBackButton)  as TextView
        errorText.setText(msg)
        val backToMainButton = findViewById(R.id.errorBackButton) as Button
        backToMainButton.setOnClickListener(View.OnClickListener { goBackToMain() })
    }

    private fun goBackToMain() {
        val intent = Intent(this@ErrorActivity, MainActivity::class.java)
        this@ErrorActivity.startActivity(intent)
    }

    companion object {
        internal fun start(context: Context, msg: String) {
            val intent = Intent(context, ErrorActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("message", msg)
            context.startActivity(intent)
        }
    }
}
