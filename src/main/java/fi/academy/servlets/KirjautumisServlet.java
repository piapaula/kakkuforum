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
import java.sql.*;

@WebServlet(name = "KirjautumisServlet", urlPatterns = {"/KirjautumisServlet"})

public class KirjautumisServlet extends HttpServlet {
    @Resource(name = "kakkuforum")
    DataSource ds;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String nimimerkki = request.getParameter("nimimerkki");
        String salasana = request.getParameter("salasana");
        try {
            Connection con = ds.getConnection();
            String sql = "Select nimimerkki, salasana from henkilo where nimimerkki=? and salasana=?";
            PreparedStatement lause = con.prepareStatement(sql);
            lause.setString(1, nimimerkki);
            lause.setString(2, salasana);
            ResultSet rs = lause.executeQuery();
            System.out.println("hyvä");
            if (rs.next()) {
                response.setContentType("text/html");
                out.println("<!DOCTYPE html>");
                out.println("<html lang='fi'>");
                out.println("<head>");
                out.println("<meta charset='utf-8'/>");
                out.println("<title>Kirjautuminen onnistui</title>");
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
                out.print("<a href=\"UlosServlet\">Kirjaudu ulos</a>");
                out.print(" | ");
                out.println("</nav>");
                out.print("<div id=\"vasen\">");
                out.print("<h1>Olet kirjautunut sisään käyttäjänä " + nimimerkki +".</h1>");
                out.print("</div>");
                out.println("<div id=\"container\">");
                out.println("<h1>Käyttäjätunnus ja salasana oikein!<br>Tervetuloa sivulle " + nimimerkki+ "!</h1>");
                out.println("</div>");
                out.println("</body>");
                out.println("</html>");
                HttpSession session=request.getSession();
                session.setAttribute("nimimerkki",nimimerkki);
            }
            else {
                response.setContentType("text/html");
                out.println("<!DOCTYPE html>");
                out.println("<html lang='fi'>");
                out.println("<head>");
                out.println("<meta charset='utf-8'/>");
                out.println("<title>Kirjautuminen epäonnistui</title>");
                out.println("<link rel=\"stylesheet\" href=\"tyylit.css\">");
                out.println("<h3 id=\"red\">Käyttäjätunnus tai salasana väärin! Kokeile uudelleen!</h3>");

                request.getRequestDispatcher("kirjaudu.jsp").include(request, response);
            } out.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}