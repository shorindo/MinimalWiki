<%@ page language="java"
    contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://shorindo.com/tags/minimalwiki" prefix="wiki" %>
<%--
/*
 * Copyright 2015 Shorindo, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
--%>
<!doctype html>
<html>
<head>
  <title><wiki:write name="title"/></title>
  <link rel="stylesheet" type="text/css" href="<wiki:link path="/css/w2ui.css"/>">
  <link rel="stylesheet" type="text/css" href="<wiki:link path="/css/style.css"/>">
  <script src="<wiki:link path="/js/jquery-2.1.4.js"/>"></script>
  <script src="<wiki:link path="/js/w2ui.js"/>"></script>
  <script src="<wiki:link path="/js/marked.js"/>"></script>
</head>
<body>

<div id="tabs" style="width: 100%;"></div>
<div id="tab-view"></div>
<textarea id="tab-edit"><wiki:write name="wikiText"/></textarea>

<script type="text/javascript">
$(function () {
    $('#tabs').w2tabs({
        name: 'tabs',
        active: '<wiki:write name="active"/>',
        right: '<div id="breadcrumb">a&gt;b&gt;c</div>' +
               '<div id="list-button" class="w2ui-icon icon-tile" title="一覧"></div>' +
               '<div id="save-button" class="w2ui-icon icon-page" title="保存" onclick="save(event)"></div>',
        tabs: [
            { id: 'tab-view', caption: '<%= request.getAttribute("title") %>' },
            { id: 'tab-edit', caption: 'edit' }
        ],
        onRender: function(event) {
            render();
        },
        onClick: function (event) {
            $('#' + w2ui.tabs.active).hide();
            $('#' + event.target).show();
            if (w2ui.tabs.active == 'tab-edit') {
                render();
            } else {
            	$('#tab-edit').focus();
            }
        }
    });

    function render() {
        var text = $('#tab-edit').val();
        text = text.replace(/\[\[([^\[\]]+)\]\]/g, function(whole, g1) {
            var part = g1.split(/\|/, 2);
            if (part.length > 1) {
                return '<a href="' + part[0] + '">' + part[1] + '</a>';
            } else {
                return '<a href="' + g1 + '">' + g1 + '</a>';
            }
        });
        $('#tab-view').html(marked.parse(text));
    }

    function onResize() {
        var height = $(window).height() - $('#' + w2ui.tabs.active).offset().top - 10;
        var width = $(window).width() - 10;
        $('#tab-view').css('width', width);
        $('#tab-view').css('height', height);
        $('#tab-view').css('min-height', height);
        $('#tab-edit').css('width', width);
        $('#tab-edit').css('height', height);
        $('#tab-edit').css('min-height', height);
    }

    function message(text) {
        $('<div id="alert" class="grad">' + text + '</div>')
            .appendTo($("body"))
            .css('position', 'absolute')
            .css('border', '1px solid gray')
            .css('background', 'yellow')
            .css('font-size', '14px')
            .css('padding', '3px');
        var $ah = $("#alert").height();
        var $aw = $("#alert").width();
        var $top = $(window).height()/2-$ah/2;
        var $left = $(window).width()/2-$aw/2;
        $("#alert").css({"top":$top,"left":$left,"opacity":0}).animate({"opacity":1},500);
        setTimeout(function(){
            $("#alert").delay(500).animate({"opacity":0}, 1000, function() {
                $(this).remove();
                $("body").css("overflow","auto");
            });
        }, 1000);
    }

    $(window).on('load', onResize);
    $(window).on('resize', onResize);
    $(window).on('keydown', function(evt) {
        if (evt.keyCode == 112) {
            evt.preventDefault();
            evt.stopPropagation();
            if (w2ui.tabs.active == 'tab-view') {
                w2ui.tabs.click('tab-edit');
            } else {
                w2ui.tabs.click('tab-view');
            }
            return false;
        }
    });
    $('#<%= request.getAttribute("active") %>').show();
    $('#<%= request.getAttribute("active") %>').focus();
    $('#tab-edit').on('keypress', function(evt) {
        if (evt.ctrlKey && evt.charCode == 115) {
            evt.preventDefault();
            evt.stopPropagation();
            save();
            return false;
        }
    });
});

function save(evt) {
    $.ajax({
        type: "POST",
        url: location.href,
        data: "method=save&wikiText=" + encodeURIComponent($('#tab-edit').val()),
        success: function(msg){
            message($("#save-button"), '<div class="message">保存しました</div>');
        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
            alert(errorThrown);
            this;
        }
     });
}

function message($target, text) {
    $target.w2overlay({
        html: text
    });
}
</script>

</body>
</html>
