package jp.ac.titech.itpro.sdl.phototaker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private final static int REQ_PHOTO = 1234;
    private Bitmap photoImage = null;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button photoButton = findViewById(R.id.photo_button);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // TODO: You should setup appropriate parameters for the intent

                PackageManager manager = getPackageManager();
                //List activities = manager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Toast.makeText(MainActivity.this, "poyopoyo", Toast.LENGTH_LONG).show();
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(v.getContext(),
                            "com.example.android.fileprovider",
                            photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(intent, REQ_PHOTO);
                } else {
                    Toast.makeText(MainActivity.this, R.string.toast_no_activities, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void showPhoto() {
        if (photoImage == null) {
            return;
        }
        ImageView photoView = findViewById(R.id.photo_view);
        photoView.setImageBitmap(photoImage);
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(reqCode, resCode, data);
        switch (reqCode) {
            case REQ_PHOTO:
                if (resCode == RESULT_OK) {
                    //Bundle extras = data.getExtras();
                    //photoImage = (Bitmap) extras.get("data");
                    photoImage = BitmapFactory.decodeFile(currentPhotoPath);
                    showPhoto();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showPhoto();
    }
}
