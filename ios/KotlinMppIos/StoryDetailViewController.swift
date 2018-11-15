//
//  StoryDetailViewController.swift
//  KotlinMppIos
//
//  Created by landerlyoung on 2018/11/15.
//  Copyright © 2018 landerlyoung. All rights reserved.
//

import UIKit
import WebKit
import os.log


class StoryDetailViewController: UIViewController {

    @IBOutlet weak var webview: WKWebView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    
    @IBAction func back(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
}
