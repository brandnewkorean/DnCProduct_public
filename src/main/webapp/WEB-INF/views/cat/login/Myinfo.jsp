<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>내정보</title>
<style type="text/css">
	#profile{
		margin: 0 auto;
		width: 100px;
		height: 100px;
		border-radius: 50%;
	}
</style>
</head>
<body>
	<div id=profile>
		<input type="hidden" id=image value="${client.profile}">
	</div>
	id : ${cv.id}<br>
	name : ${cv.name}<br>
	birthday : ${cv.birthday}<br>
	email : ${cv.email}<br>
	address : ${cv.address}<br>
	<script src="http://code.jquery.com/jquery-latest.min.js?ver=<%= System.currentTimeMillis() %>"></script>
	<script type="text/javascript" src="resources/script/myinfo.js?ver=<%= System.currentTimeMillis()%>"></script>
</body>
</html>