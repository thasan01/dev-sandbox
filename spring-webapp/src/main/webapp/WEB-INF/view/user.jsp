<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>    
<html lang="en">
<head>
	<title>User Page</title>
	<jsp:include page="section/header.jsp"/>
</head>	
<body>
	<div>
		<div>
			<h1>Simple WebApp</h1><br/>	
			User <strong><c:out value="${authUser.name}"/></strong><br/>	
			Access: <c:out value="${authUser.authorities}"/><br/>
			<a href="<c:url value = "/"/>">Click here</a> to return home.
		</div>
	</div>
</body>
</html>