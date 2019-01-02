<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!doctype html>
<html>

<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>会员登录</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" type="text/css"/>
    <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
    <!-- 引入自定义css文件 style.css -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css"/>

    <style>
        body {
            margin-top: 20px;
            margin: 0 auto;
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
</div>

<div class="container">
    <div class="row">

        <div style="margin:0 auto; margin-top:10px;width:950px;">
            <strong>我的订单</strong>
            <table class="table table-bordered">
                <c:forEach items="${page.records}" var="order">
                    <tbody>
                    <tr class="success">
                        <th colspan="5">
                            订单编号:${order.oid} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            总金额：￥${order.total}元 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <c:if test="${order.state == 1}"><a href="${pageContext.request.contextPath}/OrderServlet?method=payOrderWithOid&oid=${order.oid}" style="color: red">待付款</a></c:if>
                            <c:if test="${order.state == 2}"><font color="#ff4500">已付款待发货</font></c:if>
                            <c:if test="${order.state == 3}"><a href="javascript:void(0);" id="signIn" oid="${order.oid}"><font color="orange">待签收</font></a></c:if>
                            <c:if test="${order.state == 4}"><font color="green">已签收</font></c:if>
                        </th>
                    </tr>
                    <tr class="warning">
                        <th>图片</th>
                        <th>商品</th>
                        <th>价格</th>
                        <th>数量</th>
                        <th>小计</th>
                    </tr>
                    <c:forEach items="${order.orderItems}" var="item">
                        <tr class="active">
                            <td width="60" width="30%">
                                <input type="hidden" name="id" value="22">
                                <img src="${pageContext.request.contextPath}/${item.product.pimage}" width="70"
                                     height="60">
                            </td>
                            <td width="30%">
                                <a target="_blank">${item.product.pname}</a>
                            </td>
                            <td width="15%">
                                ￥${item.product.shop_price}
                            </td>
                            <td width="10%">
                                    ${item.quantity}
                            </td>
                            <td width="15%">
                                <span class="subtotal">￥${item.total}</span>
                            </td>

                        </tr>
                    </c:forEach>

                    </tbody>
                </c:forEach>
            </table>
        </div>
    </div>

    <%@include file="pageFile.jsp" %>

</div>

<div class="container">
    <%@include file="footer.jsp" %>
</div>
</body>
<script type="text/javascript">
    $(function () {
        $("#signIn").click(function () {
            var oid = $(this).attr("oid");
            window.location = "${pageContext.request.contextPath}/OrderServlet?method=updateOrderState&state=4&oid="+oid;
        });
    });
</script>
</html>