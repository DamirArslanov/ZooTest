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
        $(document).ready(function(){
            $('#cages').DataTable();
        });
    </script>
</head>
<body>
    <c:if test="${!empty cages}">
        <table id="cages">
            <thead>
                <tr>
                    <th>№ КЛЕТКИ</th>
                    <th></th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${cages}" var="cage">
                    <tr>
                        <td>${cage.number}</td>
                        <td><a href="${pageContext.request.contextPath}/editcage?id=${cage.cageID}">Редактировать</a></td>
                        <td><a href="${pageContext.request.contextPath}/deletecage?id=${cage.cageID}">Удалить</a></td>
                    </tr>
                </c:forEach>
            <tbody>
        </table>
    </c:if>
<hr/>
    <form method="post"
          action="${pageContext.request.contextPath}/editcage" id="formId">
        <fieldset>
            <legend>
                <c:choose>
                    <c:when test="${not empty cage.cageID}">
                        Обновить клетку
                    </c:when>
                    <c:otherwise>
                        Добавить клетку
                    </c:otherwise>
                </c:choose>
            </legend>

            <div>
                <label for="number">№ КЛЕТКИ</label> <input type="text" name="number"
                                                     id="number" value="${cage.number}" />
            </div>

            <c:if test="${!empty cage.cageID}">
                <input type="hidden" name="id" value="${cage.cageID}" />
            </c:if>

        </fieldset>

        <div class="button-row">
            <input type="submit" value="Submit" />
        </div>
    </form>
</body>
</html>
