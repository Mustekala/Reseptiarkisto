
package reseptiarkisto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import reseptiarkisto.database.Database;
import reseptiarkisto.domain.Arvostelu;

public class ArvosteluDao implements Dao<Arvostelu, Integer> {

    private Database database;

    public ArvosteluDao(Database database) {
        this.database = database;
    }

    @Override
    public Arvostelu findOne(Integer key) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, annos_id, nimi, arvosana, kommentti FROM Arvostelu WHERE id = ?");
            stmt.setInt(1, key);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new Arvostelu(result.getInt("id"), result.getInt("annos_id"), result.getString("nimi"), result.getInt("arvosana"), result.getString("kommentti"));
        }
    }

    @Override
    public List<Arvostelu> findAll() throws SQLException {
        List<Arvostelu> arvostelut = new ArrayList<>();

        try (Connection conn = database.getConnection();
            ResultSet result = conn.prepareStatement("SELECT id, annos_id, nimi, arvosana, kommentti FROM Arvostelu").executeQuery()) {

            while (result.next()) {
                arvostelut.add(new Arvostelu(result.getInt("id"), result.getInt("annos_id"), result.getString("nimi"), result.getInt("arvosana"), result.getString("kommentti")));
            }
        }

        return arvostelut;
    }

    @Override
    public Arvostelu saveOrUpdate(Arvostelu object) throws SQLException {
        // simply support saving -- disallow saving if task with 
        // same name exists
        Arvostelu byName = findByName(object.getNimi());

        if (byName != null) {
            return byName;
        } 

        try (Connection conn = database.getConnection()) {
            if (object.getNimi() != null) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO Arvostelu (annos_id, nimi, arvosana, kommentti) VALUES (?, ?, ?, ?)");
                stmt.setInt(1, object.getAnnosId());
                stmt.setString(2, object.getNimi());
                stmt.setInt(3, object.getArvosana());
                stmt.setString(4, object.getKommentti());
                stmt.executeUpdate();
            }
        }

        return findByName(object.getNimi());
    }

    private Arvostelu findByName(String name) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, annos_id, nimi, arvosana, kommentti FROM Arvostelu WHERE nimi = ?");
            stmt.setString(1, name);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new Arvostelu(result.getInt("id"), result.getInt("annos_id"), result.getString("nimi"), result.getInt("arvosana"), result.getString("kommentti"));
        }
    }
    
              
    public List<Arvostelu> findAnnosArvostelut(Integer annosId) throws SQLException {
        String query = "SELECT Arvostelu.id, Arvostelu.annos_id, Arvostelu.nimi, Arvostelu.arvosana, Arvostelu.kommentti FROM Annos, Arvostelu\n"
            + "              WHERE Annos.id = Arvostelu.annos_id "
            + "                  AND Arvostelu.annos_id = ?\n";
  
        List<Arvostelu> arviot = new ArrayList<>();

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, annosId);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                arviot.add(new Arvostelu(result.getInt("id"), result.getInt("annos_id"), result.getString("nimi"), result.getInt("arvosana"), result.getString("kommentti")));
            }

        }

        return arviot;
    }
    
    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
