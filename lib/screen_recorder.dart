import 'dart:async';

import 'package:flutter/services.dart';

class ScreenRecorder {
  static const MethodChannel _channel = const MethodChannel('screen_recorder');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<bool?> get startRecordScreen async {
    final bool? version = await _channel.invokeMethod('startRecordScreen');
    return version;
  }

  static Future<bool?> get stopRecordScreen async {
    final bool? version = await _channel.invokeMethod('stopRecordScreen');
    return version;
  }
}
