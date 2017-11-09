<%@ page import="javax.sql.*" %>
<%@ page import="javax.annotation.Resource" %>
<%@ page import="java.sql.*" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 7.11.2017
  Time: 13.51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Kuppikakut</title>
    <link rel="stylesheet" href="tyylit.css">
    <script src="toiminnallisuudet.js"></script>
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

<%--

Tämä sivu näyttää kommenttiketjut. Tiedot (attribuutit) haetaan KommenttiServletiltä. Haettavat attribuutit ovat aloitusviesti, joka näytetään sivulla
ylimmäisenä. Aloitusviestin alta löytyy kommenttikenttä, johon on mahdollista kirjoittaa viesti. Kentän submit-nappia painamamalla päivitetään lisätty
kommentti tietokantaan sekä lisätään sivulle hyödyntäen KommenttiServletin post-metodia.

--%>

<div class="aihesivu">
    <div class="alkuperainen">
        <p><%=request.getAttribute("aloitusviesti")%> </p>
    </div>

    <%-- luodaan viestikenttä, jonka tiedot lähetetään KommenttiServletille post-metodia hyödyntäen --%>

    <img src="https://1.soompi.io/wp-content/uploads/2015/03/keyboard-waffle-korea-540x540.jpg" alt="herkkunäppäimistö"/>

    <form action="KommenttiServlet" method="post">
        <br>Kirjoita vastauksesi alle:<br>
        <textarea name="kommenttikentta" cols="100" rows="10"></textarea>
        <input name="idviesti" value ="<%=request.getAttribute("id")%>"/>
        <input type="hidden" name="kommentoija" value="<%=session.getAttribute("nimimerkki")%>"/>
        <br>
        <input type="submit" value="Lähetä viesti"/>
    </form>
    <br>
    <h1>Alla viestiin tulleet kommentit</h1>
    <div class="kommenttistyle">
        <p><%=request.getAttribute("kommentit")%> </p>
    </div>

    <h2>Palaa alueelle:</h2>
    <form action="PaivitysServlet" method="get">
        <select name="viestialue">
            <option value="yleinen">Yleistä kakkukeskustelua</option>
            <option value="kuppikakut">Kuppikakut</option>
            <option value="haakakut">Hääkakut</option>
            <option value="taytekakut">Täytekakut</option>
            </option>
        </select>
        <input type="submit" value="Klikkaa itsesi alueelle!"/>
    </form>

</div>
<footer>
    <p>Tekijät/Copyright: Titta, Pia, Kristiina ja Riina</p>
    <p>Ota yhteyttä: <a href="mailto:academy@academy.fi">academy@academy.fi</a>.</p>
</footer>
</body>
</html>