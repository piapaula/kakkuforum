package fi.academy.servlets;

        import javax.annotation.Resource;
        import javax.servlet.ServletException;
        import javax.servlet.annotation.WebServlet;
        import javax.servlet.http.HttpServlet;
        import javax.servlet.http.HttpServletRequest;
        import javax.servlet.http.HttpServletResponse;
        import javax.sql.DataSource;
        import java.io.IOException;
        import java.io.PrintWriter;
        import java.sql.*;
        import java.util.ArrayList;


@WebServlet(name = "RekServlet", urlPatterns = {"/RekServlet"})
public class RekServlet extends HttpServlet {
    @Resource(name = "kakkuforum")
    DataSource ds;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nimi = request.getParameter("nimi");
        String nimimerkki = request.getParameter("nimimerkki");
        String salasana = request.getParameter("salasana");

        String haenimet = "select nimimerkki from henkilo";
        ArrayList<String> nimet = new ArrayList<>();
        try (Connection con = ds.getConnection()) {
            PreparedStatement lause = con.prepareStatement(haenimet);
            ResultSet tulos = lause.executeQuery();
            while (tulos.next()) {
                String nimimerkkiTaulukossa = tulos.getString("nimimerkki");
                nimet.add(nimimerkkiTaulukossa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (nimi.isEmpty() || nimimerkki.isEmpty() || salasana.isEmpty()) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html lang='fi'>");
            out.println("<head>");
            out.println("<meta charset='utf-8'/>");
            out.println("<title>Rekisteröityminen epäonnistui</title>");
            out.println("<link rel=\"stylesheet\" href=\"tyylit.css\">");
            out.println("<h3 id=\"red\">Jokin meni pieleen, muistithan täyttää kaikki kentät?</h3>");
            request.getRequestDispatcher("rekisteroidy.jsp").include(request, response);

        }  else if(nimet.contains(nimimerkki)) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html lang='fi'>");
            out.println("<head>");
            out.println("<meta charset='utf-8'/>");
            out.println("<title>Rekisteröityminen epäonnistui</title>");
            out.println("<link rel=\"stylesheet\" href=\"tyylit.css\">");
            out.println("<h3 id=\"red\">Nimimerkki on jo käytössä, valitse joku toinen nimimerkki!</h3>");
            request.getRequestDispatcher("rekisteroidy.jsp").include(request, response);

        } else {

            try (Connection con = ds.getConnection()) {
                String sql = "insert into henkilo (nimi, nimimerkki, rooli, salasana) values (?, ?, ?, ?)";
                PreparedStatement lause = con.prepareStatement(sql);
                lause.setString(1, nimi);
                lause.setString(2, nimimerkki);
                lause.setString(3, "rekisteroitynyt");
                lause.setString(4, salasana);
                lause.executeUpdate();
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<!DOCTYPE html>");
                out.println("<html lang='fi'>");
                out.println("<head>");
                out.println("<meta charset='utf-8'/>");
                out.println("<title>Rekisteröityminen</title>");
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
                out.print("<a href=\"kirjauduUlos\">Kirjaudu ulos</a>");
                out.print(" | ");
                out.println("</nav>");
                if (nimimerkki == null) {
                    out.print("<a href=\"kirjaudu.jsp\">Kirjaudu sisään</a> KakkuForumiin!</h1>");
                } else {
                    out.print("<h1>Olet kirjautunut sisään käyttäjänä " + nimimerkki +".</h1>");
                }
                out.println("<div id=\"container\">");
                out.println("<h1>Rekisteröityminen onnistui!</h1>");
                out.println("</div>");
                out.println("</body>");
                out.println("</html>");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}