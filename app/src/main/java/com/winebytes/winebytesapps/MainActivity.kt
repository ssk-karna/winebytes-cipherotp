package com.winebytes.winebytesapps

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.winebytes.cipherotp.OtpBoxView;

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val otpBoxView = findViewById<OtpBoxView>(R.id.otpBoxView)
        val btnFetchOtp = findViewById<Button>(R.id.btn_fetch_otp)
        val tvOtp = findViewById<TextView>(R.id.tv_otp)
        otpBoxView.setBorderColor(R.color.green)
        otpBoxView.setOtpBoxCount(6)

        btnFetchOtp.setOnClickListener {
            val otp = otpBoxView.getOtp()
            tvOtp.setText(otp)
            // Handle the OTP value as needed
        }


    }

}