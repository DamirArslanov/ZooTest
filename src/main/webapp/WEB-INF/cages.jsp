<%--
  Created by IntelliJ IDEA.
  User: ArslanovDamir
  Date: 19.12.2016
  Time: 0:06
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
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS: You can use this stylesheet to override any Bootstrap styles and/or apply your own styles -->
    <link href="css/custom.css" rel="stylesheet">


    <!-- Material Design (Tech. preview) -->
    <link href="/css/material.min.css" rel="stylesheet">
    <link href="/css/dataTables.material.min.css" rel="stylesheet">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <!-- Custom Fonts from Google -->
    <link rel="shortcut icon" href="/images/indie.ico" type="image/x-icon">
    <link href='http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800' rel='stylesheet' type='text/css'>


    <title>Cage Management</title>

    <script type="text/javascript" charset="utf8" src="https://code.jquery.com/jquery-3.1.1.min.js"></script>

    <link rel="stylesheet" type="text/css" href="/css/datatable/jquery.dataTables.css">
    <script type="text/javascript" charset="utf8" src="/js/datatable/jquery.dataTables.js"></script>
    <link rel="stylesheet" href=" https://cdn.datatables.net/buttons/1.2.4/css/buttons.dataTables.min.css">
    <script type="text/javascript" charset="utf8" src=" https://cdn.datatables.net/buttons/1.2.4/js/dataTables.buttons.min.js"></script>



    <script>
        var rowId = '';
        $(document).ready(function () {
            var table = $('#cages').DataTable({
                dom: 'Bfrtip',
                buttons: [
                    {
                        className: 'btn',
                        text: 'Add Cage',
                        action: function ( e, dt, node, config ) {
                            $('#myModal').modal('show');
                        },
                    }
                ],
                responsive: true,
                ajax: {
                    type: 'POST',
                    url: '/cages',
                    dataSrc: ''
                },
                columnDefs: [
                    {
                        "targets": 1,
                        "visible": false,
                        "searchable": false
                    },
                    {
                        "targets": 2,
                        "data": null,
                        "defaultContent": "<button id='edit' class='custombtn custombtn-default'>Редактировать</button>"
                    },
                    {
                        "targets": 3,
                        "data": null,
                        "defaultContent": "<button id='delete' class='custombtn custombtn-default'>Удалить</button>"
                    }
                ],
                columns: [
                    {data: 'number'},
                    {data: 'cageID'}
                ]
            });

            $('#cages tbody').on('click', '#edit', function () {
                console.log('Вошли по клику EDIT');
                $('#myModal').modal('show');
                var data = table.row($(this).parents('tr')).data();
                rowId = table.row($(this).parents('tr')).index();

                $("#number").val(data.number);
                $("#cageID").val(data.cageID);
                console.log(data.cageID)
            });


            $('#cages tbody').on('click', '#delete', function () {
                var data = table.row($(this).parents('tr')).data();
                console.log('ID удаленой клетки' + data.cageID);
                $.get("/deletecage", {id: data.cageID}, function (data) {
                    alert(data);
                });
                table
                        .row($(this).parents('tr'))
                        .remove()
                        .draw();
            });

            //чистим bootstrap modal форму
            $('#myModal').on('hidden.bs.modal', function () {
                $(this).find("input,textarea,select").val('').end();

            });

            $(function () {
                $("#submit").click(function () {
                    var sendData = $('#form').serialize();
                    $.post("/editcage",
                            sendData,
                            function (data) {
                                var table = $('#cages').DataTable();

                                if (rowId == '') {
                                    table.row.add({
                                        'number': data.number,
                                        'cageID': data.cageID
                                    })
                                            .draw();

                                    jQuery('#form').get(0).reset();
                                    $("#myModal").modal('hide');
                                } else {
                                    table.row(rowId).data(data).draw();
                                    rowId = '';
                                    jQuery('#form').get(0).reset();
                                    $("#myModal").modal('hide');
                                }
                            });
                    return false;
                });
            });

        });
    </script>
</head>
<body class="custombody">
<!-- Navigation -->
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
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false">Zoo Management <span class="caret"></span></a>
                    <ul class="dropdown-menu" aria-labelledby="about-us">
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

<div class="customcontent">
    <h3 style="color: #fff">Cage Management</h3>
    <div class="customtable">
        <table id="cages" class="mdl-data-table mdl-typography-text-left">
            <thead>
            <tr>
                <th>№ КЛЕТКИ</th>
                <th>id</th>
                <th>Редактировать</th>
                <th>Удалить</th>
            </tr>
            </thead>
        </table>
    </div>
    <hr style=" margin-left: 100px;margin-right: 100px; opacity: 0.2"/>
</div>

<div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Сохранить клетку</h4>
            </div>
            <div class="modal-body">
                <form id="form" class="form-horizontal">
                    <fieldset>

                        <div class="form-group">
                            <label for="number" class="col-md-2 control-label">№ Клетки</label>

                            <div class="col-md-10">
                                <input type="text" class="form-control" name="number" id="number"
                                       placeholder="Cage number">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="cageID" class="col-md-2 control-label">ID</label>

                            <div class="col-md-10">
                                <input type="text" class="form-control" id="cageID" name="cageID" readonly>
                            </div>
                        </div>
                    </fieldset>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="submit" id="submit" class="btn btn-primary">Save changes</button>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap Core JavaScript -->
<script src="js/bootstrap.min.js"></script>

<!-- Plugin JavaScript -->
<script src="js/jquery.easing.min.js"></script>

<!-- Custom Javascript -->
<script src="js/custom.js"></script>

</body>
</html>