package dk.madplanen.model;

public class Ingrediens {

    private long id;
    private String navn;
    private String beskrivelse;
    private IngrediensType type;

    public Ingrediens() {

    }
    public Ingrediens(String navn, String beskrivelse, IngrediensType type) {
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

    public IngrediensType getType() {
        return type;
    }

    public void setType(IngrediensType type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
