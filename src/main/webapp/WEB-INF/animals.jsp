<%--
  Created by IntelliJ IDEA.
  User: ArslanovDamir
  Date: 19.12.2016
  Time: 0:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Animals Management</title>
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
<form method="post"
      action="${pageContext.request.contextPath}/editanimal" id="formId">
    <fieldset>
        <legend>
            <c:choose>
                <c:when test="${not empty animal.id}">
                    Обновить питомца
                </c:when>
                <c:otherwise>
                    Добавить питомца
                </c:otherwise>
            </c:choose>
        </legend>

        <div>
            <label for="name">Питомец</label> <input type="text" name="name"
                                                 id="name" value="${animal.name}" />
        </div>

        <div>
            <label for="animalClass">Класс</label> <input type="text" name="animalClass" id="animalClass"
                                                        value="${animal.animalClass}" />
        </div>

        <div>
            <label for="age">Возраст</label> <input type="text" name="age" id="age"
                                                          value="${animal.age}" />
        </div>

        <div>
            <label for="cage">№ Клетки</label> <input type="text" name="cage" id="cage"
                                                          value="${animal.cage}" />
        </div>


        <c:if test="${!empty animal.id}">
            <input type="hidden" name="id" value="${animal.id}" />
        </c:if>

    </fieldset>

    <div class="button-row">
        <input type="submit" value="Submit" />
    </div>
</form>
</body>
</html>
