package dk.madplanen.api.dto;

public class CreateIngredient extends IngredientBaseDto{



    public CreateIngredient() {}

    public CreateIngredient(String navn, String beskrivelse, String type) {
        super(navn, beskrivelse, type);
    }


}
