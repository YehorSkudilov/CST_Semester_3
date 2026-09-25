import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.*;

public class MainServlet extends HttpServlet {

  public void doGet(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {
      HttpSession session = request.getSession(false);
      if (session == null) {
        response.setStatus(302);
	response.sendRedirect("login");
	return;
      }

      String action = request.getParameter("action");
      if (action != null) {
         if ("upload".equals(action)) {
            response.sendRedirect("upload");
         } else if ("play".equals(action)) {
            response.sendRedirect("play");
         } else if ("logout".equals(action)) {
            response.sendRedirect("logout");
         } else {
            response.sendRedirect("main");
         }
         return;
      }

      String title = "Logged in as: ";
      title += session.getAttribute("USER_ID");
      response.setContentType("text/html");
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
      String html = docType + "<html>\n" + "<head><title>" + title + "</title></head>\n"
						+ "<body bgcolor=\"#f0f0f0\">\n" + "<h1 align=\"center\">" + title + "</h1>\n"+
						"<div style=\"text-align: center;\">\n" +
						"<form action=\"main\" method=\"GET\">\n" +
                    	"<button type=\"submit\" name=\"action\" value=\"upload\">UPLOAD</button>\n" +
                    	"<button type=\"submit\" name=\"action\" value=\"play\">PLAY GALLERY</button>\n" +
                    	"<button type=\"submit\" name=\"action\" value=\"logout\">LOGOUT</button>\n" +
                    	"</form>\n" +
						"</div>\n" +  "</body></html>";

      PrintWriter out = response.getWriter();
      out.println(html);
  }
}
