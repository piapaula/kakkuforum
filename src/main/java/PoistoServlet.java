

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "PoistoServlet", urlPatterns = {"/PoistoServlet"})
public class PoistoServlet extends HttpServlet {
    @Resource(name = "kakkuforum")
    DataSource ds;


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String viestinID = request.getParameter("viestiid");
        String nimimerkki = (String) request.getAttribute("nimimerkki");

        try {
            Connection con = ds.getConnection();
            String deletesql = "DELETE FROM viestit WHERE id=?";
            PreparedStatement lause = con.prepareStatement(deletesql);
            lause.setString(1, viestinID);
            lause.executeUpdate();

            response.setContentType("text/html");
            out.println("<!DOCTYPE html>");
            out.println("<html lang='fi'>");
            out.println("<head>");
            out.println("<meta charset='utf-8'/>");
            out.println("<title>Viestiketju poistettu</title>");
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
            out.println("<h1>Viestiketju poistettu!</h1>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
            out.print("<footer><p>Tekijät/Copyright: Titta, Pia, Kristiina ja Riina</p>");
            out.print("<p>Ota yhteyttä: <a href=\"mailto:academy@academy.fi\">academy@academy.fi</a>.</p></footer>");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
