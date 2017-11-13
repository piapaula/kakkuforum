<%@ page import="java.sql.*" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 8.11.2017
  Time: 10.53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Profiili</title>
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
        <h1>Profiili</h1>
        <%
            // Katsotaanko onko käyttäjä kirjautunut sisään. Jos ei, pyydetään kirjautumaan sisään.
            HttpSession istunto = request.getSession(false);
            String nimimerkki = (String) istunto.getAttribute("nimimerkki");
            if (nimimerkki == null) {
        %> <h2>Sinun täytyy <a href="kirjaudu.jsp">kirjautua sisään</a>, jotta voit katsella profiiliasi</h2>
        <% } else if (nimimerkki != null) {
            // Jos käyttäjä on kirjautunut sisään, katsotaan löytyykö kyseiseltä käyttäjältä tietokannasta kuvaus.
            // Jos kuvaus löytyy, näytetään kuvaus.
            String sql = "select nimi, kuvaus from henkilo where nimimerkki = ?";
            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/kakkuforum", "root", "academy");
                PreparedStatement lause = con.prepareStatement(sql);
                lause.setString(1, nimimerkki);
                ResultSet tulos = lause.executeQuery();
                String profiilikuvaus = "";
                String nimi = "";
                while (tulos.next()) {
                    profiilikuvaus = tulos.getString("kuvaus");
                    nimi = tulos.getString("nimi");
                }
                // Jos käyttäjältä ei löydy tietokannasta kuvausta, printataan formi, jolla sen voi lisätä.
                if (profiilikuvaus != null) { %>
        <h2>Käyttäjä:  <%out.print(nimimerkki);%></h2>
        <h2>Nimi:  <%out.print(nimi);%></h2>
        <h2>Profiilikuvaus: <br> <%out.print(profiilikuvaus);%></h2>
        <%
        } else if (profiilikuvaus == null) { %>
        <h2>Käyttäjä:  <%out.print(nimimerkki);%></h2>
        <h2>Nimi:  <%out.print(nimi);%></h2>
        <form action="KuvauksenTallennusServlet" , method="post">
            Et ole vielä lisännyt profiilikuvausta, lisää se tässä: <br> <textarea name="kuvaus" cols="50"
                                                                                   rows="10"></textarea> <br>
            <input type="submit" value="Tallenna"/>
        </form>
        <%
                        con.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        %>
    </div>
</div>
<footer>
    <p>Tekijät/Copyright: Titta, Pia, Kristiina ja Riina</p>
    <p>Ota yhteyttä: <a href="mailto:academy@academy.fi">academy@academy.fi</a>.</p>
</footer>
</body>
</html>