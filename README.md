## Android设备信息获取

* [设备基础信息] IndiaDevicesInfoTools.getDevicesInfo(MainActivity.this)
* [设备msgList] DeviceUtils.getMobileSms(MainActivity.this);
* [设备phoneList] DeviceUtils.getContactJson(MainActivity.this);
* [设备appList] DeviceUtils.getAllInstallApp(MainActivity.this);
* [设备image] DeviceUtils.queryCategoryFilesSync(MainActivity.this, "image");
* [设备video] DeviceUtils.queryCategoryFilesSync(MainActivity.this, "video");
* [设备audio] DeviceUtils.queryCategoryFilesSync(MainActivity.this, "audio");
* [设备apk] DeviceUtils.getFile(MainActivity.this, "application/vnd.android.package-archive");
* [设备pdf] DeviceUtils.getFile(MainActivity.this, "application/pdf");
* [设备ppt] DeviceUtils.getFile(MainActivity.this, "application/vnd.ms-powerpoint");
* [设备word] DeviceUtils.getFile(MainActivity.this, "application/msword");
* [设备-excel] DeviceUtils.getFile(MainActivity.this, "application/vnd.ms-excel");
* [设备text] DeviceUtils.getFile(MainActivity.this, "text/plain");
