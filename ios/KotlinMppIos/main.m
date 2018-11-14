//
//  main.m
//  KotlinMppIos
//
//  Created by landerlyoung on 2018/11/14.
//  Copyright © 2018 landerlyoung. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#import "KotlinMppIos-Swift.h"
#import "SharedCode/SharedCode.h"

int main(int argc, char* argv[]) {
    @autoreleasepool {
        return [SharedCodeIosCoroutinesKt runBlockingBlock:^SharedCodeInt * _Nonnull {
             int ret = UIApplicationMain(argc, argv, nil, NSStringFromClass([AppDelegate class]));
            return [SharedCodeInt numberWithInt: ret];
        }];
    }
}
