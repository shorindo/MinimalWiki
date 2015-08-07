<%@ page language="java"
    contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
  <title><%= request.getAttribute("title") %></title>
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/w2ui.css" />
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css" />
  <script src="<%=request.getContextPath()%>/js/jquery-2.1.4.js"></script>
  <script src="<%=request.getContextPath()%>/js/w2ui.js"></script>
  <script src="<%=request.getContextPath()%>/js/marked.js"></script>
</head>
<body>

<div id="tabs" style="width: 100%;"></div>
<div id="tab-view"></div>
<textarea id="tab-edit"><%= request.getAttribute("wikiText") %></textarea>

<script type="text/javascript">
$(function () {
    $('#tabs').w2tabs({
        name: 'tabs',
        active: '<%= request.getAttribute("active") %>',
        right: '<a href="javascript:save()">save</a>',
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

    $(window).on('load', onResize);
    $(window).on('resize', onResize);
    $('#<%= request.getAttribute("active") %>').show();
    $('#<%= request.getAttribute("active") %>').focus();
});

function save() {
	$.ajax({
	   type: "POST",
	   url: location.href,
	   data: "wikiText=" + encodeURIComponent($('#tab-edit').val()),
	   success: function(msg){
	       message( "保存しました");
	   }
	});
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
</script>

</body>
</html>
