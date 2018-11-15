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
import SharedCode


class StoryDetailViewController: UIViewController {

    @IBOutlet weak var webview: WKWebView!

    @IBOutlet weak var navigationTitle: UINavigationItem!
    
    var detailStory :Story? = nil
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        navigationTitle.title = detailStory?.title
    }
    
    @IBAction func back(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
}
