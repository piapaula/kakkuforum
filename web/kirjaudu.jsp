<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Kirjaudu</title>
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
<% if (session.getAttribute("nimimerkki") == null) { %>
<div id="container"><br>
    <div>
        <h1>Kirjaudu sisään KakkuForumiin</h1>
        <form method="post" action="KirjautumisServlet">
            Nimimerkki: <input type="text" name="nimimerkki" /><br/>
            Salasana: <input type="password" name="salasana" /><br/><br/>
            <input type="submit" value="Kirjaudu sisään" />
        </form>
        <h3>Jos et ole vielä rekisteröitynyt käyttäjäksi, voit tehdä sen <a href="rekisteroidy.jsp">täällä</a></h3>
    </div>
<% } else {%>
    <div id="container"><br>
        <h1>Olet jo kirjautunut sisään käyttäjänä ${nimimerkki}!</h1>
<% } %>
        <footer>
            <p>Tekijät/Copyright: Titta, Pia, Kristiina ja Riina</p>
            <p>Ota yhteyttä: <a href="mailto:academy@academy.fi">academy@academy.fi</a>.</p>
        </footer>
</body>
</html>
