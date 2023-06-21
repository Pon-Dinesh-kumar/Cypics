package com.example.cypics;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

public class LoginProgressDialog extends Dialog {
    private ProgressBar mProgressBar;

    public LoginProgressDialog(Context context) {
        super(context);
        setContentView(R.layout.login_progress_dialog);

        mProgressBar = findViewById(R.id.progress_bar);
    }

    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        show();
    }

    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
        dismiss();
    }
}
