<%--
  Created by IntelliJ IDEA.
  User: ArslanovDamir
  Date: 16.12.2016
  Time: 0:47
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
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


    <title>Zoo Management</title>

    <script type="text/javascript" charset="utf8" src="/js/datatable/jquery-3.1.1.min.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

    <link rel="stylesheet" type="text/css" href="/css/datatable/jquery.dataTables.css">
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <link rel="stylesheet" href="css/buttons.dataTables.min.css">


    <script type="text/javascript" charset="utf8" src="/js/datatable/jquery.dataTables.js"></script>
    <script type="text/javascript" charset="utf8" src=" https://cdn.datatables.net/buttons/1.2.4/js/dataTables.buttons.min.js"></script>


    <script>

        var rowId = '';
        var validKeeper = '';
        $(document).ready(function () {
            var table =  $('#animals').DataTable( {
                responsive: true,
                dom: 'Bfrtip',
                buttons: [
                    {
                        className: 'btn',
                        text: 'Add Animal',
                        action: function ( e, dt, node, config ) {
                            $('#myModal').modal('show');
                        },
                    }
                ],
                ajax: {
                    type: 'POST',
                    url: '/management',
                    dataSrc: ''
                },
                columnDefs: [
                    {
                        "targets": 5,
                        "visible": false,
                        "searchable": false
                    },
                    {
                        "targets": 6,
                        "data": null,
                        "defaultContent": "<button  id='edit' class='custombtn'>Редактировать</button>"
                    },
                    {
                        "targets": 7,
                        "data": null,
                        "defaultContent": "<button   id='delete' class='custombtn'>Удалить</button>"
                    }
                ],
                columns: [
                    { data: 'name'},
                    { data: 'age'},
                    { data: 'animalClass'},
                    { data: 'cage', render: function ( data, type, row ) {
                        if (data == null) {
                            return null;
                        } else {
                            return data.number;
                        }
                    } },
                    { data: 'keeper', render: function ( data, type, row ) {
                        if (data == null) {
                            return null;
                        } else {
                            return data.name + " " + data.surname;
                        }
                    } },
                    { data : 'id'}
                ]

            });

            $("#name").keyup(function() {
                var name = $(this).val();
                if(name.length > 1) {
                    $("#result").html('checking...');
                    $.ajax({
                        type : 'GET',
                        url  : '/search',
                        data : $(this).serialize(),
                        success : function(data) {
                            if (data == 'true') {
                                $("#result").html("<div id='result' style='color:brown'>Имя питомца занято!</div>");
                                $('#submit').attr("disabled", true);
                            }
                            else if (data == 'false') {
                                $("#result").html("<div id='result' style='color:green'>Имя питомца свободно!</div>");
                                $('#submit').attr("disabled", false);
                            }

                        }
                    });
                    return false;
                }
                else {
                    $("#result").html('');
                }
            });

            $('#animals tbody').on( 'click', '#delete', function () {
                var data = table.row( $(this).parents('tr') ).data();
                console.log('ID удаленого питомца' + data.id);
                $.get( "/deleteanimal", {id: data.id}, function(data) {
                    alert( data );
                } );
                table
                        .row( $(this).parents('tr')  )
                        .remove()
                        .draw();
            } );


            $('#animals tbody').on( 'click', '#edit', function () {
                $('#myModal').modal('show');
                var data = table.row( $(this).parents('tr') ).data();
                rowId = table.row( $(this).parents('tr') ).index();
                $("#name").val(data.name);
                $("#animalClass").val(data.animalClass);
                $("#age").val(data.age);
                if (data.keeper != undefined) {
                    $("#keeper").val(data.keeper.nameSurname);
                }
                if (data.cage != undefined) {
                    $("#cage").val(data.cage.number);
                }
                $("#id").val(data.id);
            } );

            $('#myModal').on('hidden.bs.modal', function () {
                $(this).find("input,textarea,select").val('').end();
                jQuery('#form').get(0).reset();
                $("#result").html('');
                $('#submit').attr("disabled", false);
            });

            $(function() {
                $("#submit").click(function(){
                    var sendData = $('#form').serialize();
                    console.log(sendData)
                    $.post("/editanimal",
                            sendData,
                            function(data){
                                var table = $('#animals').DataTable();
                                if (rowId == '') {
                                    table.row.add({
                                        'name': data.name,
                                        'age': data.age,
                                        'animalClass': data.animalClass,
                                        'cage': data.cage,
                                        'keeper': data.keeper,
                                        'id': data.id
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
    <script>
        $(function () {
            $('#keeper').autocomplete({
                minLength: 2,
                source: function( request, response ) {
                    $.ajax( {
                        type: "POST",
                        url: "/search",
                        dataType: "json",
                        data: {
                            term: request.term
                        },
                        success: function( data ) {
                            response($.map(data, function(item){
                                return{
                                    label: item.name  + " " + item.surname,
                                    value: item.name  + " " + item.surname
                                }
                            }));
                        }
                    } );
                }
            })
        } );
    </script>

</head>
<body class="custombody">
<div>
    <div>
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
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button"
                               aria-haspopup="true" aria-expanded="false">Zoo Management <span class="caret"></span></a>
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
    </div><!-- Navigation -->

    <div class="customcontent">
        <h3 style="color: #fff; font-family: 'Open Sans', 'Helvetica Neue', Arial, sans-serif;">Animal Management</h3>
        <div class="customtable">
            <table id="animals" class="mdl-data-table mdl-typography-text-left">
                <thead>
                <tr>
                    <th>Питомец</th>
                    <th>Возраст</th>
                    <th>Класс</th>
                    <th>№ Клетка</th>
                    <th>Смотритель</th>
                    <th>id</th>
                    <th>Редактировать</th>
                    <th>Удалить</th>
                </tr>
                </thead>
            </table>
        </div>
        <hr style=" margin-left: 100px;margin-right: 100px; opacity: 0.2"/>

        <div>
        </div>
    </div>
</div>


<div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Сохранить питомца</h4>
            </div>
            <div class="modal-body">
                <form id="form" class="form-horizontal">
                    <fieldset>

                        <div class="form-group">
                            <label for="name" class="col-md-2 control-label">Питомец</label>

                            <div class="col-md-10">
                                <input type="text" class="form-control" name="name" id="name" placeholder="Имя питомца должно быть уникально">
                                <%--<p id="result"></p>--%>
                                <div id="result"></div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="animalClass" class="col-md-2 control-label">Класс</label>

                            <div class="col-md-10">
                                <input type="text" class="form-control" name="animalClass" id="animalClass"
                                       placeholder="Класс питомца">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="age" class="col-md-2 control-label">Возраст</label>

                            <div class="col-md-10">
                                <input type="text" class="form-control" id="age" name="age" placeholder="Возраст питомца">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="keeper" class="col-md-2 control-label">Смотритель</label>

                            <div class="col-md-10">
                                <input type="text" class="form-control ui-widget" id="keeper" name="keeper"
                                       placeholder="Введите имя действующего смотрителя">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="cage" class="col-md-2 control-label">№ Клетки</label>

                            <div class="col-md-10">
                                <input type="text" class="form-control" id="cage" name="cage" placeholder="Клетка">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="id" class="col-md-2 control-label">ID</label>

                            <div class="col-md-10">
                                <input type="text" class="form-control" id="id" name="id" readonly>
                            </div>
                        </div>
                    </fieldset>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="submit" id="submit" name="submit" class="btn btn-primary">Save changes</button>
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
