package fi.academy.servlets;


import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;


@WebServlet(name = "PaivitysServlet", urlPatterns = {"/PaivitysServlet"})
public class PaivitysServlet extends HttpServlet {


    @Resource(name = "kakkuforum")
    DataSource ds;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String otsikko = request.getParameter("otsikko");
        String viesti = request.getParameter("viestikentta");
        String viestialue = request.getParameter("viestialue");
        request.setCharacterEncoding("utf-8");

        try (Connection con = ds.getConnection()) {

            String sql = "INSERT INTO viestit (otsikko, tekstikentta, viestialue) VALUES (?, ?, ?)";
            PreparedStatement lause = con.prepareStatement(sql);
            lause.setString(1, otsikko);
            lause.setString(2, viesti);
            lause.setString(3, viestialue);
            lause.executeUpdate();

            String sqlhaku = "SELECT id, otsikko, tekstikentta, kirjoitettu, author FROM viestit";
            PreparedStatement ps = con.prepareStatement(sqlhaku);
            ResultSet tulos = ps.executeQuery(sqlhaku);


            StringBuilder tulostettavat = new StringBuilder();

            while (tulos.next()) {
                Timestamp ai = tulos.getTimestamp("viestit.kirjoitettu");
                String ots = tulos.getString("viestit.otsikko");
                String vie = tulos.getString("viestit.tekstikentta");
                tulostettavat.append("<div class='viestik'>");
                tulostettavat.append("<aside><h2>Viestiketjun aihe:" + ots + "</h2><br><br><br>" +
                        "<a href='#'>Viestin kirjoittaja</a><br><br><br>" +
                        "<img src='https://1.soompi.io/wp-content/uploads/2015/03/keyboard-waffle-korea-540x540.jpg' alt='herkkunäppäimistö'/>" +
                        "<div> Kirjoitettu: <br>" + ai + "<div>" +
                        "</aside>");

                tulostettavat.append("<section>");
                tulostettavat.append("<h1>" + ots + "</h1>");
                tulostettavat.append("<p>" + vie + "</p>");

                tulostettavat.append("</div>");
                tulostettavat.append("</section>");
                tulostettavat.append("</div>");

            }

            request.setAttribute("tulostettavat", tulostettavat);

            RequestDispatcher rd = request.getRequestDispatcher("ketjunnayttosivu.jsp");
            rd.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /*

    Alla get-metodia hyödyntäen lähetetään attribuutit otsikko ja tulostettavat ketjunnayttosivu.jsp.sivulle.
    Attribuutti tulostettavat on StringBuilder, jossa on HTML-muotoilut valmiina.
    tulostettavat-attribuutti sisältää yksiityiskohtaista tietoa viestiketjusta sekä mahdollisuuden poistaa viestiketju (tähän oikeudet vain adminilla).
    Attribuutti sisältää myös formin, jonka submit-nappulaa klikkaamalla pääsee tarkastelemaan viestiketjua. Tällöin ohjaudutaan KommenttiServletin
    get-metodin avulla kommenttiketjusivu.jsp-sivulle.

     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String viestialue = request.getParameter("viestialue");

        //haetaan kaikki tietyn viestialueen viestit
        try {
            Connection con = ds.getConnection();

            String sqlhaku = "SELECT id, otsikko, tekstikentta, kirjoitettu, author FROM viestit WHERE viestialue='" + viestialue + "'";
            PreparedStatement ps = con.prepareStatement(sqlhaku);
            ResultSet tulos = ps.executeQuery(sqlhaku);

            int id = 0;
            StringBuilder tulostettavat = new StringBuilder();
            while (tulos.next()) {
                Timestamp ai = tulos.getTimestamp("viestit.kirjoitettu");
                String ots = tulos.getString("viestit.otsikko");
                String vie = tulos.getString("viestit.tekstikentta");
                id = tulos.getInt("viestit.id");
                String author = tulos.getString("viestit.author");

                String kommenttimaara = "SELECT id FROM kommentit WHERE viestiID='" + id + "'";
                PreparedStatement psmaara = con.prepareStatement(kommenttimaara);
                ResultSet maaratulos = psmaara.executeQuery(kommenttimaara);
                int koko = 0;
                if (maaratulos != null) {
                    maaratulos.beforeFirst();
                    maaratulos.last();
                    koko = maaratulos.getRow();
                }

                tulostettavat.append("<div id='container'>");
                tulostettavat.append("<div class='ketjut'>");
                tulostettavat.append("<aside><h2>Tietoja viestiketjusta</h2><br>" +
                        "<div> Vastausten määrä: " + koko + "<br>" +
                        "<br>Viestiketjun aloittaja: <br>" + author + "<br>" +
                        "<br>Viestiketju aloitettu: <br>" + ai + "<div>");
                tulostettavat.append("</aside>");

                tulostettavat.append("<section>");
                tulostettavat.append("<p>Kiinnostuitko?</p><br>");
                tulostettavat.append("<div class=togglaus><form action='KommenttiServlet' method='get'>" +
                        "       <input type='hidden' name='viesti_id' value='" + id + "' />" +
                        "       <input type='submit' value='Siirry viestiketjuun " + ots + "'/>" +
                        "   </form></div>");
                tulostettavat.append("<img src='https://1.soompi.io/wp-content/uploads/2015/03/keyboard-waffle-korea-540x540.jpg' alt='herkkunäppäimistö'/>");
                tulostettavat.append("</section>");

                HttpSession session = request.getSession(false);
                String nimimerkki = (String) session.getAttribute("nimimerkki");
                String hae = "SELECT rooli FROM henkilo WHERE nimimerkki=?";
                PreparedStatement lause = con.prepareStatement(hae);
                lause.setString(1, nimimerkki);
                ResultSet hlo = lause.executeQuery();
                String rooli = "";
                while (hlo.next()) {
                    rooli = hlo.getString("rooli");
                }
                if (rooli.equals("admin")) {
                    tulostettavat.append("<article>");
                    tulostettavat.append("<br><p></p>");
                    tulostettavat.append("<p>Turhaa löpinää?</p><br>");
                    tulostettavat.append("<form action='PoistoServlet' method='post'>");
                    tulostettavat.append("<input type='hidden' name='viestiid' value='" + id + "' />");
                    tulostettavat.append("<input type='hidden' name='viestialue' value='" + viestialue + "' />");
                    tulostettavat.append("<input type='submit' value='Poista viestiketju " + ots + "'/></form>");
                    tulostettavat.append("</article>");

                }

                tulostettavat.append("</div>");
                tulostettavat.append("</div>");

            }
            request.setAttribute("viestinID", id);
            request.setAttribute("otsikko", viestialue);
            request.setAttribute("tulostettavat", tulostettavat);


            RequestDispatcher rd = request.getRequestDispatcher("ketjunnayttosivu.jsp");
            rd.forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}