import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SimpleFormInsert")
public class SimpleFormInsert extends HttpServlet {
   private static final long serialVersionUID = 1L;

   public SimpleFormInsert() {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String name = request.getParameter("name");
      String email = request.getParameter("email");
      String subject = request.getParameter("subject");
      String message = request.getParameter("message");

      Connection connection = null;
      String insertSql = " INSERT INTO Contact (id, NAME, EMAIL, SUBJECT, MESSAGE) values (default, ?, ?, ?, ?)";

      try {
         DBConnection_contact.getDBConnection();
         connection = DBConnection_contact.connection;
         PreparedStatement preparedStmt = connection.prepareStatement(insertSql);
         preparedStmt.setString(1, name);
         preparedStmt.setString(2, email);
         preparedStmt.setString(3, subject);
         preparedStmt.setString(4, message);
         preparedStmt.execute();
         connection.close();
      } catch (Exception e) {
         e.printStackTrace();
      }

      // Set response content type
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      String title = "Insert Data to DB table";
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
      out.println(docType + //
            "<html>\n" + //
            "<head><title>" + title + "</title></head>\n" + //
            "<body bgcolor=\"#f0f0f0\">\n" + //
            "<h2 align=\"center\">" + title + "</h2>\n" + //
            "<ul>\n" + //

            "  <li><b>Name</b>: " + name + "\n" + //
            "  <li><b>Email</b>: " + email + "\n" + //
            "  <li><b>Subject</b>: " + subject + "\n" + //
            "  <li><b>Message</b>: " + message + "\n" + //

            "</ul>\n");

      out.println("<a href=/webproject/simpleFormSearch.html>Search Data</a> <br>");
      out.println("</body></html>");
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }

}