package com.rowell.andrew.colorgetter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CameraActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        imageView = (ImageView) this.findViewById(R.id.imageView);
        this.textView = (TextView) this.findViewById(R.id.textView);
        takePicture(null);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }

    public void takePicture(View view) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int eventX = (int) event.getX();
            int eventY = (int) event.getY();
            int[] values = new int[2];
            imageView.getLocationOnScreen(values);

            int xOnField = eventX - values[0];
            int yOnField = eventY - values[1];

            Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            int realX = (int) (((double) xOnField / (double) imageView.getWidth())
                    * (double) bitmap.getWidth());
            int realY = (int) (((double) yOnField / (double) imageView.getHeight())
                    * (double) bitmap.getHeight());

            if (realX > 0 && realX < bitmap.getWidth()
                    && realY > 0 && realY < bitmap.getHeight()) {

                int pixel = bitmap.getPixel(realX, realY);
                int redValue = Color.red(pixel);
                int blueValue = Color.blue(pixel);
                int greenValue = Color.green(pixel);
                this.textView.setText("(" + redValue + "," + blueValue + "," + greenValue + ")");
            }
        }
        return super.onTouchEvent(event);
    }
}
