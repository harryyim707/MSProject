package com.example.dietapp;

import static android.os.Environment.DIRECTORY_PICTURES;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class dataFix extends AppCompatActivity {
    ImageButton srchBtn, srch2;
    private TextView calVal, carVal, proVal, fatVal, nu;
    private SingleItem item;
    EditText foodName, foodAmount, review, timeInput, inPlace;
    Button svBtn, delBtn, apply;
    ImageView imgView;

    Integer number, tmpcal;
    Float tmpcar, tmppro, tmpfat;
    String reviewStr, inputTime, dateInfo, timeInfo, food;
    String imgDir = "null"; //여기에 이미지 디렉토리 저장
    String address, where;
    int when;

    String id, name, cal, car, pro, fat, rev, time, place, pic, date;

    String info;
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    private Uri photoUri;

    // 갤러리 폴더에 바로 반영사항을 업데이트 (미디어 스캐닝)
    private MediaScanner mMediaScanner;
    ImageView imageView;
    SimpleDateFormat format;

    DBManager dbManager;
    SQLiteDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_fix);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        cal = intent.getStringExtra("calories");
        car = intent.getStringExtra("carbo");
        pro = intent.getStringExtra("pro");
        fat = intent.getStringExtra("fat");
        rev = intent.getStringExtra("review");
        time = intent.getStringExtra("time");
        place = intent.getStringExtra("place");
        pic = intent.getStringExtra("imgDir");
        number = Integer.parseInt(intent.getStringExtra("quantity"));
        date = intent.getStringExtra("date");

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
        inPlace = (EditText) findViewById(R.id.inputPlace);
        srch2 = (ImageButton)findViewById(R.id.searchBtn2);
        apply = (Button) findViewById(R.id.apply);
        imgView = (ImageView) findViewById(R.id.imageView);
        foodName.setText(name);
        calVal.setText(cal);
        carVal.setText(car);
        proVal.setText(pro);
        fatVal.setText(fat);
        review.setText(rev);
        timeInput.setText(time);
        if(place != null && !place.contains("null")){
            String[] pl = place.split("\\(");
            inPlace.setText(pl[0]);
        }
        if(pic!=null){
            File file = new File(pic);
            if(file.exists()){
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                imgView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imgView.setImageBitmap(bitmap);
            }
        }
        dbManager = new DBManager(this, DBManager.DB, null, DBManager.DB_VERSION);
        db = dbManager.getWritableDatabase();
    }

    @Override
    public void onResume() {
        super.onResume();
        srchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food = foodName.getText().toString();
                String url = "https://www.fatsecret.kr/칼로리-영양소/search?q=" + food;
                Intent selectFood = new Intent(dataFix.this, SelectFood.class);
                selectFood.putExtra("link", url);
                selectFood.putExtra("name", food);
                launcher.launch(selectFood);
                name = food;
            }
        });

        srch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = inPlace.getText().toString();
                Intent intent = new Intent(dataFix.this, Map.class);
                intent.putExtra("input", str);
                launcher2.launch(intent);
                place = str;
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = foodAmount.getText().toString();
                if (!str.equals("")) {
                    number = Integer.parseInt(str);
                    tmpcal = item.getCalVal() * number;
                    tmpcar = item.getCarVal() * number;
                    tmppro = item.getProVal() * number;
                    tmpfat = item.getCarVal() * number;
                    calVal.setText("" + tmpcal);
                    carVal.setText("" + tmpcar);
                    proVal.setText("" + tmppro);
                    fatVal.setText("" + tmpfat);
                    cal = ""+tmpcal;
                    car = ""+tmpcar;
                    pro = ""+tmppro;
                    fat = ""+tmpfat;
                }
            }
        });
        svBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewStr = review.getText().toString();
                if (reviewStr.equals("")) reviewStr = null;
                Date currentTime = Calendar.getInstance().getTime();
                format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                dateInfo = format.format(currentTime);
                inputTime = timeInput.getText().toString();
                if (!inputTime.equals("")) {
                    if(inputTime.contains(":")){
                        String[] t = inputTime.split(":");
                        if (Integer.parseInt(t[0]) < 10 || Integer.parseInt(t[1]) < 10) {
                            if (Integer.parseInt(t[0]) < 10) {
                                int tmp = Integer.parseInt(t[0]);
                                t[0] = "0" + tmp;
                            }
                            if (Integer.parseInt(t[1]) < 10) {
                                int tmp = Integer.parseInt(t[1]);
                                t[1] = "0" + tmp;
                            }
                        }
                        timeInfo = t[0] + t[1];
                    }
                    else timeInfo = inputTime;
                } else {
                    timeInfo = null;
                }
                rev = reviewStr;
                place = address;
                //save in db
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBManager.COLUMN_NAME, name);
                contentValues.put(DBManager.COLUMN_CAL, cal);
                contentValues.put(DBManager.COLUMN_CAR, car);
                contentValues.put(DBManager.COLUMN_PRO, pro);
                contentValues.put(DBManager.COLUMN_FAT, fat);
                contentValues.put(DBManager.COLUMN_QUANTITY, number);
                contentValues.put(DBManager.COLUMN_REVIEW, rev);
                contentValues.put(DBManager.COLUMN_TIME, time);
                contentValues.put(DBManager.COLUMN_IMG_DIR, pic);
                contentValues.put(DBManager.COLUMN_ADDRESS, place);
                db.update(DBManager.TABLE_NAME, contentValues, "id=?", new String[]{id});
                finish();
            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.delete(DBManager.TABLE_NAME, "ID=?", new String[]{id});
                finish();
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
                pic = imageFilePath;
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
                        item = new SingleItem(tmp[0], tmp[1], tmp[2], tmp[3],
                                Integer.parseInt(tmp[4]), Float.parseFloat(tmp[5]), Float.parseFloat(tmp[6]), Float.parseFloat(tmp[7]));
                        nu.setText("" + item.getStandard() + " / " + item.getWeight() + " 영양성분");
                        foodName.setText(item.getName());
                        calVal.setText("" + item.getCalVal());
                        carVal.setText("" + item.getCarVal());
                        proVal.setText("" + item.getProVal());
                        fatVal.setText("" + item.getFatVal());
                    }
                }
            });

    ActivityResultLauncher<Intent> launcher2 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        Intent intent = result.getData();
                        where=intent.getStringExtra("place");
                        address = intent.getStringExtra("result");
                        address = where+address;
                        inPlace.setText(where);
                    }
                }
            });
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
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
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                    result = "Save Error fOut";
                }

                // 비트맵 사진 폴더 경로에 저장
                rotate(bitmap, exifDegree).compress(Bitmap.CompressFormat.PNG, 70, fOut);

                try {
                    fOut.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                try {
                    fOut.close();
                    // 방금 저장된 사진을 갤러리 폴더
                    mMediaScanner.mediaScanning(strFolderName + "/" + filename + ".png");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    result = "File close Error";
                }

                // 이미지 뷰에 비트맵 set해서 이미지 나타내기
                imageView.setImageBitmap(rotate(bitmap, exifDegree));
            }

        }
    }
    private int exifOrientationToDegrees ( int exifOrientation){
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
    private Bitmap rotate (Bitmap bitmap,float degree){
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}