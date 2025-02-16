package ru.kodazm.device_screen_recorder

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import androidx.annotation.NonNull
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

/** ScreenRecorderPlugin */
class DeviceScreenRecorderPlugin : FlutterPlugin, MethodCallHandler, HBRecorderListener, ActivityAware, PluginRegistry.ActivityResultListener {
    private lateinit var channel: MethodChannel
    private var recorder: HBRecorder? = null
    private lateinit var context: Context
    private lateinit var activity: Activity
    private var path: String? = ""
    private var name: String? = ""

    private val SCREEN_RECORD_REQUEST_CODE = 333;

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "device_screen_recorder")
        channel.setMethodCallHandler(this)
        context = flutterPluginBinding.applicationContext
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    // ActivityAware
    override fun onDetachedFromActivity() {
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
        binding.addActivityResultListener(this);
        recorder = HBRecorder(activity, this)
    }

    override fun onDetachedFromActivityForConfigChanges() {
    }

    // PluginRegistry.ActivityResultListener
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (requestCode == SCREEN_RECORD_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                //Start screen recording
                recorder?.setOutputPath(path)
                if (name != "") {
                    recorder?.fileName = name
                }
                recorder?.setAudioSource("DEFAULT")
                recorder?.isAudioEnabled(true)
                recorder?.recordHDVideo(false);
                recorder?.startScreenRecording(data, resultCode, activity)
            }
        }
        return true;
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        } else if (call.method == "startRecordScreen") {
            var name = call.argument<String?>("name")
            var path = call.argument<String?>("path")
            startRecordScreen(name  , path)
            result.success(true)
        } else if (call.method == "stopRecordScreen") {
            var path = stopRecordScreen()
            result.success(path)
        } else {
            result.notImplemented()
        }
    }

    fun startRecordScreen(name: String? , path: String?) {
        this.name = name;
        this.path = path;
        val mediaProjectionManager = context.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager?
        val permissionIntent = mediaProjectionManager?.createScreenCaptureIntent()
        activity.startActivityForResult(
            permissionIntent!!,
            SCREEN_RECORD_REQUEST_CODE,
            null)
    }

    fun stopRecordScreen(): String? {
        recorder?.stopScreenRecording()
        return recorder?.getFilePath()
    }

    // HBRecorderListener
    override fun HBRecorderOnStart() {
    }

    override fun HBRecorderOnComplete() {
    }

    override fun HBRecorderOnError(errorCode: Int, errorMessage: String) {
    }
}
