<%--
  Created by IntelliJ IDEA.
  User: ArslanovDamir
  Date: 16.12.2016
  Time: 0:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Zoo Management</title>

    <script type="text/javascript" charset="utf8" src="/js/datatable/jquery-3.1.1.min.js"></script>

    <link rel="stylesheet" type="text/css" href="/css/datatable/jquery.dataTables.css">

    <script type="text/javascript" charset="utf8" src="/js/datatable/jquery.dataTables.js"></script>



<%--<link rel="stylesheet" type="text/css" href="css/bluecss.css">--%>
    <script>
     $(document).ready(function(){
         $('#animals').DataTable();
     });
 </script>
</head>
<body>
<c:if test="${!empty animals}">
    <table id="animals">
        <thead>
            <tr>
                <th>Питомец</th>
                <th>Возраст</th>
                <th>Класс</th>
                <th>№ Клетка</th>
                <th>Смотритель</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${animals}" var="animal">
                <tr>
                    <td>${animal.name}</td>
                    <td>${animal.age}</td>
                    <td>${animal.animalClass}</td>
                    <td>${animal.cage.number}</td>
                    <td>${animal.keeper.name} &nbsp; ${animal.keeper.surname}</td>
                </tr>
            </c:forEach>
        <tbody>
    </table>
</c:if>
</body>
</html>
