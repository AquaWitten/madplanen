package dk.madplanen.api.dto;

public class IngredientBaseDto {

    private String navn;
    private String beskrivelse;
    private String type;

    public IngredientBaseDto() {

    }

    public IngredientBaseDto(String navn, String beskrivelse, String type) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
