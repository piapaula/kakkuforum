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

Tällä sivulla näytetään kaikki tietyn alueen viestiketjut. Sivulle ohjaudutaan index.jps-sivulta, ja viestiketjujen tiedot
haetaan PaivitysServletin avulla get-metodia hyödyntäen. Eli attribuutit otsikko ja tulostettavat saadaan PaivitysServletilta.

Tällä viestiketjusivulla näytetään myös mahdollisuus luoda uusi viestiketju.

--%>


<div class="aihesivu">
    <h1>Olet keskustelualueella: <%=request.getAttribute("otsikko")%> </h1>
    <img src="https://bestfriendsforfrosting.com/wp-content/uploads/2016/10/cake-slices-many.jpg" alt="paljon">
    <form action="luoviestiketju.jsp">
        <p>Eikö haluamastasi aiheesta löydy vielä keskustelua?</p>
        <input type="submit" value="Aloita uusi viestiketju"/>
    </form>

    <br>
    <br>
    <br>
    <p><%=request.getAttribute("tulostettavat")%> </p>

</div>

<footer>
    <p>Tekijät/Copyright: Titta, Pia, Kristiina ja Riina</p>
    <p>Ota yhteyttä: <a href="mailto:academy@academy.fi">academy@academy.fi</a>.</p>
</footer>
</body>
</html>