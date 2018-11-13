//
//  ViewController.swift
//  KotlinMppIos
//
//  Created by landerlyoung on 2018/11/8.
//  Copyright © 2018 landerlyoung. All rights reserved.
//

import UIKit
import SharedCode

class ViewController: UIViewController {

    @IBOutlet weak var textField: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        textField.text = CommonKt.createApplicationScreenMessage()

        do {
            let str = try ActualKt_.nonSuspendHttpGet(url: "https://www.qq.com/")
            print(str)
        } catch let error as NSError {
            print(error)
        }
    }

    func test() {
        //  NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
        //    [request setHTTPMethod:@"GET"];
        //    [request setURL:[NSURL URLWithString:url]];
        //
        //    NSError *error = nil;
        //    NSHTTPURLResponse *responseCode = nil;
        //
        //    NSData *oResponseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&responseCode error:&error];
        //
        //    if([responseCode statusCode] != 200){
        //        NSLog(@"Error getting %@, HTTP status code %i", url, [responseCode statusCode]);
        //        return nil;
        //    }
        //
        //    return [[NSString alloc] initWithData:oResponseData encoding:NSUTF8StringEncoding];
//        let req = URLRequest(url: URL(string: "https://www.qq.com/")!!)
//        req.httpMethod = "GET"
//        NSURLConnection.sendSynchronousRequest(<#T##request: URLRequest##Foundation.URLRequest#>, returning: <#T##AutoreleasingUnsafeMutablePointer<URLResponse?>?##Swift.AutoreleasingUnsafeMutablePointer<Foundation.URLResponse?>?#>)
    }
}

