package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseBuilder {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:villa_booking.db";

        String sql = """
            PRAGMA foreign_keys = ON;

            CREATE TABLE IF NOT EXISTS villas (
              id INTEGER PRIMARY KEY,
              name TEXT NOT NULL,
              description TEXT NOT NULL,
              address TEXT NOT NULL
            );

            CREATE TABLE IF NOT EXISTS room_types (
              id INTEGER PRIMARY KEY,
              villa INTEGER NOT NULL,
              name TEXT NOT NULL,
              quantity INTEGER DEFAULT 1,
              capacity INTEGER DEFAULT 1,
              price INTEGER NOT NULL,
              bed_size TEXT NOT NULL,
              has_desk INTEGER DEFAULT 0,
              has_ac INTEGER DEFAULT 0,
              has_tv INTEGER DEFAULT 0,
              has_wifi INTEGER DEFAULT 0,
              has_shower INTEGER DEFAULT 0,
              has_hotwater INTEGER DEFAULT 0,
              has_fridge INTEGER DEFAULT 0,
              FOREIGN KEY (villa) REFERENCES villas(id)
            );

            CREATE TABLE IF NOT EXISTS customers (
              id INTEGER PRIMARY KEY,
              name TEXT NOT NULL,
              email TEXT NOT NULL,
              phone TEXT
            );

            CREATE TABLE IF NOT EXISTS bookings (
              id INTEGER PRIMARY KEY,
              customer INTEGER,
              room_type INTEGER,
              checkin_date TEXT NOT NULL,
              checkout_date TEXT NOT NULL,
              price INTEGER,
              voucher INTEGER,
              final_price INTEGER,
              payment_status TEXT DEFAULT 'waiting',
              has_checkedin INTEGER DEFAULT 0,
              has_checkedout INTEGER DEFAULT 0,
              FOREIGN KEY (customer) REFERENCES customers(id),
              FOREIGN KEY (room_type) REFERENCES room_types(id),
              FOREIGN KEY (voucher) REFERENCES vouchers(id)
            );

            CREATE TABLE IF NOT EXISTS reviews (
              booking INTEGER PRIMARY KEY,
              star INTEGER NOT NULL,
              title TEXT NOT NULL,
              content TEXT NOT NULL,
              FOREIGN KEY (booking) REFERENCES bookings(id)
            );

            CREATE TABLE IF NOT EXISTS vouchers (
              id INTEGER PRIMARY KEY,
              code TEXT NOT NULL,
              description TEXT NOT NULL,
              discount REAL NOT NULL,
              start_date TEXT NOT NULL,
              end_date TEXT NOT NULL
            );
        """;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Database berhasil dibuat.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
