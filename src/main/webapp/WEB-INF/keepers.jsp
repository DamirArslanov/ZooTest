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

    <%--<script src="/js/datatable/jquery-3.1.1.min.js"></script>--%>
    <%--<link rel="stylesheet" type="text/css" href="/css/datatable/jquery.dataTables.css">--%>

    <link rel="stylesheet" type="text/css" href="//cdn.datatables.net/1.10.13/css/jquery.dataTables.css">
    <%--<script type="text/javascript" charset="utf8" src="/js/datatable/jquery.dataTables.js"></script>--%>

    <script type="text/javascript" charset="utf8" src="//cdn.datatables.net/1.10.13/js/jquery.dataTables.js"></script>
    <script>
        $(document).ready(function(){
            $('#keepers').DataTable();
        });
    </script>

</head>
<body>
    <c:if test="${!empty keepers}">
        <table id="keepers">
            <thead>
                <tr>
                    <th>Имя</th>
                    <th>Фамилия</th>
                    <th></th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
            <c:forEach items="${keepers}" var="keeper">
            <tr>
                <td>${keeper.name}</td>
                <td>${keeper.surname}</td>
                <td><a href="${pageContext.request.contextPath}/editkeeper?id=${keeper.id}">Редактировать</a></td>
                <td><a href="${pageContext.request.contextPath}/deletekeeper?id=${keeper.id}">Удалить</a></td>
            </tr>
            </c:forEach>
            <tbody>
        </table>
    </c:if>
<hr/>
    <form method="post"
          action="${pageContext.request.contextPath}/editkeeper" id="formId">
        <fieldset>
            <legend>
                <c:choose>
                    <c:when test="${not empty keeper.id }">
                        Обновить смотрителя
                    </c:when>
                    <c:otherwise>
                        Добавить смотрителя
                    </c:otherwise>
                </c:choose>
            </legend>

            <div>
                <label for="name">Имя</label> <input type="text" name="name"
                                                        id="name" value="${keeper.name}" />
            </div>

            <div>
                <label for="surname">Фамилия</label> <input type="text" name="surname" id="surname"
                                                          value="${keeper.surname}" />
            </div>


            <c:if test="${!empty keeper.id}">
                <input type="hidden" name="id" value="${keeper.id}" />
            </c:if>

        </fieldset>

        <div class="button-row">
            <input type="submit" value="Submit" />
        </div>
    </form>
</body>
</html>
