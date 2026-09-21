import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
public class PlayServlet extends HttpServlet {
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String errMsg = "";
      Connection con = null;
      List<String> contentPaths = new ArrayList<String>();
      try {
         try { Class.forName("oracle.jdbc.OracleDriver"); } catch (Exception ex) { }
         con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:49712:XE", "system", "oracle1");
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery("select id, question, contentpath from trivias order by id");
         while (rs.next()) {
            contentPaths.add(rs.getString("contentpath"));
	 }
         stmt.close();
         con.close();
      } catch(SQLException ex) {
         errMsg = errMsg + "\n--- SQLException caught ---\n";
         while (ex != null) {
            errMsg += "Message: " + ex.getMessage ();
            errMsg += "SQLState: " + ex.getSQLState ();
            errMsg += "ErrorCode: " + ex.getErrorCode ();
            ex = ex.getNextException();
            errMsg += "";
         }
      }

      HttpSession session = request.getSession(true);
      Integer idx = (Integer) session.getAttribute("PLAY_INDEX");
      if (idx == null) idx = 0;
      String dir = request.getParameter("dir");
      if ("next".equals(dir)) idx = idx + 1;
      else if ("prev".equals(dir)) idx = idx - 1;

      String contentPath;
      if (contentPaths.isEmpty()) {
         idx = 0;
         contentPath = "tgbNymZ7vqY"; // fallback when no rows are found in the trivias table
      } else {
         if (idx < 0) idx = contentPaths.size() - 1;
         if (idx >= contentPaths.size()) idx = 0;
         contentPath = contentPaths.get(idx);
      }
      session.setAttribute("PLAY_INDEX", idx);

      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      out.println(
"<!DOCTYPE html>" +
"<meta charset='UTF-8'>" +
"<body>" +
"<div>" +
"<iframe id=\"Video\" width=\"420\" height=\"345\" src=https://www.youtube.com/embed/" + contentPath +"?autoplay=1&mute=1&start=62&end=162>" +
"</iframe>" +
"</div>" +
"<div>" +
"<form action='/trivia/play' method='GET'>" +
"<br>" +
"<div class='button'>" +
"<button class='button' id='prev' name='dir' value='prev'>Prev</button>" +
"<button class='button' id='next' name='dir' value='next'>Next</button>" +
"</div>" +
"<br>" +
"</form>" +
"<div>" +
"<form action='main' method='GET'>" +
"<button class='button' id='main'>Main</button>" +
"</form>" +
"</div>" +
"<br>" +
"</body>" +
"</html>"
      );
   }
}