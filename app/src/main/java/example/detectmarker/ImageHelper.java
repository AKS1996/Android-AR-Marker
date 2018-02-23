package example.detectmarker;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;

public class ImageHelper {

    public static Uri content_download_dir;
    public static Uri file_read_uri;

    public ImageHelper(Context context){
        try {
            File file = File.createTempFile("who", ".jpg", context.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            File file2 = new File(Environment.getExternalStorageDirectory(),"/who.png");

            if (Build.VERSION.SDK_INT > 21) { //use this if Lollipop (API 22) or above
                content_download_dir = FileProvider.getUriForFile(context.getApplicationContext(), context.getPackageName(), file);
                file_read_uri =Uri.fromFile(file);
            }
            else {
                content_download_dir = Uri.fromFile(file2);
                file_read_uri = Uri.fromFile(file2);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
