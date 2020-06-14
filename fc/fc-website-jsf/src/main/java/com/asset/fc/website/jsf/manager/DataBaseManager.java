package com.asset.fc.website.jsf.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author nour.ihab
 */
@ManagedBean
@ApplicationScoped
public class DataBaseManager {

    private HikariConfig config = null;
    private HikariDataSource ds = null;

    @PostConstruct
    public void init() {

        config = new HikariConfig("/db.properties");
        ds = new HikariDataSource(config);
    }

    @PreDestroy
    public void destroy() {
        ds.close();
    }

    public Connection openConnection() throws ClassNotFoundException, SQLException {
        try {
            return ds.getConnection();
        } catch (Exception ex) {
            throw ex;
        }

    }

    public void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println("Failed to close the connection");
            }
        }
    }

    public static void closeResources(PreparedStatement pst) {
        if (pst != null) {
            try {
                pst.close();
            } catch (SQLException ex) {
                System.out.println("Failed to close the Prepared Statment");
            }
        }
    }

    public static void closeResoucres(PreparedStatement pst, ResultSet resultSet) {
        closeResources(pst);
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException ex) {
                System.out.println("Failed to close the Resources");
            }

        }
    }
}
