<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 8.11.2017
  Time: 14.29
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Etsi</title>
    <link rel="stylesheet" href="tyylit.css">
</head>
<body>
<nav>
    <a href="rekisteroidy.jsp">Rekisteröidy</a> |
    <a href="profiili.jsp">Oma profiili</a> |
    <a href="index.jsp">KakkuForum</a> |
    <a href="haku.jsp">Hae sivustolta</a> |
    <a href="UlosServlet">Kirjaudu ulos</a> |
</nav>
<div id="vasen">
    <% if (session.getAttribute("nimimerkki") == null) { %>


    <h1><a href="kirjaudu.jsp">Kirjaudu sisään</a> KakkuForumiin!</h1>

    <% } else {%>

    <h1>Olet kirjautunut sisään käyttäjänä ${nimimerkki}.</h1>
    <% } %>
</div>
<div id="container">
    <br>
<form method="post" action="SearchServlet">
    <table border="0" width="700" align="center" bgcolor="#ffc0cb" cellpadding="10px">
        <tr><td colspan=2 style="font-size:12pt;" align="center">
            <h3>Etsi</h3></td></tr>
        <tr><td align="center"><input type="text" name="search">
            <input  type="submit" value="Etsi"></td></tr>
    </table>
</form>
</div>
<footer>
    <p>Tekijät/Copyright: Titta, Pia, Kristiina ja Riina</p>
    <p>Ota yhteyttä: <a href="mailto:academy@academy.fi">academy@academy.fi</a>.</p>
</footer>
</body>
</html>

