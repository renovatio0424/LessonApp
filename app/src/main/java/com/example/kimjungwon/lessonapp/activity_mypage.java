package com.example.kimjungwon.lessonapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

/**
 * Created by kimjungwon on 2017-03-18.
 */

public class activity_mypage extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar ;

    ImageView profileimage;
    TableRow change_Userinfo,change_lessoninfo,regist_lesson,manage_lesson,logout;

    String User_id, User_job, User_name;
    private static String TAG = activity_mypage.class.getSimpleName();

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private String imageUri ="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mypage);
        toolbar = (Toolbar) findViewById(R.id.toolbar_chat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("마이 페이지");

        //직업 별로 다르게 !
        User_id = getIntent().getStringExtra("id");
        User_name = getIntent().getStringExtra("name");
        User_job = getIntent().getStringExtra("job");

        profileimage = (ImageView) findViewById(R.id.mypage_profileimg);

        change_Userinfo = (TableRow) findViewById(R.id.mypage_profile_info);
        change_lessoninfo = (TableRow) findViewById(R.id.mypage_lesson_info);
        regist_lesson = (TableRow) findViewById(R.id.mypage_insert_lesson);
        manage_lesson = (TableRow) findViewById(R.id.mypage_manage_lesson);
        logout = (TableRow) findViewById(R.id.mypage_logout);

        //학생일 경우 수업 등록/수업 관리 안뜨도록
        if(User_job.equals("student")){
            regist_lesson.setVisibility(View.GONE);
            manage_lesson.setVisibility(View.GONE);
        }

        profileimage.setOnClickListener(this);
        change_Userinfo.setOnClickListener(this);
        change_lessoninfo.setOnClickListener(this);
        regist_lesson.setOnClickListener(this);
        manage_lesson.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.mypage_profileimg:
                DialogInterface.OnClickListener CameraListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        doTakePhotoAction();
                    }
                };
                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        doTakeAlbumAction();
                    }
                };
                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                };

                new AlertDialog.Builder(this).
                        setTitle("업로드할 이미지 선택").
                        setPositiveButton("사진촬영", CameraListener).
                        setNegativeButton("앨범 선택", albumListener).
                        setNeutralButton("취소", cancelListener).
                        show();
//                Toast.makeText(this, "profile img !!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mypage_profile_info:
                Toast.makeText(this, "profile info !!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mypage_lesson_info:
                Toast.makeText(this, "lesson info !!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mypage_insert_lesson:
                Toast.makeText(this, "insert info !!", Toast.LENGTH_SHORT).show();
                Intent goRegistClass = new Intent(getApplicationContext(), activity_regist_class.class).
                        putExtra("id",User_id).putExtra("name",User_name).putExtra("job",User_job);
                startActivity(goRegistClass);
                break;
            case R.id.mypage_manage_lesson:
                Toast.makeText(this, "manage lesson !!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mypage_logout:
                Toast.makeText(this, "logout !!", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private void doTakePhotoAction() {
        Log.d(TAG, "doTakePhotoAction");
        Intent goCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(goCamera, PICK_FROM_CAMERA);
    }

    private void doTakeAlbumAction() {
        Log.d(TAG, "doTakeAlbumAction");
        Intent goAlbum = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(goAlbum, PICK_FROM_ALBUM);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            Log.d(TAG, "onActivityResult fail");
        } else {
            switch (requestCode) {
                case PICK_FROM_ALBUM:
                    Uri image_album = data.getData();
                    CropImage.activity(image_album).setGuidelines(CropImageView.Guidelines.ON).start(this);
                    Log.d(TAG, "PICK FROM ALBUM OK");
                    break;
                case PICK_FROM_CAMERA:
                    Uri image_camera = data.getData();
                    CropImage.activity(image_camera).setGuidelines(CropImageView.Guidelines.ON).start(this);
                    Log.d(TAG, "PICK FROM CAMERA OK");
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri resultUri = result.getUri();
//                    backgroundimg.setImageURI(resultUri);
                    profileimage.setBackgroundResource(R.color.Transparent);
                    Glide.with(this).load(resultUri).into(profileimage);
                    imageUri = resultUri.toString();
                    Log.d(TAG, "CROP OK \nresultUri: " + resultUri);
                    break;
            }
        }
    }
}
