<%@ page language="java" 
    contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
  <title><%= request.getAttribute("title") %></title>
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/js/w2ui.css" />
  <script src="<%=request.getContextPath()%>/js/jquery-2.1.4.js"></script>
  <script src="<%=request.getContextPath()%>/js/w2ui.js"></script>
  <script src="<%=request.getContextPath()%>/js/marked.js"></script>
  <style type="text/css">
  body { padding:0; margin:0; overflow:hidden; }
  #tab-view, #tab-edit {
    width:100%;
    font-size: 100%;
    border: none;
    padding: 5px;
    overflow: auto;
    font-family: monospace;
  }
  </style>
</head>
<body>

<div id="tabs" style="width: 100%;"></div>
<div id="tab-view">view</div>
<textarea id="tab-edit" style="display:none;">edit</textarea>

<script type="text/javascript">
$(function () {
    $('#tabs').w2tabs({
        name: 'tabs',
        active: 'tab-view',
        right: 'menu',
        tabs: [
            { id: 'tab-view', caption: '<%= request.getAttribute("title") %>' },
            { id: 'tab-edit', caption: 'edit' }
        ],
        onClick: function (event) {
            if (w2ui.tabs.active == 'tab-edit') {
                $('#tab-view').html(marked.parse($('#tab-edit').val()));
            }
            $('#' + w2ui.tabs.active).hide();
            $('#' + event.target).show();
        }
    });
});
</script>

</body>
</html>
