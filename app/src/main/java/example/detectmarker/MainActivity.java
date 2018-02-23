package example.detectmarker;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static example.detectmarker.Constants.MAX_IMG_DIMEN;
import static example.detectmarker.Constants.PICK_IMAGE_GALLERY;
import static example.detectmarker.Constants.QUALITY;
import static example.detectmarker.Constants.REQUEST_CAMERA_PERMISSION;
import static example.detectmarker.Constants.TAKE_IMAGE;
import static example.detectmarker.ImageHelper.file_read_uri;
import static example.detectmarker.ImageHelper.content_download_dir;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.resultImageView);
        new ImageHelper(this);

        findViewById(R.id.takeImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeImage();
            }
        });
    }

    public native byte[] detectMarker(int width,int height,byte[] data);

    public void takeImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setCancelable(false);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PICK_IMAGE_GALLERY);

                } else if (items[item].equals("Take Photo")) {
                    if (Build.VERSION.SDK_INT >= 23 &&
                            ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA},
                                REQUEST_CAMERA_PERMISSION);
                    } else {
                        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, content_download_dir);
                        startActivityForResult(intent, TAKE_IMAGE);
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                    finish(); // Goto main activity
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_IMAGE_GALLERY:
                if (resultCode == RESULT_OK && data != null && data.getData() != null)
                    saveImageCopy(data.getData());
                else
                    finish();

                break;

            case TAKE_IMAGE:
                if (resultCode == RESULT_OK)
                    saveImageCopy(null);
                else
                    finish();

                break;

            case REQUEST_CAMERA_PERMISSION:
                if (resultCode == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, content_download_dir);
                    intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                    startActivityForResult(intent, TAKE_IMAGE);
                } else
                    finish();
                break;

        }
    }

    /**
     * Make copy of the image
     * Resize it so that h,w < 1400
     * Set the uri for future use
     *
     * @param uri URI of the selected image
     */
    private void saveImageCopy(Uri uri){

        if (uri == null) // Null only in case of camera
            uri = file_read_uri;

        try{
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);

            int h1 = bitmap.getHeight();
            int w1 = bitmap.getWidth();

            // Shrink by 25% recursively
            while (h1 > MAX_IMG_DIMEN || w1 > MAX_IMG_DIMEN) {
                bitmap = Bitmap.createScaledBitmap(bitmap, w1 * 3 / 4, h1 * 3 / 4, false);
                h1 = bitmap.getHeight();
                w1 = bitmap.getWidth();
            }

            // On my emulator image type was ARGB_8888
            // Also on java bytes are signed only
            // In C++ they can be signed or unsigned

            // Also Android IMG's are ARGB_8888 by def
            // OpenCV needs RGB888

            // TODO Leaving ARGB to RGB for now
            // TODO Leaving rotation also

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY, stream);

            byte[] byteArray = stream.toByteArray();

            Log.d("###","Input Stream size was "+byteArray.length);

            byte[] output = detectMarker(w1,h1,byteArray);

            Log.d("###","Output Stream size was "+output.length);
            bitmap = BitmapFactory.decodeByteArray(output, 0, output.length);

            imageView.setImageBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

}
