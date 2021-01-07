package com.example.stationer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stationer.AdminActivity;
import com.example.stationer.LoginActivity;
import com.example.stationer.R;

public class AdminFragment extends Fragment {

    private EditText accountText;

    private EditText passwordText;

    private Button login;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_frag, container, false);
        accountText = view.findViewById(R.id.account_admin);
        passwordText = view.findViewById(R.id.password_admin);
        login = view.findViewById(R.id.login_admin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountText.getText().toString();
                String password = passwordText.getText().toString();
                if(account.equals("admin") && password.equals("123456")){
                    Intent intent = new Intent(getActivity(), AdminActivity.class);
                    startActivity(intent);
                    LoginActivity loginActivity = (LoginActivity) getActivity();
                    loginActivity.finish();
                } else {
                    Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
