//
//  RNNaurtIosModule.swift
//  naurt_react_native_typescript
//
//  Created by Bob on 07/03/2022.
//

import Foundation
import Naurt_SDK
//Foobar
@objc(RNNaurtIosModule)
class RNNaurtIosModule: NSObject {
  @objc
  static func requiresMainQueueSetup() -> Bool {
    return true
  }
  
  @objc
  static func initialiseNaurt(apiKey: String, precision: Int) {
    Naurt.shared.initialise(apiKey: apiKey, precision: precision);
  }
  
  @objc
  static func startNaurt() {
    Naurt.shared.start()
  }
  
  @objc
  static func stopNaurt() {
    Naurt.shared.stop()
  }
  
  @objc
  static func pauseNaurt() {
    Naurt.shared.pause()
  }
  
  @objc
  static func resumeNaurt() {
    Naurt.shared.resume()
  }
}
