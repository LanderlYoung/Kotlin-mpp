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
}

