<%--
  Created by IntelliJ IDEA.
  User: ArslanovDamir
  Date: 18.12.2016
  Time: 21:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Keepers Management</title>
    <script type="text/javascript" charset="utf8" src="https://code.jquery.com/jquery-3.1.1.min.js"></script>

    <link rel="stylesheet" type="text/css" href="//cdn.datatables.net/1.10.13/css/jquery.dataTables.css">
    <script type="text/javascript" charset="utf8" src="//cdn.datatables.net/1.10.13/js/jquery.dataTables.js"></script>

    <script>
        var rowId = '';
        $(document).ready(function () {
            var table = $('#keepers').DataTable({
                ajax: {
                    type: 'POST',
                    url: '/keepers',
                    dataSrc: ''
                },
                columnDefs: [
                    {
                        "targets": 2,
                        "visible": false,
                        "searchable": false
                    },
                    {
                        "targets": 3,
                        "data": null,
                        "defaultContent": "<button id='edit'>Редактировать</button>"
                    },
                    {
                        "targets": 4,
                        "data": null,
                        "defaultContent": "<button id='delete'>Удалить</button>"
                    }
                ],
                columns: [
                    {data: 'name'},
                    {data: 'surname'},
                    {data: 'id'}
                ]
            });

            $('#keepers tbody').on('click', '#edit', function () {
                console.log('Вошли по клику EDIT')
                var data = table.row($(this).parents('tr')).data();
                rowId = table.row($(this).parents('tr')).index();

                $("#name").val(data.name);
                $("#surname").val(data.surname);
                $("#id").val(data.id);
            });

            $('#keepers tbody').on('click', '#delete', function () {
                var data = table.row($(this).parents('tr')).data();
                console.log('ID удаленого смотрителя' + data.id);
                $.get("/deletekeeper", {id: data.id}, function (data) {
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
                    $.post("/editkeeper",
                            sendData,
                            function (data) {
                                var table = $('#keepers').DataTable();

                                if (rowId == '') {
                                    table.row.add({
                                        'name': data.name,
                                        'surname': data.surname,
                                        'id': data.id
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
<table id="keepers">
    <thead>
    <tr>
        <th>Имя</th>
        <th>Фамилия</th>
        <th>id</th>
        <th>Редактировать</th>
        <th>Удалить</th>
    </tr>
    </thead>
</table>
<hr/>
<form id="form">
    Сохранить смотрителя
    <div>
        <label for="name">Имя</label> <input type="text" name="name"
                                             id="name" value="${keeper.name}"/>
    </div>

    <div>
        <label for="surname">Фамилия</label> <input type="text" name="surname" id="surname"
                                                    value="${keeper.surname}"/>
    </div>

    <div>
        <label for="id">ID</label> <input type="text" name="id" id="id" value="${keeper.id}" readonly/>
    </div>

    <div class="button-row">
        <input type="submit" id="submit" value="Submit"/>
    </div>
</form>
</body>
</html>
