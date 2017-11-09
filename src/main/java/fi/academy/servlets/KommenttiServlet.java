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

    @Resource(name="kakkuforum")
    DataSource ds;

    /*
    Post-metodilla tehdään kommenttiketju.jsp.sivun viestinlisäyskentän pyytämät toimenpiteet. Eli lisätään lähetetty kommnetti tietokantaan ja
    päivitetään se kommenttiketju.jsp-sivulle.

     */

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session= request.getSession();
        String kommentoija = (String) session.getAttribute("nimimerkki");

        // haetaan kommentit, jotka liittyvät tietyn ID:n omaavaan viestiin
        // lisätään nämä kommentit StringBuilderiin

        try (Connection con = ds.getConnection()) {

            int id = Integer.parseInt(request.getParameter("idviesti"));
            String kommentti = request.getParameter("kommenttikentta");

            // lisätään lähetetty kommentti kommenttitaulukkoon
            String kommenttisql = "INSERT INTO kommentit (kommenttikentta, viestiID, kommentoija) VALUES (?, ?, ?)";
            PreparedStatement kommenttilause = con.prepareStatement(kommenttisql);
            kommenttilause.setString(1, kommentti);
            kommenttilause.setInt(2, id);
            kommenttilause.setString(3, kommentoija);
            kommenttilause.executeUpdate();

            //haetaan kaikki kyseistä viesti-id:tä koskevat kommentit kommenttitaulukosta

            String kommenttihaku = "SELECT kommentit.aikaleima, kommentit.id, kommentit.kommenttikentta, kommentit.viestiID, kommentit.kommentoija, viestit.kirjoitettu, viestit.tekstikentta, viestit.otsikko " +
                    "FROM kommentit LEFT JOIN viestit " +
                    "on kommentit.viestiid=viestit.id " +
                    "WHERE viestiID='" + id +"'";
            PreparedStatement psKommentit = con.prepareStatement(kommenttihaku);
            ResultSet kommenttitulos = psKommentit.executeQuery(kommenttihaku);

            StringBuilder aloitusviesti = new StringBuilder();
            StringBuilder lisatytkommentit = new StringBuilder();

            while(kommenttitulos.next()){
                String kom = kommenttitulos.getString("kommentit.kommenttikentta");
                String leima = kommenttitulos.getString("kommentit.aikaleima");
                String komment = kommenttitulos.getString("kommentit.kommentoija");
                String alkup = kommenttitulos.getString("viestit.tekstikentta");
                String alkupotsikko = kommenttitulos.getString("viestit.otsikko");
                String alkupleima = kommenttitulos.getString("viestit.kirjoitettu");


                if (alkupotsikko==null || alkupotsikko==""){
                    alkupotsikko = "Viestille ei ole annettu otsikkoa";
                }

                if (aloitusviesti.toString().equals("")) {
                    aloitusviesti.append("<div class='viestik'>");
                    aloitusviesti.append("<aside>" +
                            "<a href='#'>Viestin kirjoittaja:</a><br><br><br>" +
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
                        "<p>"+komment+"</p><br><br><br>" +
                        "<img src='https://1.soompi.io/wp-content/uploads/2015/03/keyboard-waffle-korea-540x540.jpg' alt='herkkunäppäimistö'/>" +
                        "<div> Viesti lähetetty: <br>" + leima + "<div>" +
                        "</aside>");

                lisatytkommentit.append("<section>");
                lisatytkommentit.append("<p><em>Vastaus viestiin otsikolla:" + alkupotsikko + "</em></p><br>");
                lisatytkommentit.append("<p>" + kom + "</p>");
                lisatytkommentit.append("</section>");

                lisatytkommentit.append("<details>");
                lisatytkommentit.append("<br><p></p>");
                lisatytkommentit.append("<p>Turhaa löpinää?</p><br><br>");
                lisatytkommentit.append("<input type='submit' value='Poista viesti'/></form>");
                lisatytkommentit.append("</details>");

                lisatytkommentit.append("</div>");
            }

            request.setAttribute("aloitusviesti", aloitusviesti);
            request.setAttribute("kommentit", lisatytkommentit);
            request.setAttribute("id", id);

            RequestDispatcher rd=request.getRequestDispatcher("kommenttiketjusivu.jsp");
            rd.forward(request,response);





        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // haetaan kommentit, jotka liittyvät tietyn ID:n omaavaan viestiin

        try (Connection con = ds.getConnection()) {

            String id = request.getParameter("viesti_id");
            String kommentti = request.getParameter("kommenttikentta");
            String kommentoija = request.getParameter("nimimerkki");

            //haetaan kaikki kyseistä viesti-id:tä koskevat kommentit kommenttitaulukosta

            String kommenttihaku = "SELECT kommentit.aikaleima, kommentit.id, kommentit.kommenttikentta, kommentit.viestiID, " +
                    "kommentit.kommentoija, "  +
                    "viestit.tekstikentta, viestit.otsikko, viestit.kirjoitettu, viestit.author " +
                    "FROM kommentit LEFT JOIN viestit " +
                    "on kommentit.viestiid=viestit.id " +
                    "WHERE viestiID='" + id +"'";
            PreparedStatement psKommentit = con.prepareStatement(kommenttihaku);
            ResultSet kommenttitulos = psKommentit.executeQuery(kommenttihaku);



            StringBuilder aloitusviesti = new StringBuilder();
            StringBuilder lisatytkommentit = new StringBuilder();


            while(kommenttitulos.next()){
                String kom = kommenttitulos.getString("kommentit.kommenttikentta");
                String leima = kommenttitulos.getString("kommentit.aikaleima");
                String alkup = kommenttitulos.getString("viestit.tekstikentta");
                String kommentoij = kommenttitulos.getString("kommentit.kommentoija");
                String alkupotsikko = kommenttitulos.getString("viestit.otsikko");
                String alkupleima = kommenttitulos.getString("viestit.kirjoitettu");



                if (alkupotsikko==null || alkupotsikko==""){
                    alkupotsikko = "Viestille ei ole annettu otsikkoa";
                }

                if (aloitusviesti.toString().equals("")) {
                    aloitusviesti.append("<div class='viestik'>");
                    aloitusviesti.append("<aside>" +
                            "<a href='#'>Viestin kirjoittaja</a><br><br><br>" +
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
                        "<a href='#'>Viestin kirjoittaja</a><br><br><br>" +
                        "<img src='https://1.soompi.io/wp-content/uploads/2015/03/keyboard-waffle-korea-540x540.jpg' alt='herkkunäppäimistö'/>" +
                        "<div> Viesti lähetetty: <br>" + leima + "<div>" +
                        "</aside>");

                lisatytkommentit.append("<section>");
                lisatytkommentit.append("<p><em>Vastaus viestiin otsikolla:" + alkupotsikko + "</em></p><br>");
                lisatytkommentit.append("<p>" + kom + "</p>");
                lisatytkommentit.append("</section>");

                lisatytkommentit.append("<details>");
                lisatytkommentit.append("<br><p></p>");
                lisatytkommentit.append("<p>Turhaa löpinää?</p><br><br>");
                lisatytkommentit.append("<input type='submit' value='Poista viesti'/></form>");
                lisatytkommentit.append("</details>");


                lisatytkommentit.append("</div>");

            }


            request.setAttribute("aloitusviesti", aloitusviesti);
            request.setAttribute("kommentit", lisatytkommentit);
            request.setAttribute("id", id);

            RequestDispatcher rd=request.getRequestDispatcher("kommenttiketjusivu.jsp");
            rd.forward(request,response);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}