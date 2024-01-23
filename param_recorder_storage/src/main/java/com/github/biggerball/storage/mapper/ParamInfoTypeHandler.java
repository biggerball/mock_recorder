package com.github.biggerball.storage.mapper;

import com.github.biggerball.entity.ParamInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

@Slf4j
public class ParamInfoTypeHandler extends BaseTypeHandler<ParamInfo> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, ParamInfo o, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, encode(o));
    }

    @Override
    public ParamInfo getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String encoded = resultSet.getString(s);
        return decode(encoded);
    }

    @Override
    public ParamInfo getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String encoded = resultSet.getString(i);
        return decode(encoded);
    }

    @Override
    public ParamInfo getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String encoded = callableStatement.getString(i);
        return decode(encoded);
    }

    private ParamInfo decode(String encodedString) {
        if (StringUtils.isEmpty(encodedString)) {
            return null;
        }
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(encodedString))) {
            try (ObjectInputStream ois = new ObjectInputStream(byteArrayInputStream)) {
                return (ParamInfo)ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                log.error("ParamInfoTypeHandler error", e);
                return null;
            }
        } catch (IOException e) {
            log.error("ParamInfoTypeHandler error", e);
            return null;
        }
    }

    private String encode(ParamInfo paramInfo) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();) {
            try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                oos.writeObject(paramInfo);
                oos.flush();
            }
            return Base64.getEncoder().encodeToString(bos.toByteArray());
        } catch (IOException e) {
            log.error("ParamInfoTypeHandler encode", e);
            return null;
        }
    }
}
