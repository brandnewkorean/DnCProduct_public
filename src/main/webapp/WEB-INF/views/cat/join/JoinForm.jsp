<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>D&C Join</title>

<link rel="stylesheet" type="text/css" href="resources/css/cat/join/joinForm.css?ver=<%= System.currentTimeMillis()%>">
<script src="resources/script/jquery-3.2.1.min.js"></script>
<script src="resources/script/joincheck.js?ver=<%= System.currentTimeMillis() %>"></script>
</head>
<body>

<img id="joinformimg" onclick="location.href='catmain'" src="resources/image/logoe.png">

<h1>D&C 회원가입</h1>

<form action="join" method="post">
	<div class="jointext">아이디<br>
		<input id="joinid" name="id" type="text">
		<br>
		<span id="idmessage" style="color: red"></span>
	</div>
	<br>
	<div class="jointext">비밀번호<br>
		<input id="joinpw" name="password" type="password">
		<br>
		<span style="color: red" id="pwmessage"></span>
	</div>
	<br>
	<div class="jointext">이름<br>
		<input id="joinname" name="name" type="text">
		<br>
		<span style="color: red" id="namemessage"></span>
	</div>
	<br>
	<div class="jointext">생년월일<br>
		<input id="joinbirthday" name="birthday" type="date" ><br>
		<br>
		<span style="color: red" id="birthdaymessage"></span>
	</div>
	<br>
	<div class="jointext" >E-mail<br> 
		<input id="joinemail" name="email" type="email">
		<br>
		<span style="color: red" id="emailmessage"></span>
	</div>
	<br>
	<div class="jointext">주소<br>
		<input id="joinaddress" name="address" type="text">
		<br>
		<span style="color: red" id="addressmessage"></span>
	</div>
	<br>
	<br><br>
<button type="submit" onclick="return allCheck()">확인</button>
<button onclick="location.href='catmain'">취소</button>
<br><br><br>
</form>
</body>
</html>