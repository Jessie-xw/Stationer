package com.example.stationer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stationer.database.MyDatabaseHelper;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private EditText setAccount;

    private EditText setPass;

    private EditText confirmPass;

    private Button register;

    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_acitivity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("注册账号");

        dbHelper = new MyDatabaseHelper(this, "Stationer.db", null, 1);
        setAccount = findViewById(R.id.set_account);
        setPass = findViewById(R.id.set_pass);
        confirmPass = findViewById(R.id.confirm_pass);
        register = findViewById(R.id.signIn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String accountContent = setAccount.getText().toString();
                String passContent = setPass.getText().toString();
                String confirmContent = confirmPass.getText().toString();
                boolean success = true;
                int errorType = 0;

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("Warning");
                builder.setCancelable(false);//不可取消

                if ("".equals(accountContent) || "".equals(passContent) || "".equals(confirmContent)) {
                    Toast.makeText(RegisterActivity.this, "输入不完整！", Toast.LENGTH_SHORT).show();
                } else {
                    Cursor cursor = db.rawQuery("select * from Customer", null);
                    if (cursor.moveToFirst()) {
                        do {
                            String account = cursor.getString(cursor.getColumnIndex("account"));
                            if (account.equals(accountContent)) {
                                success = false;
                                break;
                            }
                        } while (cursor.moveToNext());
                    }

                    if (success) {
                        if (passContent.equals(confirmContent) == false){
                            errorType = 1;
                        }
                        if (errorType == 0 && (passContent.length() < 6 || passContent.length() > 8)) {
                            errorType = 2;
                        }
                        if (errorType == 0) {
                            for (int i = 0; i < passContent.length(); i++) {
                                if (passContent.charAt(i) < '0' || passContent.charAt(i) > '9') {//不是数字要看是不是字母
                                    if (passContent.charAt(i) < 'a' || passContent.charAt(i) > 'z') {
                                        if (passContent.charAt(i) < 'A' || passContent.charAt(i) > 'Z') {
                                            errorType = 2;
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        if (errorType == 0) {
                            ContentValues values = new ContentValues();
                            values.put("account", accountContent);
                            values.put("password", passContent);
                            db.insert("Customer", null, values);
                            builder.setMessage("账号创建成功！现在去登录吧！");
                            builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            builder.show();
                        } else {
                            builder.setMessage("密码输入不正确！");
                            builder.setPositiveButton("重新输入", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setPass.setText("");
                                    confirmPass.setText("");
                                }
                            });
                            builder.show();
                        }
                    }else{
                        builder.setMessage("账号已存在！");
                        builder.setPositiveButton("重新输入", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();
                    }
                }
            }
        });
    }
}