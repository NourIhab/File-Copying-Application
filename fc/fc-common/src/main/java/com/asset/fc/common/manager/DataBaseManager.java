package com.asset.fc.common.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 *
 * @author nour.ihab
 */
public class DataBaseManager {

//    private String url;
//    private String name;
//    private String password;
    private final HikariConfig config;
    private final HikariDataSource ds;

    private static DataBaseManager dbInstance; //instance from the class

    public DataBaseManager() {

        this.config = new HikariConfig("/db.properties");
        this.ds = new HikariDataSource(this.config);
    }

    public static DataBaseManager getInstance() {
        try {
            if (dbInstance == null) {
                dbInstance = new DataBaseManager();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return dbInstance; // create an instance from the class inside the class as it is singleton
    }

    /*
    //load and read the properties file
    public final void dbIntilization(String propFileName) throws IOException, Exception {
        if (dbInstance != null) {
            Properties prop = new Properties();
            //prop.load(PropertiesManager.class.getClassLoader().getResourceAsStream(propFileName)); // load the properties file
            //HikariConfig config = new HikariConfig(prop);
            //HikariDataSource ds = new HikariDataSource(config);
            // ds.getConnection();
            // final HikariConfig hikariConfig = new HikariConfig(propFileName);
            //this.hikariDataSource = new HikariDataSource(hikariConfig);
            // System.out.println(hikariConfig.getUsername());
            /*
            url = getString(prop, "url");
            name = getString(prop, "name");
            password = getString(prop, "passowrd");
             
        }
    }
     */
    //wrap the value which is a string into a string object
//    protected String getString(Properties properties, String propertyName) throws Exception {
//        String value = properties.getProperty(propertyName);
//        if (value == null) {
//            throw new Exception("Could not find property[" + propertyName + "]");
//        }
//        return value;
//    }
    public Connection openConnection() throws ClassNotFoundException, SQLException {
        try {
            return ds.getConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
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

//    public String getUrl() {
//        return url;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getPassword() {
//        return password;
//    }
}
