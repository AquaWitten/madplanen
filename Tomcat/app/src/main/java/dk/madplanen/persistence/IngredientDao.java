package dk.madplanen.persistence;

import dk.madplanen.model.Ingredient;

import java.util.List;
import java.util.Optional;

public interface IngredientDao {

    Optional<Ingredient> getIngredient(long id);

    List<Ingredient> getAllIngredients();

    Ingredient updateIngredient(Ingredient ingredient);

    Ingredient createIngredient(Ingredient ingredient);

    void deleteIngredient(long id);
}
