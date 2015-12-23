package net.ilexiconn.hipster.thread;

import android.graphics.*;
import android.os.AsyncTask;
import android.widget.ImageView;
import net.ilexiconn.hipster.MainActivity;
import net.ilexiconn.hipster.R;
import net.ilexiconn.hipster.config.Config;
import net.ilexiconn.hipster.util.ConfigUtil;
import net.ilexiconn.magister.Magister;

import java.io.*;

public class ImageThread extends AsyncTask<Void, Void, Bitmap> {
    public MainActivity activity;
    public Magister magister;

    public ImageThread(MainActivity activity) {
        this.activity = activity;
        this.magister = MainActivity.getMagister();
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        File saveDir = new File(activity.getFilesDir(), "img");
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        try {
            if (!new File(activity.getFilesDir(), "img" + File.separator + magister.user.username + ".png").exists()) {
                return (Bitmap) magister.getImage(200, 200, true).getImage();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        File saveDir = new File(activity.getFilesDir(), "img");
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        Bitmap image;
        if (bitmap != null) {
            image = getCroppedBitmap(bitmap);
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(new File(saveDir, magister.user.username + ".png"));
                image.compress(Bitmap.CompressFormat.PNG, 0, out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                image = BitmapFactory.decodeStream(new FileInputStream(new File(saveDir, magister.user.username + ".png")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
        }
        Config config = ConfigUtil.loadConfig(activity);
        activity.setBitmap(image);
        ImageView toolbarPicture = (ImageView) activity.findViewById(R.id.toolbar_avatar);
        if (config.toolbarAvatar) {
            toolbarPicture.setImageBitmap(image);
        }
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0xff424242);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
