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
import MBProgressHUD

class StoryDetailViewController: UIViewController {

    @IBOutlet weak var webview: WKWebView!

    @IBOutlet weak var navigationTitle: UINavigationItem!

    var detailStory: Story!

    var presenter: StoryContentPresenter!

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        navigationTitle.title = detailStory.title
        presenter = StoryContentPresenter(storyId: detailStory.id)

        presenter.onLoadingStatusChange = { [unowned self] loading in
            print("StoryDetailViewController.onLoadingStatusChange:\(loading.boolValue)")
            if (loading.boolValue) {
                MBProgressHUD.showAdded(to: self.webview, animated: true)
            } else {
                MBProgressHUD.hide(for: self.webview, animated: true)
            }
            return KotlinUnit()
        }
        presenter.onError = { [unowned self] error in
            let alertController = UIAlertController(title: "Error", message: error.message, preferredStyle: .alert)
            let alertAction = UIAlertAction(title: "Ok", style: .cancel, handler: nil)
            alertController.addAction(alertAction)
            self.present(alertController, animated: true, completion: nil)

            return KotlinUnit()
        }

        presenter.onLoadData = { [unowned self] data in
            self.renderData(data)
            return KotlinUnit()
        }

        presenter.onActivate()
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        presenter.onDeactivate()
    }

    private func renderData(_ data: KotlinPair) {
        let storyContent = data.first as! StoryContent
        let html = data.second as! String

        navigationTitle.title = storyContent.title
        webview.loadHTMLString(html, baseURL: nil)
    }
}
