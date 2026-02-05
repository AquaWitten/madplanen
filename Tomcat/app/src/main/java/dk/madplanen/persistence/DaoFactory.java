package dk.madplanen.persistence;

public class DaoFactory {

    private DaoFactory() {}

    public static IngredientDao IngredientDao() {
        return new IngredientDaoImpl();
    }


}
