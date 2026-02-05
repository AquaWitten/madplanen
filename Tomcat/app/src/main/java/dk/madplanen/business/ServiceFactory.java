package dk.madplanen.business;

public class ServiceFactory {

    private ServiceFactory() {}

    public static IngredientService ingredientService() {
        return new IngredientServiceImpl();
    }
}
