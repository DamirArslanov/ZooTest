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
    <title>Zoo Management</title>

    <script type="text/javascript" charset="utf8" src="/js/datatable/jquery-3.1.1.min.js"></script>

    <link rel="stylesheet" type="text/css" href="/css/datatable/jquery.dataTables.css">
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <script type="text/javascript" charset="utf8" src="/js/datatable/jquery.dataTables.js"></script>


    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <style>
        .ui-autocomplete {
            max-height: 100px;
            overflow-y: auto;
            /* prevent horizontal scrollbar */
            overflow-x: hidden;
        }
        /* IE 6 doesn't support max-height
         * we use height instead, but this forces the menu to always be this tall
         */
        * html .ui-autocomplete {
            height: 100px;
        }
    </style>
    <script>
        var rowId = '';
        $(document).ready(function () {
            var table =  $('#animals').DataTable( {
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
                        "defaultContent": "<button id='edit'>Редактировать</button>"
                    },
                    {
                        "targets": 7,
                        "data": null,
                        "defaultContent": "<button id='delete'>Удалить</button>"
                    }

                ],
                columns: [
                    { data: 'name'},
                    { data: 'age'},
                    { data: 'animalClass'},
                    { data: 'cage', render: function ( data, type, row ) {
                        return data.number;
                    } },
//                    { data: 'cage.number'},
                    { data: 'keeper', render: function ( data, type, row ) {
                        return data.nameSurname;
                    } },
//                    { data: "keeper.nameSurname"},
                    { data : 'id'}
                ]
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
                console.log('Вошли по клику EDIT')
                var data = table.row( $(this).parents('tr') ).data();
                rowId = table.row( $(this).parents('tr') ).index();
                $("#name").val(data.name);
                $("#animalClass").val(data.animalClass);
                $("#age").val(data.age);
//                $("#keeper").val(data.keeper.name+' '+data.keeper.surname);
                $("#keeper").val(data.keeper.nameSurname);
                $("#cage").val(data.cage.number);
                $("#id").val(data.id);
            } );

            $(function() {
                $("#submit").click(function(){
                    var sendData = $('#animalForm').serialize();
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
                                    rowId = '';
                                    jQuery('#animalForm').get(0).reset();

                                } else {
                                    table.row(rowId).data(data).draw();
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
<body>

    <table id="animals">
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

<hr/>
    <form id="animalForm">
        <fieldset>
            <legend>
                Сохранить питомца
            </legend>

            <div>
                <label for="name">Питомец</label> <input type="text" name="name"
                                                         id="name" value="${animal.name}"/>
            </div>

            <div>
                <label for="animalClass">Класс</label> <input type="text" name="animalClass" id="animalClass"
                                                              value="${animal.animalClass}"/>
            </div>

            <div>
                <label for="age">Возраст</label> <input type="text" name="age" id="age"
                                                        value="${animal.age}"/>
            </div>

            <div class="ui-widget">
                <label for="keeper">Смотритель</label> <input type="text" name="keeper" id="keeper"
                                                              value="${animal.keeper}"/>
            </div>

            <div>
                <label for="cage">№ Клетки</label> <input type="text" name="cage" id="cage"
                                                          value="${animal.cage}"/>
            </div>

            <div>
                <label for="id">ID</label> <input type="text" name="id" id="id"
                                                  value="${animal.id}" readonly/>
            </div>

        </fieldset>

        <div class="button-row">
            <input type="submit"  id="submit" value="submit" />
        </div>
    </form>
</body>
</html>

