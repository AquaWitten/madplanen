package dk.madplanen.business;

import dk.madplanen.model.Ingrediens;

import java.util.List;

public interface IngrediensService {

    List<Ingrediens> hentAlleIngredienser();
    Ingrediens hentIngrediens(long id);
    Ingrediens opretIngrediens(Ingrediens ingrediens);
    Ingrediens opdaterIngrediens(long id, Ingrediens ingrediens);
    boolean sletIngrediens(long id);

}
