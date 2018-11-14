package io.github.landerlyoung.kotlin.mpp.zhihudaily

/**
 * <pre>
 * Author: landerlyoung@gmail.com
 * Date:   2018-11-11
 * Time:   13:57
 * Life with Passion, Code with Creativity.
 * </pre>
 */
object StoryContentRenderer {

    fun makeHtml(story: StoryContent): String {
        return """
<html>
    <head>
        <title>${story.title}</title>
        ${story.css.foldRight(StringBuilder()) { css, sb ->
            sb.append("<link rel=\"stylesheet\" href=\"$css\" type=\"text/css\"></link>")
        }}
    </head>
    <body>
       ${story.body}
       ${story.js.foldRight(StringBuilder()) { js, sb ->
            sb.append("<script src=\"$js\"></script>")
        }}
       <style>
           $headerImageStyle
       </style>
       <script>
            var banner = document.getElementsByClassName('img-place-holder')[0]
            if (banner != undefined) {
                banner.className = 'img-wrap'
                banner.innerHTML = '<h1 class=\"headline-title\">${story.title}</h1>' +
                '<span class="img-source">${story.image_source}</span>' +
                '<img src="${story.image}" alt="">' +
                '<div class="img-mask"></div>'
            }
     </script>
    </body>
</html>
"""
    }

    private const val headerImageStyle =
            """
.img-wrap {
    position: relative;
    max-height: 375px;
    overflow: hidden;
}

.img-wrap img {
    margin-top: -18%;
    width: 640px;
}

.img-wrap .img-source {
    position: absolute;
    bottom: 10px;
    z-index: 1;
    font-size: 12px;
    color: rgba(255,255,255,.6);
    right: 40px;
    text-shadow: 0px 1px 2px rgba(0,0,0,.3);
}

.img-wrap .headline-title {
    margin: 20px 0;
    bottom: 10px;
    z-index: 1;
    position: absolute;
    color: white;
    text-shadow: 0px 1px 2px rgba(0,0,0,0.3);
}

.img-mask {
    position: absolute;
    top: 0;
    width: 100%;
    height: 100%;
    background: -moz-linear-gradient(top, rgba(0,0,0,0) 25%, rgba(0,0,0,0.6) 100%);
    /* FF3.6+ */
    background: -webkit-gradient(linear, left top, left bottom, color-stop(25%,rgba(0,0,0,0)), color-stop(100%,rgba(0,0,0,0.6)));
    /* Chrome,Safari4+ */
    background: -webkit-linear-gradient(top, rgba(0,0,0,0) 25%,rgba(0,0,0,0.6) 100%);
    /* Chrome10+,Safari5.1+ */
    background: -o-linear-gradient(top, rgba(0,0,0,0) 25%,rgba(0,0,0,0.6) 100%);
    /* Opera 11.10+ */
    background: -ms-linear-gradient(top, rgba(0,0,0,0) 25%,rgba(0,0,0,0.6) 100%);
    /* IE10+ */
    background: linear-gradient(to bottom, rgba(0,0,0,0) 25%,rgba(0,0,0,0.6) 100%);
    /* W3C */
    filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#00000000', endColorstr='#99000000',GradientType=0 );
    /* IE6-9 */
}
"""
}