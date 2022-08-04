<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>    
<html lang="en">
<head>
	<title>Simple WebApp</title>
	<jsp:include page="section/header.jsp">
		<jsp:param name="title" value="Simple Web App"/>
	</jsp:include>
</head>	
<body>
	<div>
		<div>
			<h1>Simple WebApp</h1><br/>	
			    
			 <c:set var="loggedin" value="${not empty pageContext.request.userPrincipal}"/>   
	
			 <c:choose>
			  <c:when test="${not loggedin}">
		 	  	You are not logged in. <a href="<c:url value = "/login"/>">Click here</a> to login.			  
			  </c:when>
			  <c:when test="${loggedin}">
		      		Logged in as <strong> <c:out value="${pageContext.request.userPrincipal.name}"/> </strong>.<br/>
					<ul>
						<li><a href="<c:url value = "/user"/>">Click Here</a> to go to user profile.</li>
						<li><a href="<c:url value = "/logout"/>">Click Here</a> to logout.</li>
					</ul>			  
			  </c:when>			  
			 </c:choose>			 
		      
		</div>
	</div>
</body>
</html>