
package reseptiarkisto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import reseptiarkisto.database.Database;
import reseptiarkisto.domain.Ainesosa;

public class AinesosaDao implements Dao<Ainesosa, Integer> {

    private Database database;

    public AinesosaDao(Database database) {
        this.database = database;
    }

    @Override
    public Ainesosa findOne(Integer key) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, nimi FROM Ainesosa WHERE id = ?");
            stmt.setInt(1, key);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new Ainesosa(result.getInt("id"), result.getString("nimi"));
        }
    }

    @Override
    public List<Ainesosa> findAll() throws SQLException {
        List<Ainesosa> ainesosat = new ArrayList<>();

        try (Connection conn = database.getConnection();
            ResultSet result = conn.prepareStatement("SELECT id, nimi FROM Ainesosa").executeQuery()) {

            while (result.next()) {
                ainesosat.add(new Ainesosa(result.getInt("id"), result.getString("nimi")));
            }
        }

        return ainesosat;
    }

    @Override
    public Ainesosa saveOrUpdate(Ainesosa object) throws SQLException {
        // simply support saving -- disallow saving if task with 
        // same name exists
        Ainesosa byName = findByName(object.getNimi());

        if (byName != null) {
            return byName;
        } 

        try (Connection conn = database.getConnection()) {
            if (object.getNimi() != null) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO AINESOSA (nimi) VALUES (?)");
                stmt.setString(1, object.getNimi());
                stmt.executeUpdate();
            }    
        }

        return findByName(object.getNimi());
    }

    private Ainesosa findByName(String name) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, nimi FROM Ainesosa WHERE nimi = ?");
            stmt.setString(1, name);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new Ainesosa(result.getInt("id"), result.getString("nimi"));
        }
    }
    
        
    public List<Ainesosa> findAnnosAinesosat(Integer annosId) throws SQLException {
        String query = "SELECT Ainesosa.id, Ainesosa.nimi, AnnosAinesosa.maara FROM Ainesosa, AnnosAinesosa\n"
            + "              WHERE Ainesosa.id = AnnosAinesosa.ainesosa_id "
            + "                  AND AnnosAinesosa.annos_id = ?\n";
  
        List<Ainesosa> ainesosat = new ArrayList<>();

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, annosId);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                ainesosat.add(new Ainesosa(result.getInt("id"), result.getString("nimi"), result.getString("maara")));
            }

        }

        return ainesosat;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
