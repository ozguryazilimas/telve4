<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %><!DOCTYPE html>
<%--
  ~ Copyright 2012 Stephen Connolly
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~     http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>
<html>
<head>
    <meta charset="utf-8"/>
    <tags:title/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- The styles -->
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/bootstrap-responsive.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/tobar-segais.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/custom.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/jquery.treeview.css" rel="stylesheet">

    <!-- The HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <!-- The fav and touch icons -->
    <link rel="shortcut icon"
          href="${pageContext.request.contextPath}/img/favicon.ico">
    <link rel="apple-touch-icon-precomposed" sizes="144x144"
          href="${pageContext.request.contextPath}/img/apple-touch-icon-144x144.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114"
          href="${pageContext.request.contextPath}/img/apple-touch-icon-114x114.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72"
          href="${pageContext.request.contextPath}/img/apple-touch-icon-72x72.png">
    <link rel="apple-touch-icon-precomposed"
          href="${pageContext.request.contextPath}/img/apple-touch-icon.png">
</head>
<body>
<%@include file="custom-navbar.jspf"%>
<div class="container-fluid">
<div class="row-fluid">
<div class="span4 no-print">
<div class="well sidebar-nav no-print"><%
    String query = request.getParameter("query");
    String keywordParam = request.getParameter("keywords");
    String contentsActive = "";
    String indexActive = "";
    String searchActive = "";
    if (query != null && query.length() > 0) {
        searchActive = "active";
    } else if (keywordParam != null) {
        indexActive = "active";
    } else {
        contentsActive = "active";
    }
%>
<ul class="nav nav-tabs">
    <li class="<%=contentsActive%>"><a href="#contents-nav" data-toggle="tab"><i class="icon-book"></i>İçindekiler</a>
    </li>
    <li class="<%=indexActive%>"><a href="#index-nav" data-toggle="tab"><i class="icon-list"></i>İndeks</a></li>
    <li class="<%=searchActive%>"><a href="#search-nav" data-toggle="tab"><i class="icon-search"></i>Arama</a></li>
</ul>
<div class="tab-content" id="sidebar-content">
<div class="tab-pane <%=contentsActive%>" id="contents-nav"><tags:toc id="toc"/></div>
<div class="tab-pane <%=indexActive%>" id="index-nav"><tags:keywords/></div>
<div class="tab-pane <%=searchActive%>" id="search-nav"><tags:search/></div>
</div>
</div>
<!--/.well -->
</div>
<!--/span-->
<div class="span8"><tags:contents id="content"/></div>
</div>
</div>
<script src="${pageContext.request.contextPath}/js/jquery-latest.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.treeview.js"></script>
<script src="${pageContext.request.contextPath}/js/tobar-segais.js"></script>
</body>
</html>