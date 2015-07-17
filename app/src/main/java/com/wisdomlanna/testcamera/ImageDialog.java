package com.wisdomlanna.testcamera;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by suraphol on 7/10/15 AD.
 */
public class ImageDialog extends DialogFragment {

    private ImageView imShow;
    private String path, id;
    private Bitmap imageSelected;
    private Button btnDel;
    int counterPic;

    public static ImageDialog newInstance(String path, String id, int _counterPic) {
        ImageDialog imageDialog = new ImageDialog();
        imageDialog.path = path;
        imageDialog.id = id;
        imageDialog.counterPic = _counterPic;
        return imageDialog;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = getActivity().getLayoutInflater().inflate(R.layout.image_dialog, null);
        v.findViewById(R.id.btnDeleteIm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        imageSelected = BitmapFactory.decodeFile(path + "/" + id + ".png");
        imShow = (ImageView) v.findViewById(R.id.imShow);
        imShow.setImageBitmap(imageSelected);
        btnDel = (Button) v.findViewById(R.id.btnDeleteIm);

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).deleteImageInExternalStorage(counterPic - 1);
                dismiss();
            }
        });

        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog d = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        Window w = d.getWindow();
        WindowManager.LayoutParams lp = d.getWindow().getAttributes();
        lp.dimAmount = 0.7f;
        w.setAttributes(lp);
        w.getAttributes().windowAnimations = R.style.inputDialogAnimation;
        w.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        return d;
    }
}