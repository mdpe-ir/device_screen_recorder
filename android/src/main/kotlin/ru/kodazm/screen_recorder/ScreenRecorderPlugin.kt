package ru.kodazm.screen_recorder

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import com.hbisoft.hbrecorder.HBRecorderListener
import com.hbisoft.hbrecorder.HBRecorder
import io.flutter.app.FlutterApplication

/** ScreenRecorderPlugin */
class ScreenRecorderPlugin : FlutterPlugin, MethodCallHandler, HBRecorderListener, PluginRegistry.ActivityResultListener {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel

    private var hbRecorder: HBRecorder? = null

    private val SCREEN_RECORD_REQUEST_CODE = 333;
    private val SCREEN_STOP_RECORD_REQUEST_CODE = 334;

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "screen_recorder")
        channel.setMethodCallHandler(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
//        if (requestCode == SCREEN_RECORD_REQUEST_CODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                //It is important to call this before starting the recording
//                hbRecorder?.onActivityResult(resultCode, data, (registrar.context() as FlutterApplication).currentActivity);
//                //Start screen recording
//                val pathRecord = "${storePath}/";
//                hbRecorder?.setOutputPath(pathRecord)
//                hbRecorder?.isAudioEnabled(false)
//                hbRecorder?.recordHDVideo(true);
//                hbRecorder?.startScreenRecording(data)
//            }
//        } else if (requestCode == SCREEN_STOP_RECORD_REQUEST_CODE) {
//            hbRecorder?.stopScreenRecording()
//        }
        return true;
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        } else if (call.method == "startRecordScreen") {
            startRecordScreen()
            result.success(true)
        } else {
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    fun startRecordScreen() {
//        val mediaProjectionManager = registrar.context().applicationContext.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager?
//        val permissionIntent = mediaProjectionManager?.createScreenCaptureIntent()
//        ActivityCompat.startActivityForResult(
//            (registrar.context().applicationContext as FlutterApplication).currentActivity,
//            permissionIntent!!,
//            SCREEN_RECORD_REQUEST_CODE,
//            null)
    }

    override fun HBRecorderOnStart() {
        //When the recording starts
    }

    override fun HBRecorderOnComplete() {
        //After file was created
    }

    override fun HBRecorderOnError(errorCode: Int, errorMessage: String) {
        //When an error occurs
    }
}
