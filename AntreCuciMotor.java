import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
public class AntreCuciMotor
{
    static Connection conn;
    static Statement stmt;
    static ResultSet rs;

    static InputStreamReader inputStreamReader = new InputStreamReader(System.in);
    static BufferedReader input = new BufferedReader(inputStreamReader);
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
            String databaseUrl = "jdbc:sqlite:test.db";
            conn = DriverManager.getConnection(databaseUrl);
            stmt = conn.createStatement();
            String sqlcreate = "CREATE TABLE IF NOT EXISTS transaksi(trx_id TEXT PRIMARY KEY, trx_date TEXT,trx_nopol TEXT, trx_service TEXT, trx_charge INTEGER);";
            stmt.execute(sqlcreate);

            while (!conn.isClosed()) {
                showMenu();
            }

            stmt.close();
            conn.close();
        } catch (ClassNotFoundException ex) {
            System.out.println("Error connecting to database");
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    static void showMenu() {
        System.out.println("\n========= MENU UTAMA =========");
        System.out.println("1. Masukkan Transaksi");
        System.out.println("2. Tampil Transaksi");
        System.out.println("3. Ubah Transaksi");
        System.out.println("4. Hapus Transaksi");
        System.out.println("0. Keluar");
        System.out.println("");
        System.out.print("PILIHAN (0-4)> ");
        try {
            int pilihan = Integer.parseInt(input.readLine());
            switch (pilihan) {
                case 0:
                System.exit(0);
                break;
                case 1:
                insertTrx();
                break;
                case 2:
                showTrx();
                break;
                case 3:
                updateTrx();
                break;
                case 4:
                deleteTrx();
                break;
                default:
                System.out.println("Pilihan salah!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void insertTrx() {
        try {
            // ambil input dari user
            System.out.print("Nomor Plat: ");
            String nopol = input.readLine().trim();

            int idcount = 0;
            System.out.print("Pilih Paket 1. Regular 2. Special (1/2):  ");
            String paket = "Reguler";
            int price = 10000;
            int pilihanPaket = Integer.parseInt(input.readLine());
            switch (pilihanPaket) {
                case 1:
                paket = "Reguler";
                price = 10000;
                break;
                case 2:
                paket = "Spesial";
                price = 20000;
                break;
                default:
                System.out.println("Pilihan salah!");
            }
            // query simpan
            String queryGetId="SELECT trx_id FROM transaksi ORDER BY trx_id DESC LIMIT 1;";
            rs = stmt.executeQuery(queryGetId);
            if(rs.next()==true){
                idcount = Integer.parseInt((rs.getString("trx_id")).substring(3));
            }    

            String idnya = "trx"+String.format("%04d", idcount+1);
            String sql = "INSERT INTO transaksi VALUES('%s', datetime('now', 'localtime'),'%s', '%s','%d')";
            sql = String.format(sql, idnya,nopol,paket,price);

            // simpan buku
            stmt.execute(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void showTrx() {
        String sql = "SELECT * FROM transaksi";
        String showAll="y";
        try {
            System.out.print("Tampilkan semua (y/n)?  ");
            showAll = input.readLine().trim();
            if (showAll.equalsIgnoreCase("y")){
                sql = "SELECT * FROM transaksi";
            }else if(showAll.equalsIgnoreCase("n")){
                System.out.print("No Plat:");
                String queryNopol = input.readLine().trim();
                sql = "SELECT * FROM transaksi WHERE trx_nopol='%s'";
                sql = String.format(sql, queryNopol);

            }
            rs = stmt.executeQuery(sql);

            System.out.println("+--------------------------------+");
            System.out.println("|    DATA TRANSAKSI CUMOR   |");
            System.out.println("+--------------------------------+");
            while (rs.next()) {
                String trx_id = rs.getString("trx_id");
                String trx_date= rs.getString("trx_date");
                String trx_nopol= rs.getString("trx_nopol");
                String trx_service= rs.getString("trx_service");
                int trx_charge= rs.getInt("trx_charge");
                System.out.println(String.format("%s %s %s %s %d.", trx_id, trx_date, trx_nopol,trx_service,trx_charge));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void updateTrx() {
        try {  
            // ambil input dari user
            System.out.print("ID yang mau diedit: ");
            String trx_id = input.readLine().trim();
            System.out.print("Nopol: ");
            String nopol = input.readLine().trim();

            // query update
            String sql = "UPDATE transaksi SET trx_nopol='%s' WHERE trx_id='%s'";
            sql = String.format(sql, nopol, trx_id);

            // update data buku
            stmt.execute(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void deleteTrx() {
        try {

            // ambil input dari user
            System.out.print("ID yang mau dihapus: ");
            String trx_id = input.readLine().trim();

            // buat query hapus
            String sql = String.format("DELETE FROM transaksi WHERE trx_id='%s'", trx_id);
            // hapus data
            stmt.execute(sql);

            System.out.println("Data telah terhapus...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}   