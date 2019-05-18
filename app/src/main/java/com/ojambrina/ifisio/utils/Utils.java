package com.ojambrina.ifisio.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Utils {

    public static ProgressDialog showProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);

        return progressDialog;
    }

    public static Dialog openDialog(Context context, int layout) {
        Dialog dialog = new Dialog(context);

        dialog.setContentView(layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }

    public static Spanned fromHtml(String text) {
        Spanned textSpanned;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textSpanned = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
            Log.d("STRING SPANNED", textSpanned.toString());
        } else {
            textSpanned = Html.fromHtml(text);
        }
        return textSpanned;
    }

    public static void loadGlide(@NonNull Context context, @NonNull String url, @NonNull ImageView view) {
        Activity activity = (Activity) context;
        if (!activity.isFinishing() && view != null) {
            Glide.with(context)
                    .load(url)
                    .into(view);
        }
    }

    public static void configToolbar(AppCompatActivity context, Toolbar toolbar) {
        context.setSupportActionBar(toolbar);
        context.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context.getSupportActionBar().setDisplayShowHomeEnabled(true);
        context.getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(context.getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        /*final Drawable customArrow = context.getResources().getDrawable(R.drawable.back);
        context.getSupportActionBar().setHomeAsUpIndicator(customArrow);*/

    }
}
