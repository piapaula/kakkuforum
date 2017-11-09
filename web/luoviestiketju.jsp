<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 8.11.2017
  Time: 1.52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Aloita keskustelu uudesta aiheesta</title>
    <link rel="stylesheet" href="tyylit.css">
</head>
<body>

<%--

Tämä sivulla voi luoda uuden viestiketjun tietylle viestialueelle (kuppikakut, hääkakut, täytekakut, yleinen).
Viestiketjun voi luoda vain kirjautunut käyttäjä, mikä ilmaistaan alla koodissa if/else-lauseella.

--%>

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
    <div id="formi">
    <% if (session.getAttribute("nimimerkki") == null) { %>

    <h1><a href="kirjaudu.jsp">Kirjaudu sisään</a> KakkuForumiin aloittaaksesi uuden viestiketjun</h1>

    <% } else {%>

    <img src="https://1.soompi.io/wp-content/uploads/2015/03/keyboard-waffle-korea-540x540.jpg" alt="herkkunäppäimistö"/>


    <form action="KetjunLuontiServlet" method="post">
        <br>Kirjoita viestiketjun aihe<br>
        <textarea name="otsikko" cols="50" rows="2"></textarea>
        <br>
        <br>Kirjoita aloitusviesti<br>
        <textarea name="viesti" cols="100" rows="10"></textarea>
        <br>
        <br>Aloita keskustelu alueella<br>
        <select name="viestialue">
            <option value="yleinen">Yleistä kakkukeskustelua</option>
            <option value="kuppikakut">Kuppikakut</option>
            <option value="haakakut">Hääkakut</option>
            <option value="taytekakut">Täytekakut</option>
        </select>
        <br>
        <br><br>
        <input type="submit" value="Lähetä viesti"/>
    </form>

    <% } %>

</div>
</div>
<footer>
    <p>Tekijät/Copyright: Titta, Pia, Kristiina ja Riina</p>
    <p>Ota yhteyttä: <a href="mailto:academy@academy.fi">academy@academy.fi</a>.</p>
</footer>
</body>
</html>