import 'dart:async';
import 'dart:convert';

import 'package:flutter_zhihu_daily/story_renderer.dart';
import 'package:tuple/tuple.dart';
import 'package:http/http.dart' as http;

import 'package:flutter_zhihu_daily/model.dart';

class ZhihuDailyRepository {
  static Future<LatestStories> getLatestStories() async {
    final response =
        await http.get("https://news-at.zhihu.com/api/4/news/latest");
    if (response.statusCode == 200) {
      return LatestStories.fromJson(json.decode(response.body));
    } else {
      throw Exception("Failed to load latestStories $response");
    }
  }

  static Future<Tuple2<StoryContent, String>> getStoryContent(
      int storyId) async {
    final response = await http.get(
        "https://news-at.zhihu.com/api/4/news/$storyId");

    if (response.statusCode == 200) {
      var content = StoryContent.fromJson(json.decode(response.body));
      var html = await makeStoryContextHtml(content);
      return Tuple2(content, html);
    } else {
      throw Exception("Failed to load storyContent $response");
    }
  }
}
