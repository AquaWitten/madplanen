package dk.madplanen.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.madplanen.api.dto.CreateIngredient;
import dk.madplanen.api.dto.IngredientBaseDto;
import dk.madplanen.api.dto.UpdateIngredient;
import dk.madplanen.api.exception.RequestBodyException;
import dk.madplanen.api.exception.UrlParameterException;
import dk.madplanen.business.IngredientService;
import dk.madplanen.business.ServiceFactory;
import dk.madplanen.business.exception.NameConflictException;
import dk.madplanen.business.exception.NotFoundException;
import dk.madplanen.model.Ingredient;
import dk.madplanen.model.IngredientType;
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

    private IngredientService ingredientService;

    @Override
    public void init() {
        ingredientService = ServiceFactory.ingredientService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String path = req.getPathInfo();

        //If there is no path fetch all ingredients
        if(path == null || path.equals("/")) {
            List<Ingredient> ingredients = ingredientService.getAllIngredients();

            logger.log(Level.INFO,"All ingredients have been fetched: amount=" + ingredients.size());
            createResponse(resp, HttpServletResponse.SC_OK, ingredients);

        } else {
            //Hvis there is a path parameter fetch one ingredient
            try {
                long id = getIdFromPath(path);
                Ingredient ingredient = ingredientService.getIngredient(id);

                logResponse("Ingredient was fetched: {0}", ingredient);
                createResponse(resp, HttpServletResponse.SC_OK, ingredient);
            } catch (RuntimeException exception) {
                handleException(resp, exception);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        try {
            String requestBody = getRequestBody(req);
            CreateIngredient createIngredient = readJson(requestBody, CreateIngredient.class);
            Ingredient ingrediens = mapToIngredient(createIngredient);

            Ingredient createdIngredient = ingredientService.createIngredient(ingrediens);

            logResponse("Ingredient is created: {0}" ,createdIngredient);
            createResponse(resp, HttpServletResponse.SC_CREATED, createdIngredient);

        } catch (RuntimeException exception) {
            handleException(resp, exception);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        
        try {
            long id = getIdFromPath(req.getPathInfo());
            String requestBody = getRequestBody(req);
            UpdateIngredient updateIngredient = readJson(requestBody, UpdateIngredient.class);
            Ingredient ingredient = mapToIngredient(updateIngredient);
            ingredient.setId(id);

            Ingredient updatedIngredient = ingredientService.updateIngredient(ingredient);

            logResponse("Ingredient is updated: {0}", updatedIngredient);
            createResponse(resp, HttpServletResponse.SC_OK, updatedIngredient);

        } catch (RuntimeException exception) {
            handleException(resp, exception);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        try {
            long id = getIdFromPath(req.getPathInfo());
            ingredientService.deleteIngredient(id);

            logger.log(Level.INFO, "Ingredient with id={0} has been deleted", id);
            createResponse(resp, HttpServletResponse.SC_NO_CONTENT, null);

        } catch (RuntimeException exception) {
            handleException(resp, exception);
        }
    }

    private String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder body = new StringBuilder();
        BufferedReader reader = request.getReader();

        String line;
        while((line = reader.readLine()) != null) {
            body.append(line);
        }

        String requestBody = body.toString();
        if(requestBody.isBlank()) {
            throw new RequestBodyException("Request body is missing");
        } else {
            return requestBody;
        }
    }

    private void handleException(HttpServletResponse resp, Exception e) throws IOException {
        if(e instanceof NotFoundException) {
            badRequest(resp, HttpServletResponse.SC_NOT_FOUND, e);
        } else if (e instanceof NameConflictException) {
            badRequest(resp, HttpServletResponse.SC_CONFLICT, e);
        } else if (e instanceof UrlParameterException || e instanceof JsonProcessingException || e instanceof RequestBodyException) {
            badRequest(resp, HttpServletResponse.SC_BAD_REQUEST, e);
        } else {
            logger.log(Level.SEVERE, "Unexpected error", e);
            badRequest(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
        }
    }

    private <T> T readJson(String requestBody, Class<T> clazz) {
        try {
            return mapper.readValue(requestBody, clazz);

        } catch (JsonProcessingException exception) {
            throw new RequestBodyException("JSON body is malformed and could not be parsed", exception);
        }
    }

    private long getIdFromPath(String sti) {
        if(sti == null || sti.isBlank()) {
            throw new UrlParameterException("No Url parameter found.");
        }

        String[] pathParts = sti.split("/");
        if(pathParts.length != 2) {
            //First element is empty as string is split on '/'
            throw new UrlParameterException("Path has wrong amount of parameters: Amount: " + (pathParts.length - 1));
        }

        try {
            return Long.parseLong(pathParts[1]);
        } catch (NumberFormatException exception) {
            throw new UrlParameterException("Url parameter could not be parsed to long");
        }
    }

    private void createResponse(HttpServletResponse resp, int status, Object body) throws IOException {
        if(body == null) {
            resp.setStatus(status);
        } else {
            resp.setStatus(status);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            mapper.writeValue(resp.getOutputStream(), body);
        }
    }

    public void logResponse(String message, Object objekt) {
        try {
            logger.log(Level.INFO, message, mapper.writeValueAsString(objekt));
        } catch (JsonProcessingException exception) {
            logger.log(Level.WARNING, "Unable to serialize ingredient for logging", exception);
        }
    }

    private void badRequest(HttpServletResponse resp, int status, Exception exception) throws IOException {
        logger.log(Level.INFO, exception.getMessage(), exception);
        resp.sendError(status, exception.getMessage());
    }

    private Ingredient mapToIngredient(IngredientBaseDto dto) {
        return new Ingredient(
                dto.getNavn(),
                dto.getBeskrivelse(),
                new IngredientType(dto.getType())
        );
    }
}
