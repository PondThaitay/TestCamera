package com.wisdomlanna.testcamera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


public class MainActivity extends AppCompatActivity {

    private ImageButton btnTake;
    private Context context = this;
    private LinearLayout container;
    private File captureDir;
    private File currentCaptureFile;
    private String fullPath;
    final private int PICK_IMAGE = 0;
    private static final int CAMERA_REQUEST = 1;
    public final static String APP_PATH_SD_CARD = "/com.feedback.www/";
    public final static String APP_THUMBNAIL_PATH_SD_CARD = "photos";
    private final static String user_id = "12";
    private final static String input_id = "3";
    private String namePic = user_id + input_id;
    public static int counterPic = 0;
    public static int _counterPic = 0;
    private Bitmap imageSelected;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = (LinearLayout) findViewById(R.id.imagecontainer);
        captureDir = new File(Environment.getExternalStorageDirectory(), "FeedbackPhoto");

        if (!captureDir.exists())
            captureDir.mkdirs();
        currentCaptureFile = new File(captureDir + "/capture");
        Log.i("BANK", captureDir.getAbsolutePath());
        btnTake = (ImageButton) findViewById(R.id.imBtnTakePic);
        btnTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                        .title("Select Choices!")
                        .content("Please choose for add photos.")
                        .positiveText("Take photo")
                        .negativeText("From Gallery")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                Log.i("BANK", "capture to : " + currentCaptureFile);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentCaptureFile));
                                startActivityForResult(intent, CAMERA_REQUEST);
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                photoPickerIntent.setType("image/*");
                                startActivityForResult(photoPickerIntent, PICK_IMAGE);
                            }
                        }).build();
                materialDialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            imageSelected = BitmapFactory.decodeStream(imageStream);
            // find new width when height =1000
            final int finalWidth = 1000, finalHeight = 1000;
            int newHeight = finalHeight;
            int newWidth = (imageSelected.getWidth() / imageSelected.getHeight()) * newHeight;
            if (newWidth > finalWidth) {
                newWidth = finalWidth;
                newHeight = (imageSelected.getHeight() / imageSelected.getWidth()) * newWidth;
                imageSelected = Bitmap.createScaledBitmap(imageSelected, 700, 1000, false);
                //addImageSelected(imageSelected);
                saveImageToExternalStorage(imageSelected);
                addImageTake();
            } else {
                newWidth = imageSelected.getWidth();
                newHeight = imageSelected.getHeight();
                imageSelected = Bitmap.createScaledBitmap(imageSelected, 700, 1000, false);
                //addImageSelected(imageSelected);
                saveImageToExternalStorage(imageSelected);
                addImageTake();
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap image = BitmapFactory.decodeFile(currentCaptureFile.getAbsolutePath());
            final int finalWidth = 1000, finalHeight = 1000;
            int newHeight = finalHeight;
            int newWidth = (image.getWidth() / image.getHeight()) * newHeight;
            if (newWidth > finalWidth) {
                newWidth = finalWidth;
                newHeight = (image.getHeight() / image.getWidth()) * newWidth;
                image = Bitmap.createScaledBitmap(image, 700, 1000, false);
                saveImageToExternalStorage(image);
                addImageTake();
            } else {
                newWidth = image.getWidth();
                newHeight = image.getHeight();
                image = Bitmap.createScaledBitmap(image, 700, 1000, false);
                saveImageToExternalStorage(image);
                addImageTake();
            }
        }
    }

    public void addImageTake() {
        imageSelected = BitmapFactory.decodeFile(fullPath + "/" + namePic
                + String.valueOf(_counterPic) + ".png");
        imageView = new ImageView(context);
        imageView.setTag(Integer.parseInt(namePic + String.valueOf(_counterPic)));
        imageView.setImageBitmap(imageSelected);
        imageView.setPadding(0, 10, 0, 5);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                , container.getHeight());
        lp.leftMargin = getResources().getDimensionPixelSize(R.dimen.dp5);
        imageView.setAdjustViewBounds(true);
        container.addView(imageView, container.getChildCount() - 1, lp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView = (ImageView) v;
                Toast.makeText(context, String.valueOf(imageView.getTag()) + "P" + _counterPic
                        , Toast.LENGTH_SHORT).show();
                //deleteImageInExternalStorage(_counterPic - 1);
                ImageDialog.newInstance(fullPath, String.valueOf(imageView.getTag()), _counterPic)
                        .show(getSupportFragmentManager(), null);
            }
        });
        _counterPic++;
    }

    public boolean saveImageToExternalStorage(Bitmap image) {
        fullPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + APP_PATH_SD_CARD + APP_THUMBNAIL_PATH_SD_CARD;
        try {
            File dir = new File(fullPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            OutputStream fOut = null;
            File file = new File(fullPath, namePic + String.valueOf(counterPic) + ".png");
            file.createNewFile();
            fOut = new FileOutputStream(file);

            image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

            MediaStore.Images.Media.insertImage(context.getContentResolver()
                    , file.getAbsolutePath(), file.getName(), file.getName());
            counterPic++;
            return true;
        } catch (Exception e) {
            Log.e("saveToExternalStorage()", e.getMessage());
            return false;
        }
    }

    //use : deleteImageInExternalStorage(_counterPic - 1);
    public void deleteImageInExternalStorage(int input) {
        File file = new File(fullPath, namePic + String.valueOf(input) + ".png");
        file.delete();
        Log.i("POND", "input : " + input + "");
        container.removeView(imageView);
    }
}