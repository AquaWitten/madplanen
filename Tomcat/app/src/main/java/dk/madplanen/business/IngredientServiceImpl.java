package dk.madplanen.business;

import dk.madplanen.business.exception.NameConflictException;
import dk.madplanen.business.exception.NotFoundException;
import dk.madplanen.model.Ingredient;
import dk.madplanen.persistence.DaoFactory;
import dk.madplanen.persistence.IngredientDao;
import dk.madplanen.persistence.exception.DatabaseException;

import java.sql.SQLException;
import java.util.List;

class IngredientServiceImpl implements IngredientService {

    private IngredientDao ingredientDao;

    IngredientServiceImpl() {
        ingredientDao = DaoFactory.IngredientDao();
    }


    @Override
    public List<Ingredient> getAllIngredients() {
        return ingredientDao.getAllIngredients();
    }

    @Override
    public Ingredient getIngredient(long id) {
        return ingredientDao.getIngredient(id).orElseThrow(() ->
                new NotFoundException("Ingredient not found: id=" + id)
        );
    }

    @Override
    public Ingredient createIngredient(Ingredient ingredient) {
        try {
            return ingredientDao.createIngredient(ingredient);

        } catch (DatabaseException exception) {
            if(isUniqueViolation(exception)) {
                throw new NameConflictException("Ingredient name already exists: name=" + ingredient.getNavn());
            } else {
                throw exception;
            }
        }
    }

    @Override
    public Ingredient updateIngredient(Ingredient ingredient) {
        try {
            normalizeIngredient(ingredient);
            return ingredientDao.updateIngredient(ingredient);

        } catch (DatabaseException exception) {
            if(isUniqueViolation(exception)) {
                throw new NameConflictException("Ingredient name already exists: name=" + ingredient.getNavn());
            } else {
                throw exception;
            }
        }
    }

    @Override
    public boolean deleteIngredient(long id) {
        return false;
    }

    private boolean isUniqueViolation(DatabaseException exception) {
        Throwable cause = exception.getCause();
        return cause instanceof SQLException sqlException && DatabaseException.POSTGRES_UNIQUE_VIOLATION.equals(sqlException.getSQLState());
    }

    private void normalizeIngredient(Ingredient ingredient) {
        ingredient.setNavn(normalizeStringValue(ingredient.getNavn()));
        ingredient.setBeskrivelse(normalizeStringValue(ingredient.getBeskrivelse()));

        String type = ingredient.getType().getKode();
        ingredient.getType().setKode(normalizeStringValue(type));
    }

    private String normalizeStringValue(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }
}
