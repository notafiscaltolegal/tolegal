package gov.goias.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by lucas-mp on 09/01/15.
 */
public class ResultSetOpcional  {

    private ResultSet resultSet;


    public ResultSetOpcional(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public String getString(String coluna){
        try {
            return getResultSet().getString(coluna);
        } catch (SQLException e) {
            return null;
        }
    }


    public Double getDouble(String coluna){
        try {
            return getResultSet().getDouble(coluna);
        } catch (SQLException e) {
            return null;
        }
    }

    public Integer getInt(String coluna){
        try {
            return getResultSet().getInt(coluna);
        } catch (SQLException e) {
            return null;
        }
    }
    public Timestamp getTimestamp(String coluna){
        try {
            return getResultSet().getTimestamp(coluna);
        } catch (SQLException e) {
            return null;
        }
    }





}
