
package reseptiarkisto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import reseptiarkisto.database.Database;
import reseptiarkisto.domain.AnnosAinesosa;

public class AnnosAinesosaDao implements Dao<AnnosAinesosa, Integer> {

    private Database database;

    public AnnosAinesosaDao(Database database) {
        this.database = database;
    }

    @Override
    public AnnosAinesosa findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<AnnosAinesosa> findAll() throws SQLException {
        List<AnnosAinesosa> annosainesosat = new ArrayList<>();

        try (Connection conn = database.getConnection();
            ResultSet result = conn.prepareStatement("SELECT id, annos_id, ainesosa_id, jarjestys, maara, ohje FROM AnnosAinesosa").executeQuery()) {

            while (result.next()) {
                annosainesosat.add(new AnnosAinesosa(result.getInt("id"), result.getInt("annos_id"), result.getInt("ainesosa_id"), result.getInt("jarjestys"), result.getString("maara"), result.getString("ohje")));
            }
        }

        return annosainesosat;
    }
    
    public List<AnnosAinesosa> findAllWithAnnosId(Integer id) throws SQLException {
        List<AnnosAinesosa> annosainesosat = new ArrayList<>();

        try (Connection conn = database.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT id, annos_id, ainesosa_id, jarjestys, maara, ohje FROM AnnosAinesosa WHERE annos_id = ?")) {
            stmt.setInt(1, id);
            
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                annosainesosat.add(new AnnosAinesosa(result.getInt("id"), result.getInt("annos_id"), result.getInt("ainesosa_id"), result.getInt("jarjestys"), result.getString("maara"), result.getString("ohje")));
            }
        }

        return annosainesosat;
    }
     
    public List<AnnosAinesosa> findAllWithAinesosaId(Integer id) throws SQLException {
        List<AnnosAinesosa> annosainesosat = new ArrayList<>();

        try (Connection conn = database.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT id, annos_id, ainesosa_id, jarjestys, maara, ohje FROM AnnosAinesosa WHERE ainesosa_id = ?")) {
            stmt.setInt(1, id);
            
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                annosainesosat.add(new AnnosAinesosa(result.getInt("id"), result.getInt("annos_id"), result.getInt("ainesosa_id"), result.getInt("jarjestys"), result.getString("maara"), result.getString("ohje")));
            }
        }

        return annosainesosat;
    }
    
    @Override
    public AnnosAinesosa saveOrUpdate(AnnosAinesosa object) throws SQLException {
  
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO AnnosAinesosa (annos_id, ainesosa_id, jarjestys, maara, ohje) VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, object.getAnnosId());
            stmt.setInt(2, object.getAinesosaId());
            stmt.setInt(3, object.getJarjestys());
            stmt.setString(4, object.getMaara());
            stmt.setString(5, object.getOhje());
            stmt.executeUpdate();
        }

        return null;
    }

    private AnnosAinesosa findByName(String name) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
