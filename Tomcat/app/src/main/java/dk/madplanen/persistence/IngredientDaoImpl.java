package dk.madplanen.persistence;

import dk.madplanen.model.Ingredient;
import dk.madplanen.model.IngredientType;
import dk.madplanen.persistence.exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class IngredientDaoImpl implements IngredientDao {
    @Override
    public Optional<Ingredient> getIngredient(long id) {
        String query = """
                SELECT
                    i.id,
                    i.navn,
                    i.beskrivelse,
                    t.kode AS type_kode,
                    t.navn AS type_navn
                FROM ingrediens i
                JOIN ingrediens_type t
                ON i.type_kode = t.kode
                WHERE i.id = ?
                """;

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery();){
                if(!resultSet.next()) {
                    return Optional.empty();
                }

                IngredientType type = new IngredientType();
                type.setKode(resultSet.getString("type_kode"));
                type.setNavn(resultSet.getString("type_navn"));

                Ingredient ingredient = new Ingredient();
                ingredient.setId(resultSet.getLong("id"));
                ingredient.setNavn(resultSet.getString("navn"));
                ingredient.setBeskrivelse(resultSet.getString("beskrivelse"));
                ingredient.setType(type);

                return Optional.of(ingredient);
            }
        } catch (SQLException exception) {
            throw new DatabaseException("Failed to read Ingredient with id="  + id, exception);
        }
    }

    @Override
    public List<Ingredient> getAllIngredients() {
        String query = """
                SELECT
                    i.id,
                    i.navn,
                    i.beskrivelse,
                    t.navn AS type_navn,
                    t.kode AS type_kode
                FROM ingrediens i
                JOIN ingrediens_type t
                ON i.type_kode = t.kode
                """;
        try(Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)) {

            List<Ingredient> ingredients = new ArrayList<>();
            while(resultSet.next()) {
                ingredients.add(createIngredientFromResultSet(resultSet));
            }

            return ingredients;

        } catch (SQLException exception) {
            throw new DatabaseException("Could not read Ingredients data from the database", exception);
        }
    }

    @Override
    public Ingredient updateIngredient(Ingredient ingredient) {
        String query = """
                UPDATE ingrediens
                SET
                    navn = COALESCE(?, navn),
                    beskrivelse = COALESCE(?, beskrivelse),
                    type_kode = COALESCE(?, type_kode)
                WHERE id = ?
                RETURNING id, navn, beskrivelse, type_kode
                """;

        try(Connection connection = Database.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, ingredient.getNavn());
            statement.setString(2, ingredient.getBeskrivelse());
            statement.setString(3, ingredient.getType().getKode());
            statement.setLong(4, ingredient.getId());

            try(ResultSet resultSet = statement.executeQuery()) {
                if(!resultSet.next()) {
                    throw new DatabaseException("No ingredient with id=" + ingredient.getId() + ", exists");
                }

                IngredientType type = new IngredientType();
                type.setKode(resultSet.getString("type_kode"));

                Ingredient updated = new Ingredient();
                updated.setId(resultSet.getLong("id"));
                updated.setNavn(resultSet.getString("navn"));
                updated.setBeskrivelse(resultSet.getString("beskrivelse"));
                updated.setType(type);

                return updated;
            }

        } catch (SQLException exception) {
            throw new DatabaseException("Unable to update ingredient: id=" + ingredient.getId());
        }
    }

    @Override
    public Ingredient createIngredient(Ingredient ingredient) {
        String query = """
                INSERT INTO ingrediens (navn, beskrivelse, type_kode)
                VALUES (?, ?, ?)
                ON CONFLICT (navn) DO NOTHING
                RETURNING id, navn, beskrivelse, type_kode
                """;

        try(Connection connection = Database.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, ingredient.getNavn());
            statement.setString(2, ingredient.getBeskrivelse());
            statement.setString(3, ingredient.getType().getKode());

            try(ResultSet resultSet = statement.executeQuery()) {
                if(!resultSet.next()) {
                    throw new DatabaseException("No ingredient was created");
                }

                IngredientType type = new IngredientType();
                type.setKode(resultSet.getString("type_kode"));

                Ingredient created = new Ingredient();
                created.setId(resultSet.getLong("id"));
                created.setNavn(resultSet.getString("navn"));
                created.setBeskrivelse(resultSet.getString("beskrivelse"));
                created.setType(type);

                return created;
            }

        } catch (SQLException exception) {
            throw new DatabaseException("Failed to create ingredient", exception);
        }
    }

    @Override
    public void deleteIngredient(long id) {

    }

    private Ingredient createIngredientFromResultSet(ResultSet resultSet) throws SQLException {
        IngredientType ingrediensType = new IngredientType();
        ingrediensType.setNavn(resultSet.getString("type_navn"));
        ingrediensType.setKode(resultSet.getString("type_kode"));

        Ingredient ingrediens = new Ingredient();
        ingrediens.setId(resultSet.getLong("id"));
        ingrediens.setNavn(resultSet.getString("navn"));
        ingrediens.setBeskrivelse(resultSet.getString("beskrivelse"));
        ingrediens.setType(ingrediensType);

        return ingrediens;
    }
}
