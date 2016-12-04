/*
 * Copyright 2012 Stephen Connolly
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

String.prototype.endsWith = function(str) { // http://stackoverflow.com/questions/280634/endswith-in-javascript
    var lastIndex = this.lastIndexOf(str);
    return lastIndex != -1 && lastIndex + str.length == this.length;
};
var TobairSegais = {
    toRawUri:function (uri) {
        var i = uri.indexOf('#');
        if (i != -1) {
            uri = uri.substring(0, i);
        }
        i = uri.indexOf('?');
        
        if (i != -1) {
            uri = uri.substring(0, i);
        }
        return uri + "?raw";
        
    },
    clickSupport:function () {
        if ($(this).attr("ts-immediate") == "true") return true;
        return TobairSegais.loadContent($(this).attr('href'));
    },
    addClickSupport:function (id) {
        $(id + ' a').each(function () {
            //Eğer ?topic=xxx değil ise ajax çekelim yoksa normal sorgu olsun ki redirectler doğru çalışsın
            var i = $(this).attr("href").indexOf("topic");
            if( i == -1 ){
                $(this).click(TobairSegais.clickSupport);
            }
        });
    },
    scroll:function (url) {
        // scroll content
        var i = url.indexOf('#');
        if (i != -1) {
            window.location.hash = url.substring(i);
            // XXX clumsy but cannot get $('[name="ElementNameHere"]') to work
            // note: does not work for generated IDs like d0e4161 after redeploy unless you force a browser refresh
            var nl = document.getElementsByName(url.substring(i + 1));
            for (var j = 0; j < nl.length; j++) {
                nl.item(j).scrollIntoView();
            }
        } else {
            $('#content').each(function (i, e) {
                e.scrollIntoView();
            });
        }
        
        // scroll index
        var nl = $('a').filter(function() {return this.href.endsWith(url)});
        if (nl.length > 0) {
            // XXX better to do this only if not already visible
            $(nl[0]).parents('ul').show();
        }
    },
    loadContent:function (url) {
        if (/^https?:\/\//.test(url)) {
            return true;
        } else {
            history.pushState({url:url}, "", url);
            
            $.get(TobairSegais.toRawUri(url), function ( data ) {
                //RAW html içindeki bileşenleri ( link, meta, script ) temizliyoruz
              var temp = $($.parseHTML(data));
                            
              var c = $("<div>");
              c.append(temp);
              c.find("link").remove();
              c.find("script").remove();
              c.find("meta").remove();
              
              $('#content').empty().append(c);
              TobairSegais.addClickSupport("#content");
              var i = url.indexOf('#');
              TobairSegais.scroll(url);
              document.title = $("#contents-nav a[href='"+(i == -1 ? url : url.substring(0,i))+"']").text();
            });
            
            /*
            $('#content').load(TobairSegais.toRawUri(url), function () {
                TobairSegais.addClickSupport("#content");
                var i = url.indexOf('#');
                TobairSegais.scroll(url);
                document.title = $("#contents-nav a[href='"+(i == -1 ? url : url.substring(0,i))+"']").text();
            });
            */
            return false;
        }
    },
    windowSizer:function(){
      var css = {'height':'100%','overflow':'auto','margin':0,'padding':0,'position':'relative','border':'none', 'border-redius':0};
      
      var $win = $(window); 
      var $top = $('.navbar-fixed-top').first().css(css);
      var $bottom = $('.navbar-fixed-bottom').first().css(css);
      var $body = $('.row-fluid').first().css(css);
      var $sideBar = $('.sidebar-nav').first().parent().css(css);
      var $content = $('#content').css('padding','20px 5% 20px 0').parent().css(css).css('float','right');
      var $sbContent = $('#sidebar-content').css(css);
      
      $('body').css(css);
      
      $body.height($win.height() - ($top.height() + $bottom.height()) ).parent().css({'padding':0});
      $sbContent.height($win.height() - $sbContent.offset().top).parent().css(css);
      $('.sidebar-nav').first().css({'border-right':'1px solid #ccc', 'border-radius':0});
      
      if($win.width() < 768){
        $sideBar.add($sbContent).css({'height':'auto','overflow':'auto'});
        $('#content').css('padding','20px');
      }
    }
};
window.onpopstate = function (event) {
    if (event != null && event.state != null) {
        $('#content').load(TobairSegais.toRawUri(event.state.url), function () {
            TobairSegais.addClickSupport("#content");
            TobairSegais.scroll(event.state.url);
        });
    }
};
$(function () {
    TobairSegais.addClickSupport("#content");
    TobairSegais.addClickSupport("#sidebar-content");
    TobairSegais.windowSizer();
    $(window).resize(TobairSegais.windowSizer);
});

$(document).ready(function(){
    $("#toc").treeview({
		animated: "fast",
		collapsed: true,
                persist: "location"
    });    
    TobairSegais.scroll(window.location.pathname);
})

