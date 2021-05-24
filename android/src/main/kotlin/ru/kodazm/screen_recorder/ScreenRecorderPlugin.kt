package ru.kodazm.screen_recorder

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import android.os.Environment
import java.io.File
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import com.hbisoft.hbrecorder.HBRecorderListener
import com.hbisoft.hbrecorder.HBRecorder
import io.flutter.app.FlutterApplication
import android.util.Log

/** ScreenRecorderPlugin */
class ScreenRecorderPlugin : FlutterPlugin, MethodCallHandler, HBRecorderListener, ActivityAware, PluginRegistry.ActivityResultListener {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel

    private var hbRecorder: HBRecorder? = null

    private lateinit var context: Context
    private lateinit var activity: Activity

    val storePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath + File.separator

    private val SCREEN_RECORD_REQUEST_CODE = 333;
    private val SCREEN_STOP_RECORD_REQUEST_CODE = 334;

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "screen_recorder")
        channel.setMethodCallHandler(this)
        context = flutterPluginBinding.applicationContext
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onDetachedFromActivity() {
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity

        binding.addActivityResultListener(this);
//        binding.addRequestPermissionsResultListener(this);

        hbRecorder = HBRecorder(activity, this)
        Log.d("--RECORDING ATTACHED", "onAttachedToActivity")
    }

    override fun onDetachedFromActivityForConfigChanges() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        Log.d("--RECORDING ACTIVITY", "onActivityResult")
        if (requestCode == SCREEN_RECORD_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                //Start screen recording
                val pathRecord = "${storePath}";
                hbRecorder?.setOutputPath(pathRecord)
                hbRecorder?.isAudioEnabled(false)
                hbRecorder?.recordHDVideo(true);
                hbRecorder?.startScreenRecording(data, resultCode, activity)
            }
        } else if (requestCode == SCREEN_STOP_RECORD_REQUEST_CODE) {
            hbRecorder?.stopScreenRecording()
        }
        return true;
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        } else if (call.method == "startRecordScreen") {
            startRecordScreen()
            result.success(true)
        } else if (call.method == "stopRecordScreen") {
            stopRecordScreen()
            result.success(true)
        } else {
            result.notImplemented()
        }
    }

    fun startRecordScreen() {
        Log.d("--RECORDING START", "startRecordScreen")

        val mediaProjectionManager = context.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager?
        val permissionIntent = mediaProjectionManager?.createScreenCaptureIntent()
        activity.startActivityForResult(
            permissionIntent!!,
            SCREEN_RECORD_REQUEST_CODE,
            null)
    }

    fun stopRecordScreen() {
        val mediaProjectionManager = context.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager?
        val permissionIntent = mediaProjectionManager?.createScreenCaptureIntent()
        activity.startActivityForResult(permissionIntent!!, SCREEN_STOP_RECORD_REQUEST_CODE, null);
    }

    override fun HBRecorderOnStart() {
        Log.d("--RECORDING FINISH", "finish")
    }

    override fun HBRecorderOnComplete() {
        Log.d("--RECORDING START", "start")
    }

    override fun HBRecorderOnError(errorCode: Int, errorMessage: String) {
        Log.d("--RECORDING ERROR", errorMessage)
    }
}
