import SwiftUI
import Nekomp

@main
struct iOSApp: App {

  init() {
    InjectionHelperKt.doInitDi()
  }

  var body: some Scene {
    WindowGroup {
      ContentView()
    }
  }
}
