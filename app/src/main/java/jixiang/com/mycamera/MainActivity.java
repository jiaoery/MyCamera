package jixiang.com.mycamera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static  final int REQ_1=1;

    private static  final int REQ_2=2;
    private ImageView imageView;

    private ImageView imageView2;

    private String filePath;//纪录图片所在的路径

    FileInputStream fis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView= (ImageView) findViewById(R.id.iv_image);
        imageView2= (ImageView) findViewById(R.id.iv_image2);
        filePath= Environment.getExternalStorageDirectory().getPath();//获取sd卡的路径
        filePath=filePath+"/"+"temp.png";
    }

    public void startCamera(View view){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQ_1);
    }


    public void startCamera2(View view){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri=Uri.fromFile(new File(filePath));
        //让拍照的照片放在指定的目录下 uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        startActivityForResult(intent,REQ_2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==REQ_1){
                //由于原图过大，这里获取不到原图，仅仅是缩略图
                Bundle bundle=data.getExtras();
                Bitmap bitmap= (Bitmap) bundle.get("data");
                imageView.setImageBitmap(bitmap);
            }else if(requestCode==REQ_2){
                //通过将拍照后的照片保存在指定的目录下，然后通过io流获取相应的图像数据
                try {
                    fis=new FileInputStream(filePath);
                    Bitmap bitmap= BitmapFactory.decodeStream(fis);
                    imageView2.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
