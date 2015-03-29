package example.com.finder.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alexandru on 21-Feb-15.
 */
public abstract class DownloadImageTask {//extends AsyncTask<String, Void, Bitmap> {
    private static int inSampleSize = 0;

    private static int desiredWidth, desiredHeight;

    private static HashMap<String, Bitmap> profilePictures = new HashMap<>();

    private static Bitmap image = null;


    public static void getImages(Context context, String cuteogramId, List<String> imageUrls) {
        for(String URL : imageUrls)
        {
//            getImage(context, URL);
        }
    }

    public static void getImage(Context context, final ImageView imageView, final String imageUrl) {
        if(profilePictures.get(imageUrl) != null)
        {
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageBitmap(profilePictures.get(imageUrl));
                }
            });
            return;
        }
        try {
            inSampleSize = 0;
            image = null;
            desiredWidth = 440;
            desiredHeight = 294;

            if(imageUrl.equals(""))
            {
                return;
            }
            Log.i("getImage", "url " + imageUrl);

            URL img_value = null;
            img_value = new URL(imageUrl);
            final Bitmap bitmap = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());

//            Bitmap bitmap = downloadImage(imageUrl);

            if (bitmap != null) {
                profilePictures.put(imageUrl, bitmap);
                Log.i("getImage", "insert in db");
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Bitmap downloadImage(String imageUrl) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        options.inSampleSize = inSampleSize;

        try {
            HttpURLConnection connection;
            URL url = null;
            InputStream stream;
            url = new URL(imageUrl);

            connection = (HttpURLConnection) url.openConnection();

            stream = connection.getInputStream();

            image = BitmapFactory.decodeStream(stream, null, options);

            int imageWidth = options.outWidth;

            int imageHeight = options.outHeight;

            inSampleSize = inSampleSize + 2;
            options.inSampleSize = inSampleSize;
//            if (imageWidth > desiredWidth || imageHeight > desiredHeight) {
//                System.out.println("imageWidth:" + imageWidth + ", imageHeight:" + imageHeight);
//
//                inSampleSize = inSampleSize + 2;
//                return downloadImage(imageUrl);
//
//            } else {
                options.inJustDecodeBounds = false;

                connection = (HttpURLConnection) url.openConnection();

                stream = connection.getInputStream();

                image = BitmapFactory.decodeStream(stream, null, options);

                Log.i("getImage", "returns image");
                return image;
//            }
        } catch (Exception e) {
            Log.e("getImage", e.toString());
        }
        Log.i("getImage", "returns null");
        return null;
    }

    public static void launchRingDialog(final Context context, final Runnable runnable) {
        final ProgressDialog ringProgressDialog = ProgressDialog.show(context, "Please wait ...", "Downloading cuteogram info ...", true);
        ringProgressDialog.setCancelable(false);
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {

                runnable.run();
                ringProgressDialog.dismiss();
            }
        });
        th.start();

    }
}