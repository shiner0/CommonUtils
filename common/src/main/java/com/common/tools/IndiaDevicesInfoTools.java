package com.common.tools;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.input.InputManager;
import android.net.Proxy;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;

import com.common.tool.IndiaAppUtils;import com.common.tool.IndiaBatteryTools;import com.common.tool.IndiaFileTools;import com.common.tool.IndiaSystemTools;
import com.common.tools.emulator.EmulatorCheckUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class IndiaDevicesInfoTools {

    static Map<String, Object> map = new HashMap<>();

    @SuppressLint("MissingPermission")
    public static Map<String, Object> getDevicesInfo(final Activity activity) throws JSONException {
        map.clear();

        map.put("batteryLevelMa", (Double.parseDouble(IndiaBatteryTools.getBatteryCapacity(activity)) / 100) * IndiaBatteryTools.getSystemBatteryLevel(activity));
        map.put("batteryMaxMa", IndiaBatteryTools.getBatteryCapacity(activity) + "mAh");
        map.put("isAcCharge", getBatteryStatus(activity).getString("is_ac_charge"));
        map.put("isUsbCharge", getBatteryStatus(activity).getString("is_usb_charge"));
        map.put("currentSystemTime", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date()));
        map.put("isUsingProxyPort", isWifiProxy(activity) + "");
        map.put("isUsingVpn", isDeviceInVPN() + "");
        map.put("locale_display_language", Locale.getDefault().getLanguage());
        map.put("locale_iso_3_country", getCountry());
        map.put("locale_iso_3_language", getLanguage());
        map.put("sensor_list", getSensorList(activity).toString());
        map.put("keyboard", getKeyboard(activity));

        map.put("albs", "");
        map.put("idfv", "");
        map.put("idfa", "");
        map.put("productionDate", Build.TIME);
        map.put("audio_external", getAudioExternalNumber(activity));
        map.put("audio_internal", getAudioInternalNumber(activity));
        map.put("video_external", getVideoExternalNumber(activity));
        map.put("video_internal", getVideoInternalNumber(activity));
        map.put("images_external", getImagesExternalNumber(activity));
        map.put("images_internal", getImagesInternalNumber(activity));
        map.put("download_files", getDownloadFileNumber());
        map.put("contact_group", getContactsGroupNumber(activity));
        map.put("pic_count", Integer.parseInt((String) map.get("images_external")) + Integer.parseInt((String) map.get("images_internal")) + "");


        map.put("appScreenWidth", IndiaAppUtils.getScreenWidth(activity) + "");
        map.put("appScreenHeight", IndiaAppUtils.getScreenHeight(activity) + "");
        map.put("screenDensity", IndiaAppUtils.getScreenDensity(activity, ""));
        map.put("screenDensityDpi", IndiaAppUtils.getScreenDensity(activity, "dpi"));
        map.put("fullScreen", IndiaAppUtils.isFullScreen(activity) + "");
        map.put("landscape", IndiaAppUtils.isLandscape(activity) + "");

        map.put("lastUpdateTime", IndiaSystemTools.getLastUpdateTime(activity));
        map.put("appPath", IndiaSystemTools.getAppPath(activity));
        map.put("sha1", IndiaSystemTools.getAppSignatureSHA1(activity, "SHA-1"));
        map.put("sha256", IndiaSystemTools.getAppSignatureSHA1(activity, "SHA256"));
        map.put("md5", IndiaSystemTools.getAppSignatureSHA1(activity, "MD5"));
        map.put("uid", IndiaSystemTools.getAppUid(activity) + "");
        map.put("screenWidth", IndiaAppUtils.getScreenWidths(activity) + "");
        map.put("screenHeight", IndiaAppUtils.getScreenHeights(activity) + "");
        map.put("debug", IndiaSystemTools.isAppDebug(activity) + "");
        map.put("sleepDuration", IndiaAppUtils.getSleepDuration(activity) + "");
        map.put("autoBrightnessEnabled", IndiaAppUtils.isAutoBrightnessEnabled(activity) + "");
        map.put("brightness", IndiaAppUtils.getBrightness(activity) + "");
        map.put("isPhone", IndiaAppUtils.isPhone(activity) + "");
        map.put("phoneType", IndiaAppUtils.getPhoneType(activity) + "");
        map.put("simCardReady", IndiaAppUtils.getSimState(activity) + "");
        map.put("simOperatorName", IndiaAppUtils.getSimOperatorName(activity));
        map.put("simOperatorByMnc", IndiaAppUtils.getSimOperator(activity));
        map.put("simCountryIso", IndiaAppUtils.getSimCountryIso(activity));
        map.put("networkCountryIso", IndiaSystemTools.getNetworkCountryIso(activity));
        map.put("systemApp", IndiaSystemTools.isSystemApp(activity) + "");
        map.put("foreground", !IndiaSystemTools.isAppBackground(activity) + "");
        map.put("running", "true");
        map.put("packageName", IndiaSystemTools.getAppPackageName(activity));
        map.put("name", IndiaSystemTools.getAppName(activity));
        map.put("versionName", IndiaSystemTools.getAppVersionName(activity));
        map.put("versionCode", IndiaSystemTools.getAppVersionCode(activity) + "");
        map.put("firstInstallTime", IndiaSystemTools.getFirstInstallTime(activity));
        map.put("portrait", IndiaAppUtils.isPortrait(activity) + "");
        map.put("screenRotation", IndiaAppUtils.getScreenRotation(activity) + "");
        map.put("screenLock", IndiaAppUtils.isScreenLock(activity) + "");
        map.put("networkOperator", IndiaSystemTools.getNetworkOperator(activity));
        map.put("simSerialNumber", IndiaAppUtils.getSimSerialNumber(activity));
        map.put("networkOperatorName", IndiaSystemTools.getNetworkOperatorName(activity));
        map.put("deviceId", IndiaAppUtils.getDeviceId(activity));
        map.put("serial", IndiaAppUtils.getSerial());
        String imei = (IndiaAppUtils.getIMEIOne(activity) + "," + IndiaAppUtils.getIMEITwo(activity)).equals(",") ? IndiaAppUtils.getIMEI(activity) : IndiaAppUtils.getIMEIOne(activity) + "," + IndiaAppUtils.getIMEITwo(activity);
        if (TextUtils.equals(",", imei)) {
            imei = "";
        }
        if (!TextUtils.isEmpty(imei)) {
            Log.e("imei", "imei = " + imei);
            if (imei.endsWith(",")) {
                imei = imei.substring(0, imei.length() - 1);
            }

            if (imei.startsWith(",")) {
                imei = imei.substring(1);
            }
        }
        map.put("imei", imei);
        map.put("meid", IndiaAppUtils.getMEID(activity));
        map.put("imsi", IndiaAppUtils.getIMSI(activity));
        map.put("board", Build.BOARD);
        map.put("buildId", Build.ID);
        map.put("host", Build.HOST);
        map.put("display", Build.DISPLAY);
        map.put("radioVersion", Build.getRadioVersion());
        map.put("fingerprint", Build.FINGERPRINT);
        map.put("device", Build.DEVICE);
        map.put("product", Build.PRODUCT);
        map.put("type", Build.TYPE);
        map.put("buildUser", Build.USER);
        map.put("cpuAbi", Build.CPU_ABI);
        map.put("cpuAbi2", Build.CPU_ABI2);
        map.put("baseOS", Build.VERSION.BASE_OS);
        map.put("bootloader", Build.BOOTLOADER);
        map.put("brand", Build.BRAND);
        map.put("time", Build.TIME);
        map.put("hardware", Build.HARDWARE);
        map.put("language", IndiaAppUtils.getCountryByLanguage());
        map.put("country", IndiaAppUtils.getCountryCodeByLanguage("Default"));
        map.put("sdkVersionName", Build.VERSION.RELEASE);
        map.put("sdkVersionCode", Build.VERSION.SDK_INT + "");
        map.put("androidID", IndiaAppUtils.getAndroidId(activity));
        map.put("macAddress", IndiaAppUtils.getMacAddress(activity));
        map.put("manufacturer", IndiaAppUtils.getBuildMANUFACTURER());
        map.put("model", IndiaAppUtils.getBuildBrandModel());
        map.put("abis", Arrays.asList(IndiaAppUtils.getABIs()) + "");
        map.put("isTablet", IndiaAppUtils.isTablet() + "");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            map.put("isEmulator", isEmulator(activity));
        }else {
            map.put("isEmulator", IndiaAppUtils.isEmulator(activity) + "");
        }
        map.put("sameDevice", "true");
        map.put("connected", IndiaSystemTools.isNetworkAvailable(activity) + "");
        map.put("mobileDataEnabled", IndiaSystemTools.getMobileDataEnabled(activity) + "");
        String type = IndiaSystemTools.getNetWorkType(activity);
        map.put("mobileData", (type.equals("NETWORK_2G") || type.equals("NETWORK_3G") || type.equals("NETWORK_4G") || type.equals("NETWORK_5G")) + "");
        map.put("is4G", IndiaSystemTools.is4G(activity) + "");
        map.put("is5G", IndiaSystemTools.is5G(activity) + "");
        map.put("wifiConnected", IndiaSystemTools.isWifiConnected(activity) + "");
        map.put("networkType", IndiaSystemTools.getNetWorkType(activity) + "");
        map.put("ipAddress", IndiaSystemTools.getIPAddress(true) + "");
        map.put("ipv6Address", IndiaSystemTools.getIPAddress(false));
        map.put("ipAddressByWifi", IndiaSystemTools.getWifiInfo(activity, "ipAddress"));
        map.put("gatewayByWifi", IndiaSystemTools.getWifiInfo(activity, "gateway"));
        map.put("netMaskByWifi", IndiaSystemTools.getWifiInfo(activity, "netmask"));
        map.put("serverAddressByWifi", IndiaSystemTools.getWifiInfo(activity, "serverAddress"));
        map.put("broadcastIpAddress", IndiaSystemTools.getBroadcastIpAddress());
        map.put("ssid", IndiaSystemTools.getSSID(activity));
        map.put("root", IndiaSystemTools.isAppRoot() + "");
        if (Build.VERSION.SDK_INT >= 17) {
            map.put("adbEnabled", IndiaAppUtils.isAdbEnabled(activity) + "");
        }
        map.put("sdCardEnableByEnvironment", IndiaFileTools.sdCardIsAvailable() + "");
        map.put("sdCardPathByEnvironment", IndiaFileTools.getSDCardPath());
        map.put("sdCardInfo", IndiaAppUtils.getSDCardInfo(activity).toString());
        map.put("mountedSdCardPath", IndiaAppUtils.getMountedSDCardPath(activity).toString());
        map.put("externalTotalSize", IndiaFileTools.byte2FitMemorySize(IndiaAppUtils.getExternalTotalSize(), 2));
        map.put("externalAvailableSize", IndiaFileTools.byte2FitMemorySize(IndiaAppUtils.getExternalAvailableSize(), 2));
        map.put("internalTotalSize", IndiaFileTools.byte2FitMemorySize(IndiaAppUtils.getInternalTotalSize(), 2));
        map.put("internalAvailableSize", IndiaFileTools.byte2FitMemorySize(IndiaAppUtils.getInternalAvailableSize()));
        map.put("batteryLevel", IndiaBatteryTools.getSystemBatteryLevel(activity));
        map.put("batterySum", IndiaBatteryTools.getSystemBatterySum(activity));
        map.put("batteryPercent", IndiaBatteryTools.getSystemBattery(activity) + "%");
        map.put("percentValue", IndiaAppUtils.getUsedPercentValue(activity));
        map.put("availableMemory", IndiaAppUtils.getAvailableMemory(activity));
        map.put("processCpuRate", IndiaAppUtils.getCurProcessCpuRate());
        map.put("cpuRate", IndiaAppUtils.getTotalCpuRate());
        map.put("time", IndiaSystemTools.getuptime());
        map.put("timezone", IndiaSystemTools.getTimezone());
        map.put("gpsEnabled", IndiaSystemTools.isGpsEnabled(activity));
        map.put("bootTime", IndiaSystemTools.getBoottime());
        map.put("batteryStatus", IndiaSystemTools.getBatteryStatus(activity));
        map.put("batterytemp", IndiaSystemTools.getBatterytemp(activity));
        map.put("isPlugged", IndiaSystemTools.isPlugged(activity));
        map.put("wifiBSSID", IndiaSystemTools.getWifiBSSID(activity));
        map.put("arpList", IndiaSystemTools.readArp(activity));
        map.put("bluetoothAddress", IndiaSystemTools.getBluetoothAddress(activity));
        map.put("countryZipCode", IndiaSystemTools.getCountryZipCode(activity));
        map.put("cellLocation", IndiaSystemTools.getCellLocation(activity));
        map.put("defaultHost", getDefaultHost());


        map.put("ramTotalSize", IndiaAppInfoTools.getRAMTotalMemorySize(activity));
        map.put("ramUsableSize", IndiaAppInfoTools.getRAMUsableMemorySize(activity));
        map.put("memoryCardSize", IndiaAppInfoTools.getTotalExternalMemorySize() + "");
        map.put("memoryCardUsableSize", IndiaAppInfoTools.getAvailableExternalMemorySize() + "");
        map.put("memoryCardSizeUse", IndiaAppInfoTools.getTotalExternalMemorySize() - IndiaAppInfoTools.getAvailableExternalMemorySize() + "");
        map.put("internalStorageUsable", IndiaAppInfoTools.getAvailableInternalMemorySize() + "");
        map.put("internalStorageTotal", IndiaAppInfoTools.getTotalInternalMemorySize() + "");
        map.put("network", IndiaAppInfoTools.getNetworkData(activity) + "");
        map.put("cpuNum", IndiaAppInfoTools.getNumberOfCPUCores());
        map.put("appMaxMemory", IndiaAppInfoTools.getAPPMaxMemory(activity) + "M");
        map.put("appAvailableMemory", IndiaAppInfoTools.getAPPAvailableMemory(activity) + "M");
        map.put("appFreeMemory", IndiaAppInfoTools.getAPPFreeMemory(activity) + "M");
        map.put("physicalSize", IndiaAppInfoTools.getScreenSizeOfDevice(activity));
        map.put("totalBootTimeWake", SystemClock.uptimeMillis());
        map.put("totalBootTime", SystemClock.elapsedRealtime());

        map.put("voiceMailNumber", IndiaSystemTools.getVoiceMailNumber(activity));
        map.put("available", IndiaSystemTools.isAvailable(activity) + "");
        map.put("availableByPing", IndiaSystemTools.isAvailableByPing() + "");
        map.put("availableByDns", IndiaSystemTools.isAvailableByDns() + "");
        map.put("wifiAvailable", IndiaSystemTools.isWifiAvailable(activity) + "");
        map.put("wifiSignal", getWifiRssi(activity) + "");
        map.put("cellularSignal", getMobileDbm(activity) + "");

        return map;
    }

    /**
     * 获取手机信号强度，需添加权限 android.permission.ACCESS_COARSE_LOCATION <br>
     * API要求不低于17 <br>
     *
     * @return 当前手机主卡信号强度, 单位 dBm（-1是默认值，表示获取失败）
     */
    @SuppressLint("MissingPermission")
    public static int getMobileDbm(Context context) {
        int dbm = -1;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        List<CellInfo> cellInfoList;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            cellInfoList = tm.getAllCellInfo();
            if (null != cellInfoList) {
                for (CellInfo cellInfo : cellInfoList) {
                    if (cellInfo instanceof CellInfoGsm) {
                        CellSignalStrengthGsm cellSignalStrengthGsm = ((CellInfoGsm) cellInfo).getCellSignalStrength();
                        dbm = cellSignalStrengthGsm.getDbm();
                    } else if (cellInfo instanceof CellInfoCdma) {
                        CellSignalStrengthCdma cellSignalStrengthCdma =
                                ((CellInfoCdma) cellInfo).getCellSignalStrength();
                        dbm = cellSignalStrengthCdma.getDbm();
                    } else if (cellInfo instanceof CellInfoWcdma) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            CellSignalStrengthWcdma cellSignalStrengthWcdma =
                                    ((CellInfoWcdma) cellInfo).getCellSignalStrength();
                            dbm = cellSignalStrengthWcdma.getDbm();
                        }
                    } else if (cellInfo instanceof CellInfoLte) {
                        CellSignalStrengthLte cellSignalStrengthLte = ((CellInfoLte) cellInfo).getCellSignalStrength();
                        dbm = cellSignalStrengthLte.getDbm();
                    }
                }
            }
        }
        return dbm;
    }

    public static String getDefaultHost() {
        String proHost = "";
        int proPort = 0;
        try {
            proHost = Proxy.getDefaultHost();
            proPort = Proxy.getDefaultPort();
        } catch (Exception var3) {
        }
        return proHost + " " + proPort;
    }

    public static int getWifiRssi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo info = wifiManager.getConnectionInfo();
            if (info != null) {
                return info.getRssi();
            }
        }
        return 0;
    }

    public static String getAudioExternalNumber(Context context) {
        int result = 0;
        Cursor cursor;
        for (cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{"date_added", "date_modified", "duration", "mime_type", "is_music", "year", "is_notification", "is_ringtone", "is_alarm"}, (String) null, (String[]) null, (String) null); cursor != null && cursor.moveToNext(); ++result) {
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return String.valueOf(result);
    }

    public static String getAudioInternalNumber(Context context) {
        int result = 0;

        Cursor cursor;
        for (cursor = context.getContentResolver().query(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, new String[]{"date_added", "date_modified", "duration", "mime_type", "is_music", "year", "is_notification", "is_ringtone", "is_alarm"}, (String) null, (String[]) null, "title_key"); cursor != null && cursor.moveToNext(); ++result) {
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return String.valueOf(result);
    }

    public static String getVideoExternalNumber(Context context) {
        int result = 0;
        String[] arrayOfString = new String[]{"date_added"};
        Cursor cursor;
        for (cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, arrayOfString, (String) null, (String[]) null, (String) null); cursor != null && cursor.moveToNext(); ++result) {
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return String.valueOf(result);
    }

    public static String getVideoInternalNumber(Context context) {
        int result = 0;
        String[] arrayOfString = new String[]{"date_added"};

        Cursor cursor;
        for (cursor = context.getContentResolver().query(MediaStore.Video.Media.INTERNAL_CONTENT_URI, arrayOfString, (String) null, (String[]) null, (String) null); cursor != null && cursor.moveToNext(); ++result) {
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return String.valueOf(result);
    }

    public static String getImagesExternalNumber(Context context) {
        int result = 0;

        Cursor cursor;
        for (cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"datetaken", "date_added", "date_modified", "height", "width", "latitude", "longitude", "mime_type", "title", "_size"}, (String) null, (String[]) null, (String) null); cursor != null && cursor.moveToNext(); ++result) {
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return String.valueOf(result);
    }

    public static String getImagesInternalNumber(Context context) {
        int result = 0;

        Cursor cursor;
        for (cursor = context.getContentResolver().query(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new String[]{"datetaken", "date_added", "date_modified", "height", "width", "latitude", "longitude", "mime_type", "title", "_size"}, (String) null, (String[]) null, (String) null); cursor != null && cursor.moveToNext(); ++result) {
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return String.valueOf(result);
    }


    public static String getDownloadFileNumber() {
        int result = 0;
        File[] files = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).listFiles();
        if (files != null) {
            result = files.length;
        }

        return String.valueOf(result);
    }

    public static String getContactsGroupNumber(Context context) {
        try {
            int result = 0;
            Uri uri = ContactsContract.Groups.CONTENT_URI;
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor;
            for (cursor = contentResolver.query(uri, (String[]) null, (String) null, (String[]) null, (String) null); cursor != null && cursor.moveToNext(); ++result) {
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            return String.valueOf(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static JSONObject getBatteryStatus(Context context) {
        JSONObject jSONObject = new JSONObject();
        try {
            Intent intent = context.registerReceiver((BroadcastReceiver) null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            int k = intent.getIntExtra("plugged", -1);
            switch (k) {
                case 1:
                    jSONObject.put("is_usb_charge", "false");
                    jSONObject.put("is_ac_charge", "true");
                    jSONObject.put("is_charging", "true");
                    return jSONObject;
                case 2:
                    jSONObject.put("is_usb_charge", "true");
                    jSONObject.put("is_ac_charge", "false");
                    jSONObject.put("is_charging", "true");
                    return jSONObject;
                default:
                    jSONObject.put("is_usb_charge", "false");
                    jSONObject.put("is_ac_charge", "false");
                    jSONObject.put("is_charging", "false");
                    return jSONObject;
            }
        } catch (JSONException e) {
            Log.i("异常", e.toString());
        }
        return jSONObject;
    }

    private static boolean isWifiProxy(Context context) {
        final boolean IS_ICS_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        String proxyAddress;
        int proxyPort;
        if (IS_ICS_OR_LATER) {
            proxyAddress = System.getProperty("http.proxyHost");
            String portStr = System.getProperty("http.proxyPort");
            proxyPort = Integer.parseInt((portStr != null ? portStr : "-1"));
        } else {
            proxyAddress = Proxy.getHost(context);
            proxyPort = Proxy.getPort(context);
        }
        return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);
    }

    //判断网络接口名字包含 ppp0 或 tun0
    public static boolean isDeviceInVPN() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (nif.getName().equals("tun0") || nif.getName().equals("ppp0")) {
                    Log.i("TAG", "isDeviceInVPN  current device is in VPN.");
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressLint("NewApi")
    private static JSONArray getSensorList(Context context) {
        // 获取传感器管理器
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        // 获取全部传感器列表
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        JSONArray jsonArray = new JSONArray();
        for (Sensor item : sensors) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", item.getType());
                jsonObject.put("name", item.getName());
                jsonObject.put("version", item.getVersion());
                jsonObject.put("vendor", item.getVendor());
                jsonObject.put("maxRange", item.getMaximumRange());
                jsonObject.put("minDelay", item.getMinDelay());
                jsonObject.put("power", item.getPower());
                jsonObject.put("resolution", item.getResolution());
            } catch (JSONException e) {
                Log.i("json异常", e.toString());
            }
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static String getCountry() {
        String country = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleListCompat listCompat = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration());
            for (int i = 0; i < listCompat.size(); i++) {
                country = listCompat.get(i).getCountry();
            }
        } else {
            Locale locale = Locale.getDefault();
            country = locale.getCountry();
        }
        return country;
    }

    public static String getLanguage() {
        String language = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleListCompat listCompat = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration());
            for (int i = 0; i < listCompat.size(); i++) {
                language = listCompat.get(i).getLanguage();
            }
        } else {
            Locale locale = Locale.getDefault();
            language = locale.getLanguage();
        }
        return language;
    }

    public static int getKeyboard(Context context) {
        InputManager inputManager = (InputManager) context.getSystemService(Context.INPUT_SERVICE);
        int[] inputDeviceIds = inputManager.getInputDeviceIds();
        return inputDeviceIds.length;
    }

    public static String isEmulator(Context context){
        boolean tag = false;
        try {
            tag = EmulatorCheckUtil.getSingleInstance().readSysProperty(context, null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return tag+"";
    }

}
