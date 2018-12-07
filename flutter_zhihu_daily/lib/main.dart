import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_zhihu_daily/model.dart';
import 'package:flutter_zhihu_daily/repository.dart';

void main() => runApp(new MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: 'Flutter Demo',
      theme: new ThemeData(
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
      home: new LatestStoryPage(title: '知乎日报'),
    );
  }
}

class LatestStoryPage extends StatefulWidget {
  final Future<LatestStories> latestStories = ZhihuDailyRepository
      .getLatestStories();

  LatestStoryPage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _LatestStoryPageState createState() => new _LatestStoryPageState();
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
    return new Scaffold(
      appBar: new AppBar(
        title: new Text(widget.title),
      ),
      body: new Center(
        child: new Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            new Text(
              'You have pushed the button this many times:',
            ),
            new Text(
              '$_counter',
              style: Theme.of(context).textTheme.display1,
            ),
            new FutureBuilder<LatestStories>(
                future: widget.latestStories,
                builder: (context, stories) {
                  if (stories.hasData) {
                    return Text(stories.data.stories[0].title);
                  } else if (stories.hasError) {
                    return Text("error: ${stories.error}");
                  } else {
                    return CircularProgressIndicator();
                  }
                })
          ],
        ),
      ),
      floatingActionButton: new FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: new Icon(Icons.add),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}
