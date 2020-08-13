
package bkav.android.tutorial.rssviewer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader extends AsyncTask<Void, Void, Bitmap> {
    private static final String TAG = ImageLoader.class.getSimpleName();
    
    private ImageView mImageView;
    private String mImageUrl;

    public ImageLoader(String imageUrl, ImageView imageView) {
        mImageView = imageView;
        mImageUrl = imageUrl;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        return getBitmap();
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        mImageView.setImageBitmap(bitmap);
    }

    private Bitmap getBitmap() {
        HttpURLConnection connection = null;
        InputStream input = null;
        try {
            connection = (HttpURLConnection) new URL(mImageUrl)
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();

            // Lấy ảnh về.
            input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (IOException e) {
            Log.e(TAG, "getBitmap " + e.toString());
        } finally {
            if (input != null) {
                // Đóng kết nối
                try {
                    input.close();
                } catch (IOException e) {
                }
            }

            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }
}
