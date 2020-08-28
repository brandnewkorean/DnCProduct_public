<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>food</title>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript">
$(function(){
	$('.products').click(function(){
		location.href = "products?productcode="+$(this).attr('id').substr(0,$(this).attr('id').indexOf('_'))+"&seq="+$(this).attr('id').substr($(this).attr('id').indexOf('_')+1);
	});
	
	$('.result3_page').click(function(){
		var i = parseInt($(this).text());
		var str = "${pageMaker3.makeSearch(1)}";
		console.log(str);
		
		$.ajax({
			url: "searchresult3",
			data:{
				currentPage: i,
				perPage: 10,
				keyword: "${search3.keyword}"
			},
			beforeSend: function(){
				$('#result3').append('<div style="position:absolute; left: 50%; top: 25%; width: 100%; height: 100%; z-index: 1;"><img src="resources/image/catloading.gif" style="position:relative; left: -50%; height: 50%;"></div>')
			},
			success: function(result){
				$('#result3').html(result);
			},
		});
	});
});
</script>
</head>
<body>
	<span style="color: red;">
		총 ${pageMaker3.getTotalRow()}개 중 ${list3.size()}개의 상품이 검색되었습니다.
	</span>
	<br>
	<c:choose>
		<c:when test='${list3.size() > 0}'>
			<c:forEach var="pl3" items='${list3}'>
				<div class="products" id="${pl3.productcode}_${pl3.seq}">
					<a class=group>분류 : ${pl3.group1} > ${pl3.group2}</a>
					<div class=product_innerimg>
						<img class=product_image src="resources/productimage/${productimageMap3.get(pl3.seq).get(0).filename}" width=200px height=200px>
					</div>
					${productMap3.get(pl3.seq).name}<br>
					<a class=brand>『${productMap3.get(pl3.seq).brand}』</a><br>
					<a class=price>${priceMap3.get(pl3.seq)}</a>원
				</div>
			</c:forEach>
		</c:when>
		<c:otherwise>결과가 없습니다</c:otherwise>
	</c:choose>
	<div class=blocks>
		<c:if test='${pageMaker3.prev}'>
			<a href='catstoreview${pageMaker3.makeSearch(1)}&group1=${search3.group1}'>First</a>
			<a href='catstoreview${pageMaker3.makeSearch(pageMaker3.startPageNo-1)}&group1=${search3.group1}&group2=${search3.group2}'>Prev&nbsp;</a>
		</c:if>
		<c:forEach begin='${pageMaker3.startPageNo}' end='${pageMaker3.endPageNo}' var="i">
			<c:choose>
				<c:when test='${pageMaker3.search.currentPage==i}'>
					<font size="5" color="orange">${i}</font>&nbsp;
				</c:when>
				<c:otherwise>
					<button class=result3_page>${i}</button>
<%-- 					<a href='searchresult${pageMaker3.makeSearch(i)}'>${i}</a>&nbsp; --%>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		<c:if test='${pageMaker3.next && pageMaker3.endPageNo > 0}'>
			<a href='catstoreview${pageMaker3.makeSearch(pageMaker3.endPageNo+1)}&group1=${search3.group1}&group2=${search3.group2}'>Next&nbsp;&nbsp;</a>
			<a href='catstoreview${pageMaker3.makeSearch(pageMaker3.lastPageNo)}&group1=${search3.group1}&group2=${search3.group2}'>End&nbsp;&nbsp;</a>
		</c:if>
	</div>
</body>
</html>