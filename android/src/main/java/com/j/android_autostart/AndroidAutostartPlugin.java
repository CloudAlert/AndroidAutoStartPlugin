package com.j.android_autostart;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** AndroidAutostartPlugin */
public class AndroidAutostartPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Context context;

  private static HashMap<String, List<String>> intents = new HashMap<String, List<String>>() {
    {
      put("Xiaomi", Arrays.asList(
              "com.miui.securitycenter/com.miui.permcenter.autostart.AutoStartManagementActivity",
              "com.miui.securitycenter"
      ));

      put("samsung", Arrays.asList(
              "com.samsung.android.sm_cn/com.samsung.android.sm.ui.ram.AutoRunActivity",
              "com.samsung.android.sm_cn/com.samsung.android.sm.ui.appmanagement.AppManagementActivity",
              "com.samsung.android.sm_cn/com.samsung.android.sm.ui.cstyleboard.SmartManagerDashBoardActivity",
              "com.samsung.android.sm_cn/.ui.ram.RamActivity",
              "com.samsung.android.sm_cn/.app.dashboard.SmartManagerDashBoardActivity",

              "com.samsung.android.sm/com.samsung.android.sm.ui.ram.AutoRunActivity",
              "com.samsung.android.sm/com.samsung.android.sm.ui.appmanagement.AppManagementActivity",
              "com.samsung.android.sm/com.samsung.android.sm.ui.cstyleboard.SmartManagerDashBoardActivity",
              "com.samsung.android.sm/.ui.ram.RamActivity",
              "com.samsung.android.sm/.app.dashboard.SmartManagerDashBoardActivity",

              "com.samsung.android.lool/com.samsung.android.sm.ui.battery.BatteryActivity",
              "com.samsung.android.sm_cn",
              "com.samsung.android.sm"
      ));


      put("HUAWEI", Arrays.asList(
              "com.huawei.systemmanager/.startupmgr.ui.StartupNormalAppListActivity",
              "com.huawei.systemmanager/.appcontrol.activity.StartupAppControlActivity",
              "com.huawei.systemmanager/.optimize.process.ProtectActivity",
              "com.huawei.systemmanager/.optimize.bootstart.BootStartActivity",
              "com.huawei.systemmanager"
      ));

      put("vivo", Arrays.asList(
              "com.iqoo.secure/.ui.phoneoptimize.BgStartUpManager",
              "com.iqoo.secure/.safeguard.PurviewTabActivity",
              "com.vivo.permissionmanager/.activity.BgStartUpManagerActivity",
//                    "com.iqoo.secure/.ui.phoneoptimize.AddWhiteListActivity",
              "com.iqoo.secure",
              "com.vivo.permissionmanager"
      ));

      put("Meizu", Arrays.asList(
              "com.meizu.safe/.permission.SmartBGActivity",//Flyme7.3.0(7.1.2)
              "com.meizu.safe/.permission.PermissionMainActivity",
              "com.meizu.safe"
      ));

      put("OPPO", Arrays.asList(
              "com.coloros.safecenter/.startupapp.StartupAppListActivity",
              "com.coloros.safecenter/.permission.startup.StartupAppListActivity",
              "com.oppo.safe/.permission.startup.StartupAppListActivity",
              "com.coloros.oppoguardelf/com.coloros.powermanager.fuelgaue.PowerUsageModelActivity",
              "com.coloros.safecenter/com.coloros.privacypermissionsentry.PermissionTopActivity",
              "com.coloros.safecenter",
              "com.oppo.safe",
              "com.coloros.oppoguardelf"
      ));

      put("oneplus", Arrays.asList(
              "com.oneplus.security/.chainlaunch.view.ChainLaunchAppListActivity",
              "com.oneplus.security"
      ));
      put("letv", Arrays.asList(
              "com.letv.android.letvsafe/.AutobootManageActivity",
              "com.letv.android.letvsafe/.BackgroundAppManageActivity",
              "com.letv.android.letvsafe"
      ));
      put("zte", Arrays.asList(
              "com.zte.heartyservice/.autorun.AppAutoRunManager",
              "com.zte.heartyservice"
      ));

      put("F", Arrays.asList(
              "com.gionee.softmanager/.MainActivity",
              "com.gionee.softmanager"
      ));


      put("smartisanos", Arrays.asList(
              "com.smartisanos.security/.invokeHistory.InvokeHistoryActivity",
              "com.smartisanos.security"
      ));
      //360
      put("360", Arrays.asList(
              "com.yulong.android.coolsafe/.ui.activity.autorun.AutoRunListActivity",
              "com.yulong.android.coolsafe"
      ));
      //360
      put("ulong", Arrays.asList(
              "com.yulong.android.coolsafe/.ui.activity.autorun.AutoRunListActivity",
              "com.yulong.android.coolsafe"
      ));

      put("coolpad", Arrays.asList(
              "com.yulong.android.security/com.yulong.android.seccenter.tabbarmain",
              "com.yulong.android.security"
      ));

      put("lenovo", Arrays.asList(
              "com.lenovo.security/.purebackground.PureBackgroundActivity",
              "com.lenovo.security"
      ));
      put("htc", Arrays.asList(
              "com.htc.pitroad/.landingpage.activity.LandingPageActivity",
              "com.htc.pitroad"
      ));
      //华硕
      put("asus", Arrays.asList(
              "com.asus.mobilemanager/.MainActivity",
              "com.asus.mobilemanager"
      ));

    }
  };

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    context = flutterPluginBinding.getApplicationContext();
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "android_autostart");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("customSetComponent")) {
      String manufacturer = call.argument("manufacturer");
      String pkg = call.argument("pkg");
      String cls = call.argument("cls");

      customSetComponent(manufacturer,pkg,cls,result);
    } else if(call.method.equals("navigateAutoStartSetting")) {
      navigateAutoStartSetting(result);
    } else if(call.method.equals("autoStartSettingIsAvailable")) {
      autoStartSettingIsAvailable(result);
    } else{
      result.notImplemented();
    }
  }

  private void customSetComponent(String manufacturer, String pkg, String cls,@NonNull Result result){
    String systemManufacturer = android.os.Build.MANUFACTURER;
    try {
      Intent intent = new Intent();

      if (manufacturer.equalsIgnoreCase(systemManufacturer)) {
        intent.setComponent(new ComponentName(pkg, cls));
      }

      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(intent);

      result.success(true);
    }catch (Exception e){
      result.success(false);
    }
  }

  public void navigateAutoStartSetting(@NonNull Result result) {
    String systemManufacturer = android.os.Build.MANUFACTURER;
    // Log.e("Util", "******************当前手机型号为：" + systemManufacturer);

    Set<Map.Entry<String, List<String>>> entries = intents.entrySet();
    boolean has = false;
    for (Map.Entry<String, List<String>> entry : entries) {
      String manufacturer = entry.getKey();
      List<String> actCompatList = entry.getValue();
      if (systemManufacturer.equalsIgnoreCase(manufacturer)) {
        for (String act : actCompatList) {
          try {
            Intent intent;
            if (act.contains("/")) {
              intent = new Intent();
              intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              ComponentName componentName = ComponentName.unflattenFromString(act);
              intent.setComponent(componentName);
            } else {
              intent = context.getPackageManager().getLaunchIntentForPackage(act);
            }
            context.startActivity(intent);
            has = true;
            break;
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
    if (!has) {
      try {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(intent);
        result.success(true);
      } catch (Exception e) {
        e.printStackTrace();
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        result.success(false);
      }
    } else {
      result.success(true);
    }
  }

  private void autoStartSettingIsAvailable(@NonNull Result result) {
    String systemManufacturer = android.os.Build.MANUFACTURER;

    Set<Map.Entry<String, List<String>>> entries = intents.entrySet();
    boolean has = false;

    for (Map.Entry<String, List<String>> entry : entries) {
      String manufacturer = entry.getKey();
      if (systemManufacturer.equalsIgnoreCase(manufacturer)) {
        has = true;
      }
    }

    result.success(has);
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}

