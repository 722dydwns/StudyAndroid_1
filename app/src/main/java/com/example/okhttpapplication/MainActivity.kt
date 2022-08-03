package com.example.okhttpapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.okhttpapplication.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() { //'메인' 액티비티

    //뷰 바인딩 설정
    lateinit var mainActivityBinding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //뷰 바인딩 설정
        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)
        //버튼 이벤트 처리
        mainActivityBinding.connectBtn.setOnClickListener{
           thread{ //쓰레드로 동작해야 네트워크 관련 처리 가능
               //localhost 부분에 서버 Ip 주소 담기
               val site = "http://172.30.1.9:8080/App3_CommunityServer/test.jsp"

               //okHttp 객체
               val client = OkHttpClient()

               val request = Request.Builder().url(site).get().build()
               val response = client.newCall(request).execute() //접속됨

               // 만약 서버와 통신 성공 시
               if(response.isSuccessful == true) {
                   val result = response.body?.string() //서버로부터 받은 데이터를 받아올 수 있다.
                   runOnUiThread {
                       mainActivityBinding.resultText.text = result
                   }
               }
           }
        }
    }
}