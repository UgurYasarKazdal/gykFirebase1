package com.uykazdal.gezginapp.helper;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.uykazdal.gezginapp.R;

public class ViewHelper {

    public static Dialog getDialog(Context context, String mesaj, String baslik) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_progress);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);

        TextView tvTitle = dialog.findViewById(R.id.tv_progress_title);
        TextView tvContent = dialog.findViewById(R.id.tv_progress_content);

        tvContent.setText(mesaj);
        tvTitle.setText(baslik);

        return dialog;
    }
}
