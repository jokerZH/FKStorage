package com.joker.storage.core.processor.show;

import com.joker.storage.core.processor.BaseProcessor;

import java.sql.SQLException;

public class ShowTablesProcessor extends BaseProcessor {

    @Override
    public boolean isUpdate() throws SQLException {
        return false;
    }
}
