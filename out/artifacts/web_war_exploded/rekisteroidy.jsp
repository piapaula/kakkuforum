<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 8.11.2017
  Time: 16.18
  To change this template use File | Settings | File Templates.
--%>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 7.11.2017
  Time: 11.45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Rekisteröidy</title>
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
    <div>
        <h1>Rekisteröidy KakkuForumin käyttäjäksi</h1>
        <form action="RekServlet" method="post">
            Nimi: <input type="text" name="nimi" value=""/> <br>
            Nimimerkki: <input type="text" name="nimimerkki" value=""/> <br>
            Salasana: <input type="password" name="salasana" value=""/> <br>
            <input type="submit" value="Rekisteröidy"/>
        </form>
        <h1>Jos olet jo rekisteröitynyt käyttäjäksi, voit kirjautua sisään <a href="kirjaudu.jsp">täällä</a></h1>
    </div>
</div>
<footer>
    <p>Tekijät/Copyright: Titta, Pia, Kristiina ja Riina</p>
    <p>Ota yhteyttä: <a href="mailto:academy@academy.fi">academy@academy.fi</a>.</p>
</footer>
</body>