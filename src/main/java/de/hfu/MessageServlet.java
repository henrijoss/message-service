package de.hfu;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import de.hfu.messages.domain.service.MessageService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


@WebServlet("/hello.html")
public class MessageServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        WebApplicationContext applicationContext = WebApplicationContextUtils
                .getWebApplicationContext(req.getServletContext());
        MessageService messageService = (MessageService) applicationContext.getBean("messageService");

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        out
                .append("<!DOCTYPE html><html><body>")
                .append("<link href=\"./styles/CSS/style.css\" rel=\"stylesheet\" media=\"screen\">")
                .append("<div class=\"chat\">\n" + "<article class=\"chat-list\">");
        messageService.findAllMessages().forEach((message) -> out
                .append("<section class=\"chat-massage\">" + "<p class=\"name\"><strong>")
                .append(message.getUser().getFullname())
                .append("</strong></p>\n")
                .append("<p class=\"massage\">")
                .append(message.getText())
                .append("</p>\n")
                .append("<time class=\"timestamp\"><i>")
                .append(String.valueOf(message.getDate()))
                .append("</i></time>\n")
                .append("</section>"));
        out
                .append("</article>\n" + "</div>")
                .append("</body></html>");
    }
}
