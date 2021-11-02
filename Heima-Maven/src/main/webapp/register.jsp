<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>会员注册</title>
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
<script src="js/jquery-1.11.3.min.js" type="text/javascript"></script>
<script src="js/bootstrap.min.js" type="text/javascript"></script>
<!-- 引入自定义css文件 style.css -->
<link rel="stylesheet" href="css/style.css" type="text/css" />

<style>
body {
	margin-top: 20px;
	margin: 0 auto;
}

.carousel-inner .item img {
	width: 100%;
	height: 300px;
}

font {
	color: #3164af;
	font-size: 18px;
	font-weight: normal;
	padding: 0 10px;
}
/*#span{
	font-size: 1px;
}*/
</style>

	<script type="text/javascript">
		// 对文本框定义一个函数，对其规则进行判定
		/*function checkUsername(){
			// 获取username的值
			var username = $("#username").val();
			//定义正则
			var str = /^\w{2,12}$/;
			// 匹配
			var flag = str.test(username);
			//alert(flag);
			if (flag){
				// 用户名合格，发送异步提交，查询数据库，该用户名是否存在
				$.get("/user/checkUsername",{"username":username},/!*$(this).serialize(),*!/function (data){
					//request.get();
					//alert(username);
					alert(data);
					if (data.flag){
						// 表示该用户名不存在，可以用
						$("#username").css("border","");
					}else {
						// 用户名已存在
						$("#username").css("border","1px solid red");
						flag = false;
					};
				})
			}else {
				$("#username").css("border","1px solid red");
			}
			return flag;
		};
		// 失去焦点时，调用该函数
		$(function (){
			// 页面加载完毕后执行
			//alert("hhhh");
			$("#username").blur(checkUsername);
		});*/
		// 为每个文本框定义基本的规则
		function checkUsername(){
			// 获取username属性值
			var username = $("#username").val();
			// 正则
			var reg = /^\w{2,12}$/;
			// 匹配
			var flag = reg.test(username);
			if (flag){
				// 命名合格，发送ajax请求到后台，看用户名是否已经存在
				$.get("${pageContext.request.contextPath}/user/checkUsername",{"username":username},function (data){
					//alert(data)
					if (data.flag){
						//  表示该用户名可用
						$("#username").css("border","");
					}else {
						// 表示用户名已存在，提示更换
						$("#span").html(data.errorMsg);

					}
				});
			}else {
				// 不合格，框变红
				$("#username").css("border","1px solid red");
			};
			return flag;
		};
		function checkPassword(){
			var password = $("#password").val();
			var reg = /^\w{3,12}$/;
			var flag = reg.test(password);
			if (flag){
				$("#password").css("border","");
			}else {
				$("#password").css("border","1px solid red");
			}
			return flag;
		};
		function checkConfirmPassword(){
			var confirmpassword = $("#confirmpwd").val();
			var password = $("#password").val();
			if (confirmpassword==password){
				$("#confirmpwd").css("border","");
			}else {
				$("#confirmpwd").css("border","1px solid red");
			}
		};

		$(function (){
			// 页面加载完毕，触发鼠标失去焦点事件
			$("#username").blur(checkUsername);
			$("#password").blur(checkPassword);
			$("#confirmpwd").blur(checkConfirmPassword);
			// username文本框重新获得焦点后，清空span标签体信息
			$("#username").focus(function (){
				$("#span").html("");
			})
		});

	</script>

</head>
<body>

	<!-- 引入header.jsp -->
	<jsp:include page="/header.jsp"></jsp:include>

	<div class="container"
		style="width: 100%; background: url('image/regist_bg.jpg');">
		<div class="row">
			<div class="col-md-2"></div>
			<div class="col-md-8"
				style="background: #fff; padding: 40px 80px; margin: 30px; border: 7px solid #ccc;">
				<font>会员注册</font>USER REGISTER
				<form class="form-horizontal" style="margin-top: 5px;" action="${pageContext.request.contextPath}/user/register" method="post">
					<div class="form-group">
						<label for="username" class="col-sm-2 control-label">用户名</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="username" name="username"
								placeholder="请输入用户名"><span id="span" style="font-size: 1px;color: red"></span>
						</div>
					</div>
					<div class="form-group">
						<label for="password" class="col-sm-2 control-label">密码</label>
						<div class="col-sm-6">
							<input type="password" class="form-control" id="password" name="password"
								placeholder="请输入密码">
						</div>
					</div>
					<div class="form-group">
						<label for="confirmpwd" class="col-sm-2 control-label">确认密码</label>
						<div class="col-sm-6">
							<input type="password" class="form-control" id="confirmpwd" name="repassword"
								placeholder="请输入确认密码">
						</div>
					</div>
					<div class="form-group">
						<label for="inputEmail3" class="col-sm-2 control-label">Email</label>
						<div class="col-sm-6">
							<input type="email" class="form-control" id="inputEmail3" name="email"
								placeholder="Email">
						</div>
					</div>
					<div class="form-group">
						<label for="usercaption" class="col-sm-2 control-label">姓名</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="usercaption" name="name"
								placeholder="请输入姓名">
						</div>
					</div>
					<div class="form-group opt">
						<label for="inlineRadio1" class="col-sm-2 control-label">性别</label>
						<div class="col-sm-6">
							<label class="radio-inline">
								<input type="radio"
								name="sex" id="inlineRadio1" value="male">
								男
							</label> <label class="radio-inline"> <input type="radio"
								name="sex" id="inlineRadio2" value="female">
								女
							</label>
						</div>
					</div>
					<div class="form-group">
						<label for="date" class="col-sm-2 control-label">出生日期</label>
						<div class="col-sm-6">
							<input type="date" class="form-control" id="date" name="birthday">
						</div>
					</div>

					<div class="form-group">
						<label for="date" class="col-sm-2 control-label">验证码</label>
						<div class="col-sm-3">
							<input type="text" class="form-control">

						</div>
						<div class="col-sm-2">
							<img src="./image/captcha.jhtml" />
						</div>

					</div>

					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<input type="submit" width="100" value="注册" name="submit"
								style="background: url('./images/register.gif') no-repeat scroll 0 0 rgba(0, 0, 0, 0); height: 35px; width: 100px; color: white;">
						</div>
					</div>
				</form>
			</div>

			<div class="col-md-2"></div>

		</div>
	</div>

	<!-- 引入footer.jsp -->
	<jsp:include page="/footer.jsp"></jsp:include>

</body>
</html>




