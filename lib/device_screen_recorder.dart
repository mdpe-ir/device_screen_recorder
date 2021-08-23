import 'dart:async';

import 'package:flutter/services.dart';

class DeviceScreenRecorder {
  static const MethodChannel _channel = const MethodChannel('device_screen_recorder');

  static Future<bool?> startRecordScreen({String? name , String? path}) async {
    final bool? version = await _channel.invokeMethod('startRecordScreen', {"name": name , "path":path});
    return version;
  }

  static Future<String?> stopRecordScreen() async {
    final String? path = await _channel.invokeMethod('stopRecordScreen');
    return path;
  }
}
