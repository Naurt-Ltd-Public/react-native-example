//
//  RCTNaurtModule.swift
//  naurt_react_native_typescript
//
//  Created by Bob on 09/03/2022.
//

import Foundation
import Naurt_SDK


@objc(RCTNaurtModue)
class RCTNaurtModue: NSObject {
  @objc
  func initialiseNaurt(apiKey: String, precision: Int) {
    Naurt.shared.initialise(apiKey: apiKey, precision: precision);
  }
  
  @objc
  func startNaurt() {
    Naurt.shared.start()
  }
  
  @objc
  func stopNaurt() {
    Naurt.shared.stop()
  }
  
  @objc
  func pauseNaurt() {
    Naurt.shared.pause()
  }
  
  @objc
  func resumeNaurt() {
    Naurt.shared.resume()
  }
}
