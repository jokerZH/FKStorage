package com.joker.storage.core.param.type;

import com.joker.storage.core.param.ParamType;
import com.joker.storage.core.param.Parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ParamInt implements Parameter {
    public int value;

    public ParamInt(int value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return String.valueOf(value);
    }

    @Override
    public ParamType getType() {
        return ParamType.INT;
    }

    @Override
    public void add(PreparedStatement ps, int index) throws SQLException {
        ps.setInt(index, value);
    }
}
