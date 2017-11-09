package fi.academy.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "UlosServlet",  urlPatterns = {"/UlosServlet"})
public class UlosServlet extends HttpServlet {

        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            response.setContentType("text/html");
            PrintWriter out=response.getWriter();


            HttpSession session=request.getSession();
            if(null == session.getAttribute("nimimerkki")){
                response.setContentType("text/html");
                out.println("<!DOCTYPE html>");
                out.println("<html lang='fi'>");
                out.println("<head>");
                out.println("<meta charset='utf-8'/>");
                out.println("<title>Kirjaudu ulos</title>");
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
                out.print("<h1><a href=\"kirjaudu.jsp\">Kirjaudu sisään</a> KakkuForumiin!</h1>");
                out.print("</div>");
                out.println("<div id=\"container\">");
                out.println("<h1>Et ole vielä kirjautunut sisään! Kirjaudu sisään <a href=\"kirjaudu.jsp\">täällä</a></h1>");
                out.println("</div>");
                out.println("</body>");
                out.println("<footer>");
                out.println("<p>Tekijät/Copyright: Titta, Pia, Kristiina ja Riina</p>");
                out.println("<p>Ota yhteyttä: <a href=\"mailto:academy@academy.fi\">academy@academy.fi</a>.</p>");
                out.println("</footer>");
                out.println("</html>");
                out.close();

            }else{
                response.setContentType("text/html");
                out.println("<!DOCTYPE html>");
                out.println("<html lang='fi'>");
                out.println("<head>");
                out.println("<meta charset='utf-8'/>");
                out.println("<title>Kirjaudu ulos</title>");
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
                out.print("<h1><a href=\"kirjaudu.jsp\">Kirjaudu sisään</a> KakkuForumiin!</h1>");
                out.print("</div>");
                out.println("<div id=\"container\">");
                out.println("<h1>Olet nyt kirjautunut ulos käyttäjältä "+ session.getAttribute("nimimerkki")+". <br>Kirjaudu sisään uudelleen <a href=\"kirjaudu.jsp\">täällä</a></h1>");
                out.println("</div>");
                out.println("</body>");
                out.println("<footer>");
                out.println("<p>Tekijät/Copyright: Titta, Pia, Kristiina ja Riina</p>");
                out.println("<p>Ota yhteyttä: <a href=\"mailto:academy@academy.fi\">academy@academy.fi</a>.</p>");
                out.println("</footer>");
                out.println("</html>");
                out.close();
                session.invalidate();
            }
            out.close();
        }
    }