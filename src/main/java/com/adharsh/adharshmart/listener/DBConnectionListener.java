package com.adharsh.adharshmart.listener;

import com.adharsh.adharshmart.util.ConfigUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

@WebListener
public class DBConnectionListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(DBConnectionListener.class);
    private static HikariDataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.h2.Driver");
        config.setJdbcUrl(ConfigUtil.get("db.url", "jdbc:h2:mem:adharshmart;DB_CLOSE_DELAY=-1;MODE=PostgreSQL"));
        config.setUsername(ConfigUtil.get("db.user", "sa"));
        config.setPassword(ConfigUtil.get("db.password", ""));
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);

        dataSource = new HikariDataSource(config);
        sce.getServletContext().setAttribute("dataSource", dataSource);

        executeScript("db/schema.sql");
        executeScript("db/seed.sql");
        logger.info("HikariCP initialized and schema synced successfully.");
    }

    private void executeScript(String resourcePath) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
             Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            if (is != null) {
                String sql = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                for (String query : sql.split(";")) {
                    if (!query.trim().isEmpty()) {
                        stmt.execute(query);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error executing script " + resourcePath, e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (dataSource != null) {
            dataSource.close();
            logger.info("HikariCP connection pool closed.");
        }
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }
}