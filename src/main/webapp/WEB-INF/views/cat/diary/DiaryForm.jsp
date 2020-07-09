<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="resources/css/cat/diary/DiaryForm.css?ver=<%= System.currentTimeMillis()%>">
<meta charset="UTF-8">
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript" src="resources/script/diary.js?ver=<%= System.currentTimeMillis()%>"></script>
<title>Diary</title>
</head>
<body>
	<div class="header">
		<img id="catmainlogo" src="resources/image/logob.png" width=7%>
	</div>
	<div class=intro>
		
	</div>
	<div class=container>
		<div id=diary>
			<div id=paper_range>
				<div id=calendar_wrap>
					<button class=year_btn>-</button><a id=year></a><button class=year_btn>+</button>
					<button class=month_btn>-</button><a id=month></a><button class=month_btn>+</button>
					<div id=calendar></div>
				</div>
				<a id=date></a>
				<textarea id=write placeholder="여기에 내용을 입력해주세요"></textarea>
				<button>글쓰기</button>
			</div>
		</div>
	</div>
</body>
</html>