## 🟦 43강. 서버 프로그래밍 준비

### ▶️ 서버 프로그래밍 개요

- 안드로이드 애플리케이션과 통신할 서버 프로그램 구현 위한 준비 작업 수행
- 서버는 jsp, spring, nodejs, python 등 웹 서비스를 위해 **백 엔드 개발을 할 수 있는 것 중 편한 것 사용**
- 여기서는 jsp를 활용한다.

### **🟧 설치 소프트웨어**

- Java Development Kit : 8버전
- Eclipse
- Apache-Tomcat : 9버전
- MySQL : 데이터베이스

## 🟦 44강. 데이터베이스 생성

### ▶️ 데이터베이스 테이블 구조

**1) user_table : 사용자 회원 정보 테이블**

![회원.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/70e83b3d-c258-4d8b-bf8e-deeaf179c8f6/%ED%9A%8C%EC%9B%90.png)

**2) board_table : 게시판 정보**

![개시판정보.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/2afa45ea-98c2-462a-84b9-83532bfffd53/%EA%B0%9C%EC%8B%9C%ED%8C%90%EC%A0%95%EB%B3%B4.png)

**3) content_table : 게시글 내용 정보** 

![s내용.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/adc0f7b0-68f8-4ebf-b824-2de4565faea8/s%EB%82%B4%EC%9A%A9.png)

### **🟧 전체 테이블 구조 관계도**

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/657094b1-fe4c-4a67-8b8e-1860b4c2ac3e/Untitled.png)

### **🟧 MySQL 에 Sql 쿼리문 작성**

```sql
create database app3_community_db;

use app3_community_db;

create table user_table(
user_idx int not null primary key auto_increment,
user_id varchar(100) not null unique,
user_pw varchar(100) not null,
user_autologin int not null check(user_autologin in(0, 1)),
user_nick_name varchar(100) not null unique
);

create table board_table(
board_idx int not null primary key auto_increment,
board_name varchar(100) not null unique
);

insert into board_table (board_name) values ("게시판1");
insert into board_table (board_name) values ("게시판2");
insert into board_table (board_name) values ("게시판3");
insert into board_table (board_name) values ("게시판4");

create table content_table(
content_idx int not null primary key auto_increment,
content_board_idx int not null references board_table(board_idx),
content_writer_idx int not null references user_table(user_idx),
content_subject varchar(500) not null,
content_write_date datetime not null,
content_text longtext not null,
content_image varchar(500)
);

commit;
```

## 🟦 45강. 이클립스 설정

### ▶️ 이클립스 설정

- 서버 프로그램 구현을 위해 사용할 Eclipse 기본 설정 수행
- Apache-Tomcat 서버와의 연동 설정을 수행
- 프로젝트를 생성하고 실행 테스트를 수행

## 🟦 46강. OkHttp 라이브러리 사용

### ▶️ OkHttp 라이브러리

- REST API, HTTP 통신을 간편하게 구현할 수 있도록 다양한 기능 제공하는 라이브러리

### **🟧 사용을 위한 세팅**

**1) 뷰 바인딩 설정**

- Module 수준의 build.gradle 파일에 viewBinding 설정 true 준다.

```php
buildFeatures{
viewBinding = true
}
```

**2) OkHttp라이브러리 사용을 위해 dependencies에 의존 추가한다.**

```php
implementation 'com.squareup.okhttp3:okhttp:4.9.0'
```

**3) 네트워크 사용을 위해 ‘인터넷 권한’을 추가한다.**

◾ AndroidManifest.xml 

`<uses-permission android:name = "android.permission.INTERNET"/>`

**4) AndroidManifest.xml 에 다음을 추가**

- Http 사용 시, 보안 문제 때문에 다음을 추가한다.

`android:usesCleartextTraffic="true"`

**5) 네트워크 관련 처리는 반드시 ‘쓰레드’로 동작 처리 필수**

◾ MainActivity.kt 

```kotlin
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
thread{//쓰레드로 동작해야 네트워크 관련 처리 가능
               //localhost 부분에 서버 Ip 주소 담기
               val site = "http://172.30.1.9:8080/App3_CommunityServer/test.jsp"

               //okHttp 객체
               val client = OkHttpClient()

               val request = Request.Builder().url(site).get().build()
               val response = client.newCall(request).execute() //접속됨

               // 만약 서버와 통신 성공 시
               if(response.isSuccessful == true) {
                   val result = response.body?.string() //서버로부터 받은 데이터를 받아올 수 있다.
                   runOnUiThread{
mainActivityBinding.resultText.text= result
}
}
}
        }
}
}
```

**→ 여기서 서버 연동할 site 주소 속 localhost는 자신의 컴퓨터 ip 주소로 대체해야 한다.**

**→ 명령 프롬포트에서 ipconfig 명령어 입력 시 등장하는 ip 주소 가져올 것** 

![명령 아잉피 주소.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/eee5614a-0871-4cea-aa89-0e6e61215531/%EB%AA%85%EB%A0%B9_%EC%95%84%EC%9E%89%ED%94%BC_%EC%A3%BC%EC%86%8C.png)

### **🟧 최종 모습**

**1) 서버 위에 올라간 test.jsp 파일 속 내용물**

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/99bc964a-f571-4ccb-8641-f3ffbaf9a885/Untitled.png)

**2) 위 url 주소에 ip주소 혼합시켜서 ‘안드로이드 앱’에 데이터 가져옴**

- 사용자가 버튼 클릭 시, 가져올 수 있도록 이벤트 처리되어 있다.
