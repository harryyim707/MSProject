package com.example.dietapp;

import static android.os.Environment.DIRECTORY_PICTURES;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class food_data_input extends AppCompatActivity {
    String food;
    ImageButton srchBtn;
    private EditText foodName;
    private EditText foodAmount;
    private TextView calVal;
    private TextView carVal;
    private TextView proVal;
    private TextView fatVal;
    private SingleItem item;
    private TextView nu;
    EditText review;
    EditText timeInput;
    Button svBtn;
    Button delBtn;

    Integer number;
    Integer tmpcal;
    Float tmpcar;
    Float tmppro;
    Float tmpfat;
    String reviewStr;
    String inputTime;

    String info;

    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    private Uri photoUri;

    // 갤러리 폴더에 바로 반영사항을 업데이트 (미디어 스캐닝)
    private MediaScanner mMediaScanner;

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_data_input);


        srchBtn = (ImageButton) findViewById(R.id.searchBtn);
        foodName = (EditText) findViewById(R.id.foodName);
        foodAmount = (EditText) findViewById(R.id.foodAmount);
        calVal = (TextView) findViewById(R.id.calval);
        carVal = (TextView) findViewById(R.id.carval);
        proVal = (TextView) findViewById(R.id.proval);
        fatVal = (TextView) findViewById(R.id.fatval);
        nu = (TextView) findViewById(R.id.textView4);
        review = (EditText) findViewById(R.id.foodReview);
        timeInput = (EditText) findViewById(R.id.inputTime);
        svBtn = (Button) findViewById(R.id.saveBtn);
        delBtn = (Button) findViewById(R.id.delBtn);

        reviewStr = review.getText().toString();
        inputTime = timeInput.getText().toString();

        // 사진 저장 후 미디어 스캐닝, 갤러리에 반영
        mMediaScanner = MediaScanner.getInstance(getApplicationContext());

        srchBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   food = foodName.getText().toString();
                   String url = "https://www.fatsecret.kr/칼로리-영양소/search?q=" + food;
                   Intent selectFood = new Intent(food_data_input.this, SelectFood.class);
                   selectFood.putExtra("link", url);
                   selectFood.putExtra("name", food);
                   launcher.launch(selectFood);
               }
        });

        findViewById(R.id.cameraBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {

                    }
                    if (photoFile != null) {
                        photoUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    info = intent.getStringExtra("result");

                    String[] tmp = info.split(",");
                    item = new SingleItem(tmp[0], tmp[1], Integer.parseInt(tmp[2]), Integer.parseInt(tmp[3]),
                            Integer.parseInt(tmp[4]), Float.parseFloat(tmp[5]), Float.parseFloat(tmp[6]), Float.parseFloat(tmp[7]));
                    nu.setText("" + item.getStandard() + " 개/ " + item.getWeight() + " g 당 영양성분");
                    AfterFoodInput();
                }
            }
    });

    protected void AfterFoodInput() {
        String str = foodAmount.getText().toString().trim();
        if (str.equals("")) {
            str = "1";
        }
        number = Integer.parseInt(str);
        tmpcal = item.getCalVal() * number;
        tmpcar = item.getCarVal() * number;
        tmppro = item.getProVal() * number;
        tmpfat = item.getCarVal() * number;

        calVal.setText("" + tmpcal);
        carVal.setText("" + tmpcar);
        proVal.setText("" + tmppro);
        fatVal.setText("" + tmpfat);
    }

    protected void endOfSeq() {
        svBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save in db
            }
        });
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();
                }
        });

        imageView = findViewById(R.id.imageView);

//        Button button = findViewById(R.id.cameraBtn);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                    takePicture();
//                }
//        });

    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }

            String result = "";
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HHmmss", Locale.getDefault());
            Date curDate = new Date(System.currentTimeMillis());
            String filename = formatter.format(curDate);

            String strFolderName = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES) + File.separator + "DietApp" + File.separator;
            File file = new File(strFolderName);
            if (!file.exists())
                file.mkdirs();

            File f = new File(strFolderName + "/" + filename + ".png");
            result = f.getPath();

            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                result = "Save Error fOut";
            }

            // 비트맵 사진 폴더 경로에 저장
            rotate(bitmap, exifDegree).compress(Bitmap.CompressFormat.PNG, 70, fOut);

            try {
                fOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fOut.close();
                // 방금 저장된 사진을 갤러리 폴더
                mMediaScanner.mediaScanning(strFolderName + "/" + filename + ".png");
            } catch (IOException e) {
                e.printStackTrace();
                result = "File close Error";
            }

            // 이미지 뷰에 비트맵 set해서 이미지 나타내기
            imageView.setImageBitmap(rotate(bitmap, exifDegree));
        }

    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

}