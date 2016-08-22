# MyCamera
#Android 摄像头基础demo实现，实现android中基本的摄像头基础，并写了自定义相机的功能。
有需要的朋友可以参考google的官方文档：http://www.android-doc.com/guide/topics/media/camera.html

##1.利用隐式Intent 打开camera
###1.1 获取图片以intent+onActivityResult的方式返回（图片为缩略图） 
* 首先 在mainifexst下添加权限
```<uses-permission android:name="android.permission.CAMERA" />```
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

* 首先在mainifexst下添加权限
```<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />```
* 其次在隐式intent中添加代码<br>
  ```java
   Uri uri = Uri.fromFile(new File(filePath));
    //让拍照的照片放在指定的目录下 uri
    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
  ```
     
其中filePath为保存拍照图片的地址，拍照后的照片默认保存到该地址。
* 最后配置onActivityResult方法，
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


##2.android 自定义相机
  android的系统提供了相应的camera 类来实现基本的相机的功能功能。但是API23 后系统提供了更高级的Camera2来实现
  高级的相机功能，这里使用Camera实现基本的功能
###2.1创建相机
   这里调用import android.hardware.Camera（系统自带相机）然后相应得实现获取相机（getCamera()），开始预览（setStartPreview），释放相机资源（releaseCamera）。并与activity的生命周期进行关联。

###2.2创建SurfaceView
   为了实现相机的实时预览功能，我们需要使用到SurfaceView。<br>
#### 2.2.1 将SurfaceView的SurfaceHolder与camera绑定在一起<br>
```java
    camera.setPreviewDisplay(holder);
```
   
###2.2.1 实现SurfaceHolder的Callback方法
```java
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setStartPreview(camera, holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        camera.stopPreview();
        setStartPreview(camera, holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }
```
   
###2.3关联相机和SurfaceView
###2.4调整相机的显示界面
    由于拍照的时候相机会自动横屏，为了实现竖屏拍照，需要旋转90°。
```java
   //将横屏的Cameara设置为竖直的
   camera.setDisplayOrientation(90);
```
###2.5自定义相机预览界面
```java
/设置参数
        Camera.Parameters parameters=camera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setPreviewSize(800,400);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                //对焦准确
                if(success){
                    camera.takePicture(null,null,mPictureCallback);
                }
            }
        });
```


结果如此

![](https://github.com/jixiang52002/MyCamera/blob/master/app/image/device-2016-08-22-113525.png) ![](https://github.com/jixiang52002/MyCamera/blob/master/app/image/device-2016-08-22-113616.png)







