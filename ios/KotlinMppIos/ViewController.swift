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
    
    @IBOutlet weak var tableView: UITableView!
    
    private let presenter = LatestStoryPresenter()
    
    private let dataSource = ZhihuTableViewDelegate()
    
    override func viewDidLoad() {
        super.viewDidLoad()
                
        tableView.dataSource = dataSource
        tableView.delegate = self
        
        presenter.onLoadingStatusChange = { [unowned self] loading in
            if (loading.boolValue) {
                
            } else {
                
            }
            print("loading \(loading.boolValue)")
            return KotlinUnit()
        }
        
        presenter.onError = { [unowned self] error in
            let alertController = UIAlertController(title: "Error", message: error.message, preferredStyle: .alert)
            let alertAction = UIAlertAction(title: "Ok", style: .cancel, handler: nil)
            alertController.addAction(alertAction)
            self.present(alertController, animated: true, completion: nil)
            
            print("error \(String(describing: error.message))")
            return KotlinUnit()
        }
        
        presenter.onLoadData = { [unowned self] latestStories in
            print("onLoadData size:\(latestStories.stories.count)")
            self.dataSource.data.removeAll()
            self.dataSource.data.append(contentsOf: latestStories.stories)
            self.tableView.reloadData()
            return KotlinUnit()
        }
        
       presenter.onActivate()
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        presenter.onDeactivate()
    }
    
    class ZhihuTableViewDelegate: NSObject, UITableViewDataSource {
        var data:[Story] = []
        
        func numberOfSections(in tableView: UITableView) -> Int {
            return 1
        }
        
        func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
            return data.count
        }
        
        func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
            let cell = tableView.dequeueReusableCell(withIdentifier: "tableViewStoryCellReuseIdentifier") as! StoryTableCell
            
            let story = data[indexPath.row]
            cell.setData(story)
            return cell
        }
    }
}

extension ViewController : UITableViewDelegate {
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("select row \(indexPath.row)")
        
        let story = self.dataSource.data[indexPath.row]
        present(StoryDetailViewController(), animated: true, completion: nil)
    }
}
