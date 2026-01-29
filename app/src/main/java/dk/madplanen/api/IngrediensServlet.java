package dk.madplanen.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.madplanen.model.Ingrediens;
import dk.madplanen.model.IngrediensType;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IngrediensServlet extends HttpServlet {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final Logger logger = Logger.getLogger(IngrediensServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String sti = req.getPathInfo();

        //Hvis der ikke er nogen sti hent alle ingredienser
        if(sti == null || sti.equals("/")) {
            //TODO Business logic
            List<Ingrediens> ingredienser = List.of(
                    new Ingrediens("Laks", "Dyr som lever i havet", IngrediensType.KOED),
                    new Ingrediens("Bjørn", "Dyr som lever i skoven", IngrediensType.KOED)
            );

            logger.log(Level.INFO,"Alle ingredienser er hentet");
            lavJsonResponse(resp, HttpServletResponse.SC_OK, ingredienser);

        } else {
            //Hvis der er en sti parameter hent en ingrediens
            try {
                long id = hentIdFraPath(sti);
                //TODO Business logic
                //Dummy ingrediens
                Ingrediens ingrediens = new Ingrediens("Æble", "En frugt der hænger på træer", IngrediensType.FRUGT);

                logResponse("Ingrediens er hentet: {0}", ingrediens);
                lavJsonResponse(resp, HttpServletResponse.SC_OK, ingrediens);

            } catch (NumberFormatException exception) {
                daarligRequest(resp, "Kunne ikke formatere sti parameter integer", exception);

            } catch (IllegalArgumentException exception) {
                daarligRequest(resp, "Forkert antal sti parametre er angivet i request.", exception);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        Ingrediens ingrediens;

        try {
            String requestBody = getRequestBody(req);
            if(requestBody.isBlank()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request body er påkrævet");
                return;
            }

            ingrediens = mapBodyTilIngrediens(requestBody);

        } catch (JsonProcessingException exception) {
            daarligRequest(resp, "Ugyldig JSON i request body.", exception);
            return;

        } catch (IOException exception) {
            daarligRequest(resp, "Var ikke i stand til at læse request body.", exception);
            return;
        }

        //TODO Business Logik
        logResponse("Ny ingrediens er oprettet: {0}" ,ingrediens);
        lavJsonResponse(resp, HttpServletResponse.SC_CREATED, ingrediens);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String sti = req.getPathInfo();

        if(sti == null || sti.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mangler sti parameter");
            return;
        }

        Ingrediens ingrediensOpdatering;
        
        try {
            long id = hentIdFraPath(sti);
            String requestBody = getRequestBody(req);
            if(requestBody.isBlank()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request body er påkrævet");
                return;
            }

            ingrediensOpdatering = mapBodyTilIngrediens(requestBody);
            ingrediensOpdatering.setId(id);

        } catch (NumberFormatException exception) {
            daarligRequest(resp, "Kunne ikke formatere sti parameter integer", exception);
            return;

        } catch (JsonProcessingException exception) {
            daarligRequest(resp, "Ugyldig JSON i request body.", exception);
            return;

        } catch (IllegalArgumentException exception) {
            daarligRequest(resp, "Forkert antal sti parametre er angivet i request.", exception);
            return;

        } catch (IOException exception) {
            daarligRequest(resp, "Var ikke i stand til at læse request body.", exception);
            return;
        }

        //TODO Business logik her

        logResponse("Ingrediens er opdateret: {0}", ingrediensOpdatering);
        lavJsonResponse(resp, HttpServletResponse.SC_OK, ingrediensOpdatering);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String sti = req.getPathInfo();

        long id;

        if(sti == null || sti.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mangler sti parameter");
            return;
        }

        try {
            id = hentIdFraPath(sti);

        } catch (NumberFormatException exception) {
            daarligRequest(resp, "Kunne ikke formatere sti parameter integer", exception);
            return;

        } catch (IllegalArgumentException exception) {
            daarligRequest(resp, "Forkert antal sti parametre er angivet i request.", exception);
            return;
        }

        //TODO Business logik her

        logger.log(Level.INFO, "Ingrediens er er slettet med id: {0}" ,id);
        sendIntetIndhold(resp);
    }

    private String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder body = new StringBuilder();
        BufferedReader reader = request.getReader();

        String line;
        while((line = reader.readLine()) != null) {
            body.append(line);
        }

        return body.toString();
    }

    private Ingrediens mapBodyTilIngrediens(String requestBody) throws JsonProcessingException {
        return mapper.readValue(requestBody, Ingrediens.class);
    }

    private long hentIdFraPath(String sti) throws NumberFormatException {
        String[] stiDele = sti.split("/");

        if(stiDele.length != 2) {
            //Første element vil være tom, da der splittes på '/'
            throw new IllegalArgumentException("Sti parameter indeholder forkert antal parametre. Antal: " + (stiDele.length - 1));
        }
        return Long.parseLong(stiDele[1]);
    }

    private void sendIntetIndhold(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private void lavJsonResponse(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if(body != null) {
            mapper.writeValue(resp.getOutputStream(), body);
        }
    }

    public void logResponse(String message, Object objekt) {
        try {
            logger.log(Level.INFO, message, mapper.writeValueAsString(objekt));
        } catch (JsonProcessingException exception) {
            logger.log(Level.WARNING, "Kunne ikke seralisere ingrediens til log.", exception);
        }
    }

    private void daarligRequest(HttpServletResponse resp, String message, Exception exception) throws IOException {
        logger.log(Level.INFO, message, exception);
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
    }
}
