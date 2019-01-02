<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!doctype html>
<html>

<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>商品列表</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" type="text/css"/>
    <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
    <!-- 引入自定义css文件 style.css -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css"/>

    <style>
        body {
            margin-top: 20px;
            margin: 0 auto;
            width: 100%;
        }

        .carousel-inner .item img {
            width: 100%;
            height: 300px;
        }
    </style>
</head>

<body>

<div class="container">
    <%@include file="header.jsp" %>

<c:if test="${not empty pageBean.products}">
    <div class="row" style="width:1210px;margin:0 auto;">

        <c:forEach items="${pageBean.products}" var="item">
            <div class="col-md-2">
                <a href="${pageContext.request.contextPath}/ProductServlet?method=productInfo&pid=${item.pid}">
                    <img src="${pageContext.request.contextPath}/${item.pimage}" width="130"
                         style="display: inline-block;">
                </a>
                <a href="${pageContext.request.contextPath}/ProductServlet?method=productInfo&pid=${item.pid}"
                   style='color:green'><p
                        style="white-space: nowrap;width: 100%;overflow: hidden; text-overflow: ellipsis">${item.pname}</p>
                </a>
                <p><font color="#FF0000">商城价：&yen;${item.shop_price}</font></p>
                <s>&yen;${item.market_price}</s>
            </div>
        </c:forEach>

    </div>

    <!--分页 -->
    <div style="width:380px;margin:0 auto;margin-top:50px;">
        <ul class="pagination" style="text-align:center; margin-top:10px;">
            <li class=<c:if test="${pageBean.page == 1}">"disabled"</c:if>><a
                    href="${pageContext.request.contextPath}/ProductServlet?method=list&cid=${pageBean.cid}&page=${pageBean.page-1}"
                    aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
            <c:if test="${pageBean.startPage != 0}">
                <li>
                    <a href="${pageContext.request.contextPath}/ProductServlet?method=list&cid=${pageBean.cid}&page=${pageBean.startPage}">${pageBean.startPage}</a>
                </li>
            </c:if>
            <c:if test="${pageBean.prePage != 0}">
                <li>
                    <a href="${pageContext.request.contextPath}/ProductServlet?method=list&cid=${pageBean.cid}&page=${pageBean.prePage}">${pageBean.prePage}</a>
                </li>
            </c:if>
            <li class="active">
                <a href="${pageContext.request.contextPath}/ProductServlet?method=list&cid=${pageBean.cid}&page=${pageBean.page}">${pageBean.page}</a>
            </li>
            <c:if test="${pageBean.nextPage != 0}">
                <li>
                    <a href="${pageContext.request.contextPath}/ProductServlet?method=list&cid=${pageBean.cid}&page=${pageBean.nextPage}">${pageBean.nextPage}</a>
                </li>
            </c:if>
            <c:if test="${pageBean.lastPage != 0}">
                <li>
                    <a href="${pageContext.request.contextPath}/ProductServlet?method=list&cid=${pageBean.cid}&page=${pageBean.lastPage}">${pageBean.lastPage}</a>
                </li>
            </c:if>
            <li class=<c:if test="${pageBean.page == pageBean.totalPages}">"disabled"</c:if>><a
                    href="${pageContext.request.contextPath}/ProductServlet?method=list&cid=${pageBean.cid}&page=${pageBean.page+1}"
                    aria-label="Previous"><span aria-hidden="true">&raquo;</span></a>
            </li>
        </ul>
    </div>
    <!-- 分页结束=======================        -->
</c:if>
<c:if test="${empty pageBean.products}">
    <h1>该类别暂时没有商品！</h1>
</c:if>
    <!--
           商品浏览记录:
    -->
    <div style="width:1210px;margin:0 auto; padding: 0 9px;border: 1px solid #ddd;border-top: 2px solid #999;height: 246px;">

        <h4 style="width: 50%;float: left;font: 14px/30px " 微软雅黑 ";">浏览记录</h4>
        <div style="width: 50%;float: right;text-align: right;"><a href="">more</a></div>
        <div style="clear: both;"></div>

        <div style="overflow: hidden;">

            <ul style="list-style: none;">
                <li style="width: 150px;height: 216;float: left;margin: 0 8px 0 0;padding: 0 18px 15px;text-align: center;">
                    <img src="${pageContext.request.contextPath}/products/1/cs10001.jpg" width="130px" height="130px"/>
                </li>
            </ul>

        </div>
    </div>


    <%@include file="footer.jsp" %>
</div>

</body>
<script>


    $(".disabled").click(function (event) {
        event.preventDefault();
    });
</script>
</html>