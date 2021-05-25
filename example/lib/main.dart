import 'package:flutter/material.dart';
import 'package:screen_recorder/screen_recorder.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  bool recording = false;
  String path = '';

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              recording
                  ? OutlinedButton(
                      onPressed: () async {
                        var file = await ScreenRecorder.stopRecordScreen();
                        setState(() {
                          path = file ?? '';
                          recording = false;
                        });
                      },
                      child: Text('Stop'),
                    )
                  : OutlinedButton(
                      onPressed: () async {
                        var status = await ScreenRecorder.startRecordScreen();
                        // var status = await ScreenRecorder.startRecordScreen(name: 'example');
                        setState(() {
                          recording = status ?? false;
                        });
                      },
                      child: Text('Start'),
                    ),
              Text(path)
            ],
          ),
        ),
      ),
    );
  }
}
