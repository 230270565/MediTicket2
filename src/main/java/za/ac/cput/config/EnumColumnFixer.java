/*
 EnumColumnFixer.java

 Changes old MySQL ENUM columns to VARCHAR on startup so new statuses save

 Authors:
  Joshua Reid Adams (230317693)
  Abdullahi Raage Farah (230971091)
  Aidan Barends (230255639)
  Jaden Clayton Abrahams (222206721)
  Joshua Peter Bonzet (221312536)
  Matthew Barron (230398863)
  Raul Jaaim Everts (230270565)

 Date: 22nd October 2026
*/

package za.ac.cput.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

// Older databases made from MediTicketDB.sql have ENUM columns, so new values like READ get rejected.
// On startup this changes those columns to VARCHAR. Columns that are already VARCHAR are skipped.
@Component
public class EnumColumnFixer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(EnumColumnFixer.class);

    // table -> columns mapped with @Enumerated(EnumType.STRING)
    private static final Map<String, List<String>> ENUM_COLUMNS = Map.of(
            "notification", List.of("notification_status", "notification_type"),
            "appointment", List.of("confirmation_status"),
            "ticket_status", List.of("status_type"),
            "payment", List.of("payment_method", "payment_status")
    );

    private final JdbcTemplate jdbc;

    public EnumColumnFixer(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void run(ApplicationArguments args) {
        ENUM_COLUMNS.forEach((table, columns) -> columns.forEach(column -> fix(table, column)));
    }

    private void fix(String table, String column) {
        try {
            List<Map<String, Object>> rows = jdbc.queryForList(
                    "SELECT DATA_TYPE, IS_NULLABLE FROM information_schema.COLUMNS "
                            + "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? AND COLUMN_NAME = ?",
                    table, column);
            if (rows.isEmpty() || !"enum".equalsIgnoreCase(String.valueOf(rows.get(0).get("DATA_TYPE")))) {
                return; // column missing or already VARCHAR
            }
            String nullability = "NO".equals(rows.get(0).get("IS_NULLABLE")) ? " NOT NULL" : " NULL";
            jdbc.execute("ALTER TABLE " + table + " MODIFY " + column + " VARCHAR(30)" + nullability);
            logger.info("Converted {}.{} from ENUM to VARCHAR(30)", table, column);
        } catch (Exception e) {
            // Never block startup over this; the app still runs, only unknown enum values would fail.
            logger.warn("Could not convert {}.{} to VARCHAR: {}", table, column, e.getMessage());
        }
    }
}
