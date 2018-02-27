
package reseptiarkisto.domain;

public class Arvostelu {
    private Integer id;
    private Integer annosId;
    private String nimi;
    private Integer arvosana;
    private String kommentti;
    
    public Arvostelu(Integer id, Integer annosId, String nimi, Integer arvosana, String kommentti) {
        this.id = id;
        this.annosId = annosId;
        this.nimi = nimi;
        this.arvosana = arvosana;
        this.kommentti = kommentti;
    }
    
    public Integer getId() {
        return id;
    }

    public Integer getAnnosId() {
        return annosId;
    }
    
    public String getNimi() {
        return nimi;
    }
    
    public Integer getArvosana() {
        return arvosana;
    }
    
    public String getKommentti() {
        return kommentti;
    }

}
