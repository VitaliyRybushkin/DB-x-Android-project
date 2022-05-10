package com.example.mydbas;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserActivity extends AppCompatActivity {

    private EditText nameBox;
    private EditText yearBox;
    private Button delButton;

    private DatabaseAdapter adapter;
    private long userId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        nameBox = findViewById(R.id.name);
        yearBox = findViewById(R.id.year);
        delButton = findViewById(R.id.deleteButton);
        adapter = new DatabaseAdapter(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getLong("id");
        }
        // если 0, то добавление
        if (userId > 0) {
            // получаем элемент по id из бд
            adapter.open();
            User user = adapter.getUser(userId);
            nameBox.setText(user.getName());
            yearBox.setText(String.valueOf(user.getYear()));
            adapter.close();
        } else {
            // скрываем кнопку удаления
            delButton.setVisibility(View.GONE);
        }
    }

    public void save(View view){

        String name = nameBox.getText().toString();
        String yearS = yearBox.getText().toString();
        if(!(yearS.matches("[-+]?\\d+"))){
            Toast.makeText(getApplicationContext(),"Введены не цифры",Toast.LENGTH_SHORT).show();
            recreate();
        }else {
            int year = Integer.parseInt(yearS);
            System.out.println("\n\n\n\n\n+++++++++++++++" + year);
            if (year < 0) {
                Toast.makeText(getApplicationContext(), "Возраст не может быть отрицательным", Toast.LENGTH_SHORT).show();
                recreate();
            } else {
                User user = new User(userId, name, year);


                adapter.open();
                if (userId > 0) {
                    adapter.update(user);
                } else {
                    adapter.insert(user);
                }
                adapter.close();
                Toast.makeText(getApplicationContext(),"Данные о пользователе обновлены",Toast.LENGTH_SHORT).show();
                goHome();
            }
        }
    }
    public void delete(View view){

        adapter.open();
        adapter.delete(userId);
        adapter.close();
        Toast.makeText(getApplicationContext(),"Пользователь удален",Toast.LENGTH_SHORT).show();
        goHome();
    }
    private void goHome(){
        // переход к главной activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}