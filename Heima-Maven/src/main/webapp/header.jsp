<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 登录 注册 购物车... -->
<div class="container-fluid">
	<div class="col-md-4">
		<img src="${pageContext.request.contextPath}/img/logo2.png" />
	</div>
	<div class="col-md-5">
		<img src="${pageContext.request.contextPath}/img/header.png" />
	</div>
	<div class="col-md-3" style="padding-top:20px">

		<ol class="list-inline">
			<c:if test="${empty user}">
				<li><a href="login.jsp">登录</a></li>
				<li><a href="register.jsp">注册</a></li>
			</c:if>
			<c:if test="${!empty user}">
				<span>欢迎你，${user.name}</span>
				<li><a href="cart.jsp">购物车</a></li>
				<%--查询出所有订单--%>
				<li><a href="${pageContext.request.contextPath}/product/myOrders">我的订单</a></li>
				<li><a href="${pageContext.request.contextPath}/user/exit">退出</a></li>
			</c:if>
		</ol>
	</div>
</div>

<!-- 导航条 -->
<div class="container-fluid">
	<nav class="navbar navbar-inverse">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="${pageContext.request.contextPath}/index.jsp">首页</a>
			</div>

			<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
				<c:if test="${!empty user}">
					<ul class="nav navbar-nav" id="categoryShow">
						<%--<li class="active"><a href="product_list.htm">手机数码<span class="sr-only">(current)</span></a></li>
						<li><a href="#">电脑办公</a></li>
						<li><a href="#">电脑办公</a></li>
						<li><a href="#">电脑办公</a></li>--%>
					</ul>
				</c:if>
				<form class="navbar-form navbar-right" role="search">
					<div class="form-group">
						<input type="text" class="form-control" placeholder="Search">
					</div>
					<button type="submit" class="btn btn-default">Submit</button>
				</form>
			</div>
		</div>
	</nav>
</div>
<script type="text/javascript">
	// 需要页面加载完毕后，将”手机数码“等信息展示出来
	$(function (){
		var context = "";
		$.post("${pageContext.request.contextPath}/category/categoryShow",function (data){
			// 展示
			<%--<li class="active"><a href="product_list.htm">手机数码<span class="sr-only">(current)</span></a></li>--%>
			//alert(${param.get(categoryList)});
			//alert(data.length):9
			for (var i=0;i<data.length;i++){
				// 而且每点击一个需要到数据库中查询信息，出来展示，将id携带过去，根据cid查找product表
				context+='<li class="active"><a href="${pageContext.request.contextPath}/product/productShow?cid='+data[i].cid+'">'+data[i].cname+'</a></li>';
			}
			// 将字符串添加到合适的位置展示
			$("#categoryShow").html(context);
		})
	})
</script>