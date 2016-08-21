package jixiang.com.mycamera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Administrator on 2016/8/21.
 */
public class ResultActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        String path=getIntent().getStringExtra("picPath");
        ImageView imageView= (ImageView) findViewById(R.id.piuc);
        try {
            FileInputStream fis=new FileInputStream(path);
            Bitmap bitmap=BitmapFactory.decodeStream(fis);
            Matrix matrix=new Matrix();
            matrix.setRotate(90);
            bitmap=Bitmap.createBitmap(bitmap,0,0,
                    bitmap.getWidth(),bitmap.getHeight(),matrix,true);
            imageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        //这里出现的图像为横向的图片
//        Bitmap bitmap= BitmapFactory.decodeFile(path);
//        imageView.setImageBitmap(bitmap);
    }
}
