<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!--
描述：菜单栏
-->
<div class="container-fluid">
    <div class="col-md-4">
        <a href="${pageContext.request.contextPath}/index.jsp"><img src="${pageContext.request.contextPath}/img/logo2.png"/></a>
    </div>
    <div class="col-md-5">
        <img src="${pageContext.request.contextPath}/img/header.png"/>
    </div>
    <div class="col-md-3" style="padding-top:20px">
        <ol class="list-inline">
            <%--用户已登录，显示欢迎信息--%>
            <c:if test="${not empty user}">
                <li>${user.username}</li>
                <li><a href="${pageContext.request.contextPath}/UserServlet?method=loginOut">退出</a></li>
            </c:if>
            <%--用户未登陆，显示登陆，注册信息--%>
            <c:if test="${empty user}">
                <li><a href="${pageContext.request.contextPath}/jsp/login.jsp">登录</a></li>
                <li><a href="${pageContext.request.contextPath}/UserServlet?method=registUI">注册</a></li>
            </c:if>

            <li><a href="${pageContext.request.contextPath}/jsp/cart.jsp">购物车</a></li>
            <li><a href="${pageContext.request.contextPath}/OrderServlet?method=showMyOrders&num=1">我的订单</a></li>
        </ol>
    </div>
</div>
<!--
描述：导航条
-->
<div class="container-fluid">
    <nav class="navbar navbar-inverse">
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                        data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="${pageContext.request.contextPath}/index.jsp">首页</a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav" id="categoryul">

                </ul>
                <form class="navbar-form navbar-right" role="search">
                    <div class="form-group">
                        <input type="text" class="form-control" placeholder="Search">
                    </div>
                    <button type="submit" class="btn btn-default">Submit</button>
                </form>

            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container-fluid -->
    </nav>
</div>

<script>
    $(function () {
        $.post("${pageContext.request.contextPath}/IndexServlet",{method:"findAllCats"},function (data,status) {
            // data:jsonstr字符串==>js对象
            $(data).each(function (i,item)  {
                $("#categoryul").append("<li><a href=\"${pageContext.request.contextPath}/ProductServlet?method=list&cid="+item.cid+"&page=1\">"+item.cname+"<span\n" +
                    "                            class=\"sr-only\">(current)</span></a></li>");
            });
        },"json");
    });
</script>