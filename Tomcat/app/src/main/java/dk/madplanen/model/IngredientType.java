package dk.madplanen.model;

public class IngredientType {

    private String kode;
    private String navn;

    public IngredientType() {}

    public IngredientType(String kode) {
        this.kode = kode;
    }

    public IngredientType(String kode, String navn) {
        this.kode = kode;
        this.navn = navn;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }
}
