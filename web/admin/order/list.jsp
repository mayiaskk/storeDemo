<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<HTML>
<HEAD>
    <meta http-equiv="Content-Language" content="zh-cn">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="${pageContext.request.contextPath}/css/Style1.css" rel="stylesheet" type="text/css"/>
    <script language="javascript" src="${pageContext.request.contextPath}/js/public.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js"></script>
</HEAD>
<body>
<br>
<form id="Form1" name="Form1" action="#" method="post">
    <table cellSpacing="1" cellPadding="0" width="100%" align="center" bgColor="#f5fafe" border="0">
        <TBODY>
        <tr>
            <td class="ta_01" align="center" bgColor="#afd1f3">
                <strong>订单列表</strong>
            </TD>
        </tr>

        <tr>
            <td class="ta_01" align="center" bgColor="#f5fafe">
                <table cellspacing="0" cellpadding="1" rules="all"
                       bordercolor="gray" border="1" id="DataGrid1"
                       style="BORDER-RIGHT: gray 1px solid; BORDER-TOP: gray 1px solid; BORDER-LEFT: gray 1px solid; WIDTH: 100%; WORD-BREAK: break-all; BORDER-BOTTOM: gray 1px solid; BORDER-COLLAPSE: collapse; BACKGROUND-COLOR: #f5fafe; WORD-WRAP: break-word">

                    <tr
                            style="FONT-WEIGHT: bold; FONT-SIZE: 12pt; HEIGHT: 25px; BACKGROUND-COLOR: #afd1f3">

                        <td align="center" width="5%">
                            序号
                        </td>
                        <td align="center" width="15%">
                            订单编号
                        </td>
                        <td align="center" width="10%">
                            订单金额
                        </td>
                        <td align="center" width="10%">
                            收货人
                        </td>
                        <td align="center" width="10%">
                            订单状态
                        </td>
                        <td align="center" width="50%">
                            订单详情
                        </td>
                    </tr>
                    <c:forEach items="${page.records}" var="o" varStatus="status">
                        <tr onmouseover="this.style.backgroundColor = 'white' "
                            onmouseout="this.style.backgroundColor = '#F5FAFE';">
                            <td style="CURSOR: hand; HEIGHT: 22px" align="center"
                                width="5%">
                                    ${status.count}
                            </td>
                            <td style="CURSOR: hand; HEIGHT: 22px" align="center"
                                width="15%">
                                    ${o.oid}
                            </td>
                            <td style="CURSOR: hand; HEIGHT: 22px" align="center"
                                width="10%">
                                    ${o.total}
                            </td>
                            <td style="CURSOR: hand; HEIGHT: 22px" align="center"
                                width="10%">
                                    ${o.name}
                            </td>
                            <td style="CURSOR: hand; HEIGHT: 22px" align="center"
                                width="10%">
                                <c:if test="${o.state == 1}">未付款</c:if>
                                <c:if test="${o.state == 2}"><a style="color: red;" class="button" href="${pageContext.request.contextPath}/OrderManageServlet?method=updateOrderState&oid=${o.oid}">发货</a></c:if>
                                <c:if test="${o.state == 3}"><font style="color: orange">已发货</font></c:if>
                                <c:if test="${o.state == 4}"><font style="color: green">订单完成</font></c:if>
                            </td>
                            <td align="center" style="HEIGHT: 22px" width="50%">
                                <input type="button" value="订单详情" class="OrderDescBtn" id="${o.oid}"/>
                                <table border="1" width="100%">
                                </table>
                            </td>

                        </tr>
                    </c:forEach>
                </table>
            </td>
        </tr>
        <tr>
            <td align="center">
                <%@include file="/jsp/pageFile.jsp" %>
            </td>
        </tr>
        </TBODY>
    </table>
</form>
</body>
<script type="text/javascript">
    $(function () {
        $(".OrderDescBtn").click(function () {
            var $tb = $(this).next();
            $tb.html("");

            if (this.value === "订单详情") {
                $tb.append("<tr><th>图片</th><th>商品</th><th>价格</th><th>数量</th></tr>");
                var d = {method:"showOrderDetail",oid:this.id};

                $.post("OrderManageServlet",d,function (data) {
                    $(data).each(function (i, obj) {
                        $tb.append("<tr><td><img width='40' height='45' src='${pageContext.request.contextPath}/"+obj.product.pimage+"'/></td><td>"+obj.product.pname+"</td><td>"+obj.product.shop_price+"</td><td>"+obj.quantity+"</td></tr>");
                    });
                },"json");

                this.value="关闭";
            }else if (this.value === "关闭") {
                this.value="订单详情";
            }
        });
    });
</script>
</HTML>

