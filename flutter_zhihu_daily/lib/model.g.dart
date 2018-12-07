// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

LatestStories _$LatestStoriesFromJson(Map<String, dynamic> json) {
  return LatestStories(
      date: json['date'] as String,
      stories: (json['stories'] as List)
          ?.map((e) =>
              e == null ? null : Story.fromJson(e as Map<String, dynamic>))
          ?.toList(),
      top_stories: (json['top_stories'] as List)
          ?.map((e) =>
              e == null ? null : Story.fromJson(e as Map<String, dynamic>))
          ?.toList());
}

Map<String, dynamic> _$LatestStoriesToJson(LatestStories instance) =>
    <String, dynamic>{
      'date': instance.date,
      'stories': instance.stories,
      'top_stories': instance.top_stories
    };

Story _$StoryFromJson(Map<String, dynamic> json) {
  return Story(
      id: json['id'] as int,
      title: json['title'] as String,
      type: json['type'] as int,
      ga_prefix: json['ga_prefix'] as String,
      image: json['image'] as String,
      images: (json['images'] as List)?.map((e) => e as String)?.toList());
}

Map<String, dynamic> _$StoryToJson(Story instance) => <String, dynamic>{
      'id': instance.id,
      'title': instance.title,
      'type': instance.type,
      'ga_prefix': instance.ga_prefix,
      'image': instance.image,
      'images': instance.images
    };

StoryContent _$StoryContentFromJson(Map<String, dynamic> json) {
  return StoryContent(
      id: json['id'] as int,
      type: json['type'] as int,
      title: json['title'] as String,
      body: json['body'] as String,
      js: json['js'] as List,
      css: json['css'] as List,
      share_url: json['share_url'] as String,
      recommenders: json['recommenders'] as List,
      ga_prefix: json['ga_prefix'] as String,
      image_source: json['image_source'] as String,
      image: json['image'] as String,
      images: (json['images'] as List)?.map((e) => e as String)?.toList());
}

Map<String, dynamic> _$StoryContentToJson(StoryContent instance) =>
    <String, dynamic>{
      'id': instance.id,
      'type': instance.type,
      'title': instance.title,
      'body': instance.body,
      'js': instance.js,
      'css': instance.css,
      'share_url': instance.share_url,
      'recommenders': instance.recommenders,
      'ga_prefix': instance.ga_prefix,
      'image_source': instance.image_source,
      'image': instance.image,
      'images': instance.images
    };
