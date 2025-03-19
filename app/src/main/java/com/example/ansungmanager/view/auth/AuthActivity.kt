package com.example.ansungmanager.view.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.ansungmanager.databinding.ActivityAuthBinding
import com.example.ansungmanager.retrofit.RetrofitClient
import com.example.ansungmanager.retrofit.RetrofitService
import com.example.ansungmanager.view.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAuthBinding;
    private lateinit var service: RetrofitService;
    override fun onCreate(savedInstanceState: Bundle?) {
        service = RetrofitClient.service;
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAuthBinding.inflate(layoutInflater);
        setContentView(binding.root)
        binding.start.setOnClickListener({
            val name :String = binding.name.text.toString();
            val password : String = binding.password.text.toString();
            service.getAuth(name,password).enqueue(object : Callback<Void>{
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if(response.isSuccessful){
                        val authHeader = response.headers()["Authorization"]
                        if (authHeader != null) {
                            RetrofitClient.setAccessToken(authHeader);
                        }
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(applicationContext,"이름 과 비밀번호를 다시 확인해 주세요",Toast.LENGTH_SHORT).show();
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    t.printStackTrace();
                }
            })
        })
    }
}