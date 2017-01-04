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
    <title>Cage Management</title>
    <script type="text/javascript" charset="utf8" src="https://code.jquery.com/jquery-3.1.1.min.js"></script>

    <link rel="stylesheet" type="text/css" href="//cdn.datatables.net/1.10.13/css/jquery.dataTables.css">
    <script type="text/javascript" charset="utf8" src="//cdn.datatables.net/1.10.13/js/jquery.dataTables.js"></script>
    <script>
        var rowId = '';
        $(document).ready(function () {
            var table = $('#cages').DataTable({
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
                        "defaultContent": "<button id='edit'>Редактировать</button>"
                    },
                    {
                        "targets": 3,
                        "data": null,
                        "defaultContent": "<button id='delete'>Удалить</button>"
                    }
                ],
                columns: [
                    {data: 'number'},
                    {data: 'cageID'}
                ]
            });

            $('#cages tbody').on('click', '#edit', function () {
                console.log('Вошли по клику EDIT')
                var data = table.row($(this).parents('tr')).data();
                rowId = table.row($(this).parents('tr')).index();

                $("#number").val(data.number);
                $("#cageID").val(data.cageID);
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

                                } else {
                                    table.row(rowId).data(data).draw();
                                    rowId = '';
                                    jQuery('#form').get(0).reset();
                                }
                            });
                    return false;
                });
            });

        });
    </script>
</head>
<body>
<table id="cages">
    <thead>
    <tr>
        <th>№ КЛЕТКИ</th>
        <th>id</th>
        <th>Редактировать</th>
        <th>Удалить</th>
    </tr>
    </thead>
</table>
<hr/>
<form id="form">
    Сохранить клетку

    <div>
        <label for="number">№ КЛЕТКИ</label> <input type="text" name="number"
                                                    id="number" value="${cage.number}"/>
    </div>

    <div>
        <label for="cageID">ID</label> <input type="text" name="id" id="cageID" value="${cage.cageID}" readonly/>
    </div>

    <div class="button-row">
        <input type="submit" id="submit" value="Submit"/>
    </div>
</form>
</body>
</html>
