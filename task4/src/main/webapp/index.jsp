<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<fmt:requestEncoding value="UTF-8" />
<fmt:setLocale value="${sessionScope.sessionLocale}"/>
<fmt:setBundle basename="locale.lc" var="lang"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>example</title>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
    <link rel="stylesheet" type="text/css" href="css/font-awesome-4.7.0/css/font-awesome.css">
    <%--favicon.ico error fix--%>
    <link rel="shortcut icon" href="">
</head>
<body>
<div class="navbar">
    <a id="portal-home" href="#top"><i class="fa fa-home fa-lg"></i></a>
    <nav id="settings" class="dws-menu">
        <ul>
            <li>
                <div>
                    <a id="search-settings-icon" ><i class="fa fa-cog fa-fw fa-lg"></i></a>
                </div>
                <ul>
                    <li>
                        <a><fmt:message key="parser" bundle="${lang}"/></a>
                        <ul>
                            <li><a id="DOM" onclick="selectParser(this)">DOM </a></li>
                            <li><a id="SAX" onclick="selectParser(this)">SAX </a></li>
                            <li><a id="StAX" onclick="selectParser(this)">StAX </a></li>
                        </ul>
                    </li>
                    <li>
                        <a><fmt:message key="locale" bundle="${lang}"/></a>
                        <ul>
                            <li><a id="en" onclick="selectLocale(this)"><fmt:message key="locale_en" bundle="${lang}"/></a></li>
                            <li><a id="ru" onclick="selectLocale(this)"><fmt:message key="locale_ru" bundle="${lang}"/></a></li>
                        </ul>
                    </li>
                </ul>
            </li>
        </ul>
    </nav>
</div>
<div class="file-drop" align="right">
    <form id="upload-files" action="process" method="post" enctype="multipart/form-data">
        <input type="file" name="file" id="upload_hidden" multiple />
        <input id="parser_type" name="parser_type" type="hidden" value="1">
        <div>
            <i id="file-select" class="fa fa-4x fa-dropbox" aria-hidden="true"></i>
        </div><br/>
        <input type="submit" value="<fmt:message key="file_upload" bundle="${lang}"/>" />
    </form>
    <p id="upload_visible"></p>
</div>
<div id="table_holder" class="table-holder" align="center">
</div>
<footer>
    <div><p><fmt:message key="portal_info" bundle="${lang}"/></p></div>
</footer>
</body>
<input id="upload_success" type="hidden" value="<fmt:message key="upload_success" bundle="${lang}"/>">
<input id="upload_empty_error" type="hidden" value="<fmt:message key="upload_empty_error" bundle="${lang}"/>">
<input id="upload_full_error" type="hidden" value="<fmt:message key="upload_full_error" bundle="${lang}"/>">
<input id="upload_few_error" type="hidden" value="<fmt:message key="upload_few_error" bundle="${lang}"/>">
<input id="upload_error" type="hidden" value="<fmt:message key="upload_error" bundle="${lang}"/>">
<input id="validation_ok" type="hidden" value="<fmt:message key="validation_ok" bundle="${lang}"/>">
<script src="js/jquery-ui-1.12.1/external/jquery/jquery.js"></script>
<script src="js/jquery-ui-1.12.1/jquery-ui.js"></script>
<script src="js/script.js"></script>
</html>