# android 图像处理 RGB
实现的功能

1. 相册读取
2. 转为Bitmap
3. Bitmap与三维数组的相互转化
4. Bitmap存入相册
5. 压缩 加密 认证
6. 任务栏颜色android渐变、透明
7. 使用仿IOS消息提示框
8. 使用加载GIF图的控件
9. 使用3D效果的按钮
10. 使用全局类来存储许多活动需要的实例，从而免去了使用Intent传送
11. 可检测篡改位置
12. 开启线程处理耗时操作
13. AMBTC压缩
14. 混沌加密
15. 二值化
16. 矩阵变为Bitmap要为int类型，处理过程如果使用int类型会使图像质量大打折扣，而且不能简单的强转为int类型，要使用四舍五入Math.round()方法。所以在处理过程中要一直使用double类型来保留精度。



点击按钮后，开启进度条，在线程中处理耗时工作，处理完毕后回到主进程进行UI操作
```java
dialog = new XProgressDialog(this, "正在计算PSNR...", XProgressDialog.THEME_CIRCLE_PROGRESS);
dialog.show();
new Thread() {
    public void run() {

        handler.post(new Runnable() {    // 在新线程中使用Handler向主线程发送一段代码, 主线程自动执行run()方法
            public void run() {

                dialog.dismiss();
            }
        });
    }
}.start();
```


            