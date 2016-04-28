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

    private static final int RADIUS = 1;

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

                int pixelcount = 0;
                int pixelsum = 0;
                for (int x = 0; x < bitmap.getWidth(); x++) {
                    for (int y = 0; y < bitmap.getHeight(); y++){
                        int xdist = realX - x;
                        int ydist = realY - y;
                        if (Math.sqrt((xdist * xdist) + (ydist * ydist)) < RADIUS) {
                            pixelcount += 1;
                            pixelsum += bitmap.getPixel(x, y);
                        }

                    }
                }

                int pixel = pixelsum / pixelcount;
                int redValue = Color.red(pixel);
                String red = Integer.toHexString(redValue).toUpperCase();
                if (red.length() == 1) {
                    red = "0" + red;
                }
                int greenValue = Color.green(pixel);
                String green = Integer.toHexString(greenValue).toUpperCase();
                if (green.length() == 1) {
                    green = "0" + green;
                }
                int blueValue = Color.blue(pixel);
                String blue = Integer.toHexString(blueValue).toUpperCase();
                if (blue.length() == 1) {
                    blue = "0" + blue;
                }
                this.textView.setText(red + green + blue);
            }
        }
        return super.onTouchEvent(event);
    }
}
