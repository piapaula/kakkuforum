package fi.academy.servlets;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


// Tässä servletissä käytetään sekä post- että get-metodia: postia käyt
@WebServlet(name = "KetjunLuontiServlet", urlPatterns = {"/KetjunLuontiServlet"})
public class KetjunLuontiServlet extends HttpServlet {

    @Resource(name = "kakkuforum")
    DataSource ds;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String otsikko = request.getParameter("otsikko");
        String viesti = request.getParameter("viesti");
        String viestialue = request.getParameter("viestialue");

        try (Connection con = ds.getConnection()) {
            String sql = "INSERT INTO viestit (otsikko, tekstikentta, viestialue) VALUES (?, ?, ?)";
            PreparedStatement lause = con.prepareStatement(sql);
            lause.setString(1, otsikko);
            lause.setString(2, viesti);
            lause.setString(3, viestialue);
            lause.executeUpdate();

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

            RequestDispatcher rd=request.getRequestDispatcher("kuppikakut.jsp");
            rd.forward(request,response);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


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