<%-- Created by IntelliJ IDEA. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>KakkuForum</title>
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
    <article>
      <h1>Tervetuloa KakkuForumille!</h1>
      <img src="https://images.pexels.com/photos/227432/pexels-photo-227432.jpeg?w=1260&h=750&auto=compress&cs=tinysrgb" alt="Palakakkua"/>
      <img src="https://images.pexels.com/photos/585581/pexels-photo-585581.jpeg?w=1260&h=750&auto=compress&cs=tinysrgb" alt="Kakkukuva"/>

      <p>
        <b>"Let them eat cake."</b>
        <br>
        <br>
        Tämä sivusto on syntynyt rakkaudesta jälkiruokaan. <br> Sukella sisään herkkujen ihmeelliseen maailmaan meidän muiden
        sydämensä herkuille menettäneiden kanssa <br> ja nauti kakkujen, muffinsien, vanukkaiden, karkkien, piirakoiden ja lettujen
        juhlasta!
        <br>
        <br>
        Tältä foorumilta löydät reseptivinkkejä arkeen ja juhlaan, erikoisruokavalioihin ja teemabileisiin, <br> tarinoita ja kuvia
        jäsentemme keittiöistä sekä sokerikuorrutettua vertaistukea kokkailun iloihin ja suruihin.
      </p>
      <br>
      <br>
    </article>

    <form action="PaivitysServlet" method="get">
      <img src="https://images.pexels.com/photos/69817/france-confectionery-raspberry-cake-fruit-69817.jpeg?w=1260&h=750&auto=compress&cs=tinysrgb" alt="Yleinen kakkukeskustelu" style="width:150px;height:110px;border:0;">
      <br>
      <input type="hidden" name="viestialue" value="yleinen"/>
      <input type="submit" value="Yleistä kakkukeskustelua"/>
    </form>
    <form action="PaivitysServlet" method="get">
      <img src="https://images.pexels.com/photos/140831/pexels-photo-140831.jpeg?w=1260&h=750&auto=compress&cs=tinysrgb" alt="Täytekakut" style="width:150px;height:110px;border:0;">
      <br>
      <input type="hidden" name="viestialue" value="taytekakut"/>
      <input type="submit" value="Täytekakut"/>
    </form>
    <form action="PaivitysServlet" method="get">
      <img src="https://images.pexels.com/photos/265801/pexels-photo-265801.jpeg?w=1260&h=750&auto=compress&cs=tinysrgb" alt="Hääkakut" style="width:150px;height:110px;border:0;">
      <br>
      <input type="hidden" name="viestialue" value="haakakut"/>
      <input type="submit" value="Hääkakut"/>
    </form>
    <form action="PaivitysServlet" method="get">
      <img src="https://images.pexels.com/photos/416534/pexels-photo-416534.jpeg?w=1260&h=750&auto=compress&cs=tinysrgb" style="width:150px;height:110px;border:0;">
      <br>
      <input type="hidden" name="viestialue" value="kuppikakut"/>
      <input type="submit" value="Kuppikakut"/>
    </form>
  </div>

  <footer>
    <p>Tekijät/Copyright: Titta, Pia, Kristiina ja Riina</p>
    <p>Ota yhteyttä: <a href="mailto:academy@academy.fi">academy@academy.fi</a>.</p>
  </footer>
  </body>
</html>