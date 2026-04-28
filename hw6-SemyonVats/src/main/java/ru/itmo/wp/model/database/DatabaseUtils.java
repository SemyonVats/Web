package ru.itmo.wp.model.database;

import org.mariadb.jdbc.MariaDbDataSource;

import javax.sql.DataSource;
//import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
//import java.util.Properties;

public class DatabaseUtils {
    public static DataSource getDataSource() {
        return DataSourceHolder.INSTANCE;
    }

    private static final class DataSourceHolder {
        private static final DataSource INSTANCE;

        //private static final Properties properties = new Properties();

        static {
            /*
            try {
                properties.load(DataSourceHolder.class.getResourceAsStream("/application.properties"));
            } catch (IOException e) {
                throw new RuntimeException("Can't load /application.properties.", e);
            }
            */
            try {
                MariaDbDataSource instance = new MariaDbDataSource();
                instance.setUrl("jdbc:mariadb://wp.codeforces.com:88/u06?useUnicode=true&characterEncoding=UTF-8");
                instance.setUser("u06");
                instance.setPassword("p282309");
                INSTANCE = instance;
            } catch (SQLException e) {
                throw new RuntimeException("Can't initialize DataSource.", e);
            }

            try (Connection connection = INSTANCE.getConnection()) {
                if (connection == null) {
                    throw new RuntimeException("Can't create testing connection via DataSource.");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Can't create testing connection via DataSource.", e);
            }
        }
    }
}
