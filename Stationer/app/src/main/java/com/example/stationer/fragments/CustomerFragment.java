package com.example.stationer.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stationer.Data.Data;
import com.example.stationer.LoginActivity;
import com.example.stationer.MainActivity;
import com.example.stationer.R;
import com.example.stationer.RegisterActivity;
import com.example.stationer.database.MyDatabaseHelper;

import java.util.List;

public class CustomerFragment extends Fragment {

    private EditText accountText;

    private EditText passwordText;

    private Button login;

    private TextView register;

    private MyDatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_frag, container, false);
        accountText = (EditText) view.findViewById(R.id.account_text);
        passwordText = (EditText) view.findViewById(R.id.password_text);
        register = (TextView) view.findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        dbHelper = new MyDatabaseHelper(getActivity(), "Stationer.db", null, 1);
        login = (Button) view.findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Warning");
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String accountContent = accountText.getText().toString();
                String passwordContent = passwordText.getText().toString();
                boolean success = false;

                if("".equals(accountContent) || "".equals(passwordContent)){
                    Toast.makeText(getActivity(), "尚未输入内容！", Toast.LENGTH_SHORT).show();
                } else {
                    Cursor cursor = db.rawQuery("select * from Customer", null);
                    if(cursor.moveToFirst()){
                        do{
                            String account = cursor.getString(cursor.getColumnIndex("account"));
                            String password = cursor.getString(cursor.getColumnIndex("password"));
                            if(account.equals(accountContent) && password.equals(passwordContent)){
                                success = true;
                                break;
                            }
                        } while(cursor.moveToNext());
                    }

                    if(!success){
                        builder.setMessage("登录失败！" + "\n" + "(没有账号？点击下方注册)");
                        builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.show();
                    } else{
                        Intent intent = new Intent(getActivity(), MainActivity.class);
//                        Data.setAccount(accountContent);
                        intent.putExtra("account", accountContent);
                        startActivity(intent);
                        LoginActivity loginActivity = (LoginActivity) getActivity();
                        loginActivity.finish();
                    }
                }
            }
        });

        return view;
    }
}
