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
                  itemBuilder: (context, index) {
                    return _buildListItem(context, data.stories[index]);
                  });
            } else if (future.hasError) {
              return Center(
                  child: Text("error loading data ${future.data}")
              );
            } else {
              return Center(
                child: CircularProgressIndicator(),
              );
            }
          }
        ),
    );
  }

  Widget _buildListItem(BuildContext context, Story story) =>
      Card(
          elevation: 4,
          child:
          Container(
            padding: EdgeInsets.all(8.0),
            child: Row(
              mainAxisSize: MainAxisSize.max,
              children: <Widget>[
                Image(
                    image: NetworkImage(story.coverImage),
                    width: 72,
                    height: 72),
                Padding(padding: EdgeInsets.only(left: 8)),
                Flexible(
                    child: Container(
                      child: Text(
                          story.title,
                          overflow: TextOverflow.ellipsis),
                    ))
              ],
            ),)
      );
}
