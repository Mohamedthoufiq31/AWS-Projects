package com.example;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        String currentTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern(
                        "dd-MM-yyyy HH:mm:ss"));

        response.getWriter().println(
            "<!DOCTYPE html>" +
            "<html lang='en'>" +
            "<head>" +
            "<meta charset='UTF-8'>" +
            "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
            "<title>DevOps Learning Dashboard</title>" +
            "<style>" +
            "body { margin:0; font-family:Arial,sans-serif;" +
            "background:#0f172a; color:#f8fafc; text-align:center; }" +
            ".container { max-width:700px; margin:60px auto; padding:30px; }" +
            ".card { background:#1e293b; padding:30px; border-radius:16px;" +
            "box-shadow:0 8px 25px rgba(0,0,0,0.3); }" +
            "h1 { color:#38bdf8; }" +
            ".badge { display:inline-block; background:#14532d;" +
            "color:#bbf7d0; padding:8px 14px; border-radius:20px; }" +
            "ul { list-style:none; padding:0; line-height:2; }" +
            "li { background:#334155; margin:8px 0; padding:8px;" +
            "border-radius:8px; }" +
            ".footer { color:#94a3b8; margin-top:25px; font-size:14px; }" +
            "</style>" +
            "</head>" +
            "<body>" +
            "<div class='container'>" +
            "<div class='card'>" +
            "<h1>Welcome to My DevOps Journey!</h1>" +
            "<p>Building, Deploying and Learning Every Day</p>" +
            "<p class='badge'>Application Status: Running</p>" +
            "<h2>Technologies I'm Practicing</h2>" +
            "<ul>" +
            "<li>Java & Servlets</li>" +
            "<li>Apache Tomcat</li>" +
            "<li>Maven Build Automation</li>" +
            "<li>Jenkins CI/CD</li>" +
            "<li>AWS Cloud & Linux</li>" +
            "<li>Docker & Kubernetes</li>" +
            "</ul>" +
            "<p><strong>Server Time:</strong> " +
            currentTime + "</p>" +
            "<div class='footer'>" +
            "Powered by Java Servlet & Apache Tomcat" +
            "<br>Learning by Building Real Projects" +
            "</div>" +
            "</div></div></body></html>"
        );
    }
}
