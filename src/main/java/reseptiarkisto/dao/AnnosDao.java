
package reseptiarkisto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import reseptiarkisto.database.Database;
import reseptiarkisto.domain.Annos;

public class AnnosDao implements Dao<Annos, Integer> {

    private Database database;

    public AnnosDao(Database database) {
        this.database = database;
    }

    @Override
    public Annos findOne(Integer key) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, nimi FROM Annos WHERE id = ?");
            stmt.setInt(1, key);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new Annos(result.getInt("id"), result.getString("nimi"));
        }
    }

    @Override
    public List<Annos> findAll() throws SQLException {
        List<Annos> annokset = new ArrayList<>();

        try (Connection conn = database.getConnection();
            ResultSet result = conn.prepareStatement("SELECT id, nimi FROM Annos").executeQuery()) {

            while (result.next()) {
                annokset.add(new Annos(result.getInt("id"), result.getString("nimi")));
            }
        }

        return annokset;
    }

    @Override
    public Annos saveOrUpdate(Annos object) throws SQLException {
        // simply support saving -- disallow saving if task with 
        // same name exists
        Annos byName = findByName(object.getNimi());

        if (byName != null) {
            return byName;
        } 

        try (Connection conn = database.getConnection()) {
            if (object.getNimi() != null) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO Annos (nimi) VALUES (?)");
                stmt.setString(1, object.getNimi());
                stmt.executeUpdate();
            }
        }

        return findByName(object.getNimi());
    }

    private Annos findByName(String name) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, nimi FROM Annos WHERE nimi = ?");
            stmt.setString(1, name);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new Annos(result.getInt("id"), result.getString("nimi"));
        }
    }
    
    public List<Annos> findAinesosaAnnokset(Integer ainesosaId) throws SQLException {
        String query = "SELECT Annos.id, Annos.nimi FROM Annos, AnnosAinesosa\n"
            + "              WHERE Annos.id = AnnosAinesosa.annos_id "
            + "                  AND AnnosAinesosa.ainesosa_id = ?\n";
  
        List<Annos> annokset = new ArrayList<>();

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, ainesosaId);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                annokset.add(new Annos(result.getInt("id"), result.getString("nimi")));
            }

        }

        return annokset;
    }
  
    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
