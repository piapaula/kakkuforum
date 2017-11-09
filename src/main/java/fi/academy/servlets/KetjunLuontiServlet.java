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


// Tässä servletissä käytetään sekä post- että get-metodia: postia käyt
@WebServlet(name = "KetjunLuontiServlet", urlPatterns = {"/KetjunLuontiServlet"})
public class KetjunLuontiServlet extends HttpServlet {

    @Resource(name = "kakkuforum")
    DataSource ds;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        String otsikko = request.getParameter("otsikko");
        String viesti = request.getParameter("viesti");
        String viestialue = request.getParameter("viestialue");
        String kirjoittaja = request.getParameter("kirjoittaja");

        try (Connection con = ds.getConnection()) {
            String sql = "INSERT INTO viestit (otsikko, tekstikentta, viestialue, author) VALUES (?, ?, ?, ?)";
            PreparedStatement pshakulause = con.prepareStatement(sql);
            pshakulause.setString(1, otsikko);
            pshakulause.setString(2, viesti);
            pshakulause.setString(3, viestialue);
            pshakulause.setString(4, kirjoittaja);
            pshakulause.executeUpdate();

            String sqlhaku = "SELECT id, otsikko, tekstikentta, kirjoitettu, author FROM viestit WHERE viestialue='" + viestialue + "'";
            PreparedStatement ps = con.prepareStatement(sqlhaku);
            ResultSet tulos = ps.executeQuery(sqlhaku);

            int id=0;
            StringBuilder tulostettavat = new StringBuilder();
            while (tulos.next()){
                Timestamp ai = tulos.getTimestamp("viestit.kirjoitettu");
                String ots = tulos.getString("viestit.otsikko");
                String vie = tulos.getString("viestit.tekstikentta");
                id = tulos.getInt("viestit.id");

                String kommenttimaara =  "SELECT id FROM kommentit WHERE viestiID='" + id + "'";
                PreparedStatement psmaara = con.prepareStatement(kommenttimaara);
                ResultSet maaratulos = psmaara.executeQuery(kommenttimaara);
                int koko=0;
                if (maaratulos != null) {
                    maaratulos.beforeFirst();
                    maaratulos.last();
                    koko = maaratulos.getRow();
                }
/*                tulostettavat.append("<!DOCTYPE html>");
                tulostettavat.append("<html lang='fi'>");
                tulostettavat.append("<head>");
                tulostettavat.append("<meta charset='utf-8'/>");*/
                tulostettavat.append("<div id='container'>");
                tulostettavat.append("<div class='ketjut'>");
                tulostettavat.append("<aside><a href='#'>Tietoja viestiketjusta</a><br><br><br>" +
                        "<div> Vastausten määrä: " + koko + "<br>" +
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

                HttpSession session=request.getSession(false);
                String nimimerkki = (String) session.getAttribute("nimimerkki");
                String hae = "SELECT rooli FROM henkilo WHERE nimimerkki=?";
                PreparedStatement lause = con.prepareStatement(hae);
                lause.setString(1, nimimerkki);
                ResultSet hlo  = lause.executeQuery();
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


            RequestDispatcher rd=request.getRequestDispatcher("ketjunnayttosivu.jsp");
            rd.forward(request,response);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");

        String viestialue = request.getParameter("viestialue");
        try {
            Connection con = ds.getConnection();

            String sqlhaku = "SELECT id, otsikko, tekstikentta, kirjoitettu FROM viestit WHERE viestialue='" +
                    viestialue +"'";
            PreparedStatement ps = con.prepareStatement(sqlhaku);
            ResultSet tulos = ps.executeQuery(sqlhaku);

            StringBuilder aiheet = new StringBuilder();
            while (tulos.next()){
                String aihe = tulos.getString("otsikko");
                aiheet.append("<div class='viestik'>");
                aiheet.append("<aside>");
                aiheet.append("<a href='#'>" + aihe + "</a>");
                aiheet.append("<aside>");
                aiheet.append("<br>");
                aiheet.append("</div>");
            }

            request.setAttribute("aiheet", aiheet);


            String jspsivu = viestialue + ".jsp";

            RequestDispatcher rd=request.getRequestDispatcher(jspsivu);
            rd.forward(request,response);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}