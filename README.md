# MyCamera
#Android 摄像头基础demo实现，实现android中基本的摄像头基础，并相应的拓展了摄像头的功能。
有需要的朋友可以参考google的官方文档：http://www.android-doc.com/guide/topics/media/camera.html

##1.利用隐式Intent 打开camera
###1.1 获取图片以intent+onActivityResult的方式返回（图片为缩略图） 
* 首先 在mainifexst下添加权限
                <uses-permission android:name="android.permission.CAMERA" />
* 最简洁的java代码如下：
```Java
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQ_1);
```
  这段代码能够调用手里里所有具有照相功能的app（包括系统相机），拍照后获取资源并返回，这里就需要实现onActivityResult方法获取返回的数据。
```java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==REQ_1){
                //由于原图过大，这里获取不到原图，仅仅是缩略图
                bundle=data.getExtras();
                Bitmap bitmap= (Bitmap) bundle.get("data");
                imageView.setImageBitmap(bitmap);
            }
            }
    }
```
按照所写的demo效果如下
![](https://github.com/jixiang52002/MyCamera/blob/master/app/image/QQ%E5%9B%BE%E7%89%8720160819170402.jpg)
但是我们会发现，这里从data中获取到的图片和直接拍照相册中的图片有着很多的不同。
这里是由于原图过大，而intent中所能携带的资源有限，所以回调的时候返回的是缩略图。

###1.2 通过保存原图到指定目录下，再通过I/O流读取图像数据（读取原图）
在许多的需求场合中，我们需要确保拍摄图像的保真性，这就需要保存和获取到拍照后的原图。
在这里我们使用I/O流的方式来实现。

首先在隐式intent中添加代码<br>
  ```java
   Uri uri = Uri.fromFile(new File(filePath));
    //让拍照的照片放在指定的目录下 uri
    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
  ```
     
其中filePath为保存拍照图片的地址，拍照后的照片默认保存到该地址。
之后配置onActivityResult方法，
```java
 @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
             if (requestCode == REQ_2) {
                //通过将拍照后的照片保存在指定的目录下，然后通过io流获取相应的图像数据
                try {
                    fis = new FileInputStream(filePath);
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    imageView2.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
```

这里获取到的就是原图，效果图如下
![](https://github.com/jixiang52002/MyCamera/blob/master/app/image/QQ%E5%9B%BE%E7%89%8720160819170414.jpg)








