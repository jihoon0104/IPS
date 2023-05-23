package com.example.ips2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    private ListView menuList;  //메뉴들을 출력할 리스트뷰
    private TextView text_resName, text_resAddress, text_resPhoneNum;
    private String ResName, ResAddress, ResID, ResPhoneNum;

    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent(); //전달할 데이터를 받을 Intent
        ResName = intent.getStringExtra("Selected ResName");
        ResAddress = intent.getStringExtra("Selected ResAddress");
        ResID = intent.getStringExtra("Selected ResID");
        ResPhoneNum = intent.getStringExtra("Selected ResPhoneNum");

        text_resName = findViewById(R.id.ResName);
        text_resName.setText("Restaurant Name: " + ResName);
        text_resAddress = findViewById(R.id.ResAddress);
        text_resAddress.setText("Address: " + ResAddress);
        text_resPhoneNum = findViewById(R.id.ResPhoneNum);
        text_resPhoneNum.setText("Phone Number: " + ResPhoneNum);

        menuList = findViewById(R.id.Menu);

        // assets에서 "menu.json" 파일 읽기
        String jsonString = readJsonFromAssets("menu.json");

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            List<String> menuDataList = new ArrayList<>();

            // "menu.json"의 데이터를 파싱하여 ResID가 선택된 식당의 ResID와 일치하는 경우에만 메뉴 이름을 가져와서 리스트에 추가
            int count = 0; // 가져온 데이터 개수를 카운트하기 위한 변수
            for (int i = 0; i < jsonArray.length(); i++) {
                if (count >= 30) {
                    break; // 최대 30개까지만 가져오기 위해 반복문 종료
                }

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String menuResID = jsonObject.getString("ResID");

                if (menuResID.equals(ResID)) {
                    String menuName = jsonObject.getString("MenuName");
                    menuDataList.add(menuName);
                    count++; // 데이터 개수 카운트 증가
                }
            }

            // 리스트뷰에 데이터를 표시하기 위해 ArrayAdapter 사용
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menuDataList);
            menuList.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // assets 폴더에서 파일을 읽어서 문자열로 반환하는 메서드
    private String readJsonFromAssets(String fileName) {
        try {
            InputStream inputStream = getAssets().open(fileName);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

