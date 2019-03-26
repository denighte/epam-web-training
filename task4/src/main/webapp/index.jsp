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
                            <li><a id="1" onclick="selectParser(this)">1 </a></li>
                            <li><a id="2" onclick="selectParser(this)">2 </a></li>
                            <li><a id="3" onclick="selectParser(this)">3 </a></li>
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
    <form id="upload-file" action="hello" method="post" enctype="multipart/form-data">
        <input type="file" name="file" id="upload_hidden"
               onchange="document.getElementById('upload_visible').value = this.value;" />
        <i class="fa fa-4x fa-dropbox" aria-hidden="true" onclick="document.getElementById('upload_hidden').click();"></i>
        <input type="submit" value="<fmt:message key="file_upload" bundle="${lang}"/>" />
    </form>
    <input type="text" readonly id="upload_visible" />
</div>
<footer>
    <div><p><fmt:message key="portal_info" bundle="${lang}"/></p></div>
</footer>
<script src="js/jquery-ui-1.12.1/external/jquery/jquery.js"></script>
<script src="js/jquery-ui-1.12.1/jquery-ui.js"></script>
<script src="js/script.js"></script>
</body>