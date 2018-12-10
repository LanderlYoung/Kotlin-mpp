import 'package:json_annotation/json_annotation.dart';

part 'model.g.dart';

@JsonSerializable()
class LatestStories {
  final String date;
  final List<Story> stories;
  final List<Story> top_stories;

  LatestStories({this.date, this.stories, this.top_stories});

  factory LatestStories.fromJson(Map<String, dynamic> json) =>
      _$LatestStoriesFromJson(json);

  Map<String, dynamic> toJson() => _$LatestStoriesToJson(this);
}

@JsonSerializable()
class Story {
  final int id;
  final String title;
  final int type;
  final String ga_prefix;
  final String image;
  final List<String> images;

  Story(
      {this.id,
      this.title,
      this.type,
      this.ga_prefix,
      this.image,
      this.images});

  factory Story.fromJson(Map<String, dynamic> json) => _$StoryFromJson(json);

  Map<String, dynamic> toJson() => _$StoryToJson(this);

  String get coverImage {
    if (image != null) {
      return image;
    } else if (images != null && images.isNotEmpty) {
      return images[0];
    } else {

      return null;
    }
  }

}

@JsonSerializable()
class StoryContent {
  final int id;
  final int type;
  final String title;
  final String body;
  final List<String> js;
  final List<String> css;
  final String share_url;
  final List recommenders;
  final String ga_prefix;
  final String image_source;
  final String image;
  final List<String> images;

  StoryContent(
      {this.id,
      this.type,
      this.title,
      this.body,
      this.js,
      this.css,
      this.share_url,
      this.recommenders,
      this.ga_prefix,
      this.image_source,
      this.image,
      this.images});


  factory StoryContent.fromJson(Map<String, dynamic> json) => _$StoryContentFromJson(json);

  Map<String, dynamic> toJson() => _$StoryContentToJson(this);
}
