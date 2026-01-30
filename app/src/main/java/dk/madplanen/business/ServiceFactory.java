package dk.madplanen.business;

public class ServiceFactory {

    public static IngrediensService ingrediensService() {
        return new IngrediensServiceImpl();
    }
}
