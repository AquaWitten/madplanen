package dk.madplanen.api;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OpenApiServlet extends HttpServlet {

    private final String OPENAPI_PATH = "/WEB-INF/openapi/openapi.yml";
    private final Logger logger = Logger.getLogger(OpenApiServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        ServletContext context = req.getServletContext();

        try(InputStream stream = context.getResourceAsStream(OPENAPI_PATH)) {
            if(stream == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Unable to find OpenAPI documentation");
            } else {
                resp.setCharacterEncoding("UTF-8");
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/yaml");
                resp.getOutputStream().write(stream.readAllBytes());
            }
        } catch (IOException exception) {
            logger.log(Level.WARNING, "Unable to read OpenApi.yml file",exception);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
