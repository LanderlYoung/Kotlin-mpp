import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_webview_plugin/flutter_webview_plugin.dart';
import 'package:flutter_zhihu_daily/model.dart';
import 'package:flutter_zhihu_daily/repository.dart';
import 'package:tuple/tuple.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) =>
      MaterialApp(
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
        routes:
        <String, WidgetBuilder>{
          // add routes
        },
    );
}

class LatestStoryPage extends StatefulWidget {
  LatestStoryPage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _LatestStoryPageState createState() => _LatestStoryPageState();
}

class _LatestStoryPageState extends State<LatestStoryPage> {
  final Future<LatestStories> latestStories =
  ZhihuDailyRepository.getLatestStories();

  @override
  Widget build(BuildContext context) =>
      Scaffold(
        appBar: AppBar(
          title: Text(widget.title),
        ),
        body: FutureBuilder<LatestStories>(
            future: latestStories,
            builder: (context, future) {
              if (future.hasData) {
                var data = future.data;
                return ListView.builder(
                    padding: EdgeInsets.all(8.0),
                    itemCount: data.stories.length,
                    itemBuilder: (context, index) =>
                        _buildListItem(context, data.stories[index]));
              } else if (future.hasError) {
                return Center(
                    child: Text("error loading data ${future.error}"));
              } else {
                return Center(
                  child: CircularProgressIndicator(),
                );
              }
            }),
      );

  Widget _buildListItem(BuildContext context, Story story) =>
      GestureDetector(
          onTapUp: (g) {
            Navigator.push(
                context,
                MaterialPageRoute(
                    builder: (context) => StoryContentPage(storyId: story.id)
                )
            );
          },
          child: Card(
          elevation: 4,
              child: Container(
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
                      child: Text(story.title, overflow: TextOverflow.ellipsis),
                    ))
              ],
            ),
              )));
}

class StoryContentPage extends StatefulWidget {
  final int storyId;
  StoryContentPage({Key key, @required int storyId})
      :
        storyId=storyId,
        super(key: key);

  @override
  State<StatefulWidget> createState() => _StoryContentPageState(storyId);
}

class _StoryContentPageState extends State<StoryContentPage> {
  final Future<Tuple2<StoryContent, String>> storyContent;
  final int storyId;
  String _title;

  _StoryContentPageState(int storyId)
      :
        storyId=storyId,
        storyContent = ZhihuDailyRepository.getStoryContent(storyId),
        super();

  @override
  void initState() {
    super.initState();
    _title = "loading content of $storyId";
    storyContent.then((value) {
      setState(() {
        _title = value.item1.title;
      });
    });
  }

  @override
  Widget build(BuildContext context) =>
      Scaffold(
          appBar:
          AppBar(
            title: Text(_title),
          ),
          body
              : FutureBuilder<Tuple2<StoryContent, String>>(
              future: storyContent,
              builder: (context, future) {
                if (future.hasData) {
                  return _buildStoryContent(context, future.data.item2);
                } else if (future.hasError) {
                  return Center(
                      child: Text("error loading data ${future.error}"));
                } else {
                  return Center(
                    child: CircularProgressIndicator(),
                  );
                }
              }
          )
      );

  Widget _buildStoryContent(BuildContext context, String html) =>
      WebviewScaffold(
          withJavascript: true,
          withZoom: false,
          allowFileURLs: false,
          url: Uri.dataFromString(
              html, mimeType: 'text/html', parameters: {'charset': "utf-8"}
          ).toString()
      );
}
