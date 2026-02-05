package dk.madplanen.model;

public class Ingredient {

    private long id;
    private String navn;
    private String beskrivelse;
    private IngredientType type;

    public Ingredient() {

    }
    public Ingredient(String navn, String beskrivelse, IngredientType type) {
        this.navn = navn;
        this.beskrivelse = beskrivelse;
        this.type = type;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public IngredientType getType() {
        return type;
    }

    public void setType(IngredientType type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
