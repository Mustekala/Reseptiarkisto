
package reseptiarkisto.domain;


public class AnnosAinesosa {
    
    private Integer id;
    private Integer annosId;
    private Integer ainesosaId;
    private Integer jarjestys;
    private String maara;
    private String ohje;
       
    public AnnosAinesosa(Integer id, Integer annosId, Integer ainesosaId, Integer jarjestys, String maara, String kommentti) {
        this.id = id;
        this.annosId = annosId;
        this.ainesosaId = ainesosaId;
        this.maara = maara;
        this.jarjestys = jarjestys;
        this.ohje = kommentti;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public Integer getAnnosId() {
        return this.annosId;
    }
    
    public Integer getAinesosaId() {
        return this.ainesosaId;
    }
    
    public String getMaara() {
        return this.maara;
    }
    
    public Integer getJarjestys() {
        return this.jarjestys;
    }
    
    public String getOhje() {
        return this.ohje;
    }
}
