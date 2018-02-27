
package reseptiarkisto;

import reseptiarkisto.database.*;
import reseptiarkisto.dao.*;
import java.util.HashMap;
import reseptiarkisto.domain.*;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Main {
    
    public static void main(String[] args) throws Exception {
        
        Database database = new Database("jdbc:sqlite:reseptiarkisto.db");
        
        AinesosaDao ainesosat = new AinesosaDao(database);
        AnnosDao annokset = new AnnosDao(database);
        AnnosAinesosaDao annosainesosat = new AnnosAinesosaDao(database);
        ArvosteluDao arvostelut = new ArvosteluDao(database);
        
        //Etusivu
        Spark.get("/", (req, res) -> {
            HashMap map = new HashMap<>();

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        //Ainesosien lisaaminen ja listaaminen         
        Spark.get("/ainesosat", (req, res) -> {
            HashMap map = new HashMap<>();
            
            map.put("ainesosat", ainesosat.findAll());
            
            return new ModelAndView(map, "ainesosat");
        }, new ThymeleafTemplateEngine());
        
        Spark.post("/ainesosat", (req, res) -> {
            Ainesosa osa = new Ainesosa(-1, req.queryParams("nimi"));
            ainesosat.saveOrUpdate(osa);

            res.redirect("/ainesosat");
            return "";
        });
        
        Spark.get("/ainesosat/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            Integer ainesosaId = Integer.parseInt(req.params(":id"));
            map.put("ainesosa", ainesosat.findOne(ainesosaId));
            map.put("annokset", annokset.findAinesosaAnnokset(ainesosaId));
            
            return new ModelAndView(map, "ainesosa");
        }, new ThymeleafTemplateEngine());
        
        
        //Annosten lisaaminen ja listaaminen
                
        Spark.get("/annokset", (req, res) -> {
            HashMap map = new HashMap<>();
            
            map.put("annokset", annokset.findAll());
            
            return new ModelAndView(map, "annokset");
        }, new ThymeleafTemplateEngine());
        
        Spark.get("/annokset_lisaa", (req, res) -> {
            HashMap map = new HashMap<>();
            
            map.put("annokset", annokset.findAll());
            map.put("ainesosat", ainesosat.findAll());
            
            return new ModelAndView(map, "annokset_lisaa");
        }, new ThymeleafTemplateEngine());
                
        Spark.post("/annokset", (req, res) -> {
            Annos annos = new Annos(-1, req.queryParams("nimi"));
            annokset.saveOrUpdate(annos);

            res.redirect("/annokset_lisaa");
            return "";
        });
        
        Spark.get("/annokset/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            Integer annosId = Integer.parseInt(req.params(":id"));
            map.put("annos", annokset.findOne(annosId));
            map.put("ainesosat", ainesosat.findAnnosAinesosat(annosId));
            map.put("annosainesosat", annosainesosat.findAllWithAnnosId(annosId));
            map.put("arvostelut", arvostelut.findAnnosArvostelut(annosId));
            
            return new ModelAndView(map, "annos");
        }, new ThymeleafTemplateEngine());
        
        Spark.post("/annokset/:id", (req, res) -> {
            Integer annosID = Integer.parseInt(req.params(":id"));
            Integer ainesosaId = Integer.parseInt(req.queryParams("ainesosaId"));
            Integer jarjestys = annosainesosat.findAllWithAnnosId(annosID).size() + 1;
            String maara = req.queryParams("maara");
            String ohje = req.queryParams("ohje");
            
            AnnosAinesosa aa = new AnnosAinesosa(-1, annosID, ainesosaId, jarjestys, maara, ohje);
            annosainesosat.saveOrUpdate(aa);
            
            res.redirect("/annokset_lisaa");
            
            return "";
        });
        
        /*Arvostelujen lisaaminen ja listaaminen         
        Spark.get("/arvostelut/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            Integer annosId = Integer.parseInt(req.params(":id"));
            map.put("annos", annokset.findOne(annosId));
            map.put("ainesosat", ainesosat.findAnnosAinesosat(annosId));
            map.put("annosainesosat", annosainesosat.findAllWithAnnosId(annosId));
            
            return new ModelAndView(map, "annos");
        }, new ThymeleafTemplateEngine());*/
        
        Spark.post("/arvostelut", (req, res) -> {
            Integer annosID = Integer.parseInt(req.queryParams("id"));
            String nimi = req.queryParams("nimi");
            Integer arvosana = Integer.parseInt(req.queryParams("arvosana"));
            String kommentti = req.queryParams("kommentti");
            
            Arvostelu k = new Arvostelu(-1, annosID, nimi, arvosana, kommentti);
            arvostelut.saveOrUpdate(k);
            
            res.redirect("/annokset/" + annosID);
            
            return "";
        });
    }
}
