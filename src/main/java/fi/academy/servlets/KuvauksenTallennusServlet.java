package fi.academy.servlets;

import javax.annotation.Resource;
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
import java.sql.SQLException;

@WebServlet(name = "KuvauksenTallennusServlet", urlPatterns = {"/KuvauksenTallennusServlet"})
public class KuvauksenTallennusServlet extends HttpServlet {
    @Resource(name = "kakkuforum")
    DataSource ds;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String kuvaus = request.getParameter("kuvaus");
        request.setCharacterEncoding("utf-8");
        HttpSession istunto = request.getSession(false);
        String nimimerkki = (String) istunto.getAttribute("nimimerkki");
        String sql = "update henkilo set kuvaus = ? where nimimerkki = ?";
        try (Connection con = ds.getConnection()) {
            PreparedStatement lause = con.prepareStatement(sql);
            lause.setString(1, kuvaus);
            lause.setString(2, nimimerkki);
            lause.executeUpdate();
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html lang='fi'>");
            out.println("<head>");
            out.println("<meta charset='utf-8'/>");
            out.println("<title>Profiilikuvaus</title>");
            out.println("<link rel=\"stylesheet\" href=\"tyylit.css\">");
            out.println("</head>");
            out.println("<body>");
            out.println("<nav>");
            out.print("<a href=\"rekisteroidy.jsp\">Rekisteröidy</a>");
            out.print(" | ");
            out.print("<a href=\"profiili.jsp\">Oma profiili</a>");
            out.print(" | ");
            out.print("<a href=\"index.jsp\">KakkuForum</a>");
            out.print(" | ");
            out.print("<a href=\"haku.jsp\">Hae sivustolta</a>");
            out.print(" | ");
            out.print("<a href=\"ulos.html\">Kirjaudu ulos</a>");
            out.print(" | ");
            out.println("</nav>");
            out.print("<div id=\"vasen\">");
            out.print("<h1>Olet kirjautunut sisään käyttäjänä " + nimimerkki +".</h1>");
            out.print("</div>");
            out.println("<div id=\"container\">");
            out.println("<h1>Käyttäjätunnus ja salasana oikein!<br>Tervetuloa sivulle " + nimimerkki+ "!</h1>");
            out.println("</div>");
            out.println("<div id=\"container\">");
            out.println("<h1>Profiilikuvauksesi tallennettiin! Pääset tarkastelemaan profiiliasi <a href=\"profiili.jsp\">täällä</a></h1>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}