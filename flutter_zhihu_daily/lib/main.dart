import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_zhihu_daily/model.dart';
import 'package:flutter_zhihu_daily/repository.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see the
        // application has a blue toolbar. Then, without quitting the app, try
        // changing the primarySwatch below to Colors.green and then invoke
        // "hot reload" (press "r" in the console where you ran "flutter run",
        // or press Run > Flutter Hot Reload in IntelliJ). Notice that the
        // counter didn't reset back to zero; the application is not restarted.
        primarySwatch: Colors.blue,
      ),
      home: LatestStoryPage(title: '知乎日报'),
    );
  }
}

class LatestStoryPage extends StatefulWidget {
  final Future<LatestStories> latestStories =
  ZhihuDailyRepository.getLatestStories();

  LatestStoryPage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _LatestStoryPageState createState() => _LatestStoryPageState();
}

class _LatestStoryPageState extends State<LatestStoryPage> {
  int _counter = 0;

  void _incrementCounter() {
    setState(() {
      // This call to setState tells the Flutter framework that something has
      // changed in this State, which causes it to rerun the build method below
      // so that the display can reflect the updated values. If we changed
      // _counter without calling setState(), then the build method would not be
      // called again, and so nothing would appear to happen.
      _counter++;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: FutureBuilder<LatestStories>(
          future: widget.latestStories,
          builder: (context, future) {
            if (future.hasData) {
              var data = future.data;
              return ListView.builder(
                  padding: EdgeInsets.all(8.0),
                  itemCount: data.stories.length,
                  itemExtent: 20.0,
                  itemBuilder: (context, index) {
                    return Text("story ${data.stories[index].title}");
                  });
            } else if (future.hasError) {
              return Text("error loading data ${future.data}");
            } else {
              return Center(
                child: CircularProgressIndicator(),
              );
            }
          }
        ),
    );
  }
}
