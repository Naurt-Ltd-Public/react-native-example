//
//  RCTNaurtModule.h
//  naurt_react_native_typescript
//
//  Created by Bob on 11/03/2022.
//

#ifndef RCTNaurtModule_h
#define RCTNaurtModule_h

#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(RCTNaurtModule, NSObject)
  RCT_EXTERN_METHOD(initialiseNaurt:(String)apiKey (Int) precision);
@end


#endif /* RCTNaurtModule_h */
