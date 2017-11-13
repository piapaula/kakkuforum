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
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "KommenttiServlet", urlPatterns = {"/KommenttiServlet"})
public class KommenttiServlet extends HttpServlet {

    @Resource(name = "kakkuforum")
    DataSource ds;

    /*
    Post-metodilla tehdään kommenttiketju.jsp.sivun viestinlisäyskentän pyytämät toimenpiteet. Eli lisätään lähetetty kommnetti tietokantaan ja
    päivitetään se kommenttiketju.jsp-sivulle.
     */

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        String kommentoija = (String) session.getAttribute("nimimerkki");
        request.setCharacterEncoding("utf-8");

        // haetaan kommentit, jotka liittyvät tietyn ID:n omaavaan viestiin
        // lisätään nämä kommentit StringBuilderiin

        try (Connection con = ds.getConnection()) {

            int id = Integer.parseInt(request.getParameter("idviesti"));
            String kommentti = request.getParameter("kommenttikentta");

            StringBuilder aloitusviesti = new StringBuilder();
            StringBuilder lisatytkommentit = new StringBuilder();


            // lisätään lähetetty kommentti kommenttitaulukkoon
            String kommenttisql = "INSERT INTO kommentit (kommenttikentta, viestiID, kommentoija) VALUES (?, ?, ?)";
            PreparedStatement kommenttilause = con.prepareStatement(kommenttisql);
            kommenttilause.setString(1, kommentti);
            kommenttilause.setInt(2, id);
            kommenttilause.setString(3, kommentoija);
            kommenttilause.executeUpdate();

            //haetaan kaikki kyseistä viesti-id:tä koskevat kommentit kommenttitaulukosta


            String kommenttihaku = "SELECT kommentit.aikaleima, kommentit.id, kommentit.kommenttikentta, kommentit.viestiID, kommentit.kommentoija, " +
                    "viestit.kirjoitettu, viestit.tekstikentta, viestit.otsikko, viestit.id, viestit.author " +
                    "FROM kommentit LEFT JOIN viestit " +
                    "on kommentit.viestiid=viestit.id " +
                    "WHERE viestit.id='" + id + "'";
            PreparedStatement psKommentit = con.prepareStatement(kommenttihaku);
            ResultSet kommenttitulos = psKommentit.executeQuery(kommenttihaku);


            while (kommenttitulos.next()) {
                String kom = kommenttitulos.getString("kommentit.kommenttikentta");
                String leima = kommenttitulos.getString("kommentit.aikaleima");
                String komment = kommenttitulos.getString("kommentit.kommentoija");
                String alkup = kommenttitulos.getString("viestit.tekstikentta");
                String alkupotsikko = kommenttitulos.getString("viestit.otsikko");
                String alkupleima = kommenttitulos.getString("viestit.kirjoitettu");
                String author = kommenttitulos.getString("viestit.author");


                if (alkupotsikko == null || alkupotsikko == "") {
                    alkupotsikko = "Viestille ei ole annettu otsikkoa";
                }

                if (aloitusviesti.toString().equals("")) {
                    aloitusviesti.append("<div class='viestik'>");
                    aloitusviesti.append("<aside>" +
                            "<p>Viestin kirjoittaja:" + author + "<p><br>" +
                            "<img src='https://1.soompi.io/wp-content/uploads/2015/03/keyboard-waffle-korea-540x540.jpg' alt='herkkunäppäimistö'/>" +
                            "<div> Viesti lähetetty: <br>" + alkupleima + "<div>" +
                            "</aside>");
                    aloitusviesti.append("<section>");
                    aloitusviesti.append("<h2>Viestiketjun aihe: " + alkupotsikko + "</h2><br><br><br>");
                    aloitusviesti.append("<h3>" + alkup + "</h3>");
                    aloitusviesti.append("</section>");
                    aloitusviesti.append("</div>");
                }


                lisatytkommentit.append("<div class='ketjut'>");
                lisatytkommentit.append("<aside>" +
                        "<p>" + komment + "</p><br><br><br>" +
                        "<img src='https://1.soompi.io/wp-content/uploads/2015/03/keyboard-waffle-korea-540x540.jpg' alt='herkkunäppäimistö'/>" +
                        "<div> Viesti lähetetty: <br>" + leima + "<div>" +
                        "</aside>");

                lisatytkommentit.append("<section>");
                lisatytkommentit.append("<p><em>Vastaus viestiin otsikolla:" + alkupotsikko + "</em></p><br>");
                lisatytkommentit.append("<p>" + kom + "</p>");
                lisatytkommentit.append("</section>");

                lisatytkommentit.append("</div>");
            }

            request.setAttribute("aloitusviesti", aloitusviesti);
            request.setAttribute("kommentit", lisatytkommentit);
            request.setAttribute("id", id);

            RequestDispatcher rd = request.getRequestDispatcher("kommenttiketjusivu.jsp");
            rd.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // haetaan kommentit, jotka liittyvät tietyn ID:n omaavaan viestiin

        request.setCharacterEncoding("utf-8");

        try (Connection con = ds.getConnection()) {

            String id = request.getParameter("viesti_id");

            //haetaan kaikki kyseistä viesti-id:tä koskevat kommentit kommenttitaulukosta

            String kommenttihaku = "SELECT kommentit.aikaleima, kommentit.id, kommentit.kommenttikentta, kommentit.viestiID, " +
                    "kommentit.kommentoija, " +
                    "viestit.tekstikentta, viestit.otsikko, viestit.kirjoitettu, viestit.author " +
                    "FROM kommentit LEFT JOIN viestit " +
                    "on kommentit.viestiid=viestit.id " +
                    "WHERE viestiID='" + id + "'";
            PreparedStatement psKommentit = con.prepareStatement(kommenttihaku);
            ResultSet kommenttitulos = psKommentit.executeQuery(kommenttihaku);

            StringBuilder aloitusviesti = new StringBuilder();
            StringBuilder lisatytkommentit = new StringBuilder();


            while (kommenttitulos.next()) {
                String kom = kommenttitulos.getString("kommentit.kommenttikentta");
                String leima = kommenttitulos.getString("kommentit.aikaleima");
                String alkup = kommenttitulos.getString("viestit.tekstikentta");
                String author = kommenttitulos.getString("viestit.author");
                String kommentoij = kommenttitulos.getString("kommentit.kommentoija");
                String alkupotsikko = kommenttitulos.getString("viestit.otsikko");
                String alkupleima = kommenttitulos.getString("viestit.kirjoitettu");
                int kommentinid = kommenttitulos.getInt("kommentit.id");


                if (alkupotsikko == null || alkupotsikko == "") {
                    alkupotsikko = "Viestille ei ole annettu otsikkoa";
                }

                //lisätään aloitusviesti vain kerran eli jos aloitusviesti-StringBuilder ei ole tyhjä, tätä ei tehdä
                if (aloitusviesti.toString().equals("")) {
                    aloitusviesti.append("<div class='viestik'>");
                    aloitusviesti.append("<aside>" +
                            "<p>Viestin kirjoittaja: " + author + "</p>" +
                            "<img src='https://1.soompi.io/wp-content/uploads/2015/03/keyboard-waffle-korea-540x540.jpg' alt='herkkunäppäimistö'/>" +
                            "<div> Viesti lähetetty: <br>" + alkupleima + "<div>" +
                            "</aside>");
                    aloitusviesti.append("<section>");
                    aloitusviesti.append("<h2>Viestiketjun aihe: " + alkupotsikko + "</h2><br><br><br>");
                    aloitusviesti.append("<h3>" + alkup + "</h3>");
                    aloitusviesti.append("</section>");
                    aloitusviesti.append("</div>");
                }


                lisatytkommentit.append("<div class='ketjut'>");
                lisatytkommentit.append("<aside>" +
                        "<p>Viestin kirjoittaja: " + kommentoij + "</p>" +
                        "<img src='https://1.soompi.io/wp-content/uploads/2015/03/keyboard-waffle-korea-540x540.jpg' alt='herkkunäppäimistö'/>" +
                        "<div> Viesti lähetetty: <br>" + leima + "<div>" +
                        "</aside>");

                lisatytkommentit.append("<section>");
                lisatytkommentit.append("<p><em>Vastaus viestiin otsikolla:" + alkupotsikko + "</em></p><br>");
                lisatytkommentit.append("<p>" + kom + "</p>");
                lisatytkommentit.append("</section>");

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
                    lisatytkommentit.append("<article>");
                    lisatytkommentit.append("<br><p></p>");
                    lisatytkommentit.append("<p>Turhaa löpinää?</p><br><br>");
                    lisatytkommentit.append("<form action='PoistaKommenttiServlet' method='post'>");
                    lisatytkommentit.append("<input type='hidden' name='kommentinid' value='" + kommentinid + "' />");
                    lisatytkommentit.append("<input type='submit' value='Poista viesti'/></form>");
                    lisatytkommentit.append("</article>");
                }

                lisatytkommentit.append("</div>");
            }

            if (!kommenttitulos.next()) {
                String aloitusviestinhaku = "SELECT viestit.id, viestit.tekstikentta, viestit.otsikko, viestit.kirjoitettu, viestit.author FROM viestit WHERE id=" + id;
                PreparedStatement psAloitus = con.prepareStatement(aloitusviestinhaku);
                ResultSet aloitustulos = psAloitus.executeQuery();

                while (aloitustulos.next()) {
                    String author = aloitustulos.getString("viestit.author");
                    String alkup = aloitustulos.getString("viestit.tekstikentta");
                    String alkupotsikko = aloitustulos.getString("viestit.otsikko");
                    String alkupleima = aloitustulos.getString("viestit.kirjoitettu");
                    if (alkupotsikko == null || alkupotsikko == "") {
                        alkupotsikko = "Viestille ei ole annettu otsikkoa";
                    }

                    //lisätään aloitusviesti vain kerran eli jos aloitusviesti-StringBuilder ei ole tyhjä, tätä ei tehdä
                    if (aloitusviesti.toString().equals("")) {
                        aloitusviesti.append("<div class='viestik'>");
                        aloitusviesti.append("<aside>" +
                                "<p>Viestin kirjoittaja: " + author + "</p>" +
                                "<img src='https://1.soompi.io/wp-content/uploads/2015/03/keyboard-waffle-korea-540x540.jpg' alt='herkkunäppäimistö'/>" +
                                "<div> Viesti lähetetty: <br>" + alkupleima + "<div>" +
                                "</aside>");
                        aloitusviesti.append("<section>");
                        aloitusviesti.append("<h2>Viestiketjun aihe: " + alkupotsikko + "</h2><br><br><br>");
                        aloitusviesti.append("<h3>" + alkup + "</h3>");
                        aloitusviesti.append("</section>");
                        aloitusviesti.append("</div>");
                    }

                }
            }

            request.setAttribute("aloitusviesti", aloitusviesti);
            request.setAttribute("kommentit", lisatytkommentit);
            request.setAttribute("id", id);

            RequestDispatcher rd = request.getRequestDispatcher("kommenttiketjusivu.jsp");
            rd.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}