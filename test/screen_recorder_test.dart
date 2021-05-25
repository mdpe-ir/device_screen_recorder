import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:screen_recorder/screen_recorder.dart';

void main() {
  const MethodChannel channel = MethodChannel('screen_recorder');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      if (methodCall.method == 'startRecordScreen') {
        return true;
      } else if (methodCall.method == 'stopRecordScreen') {
        return 'path';
      }
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('startRecordScreen', () async {
    expect(await ScreenRecorder.startRecordScreen(), true);
  });

  test('stopRecordScreen', () async {
    expect(await ScreenRecorder.stopRecordScreen(), 'path');
  });
}
