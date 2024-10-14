package com.sharedschool.backend.typehandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@MappedTypes({List.class})
public class ListTypeHandler implements TypeHandler<List<String>>{

    @Override
    public void setParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        if (parameter != null && !parameter.isEmpty()) {
            String delimitedString = String.join(",", parameter);
            ps.setString(i, delimitedString);
        } else {
            ps.setString(i, null);
        }
    }

    @Override
    public List<String> getResult(ResultSet rs, String columnName) throws SQLException {
        String delimitedString = rs.getString(columnName);
        return convertToStringList(delimitedString);
    }

    @Override
    public List<String> getResult(ResultSet rs, int columnIndex) throws SQLException {
        String delimitedString = rs.getString(columnIndex);
        return convertToStringList(delimitedString);
    }

    @Override
    public List<String> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String delimitedString = cs.getString(columnIndex);
        return convertToStringList(delimitedString);
    }

    private List<String> convertToStringList(String delimitedString) {
        if (delimitedString != null && !delimitedString.isEmpty()) {
            return Arrays.asList(delimitedString.split(","));
        }
        return Collections.emptyList();
    }
}
