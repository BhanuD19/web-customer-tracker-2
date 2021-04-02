<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>

<html>

<head>
    <title>List Customers</title>

    <!-- reference our style sheet -->

    <link type="text/css"
          rel="stylesheet"
          href="<c:url value='/resources/css/style.css' />">

</head>

<body>
<div id="wrapper">
    <div id="header">
        <h2>CRM - Customer Relationship Manager</h2>
    </div>
    <div id="logout-section" style="display: flex; justify-content: flex-end">
        <form:form action="${pageContext.request.contextPath}/logout" method="post" >
            <input type="submit" value="Logout">
        </form:form>
    </div>
</div>

<div id="container">

    <div id="content">

        <!-- put new button: Add Customer -->

        <security:authorize access="hasAnyRole('MANAGER', 'ADMIN')">
            <input type="button" value="Add Customer"
               onclick="window.location.href='showFormForAdd'; return false;"
               class="add-button"
            />
        </security:authorize>

        <!--  add our html table here -->

        <table>
            <tr>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Email</th>
                <th>Action</th>
            </tr>

            <!-- loop over and print our customers -->
            <c:forEach var="tempCustomer" items="${customers}">

                <!-- construct an "update" link with customer id -->
                <c:url var="updateLink" value="/customer/showFormForUpdate">
                    <c:param name="customerId" value="${tempCustomer.id}"/>
                </c:url>

                <!-- construct an "delete" link with customer id -->
                <c:url var="deleteLink" value="/customer/delete">
                    <c:param name="customerId" value="${tempCustomer.id}"/>
                </c:url>

                <tr>
                    <td> ${tempCustomer.firstName} </td>
                    <td> ${tempCustomer.lastName} </td>
                    <td> ${tempCustomer.email} </td>

                    <security:authorize access="hasAnyRole('MANAGER', 'ADMIN')" >
                        <td>
                            <!-- display the update link -->
                            <a href="${updateLink}">Update</a>
                            |
                            <a href="${deleteLink}"
                                onclick="if (!(confirm('Are you sure you want to delete this customer?'))) return false">Delete</a>
                        </td>
                    </security:authorize>
                </tr>

            </c:forEach>

        </table>

    </div>

</div>


</body>
</html>