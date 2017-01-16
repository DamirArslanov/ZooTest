<%--
  Created by IntelliJ IDEA.
  User: ArslanovDamir
  Date: 15.12.2016
  Time: 0:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->


    <!-- Bootstrap Core CSS -->
    <link href="/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS: You can use this stylesheet to override any Bootstrap styles and/or apply your own styles -->
    <link href="/css/custom.css" rel="stylesheet">

    <!-- Custom Fonts from Google -->
    <link rel="shortcut icon" href="/images/indie.ico" type="image/x-icon">
    <link href='http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800' rel='stylesheet' type='text/css'>


    <script type="text/javascript" charset="utf8" src="/js/datatable/jquery-3.1.1.min.js"></script>
    <title>File Upload</title>


</head>
<body class="custombody">
<nav id="siteNav" class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container">
        <!-- Logo and responsive toggle -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/">
                <span class="glyphicon glyphicon-fire"></span>
                ZOO
            </a>
        </div>
        <!-- Navbar links -->
        <div class="collapse navbar-collapse" id="navbar">
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <a href="<c:url value="/"/>">Home</a>
                </li>
                <li class="dropdown active">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Zoo Management <span class="caret"></span></a>
                    <ul class="dropdown-menu" aria-labelledby="about-us" >
                        <li><a href="<c:url value="/management"/>">Animal Mngmnt</a></li>
                        <li><a href="<c:url value="/keepers"/>">Keeper Mngmnt</a></li>
                        <li><a href="<c:url value="/cages"/>">Cage Mngmnt</a></li>
                        <li><a href="<c:url value="/UploadServlet"/>">Xml Mngmnt</a></li>
                    </ul>
                </li>
                <li>
                    <a href="#">About</a>
                </li>
            </ul>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container -->
</nav>
<%--<div style="padding-top: 60px">--%>
    <%--<h2>Загрузка XML - файла</h2>--%>
    <%--<form method="post" action="UploadServlet"--%>
          <%--enctype="multipart/form-data">--%>
        <%--Выберите соответствующий файл: <input type="file" name="file" size="60"/>--%>
        <%--<input type="submit" value="Upload" />--%>
    <%--</form>--%>

<%--</div>--%>

<%--<script type="text/javascript">--%>
    <%--$(function(){--%>
<%--//        var senddata = $('#myform').serialize();--%>
<%--//        console.log(senddata.cage);--%>
<%--//        console.log(senddata);--%>
        <%--$("#file").uploadify({--%>
<%--//            'formData'      : {'mydata' : 'someValue'},--%>
            <%--'formData'      : {senddata : $('#myform').serialize()},--%>
            <%--'auto'     : false,--%>
            <%--uploader: 'UploadServlet',--%>
            <%--swf: 'uploadify/uploadify.swf',--%>
<%--//            cancelImg: 'uploadify/img/uploadify-cancel.png',--%>
            <%--onUploadSuccess : function(file, data, response) {--%>
                <%--alert('The file was saved to: ' + data);--%>
            <%--}--%>
        <%--});--%>
        <%--$("a").click(function(){--%>
            <%--$("#file").uploadifyUpload();--%>
        <%--});--%>
    <%--});--%>
<%--</script>--%>

<%--<div style="padding-top: 60px">--%>
    <%--<input id="file0" name="file" type="file" />--%>
    <%--<a href="#">Загрузить файл</a>--%>
<%--</div>--%>

<%--<form id="myform" style="padding-top: 60px">--%>
    <%--<input type="file" id="file" name="file" />--%>

    <%--<input type="text" id="cage" name="cage" placeholder="Animal Cage">--%>
    <%--<input type="submit" id="submit" value="upload" />--%>
    <%--<a href="javascript:$('#file_upload').uploadify('upload','*')">Upload Files</a>--%>
<%--</form>--%>
<%--<a type="button" class="btn custombtn" href="javascript:$('#file').uploadify('upload','*')">Upload Files</a>--%>
<%--<script>--%>
    <%--$(function() {--%>
        <%--$('#upload-form').ajaxForm({--%>
            <%--success: function(msg) {--%>
                <%--alert("File has been uploaded successfully");--%>
            <%--},--%>
            <%--error: function(msg) {--%>
                <%--$("#upload-error").text("Couldn't upload file");--%>
            <%--}--%>
        <%--});--%>
    <%--});--%>
<%--</script>--%>

<script type="text/javascript">
    $(function(){
        $('#form').on('submit', function(e){
            e.preventDefault();
            console.log("Вошли в Post");
            var $that = $(this),
                    formData = new FormData($that.get(0)); // создаем новый экземпляр объекта и передаем ему нашу форму (*)
            $.ajax({
                url: "UploadServlet",
                type: "POST",
                contentType: false, // важно - убираем форматирование данных по умолчанию
                processData: false, // важно - убираем преобразование строк по умолчанию
                data: formData,
                dataType: 'json',
                success: function(data){
                    if(data){
                        console.log("Animal name " + data.name + " age " + data.age + " keeper: " + data.keeper.name + " " + data.keeper.surname);
//                        alert(data);
                    }
                }
            });
        });
    });
</script>

<form id="form"  style="padding-top: 90px">
    <input  type="file" id="file" name="file" />
    <input type="radio" name="record" value="true">Переписать питомцев при совпадении имен<br>
    <input type="radio" name="record" value="false" checked="checked">Не добавлять<br>
    <input class="btn" type="submit" id="submit" value="upload" />
</form>

<div style="padding-top: 10px">
    <a class="btn btn-info" href="<c:url value="/xml"/>">Get XML</a>
</div>


    <!-- Bootstrap Core JavaScript -->
    <script src="js/bootstrap.min.js"></script>

    <!-- Plugin JavaScript -->
    <script src="js/jquery.easing.min.js"></script>

    <!-- Custom Javascript -->
    <script src="js/custom.js"></script>

</body>
</html>
