import 'dart:async';
import 'dart:convert';

import 'package:http/http.dart' as http;

import 'package:flutter_zhihu_daily/model.dart';

class ZhihuDailyRepository {
  static Future<LatestStories> getLatestStories() async {
    final response =
        await http.get("https://news-at.zhihu.com/api/4/news/latest");
    if (response.statusCode == 200) {
      print(response.body);

      return LatestStories.fromJson(json.decode(response.body));
    } else {
      throw Exception("Failed to load latestStories $response");
    }
  }
}
