package org.rmj.guanzongroup.useraccount.Etc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.appcompat.app.AppCompatActivity;

import com.websitebeaver.documentscanner.DocumentScanner;

public class DocScanner {
    private static final String TAG = DocScanner.class.getSimpleName();

    private final ComponentActivity instance;

    public interface OnScanDocumentListener{
        void OnScanned(Bitmap bitmap);
        void OnFailed(String message);
        void OnCancelled();
    }

    public DocScanner(ComponentActivity activity) {
        this.instance = activity;
    }

    public void initScanner(OnScanDocumentListener listener){
        // display the first cropped image
        //                    croppedImageView.setImageBitmap(
        //                            BitmapFactory.decodeFile(croppedImageResults.get(0))
        //                    );
        // an error happened
        //                    Log.v("documentscannerlogs", errorMessage);
        // user canceled document scan
        //                    Log.v("documentscannerlogs", "User canceled document scan");

        DocumentScanner poScan = new DocumentScanner(
                instance,
                (croppedImageResults) -> {
                    //Decode the image result into bitmap to display the result on image view
                    listener.OnScanned(BitmapFactory.decodeFile(croppedImageResults.get(0)));
                    return null;
                },
                (errorMessage) -> {
                    // an error happened
                    Log.d(TAG, errorMessage);
                    listener.OnFailed(errorMessage);
                    return null;
                },
                () -> {
                    // user canceled document scan
                    Log.d(TAG, "User canceled document scan");
                    listener.OnCancelled();
                    return null;
                },
                null,
                null,
                null
        );

        poScan.startScan();
    }
}
