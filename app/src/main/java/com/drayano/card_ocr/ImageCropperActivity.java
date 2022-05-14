package com.drayano.card_ocr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

public class ImageCropperActivity extends AppCompatActivity
{
    MaterialButton btn_retry;
    ImageView cropImageView;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_cropper);

        uri = Uri.parse(getIntent().getStringExtra("imageUri"));
        btn_retry = findViewById(R.id.btn_logo_retry);
        cropImageView = findViewById(R.id.cropImageView);

        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCorp(uri);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK)
            {
                Uri resultUri = result.getUri();
            }

            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            }
        }

        if ( requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK)
            {
                cropImageView.setImageURI(result.getUri());
            }
        }
    }

    private void startCorp(Uri imageUri)
    {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    private String getStringImage()
    {
        Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
        byte [] byte_arr = stream.toByteArray();
        String image_str = Base64.encodeToString(byte_arr,Base64.DEFAULT);

        return image_str;
    }

    /* ! IMPORTANT ! convert img to string for http to DB server */

//    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
//    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//    bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
//    byte [] byte_arr = stream.toByteArray();
//    String image_str = Base64.encodeBytes(byte_arr);
}