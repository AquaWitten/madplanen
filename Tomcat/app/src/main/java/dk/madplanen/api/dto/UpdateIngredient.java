package dk.madplanen.api.dto;

public class UpdateIngredient extends IngredientBaseDto {
    public UpdateIngredient() {

    }

    public UpdateIngredient(String navn, String beskrivelse, String type) {
        super(navn, beskrivelse, type);
    }
}
