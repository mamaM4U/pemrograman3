import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
 
/**
 * Program untuk membuat koneksi ke database SQLite.
 * @author http://nursalim83.blogspot.com
 *
 */
public class JdbcSQLiteConnectionDemo {
 
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
            String databaseUrl = "jdbc:sqlite:test.db";
            Connection conn = DriverManager.getConnection(databaseUrl);
            if (conn != null) {
                System.out.println("Successfully Connected to the database!");
                DatabaseMetaData dbm = (DatabaseMetaData) conn.getMetaData();
                System.out.println("Driver name: " + dbm.getDriverName());
                System.out.println("Driver version: " + dbm.getDriverVersion());
                System.out.println("Product name: " + dbm.getDatabaseProductName());
                System.out.println("Product version: " + dbm.getDatabaseProductVersion());
                conn.close();
            }
        } catch (ClassNotFoundException ex) {
      System.out.println("Error connecting to database");
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}   