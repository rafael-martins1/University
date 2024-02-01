package com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth.rowmapper;

import com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth.model.Event;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventRowMapper implements RowMapper<Event> {

    @Override
    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Event(
                rs.getString("event_name"),
                rs.getString("event_description"),
                rs.getDate("event_date"),
                rs.getDouble("join_value")
        );
    }
}
