//
//  RCTNaurtModule.swift
//  naurt_react_native_typescript
//
//  Created by Bob on 09/03/2022.
//

import Foundation
import Naurt_SDK


@objc(RCTNaurtModule) open class RCTNaurtModule: NSObject {
  @objc static func requiresMainQueueSetup() -> Bool {
    return true
  }
  
  @objc public func initialiseNaurt(apiKey: String, precision: Int) {
    Naurt.shared.initialise(apiKey: apiKey, precision: precision);
  }
  
  @objc public func startNaurt() {
    Naurt.shared.start()
  }
  
  @objc public func stopNaurt() {
    Naurt.shared.stop()
  }
  
  @objc public func pauseNaurt() {
    Naurt.shared.pause()
  }
  
  @objc public func resumeNaurt() {
    Naurt.shared.resume()
  }
}
