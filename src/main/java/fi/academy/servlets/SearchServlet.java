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


@WebServlet(name = "SearchServlet", urlPatterns = {"/SearchServlet"})
public class SearchServlet extends HttpServlet {
    @Resource(name = "kakkuforum")
    DataSource ds;


        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String haku = request.getParameter("search");
            HttpSession session = request.getSession();
            String nimimerkki = (String) session.getAttribute("nimimerkki");

            try {
                try (Connection con = ds.getConnection()) {
                    String sql = "select * from viestit where tekstikentta like '%" + haku + "%' or otsikko like '%" + haku + "%'";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ResultSet tulos = ps.executeQuery();
                    response.setContentType("text/html");
                    try (PrintWriter out = response.getWriter()) {

                        StringBuilder sb = new StringBuilder();
                        while (tulos.next()) {
                            String otsikko = tulos.getString("otsikko");
                            String viesti = tulos.getString("tekstikentta");
                            sb.append("<h2>").append(otsikko).append(":").append("</h2>").append("<p>").append(viesti).append("</p>");
                        }
                        if (sb.toString().equals("") || haku.isEmpty()) {
                            response.setContentType("text/html");
                            out.println("<!DOCTYPE html>");
                            out.println("<head>");
                            out.println("<link rel=\"stylesheet\" href=\"tyylit.css\">");
                            out.println("<html lang='fi'>");
                            out.println("<meta charset='utf-8'/>");
                            out.println("<title>Haun tulokset</title>");
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
                            if (nimimerkki == null) {
                                out.print("<h1><a href=\"kirjaudu.jsp\">Kirjaudu sisään</a> KakkuForumiin!</h1>");
                            } else {
                                out.print("<h1>Olet kirjautunut sisään käyttäjänä " + nimimerkki +".</h1>");
                            }
                            out.println("</div>");
                            out.println("<div id=\"container\">");
                            out.println("<h1>Haulla ei löytynyt osumia</h1>");
                            out.println("</div>");
                            out.print("<footer><p>Tekijät/Copyright: Titta, Pia, Kristiina ja Riina</p>");
                            out.print("<p>Ota yhteyttä: <a href=\"mailto:academy@academy.fi\">academy@academy.fi</a>.</p></footer>");
                            out.println("</body>");
                            out.println("</html>");

                        } else {
                            response.setContentType("text/html");
                            out.println("<!DOCTYPE html>");
                            out.println("<head>");
                            out.println("<link rel=\"stylesheet\" href=\"tyylit.css\">");
                            out.println("<html lang='fi'>");
                            out.println("<meta charset='utf-8'/>");
                            out.println("<title>Kirjautuminen onnistui</title>");
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
                            if (nimimerkki == null) {
                                out.print("<h1><a href=\"kirjaudu.jsp\">Kirjaudu sisään</a> KakkuForumiin!</h1>");
                            } else {
                                out.print("<h1>Olet kirjautunut sisään käyttäjänä " + nimimerkki +".</h1>");
                            }
                            out.println("</div>");
                            out.println("<div id=\"hakudiv\">");
                            out.println(sb.toString());
                            out.println("</div>");
                            out.print("<footer><p>Tekijät/Copyright: Titta, Pia, Kristiina ja Riina</p>");
                            out.print("<p>Ota yhteyttä: <a href=\"mailto:academy@academy.fi\">academy@academy.fi</a>.</p></footer>");
                            out.println("</body>");
                            out.println("</html>");
                    }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }