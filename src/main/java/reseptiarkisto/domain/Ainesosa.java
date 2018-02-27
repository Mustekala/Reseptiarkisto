
package reseptiarkisto.domain;

public class Ainesosa {
    
    private Integer id;
    private String nimi;
    private String maara;
    
    public Ainesosa(Integer id, String nimi) {
        this.id = id;
        this.nimi = nimi;
    }
    
    //Kun ainesosia listataan annoksen omalle sivulle, lisataan annos-olioon myos maara, 
    //joka saadaan AnnosAinesosa-oliosta. Nain maara saadaan nakymaan ainesosan nimen vieressa sivulla.
    public Ainesosa(Integer id, String nimi, String maara) {
        this.id = id;
        this.nimi = nimi;
        this.maara = maara;
    }
    
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNimi() {
        return this.nimi;
    }
    
    public String getMaara() {
        return this.maara;
    }

}   
