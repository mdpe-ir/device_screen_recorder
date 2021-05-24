#import "ScreenRecorderPlugin.h"
#if __has_include(<screen_recorder/screen_recorder-Swift.h>)
#import <screen_recorder/screen_recorder-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "screen_recorder-Swift.h"
#endif

@implementation ScreenRecorderPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftScreenRecorderPlugin registerWithRegistrar:registrar];
}
@end
