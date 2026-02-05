package dk.madplanen.business;

import dk.madplanen.model.Ingredient;

import java.util.List;

public interface IngredientService {

    List<Ingredient> getAllIngredients();
    Ingredient getIngredient(long id);
    Ingredient createIngredient(Ingredient ingrediens);
    Ingredient updateIngredient(Ingredient ingrediens);
    boolean deleteIngredient(long id);

}
