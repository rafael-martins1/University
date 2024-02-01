package com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth.model.Event;
import com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth.model.EventParticipants;
import com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth.rowmapper.EventRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class EventDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Event> eventRowMapper = new EventRowMapper();

    @Transactional
    public void saveEvent(final Event event) {

        String insertEventSql = "INSERT INTO events (event_name, event_description, event_date, join_value) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(insertEventSql, event.getEventName(), event.getEventDescription(), event.getEventDate(), event.getJoinValue());

    }

    public List<String> getEventNames() {
        return jdbcTemplate.queryForList("SELECT event_name FROM events", String.class);
    }

    public Integer getEventIdByName(String eventName) {
        String sql = "SELECT id FROM events WHERE event_name = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, eventName);
    }

    @Transactional
    public void saveParticipant(final EventParticipants participant) {

        String insertParticipantSql = "INSERT INTO event_participants (event_id, participant_username, participant_real_name, participant_type, participant_gender, is_paid) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(
                insertParticipantSql,
                participant.getEventId(),
                participant.getParticipantUsername(),
                participant.getParticipantRealName(),
                participant.getParticipantType(),
                participant.getParticipantGender(),
                participant.getIsPaid()
        );

    }

    public  List<String> getParticipantsForEvent(int event_id) {
        String sql = "SELECT participant_username FROM event_participants WHERE event_id = ?";
        return jdbcTemplate.queryForList(sql, String.class, event_id);
    }

    public Double getEventValueByName(String event_name){
        String sql = "SELECT join_value FROM events WHERE event_name= ?";
        return jdbcTemplate.queryForObject(sql, Double.class, event_name);
    }

    public int saveTimestampEntry(int eventId, String raceLocal, Time timeTimestamp, String participant_username) {
        String sql = "SELECT is_paid FROM event_participants WHERE event_id = ? AND participant_username = ?";
        String sql1 = "SELECT COUNT(*) FROM times WHERE event_id = ? AND race_local = ? AND participant_username = ?";
        String sql2 = "INSERT INTO times (event_id, race_local, time_timestamp, participant_username) VALUES (?, ?, ?, ?)";
        if(!jdbcTemplate.queryForObject(sql, Boolean.class, eventId, participant_username)) {
            return 1;
        } else if(jdbcTemplate.queryForObject(sql1, Integer.class, eventId, raceLocal, participant_username) > 0) {
            return 2;
        } else {
            jdbcTemplate.update(sql2, eventId, raceLocal, timeTimestamp, participant_username);
            return 0;
        }
    }

    public void savePaymentInfo(String username, int event_id, String entity, String reference, double value) {
        String sql = "INSERT INTO payment_info (user_name, event_id, entity, reference, value) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, username, event_id, entity, reference, value);
    }

    public String getEvents(String username) throws JsonProcessingException {
        String sql = "SELECT ep.event_id, ep.participant_real_name, ep.participant_type, ep.participant_gender, ep.is_paid, e.event_name " +
                "FROM event_participants ep " +
                "INNER JOIN events e ON ep.event_id = e.id " +
                "WHERE ep.participant_username = ?";

        List<Map<String, Object>> eventos = jdbcTemplate.queryForList(sql, username);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(eventos);
    }


    public List<EventParticipants> getEventParticipantsByEventId(int eventId) {
        String sql = "SELECT  ep.participant_username, ep.participant_real_name, ep.participant_type, ep.participant_gender, ep.is_paid " +
                "FROM event_participants ep " +
                "WHERE ep.event_id = ?";

        return jdbcTemplate.query(sql, new Object[]{eventId}, (resultSet, rowNum) -> {
            EventParticipants participant = new EventParticipants();
            participant.setParticipantUsername(resultSet.getString("participant_username"));
            participant.setParticipantRealName(resultSet.getString("participant_real_name"));
            participant.setParticipantType(resultSet.getString("participant_type"));
            participant.setParticipantGender(resultSet.getString("participant_gender"));
            participant.setPaid(resultSet.getBoolean("is_paid"));
            return participant;
        });
    }

    public List<Event> findPastEvents(int page, int size) {
        Date currentDate = new Date(System.currentTimeMillis());
        int offset = page * size;

        String sql = "SELECT * FROM events WHERE event_date < ? ORDER BY event_date DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{currentDate, size, offset}, eventRowMapper);
    }

    public List<Event> findOngoingEvents() {
        Date currentDate = new Date(System.currentTimeMillis());

        String sql = "SELECT * FROM events WHERE event_date = ? ORDER BY event_date DESC";
        return jdbcTemplate.query(sql, new Object[]{currentDate}, eventRowMapper);
    }

    public List<Event> findFutureEvents() {
        Date currentDate = new Date(System.currentTimeMillis());

        String sql = "SELECT * FROM events WHERE event_date > ? ORDER BY event_date ASC";
        return jdbcTemplate.query(sql, new Object[]{currentDate}, eventRowMapper);
    }

    public List<Event> findPastEvents() {
        Date currentDate = new Date(System.currentTimeMillis());

        String sql = "SELECT * FROM events WHERE event_date < ? ORDER BY event_date DESC";
        return jdbcTemplate.query(sql, new Object[]{currentDate}, eventRowMapper);
    }

    public List<Event> paginateEvents(List<Event> allEvents, int page, int size) {
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, allEvents.size());

        return allEvents.subList(fromIndex, toIndex);
    }

    public String searchEventsByName(String event_name) throws JsonProcessingException {
        String sql = "SELECT * FROM events WHERE event_name = ?";

        List<Map<String, Object>> eventos = jdbcTemplate.queryForList(sql, event_name);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(eventos);
    }

    public String searchEventsByDate(Date date) throws JsonProcessingException {
        String sql = "SELECT * FROM events WHERE event_date = ?";

        List<Map<String, Object>> eventos = jdbcTemplate.queryForList(sql, date);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(eventos);
    }

    public List<EventParticipants> updateParticipantsWithTimes(int eventId, List<EventParticipants> participants) {
        String sql = "SELECT " +
                "  start.event_id, " +
                "  start.participant_username, " +
                "  start.time_timestamp AS start_time, " +
                "  finish.time_timestamp AS finish_time " +
                "FROM " +
                "  times AS start " +
                "JOIN " +
                "  times AS finish ON start.event_id = finish.event_id " +
                "  AND start.participant_username = finish.participant_username " +
                "  AND start.race_local = 'start' " +
                "  AND finish.race_local = 'finish' " +
                "WHERE " +
                "  start.event_id = ? " +
                "  AND start.time_timestamp IS NOT NULL " +
                "  AND finish.time_timestamp IS NOT NULL ";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, eventId);

        // Lista para participantes que já acabaram a corrida
        List<EventParticipants> participantsWithTimes = new ArrayList<>();

        // Percorre os resultados
        for (Map<String, Object> row : result) {
            String username = (String) row.get("participant_username");
            Time startTime = (Time) row.get("start_time");
            Time finishTime = (Time) row.get("finish_time");

            // Encontra o participante na lista existente
            for (EventParticipants participant : participants) {
                if (participant.getParticipantUsername().equals(username)) {
                    // Calcula a diferença de tempo em segundos se ambos os tempos existirem
                    if (startTime != null && finishTime != null) {
                        long timeDifferenceSeconds = (finishTime.getTime() - startTime.getTime()) / 1000;
                        participant.setTime(timeDifferenceSeconds/60);

                        // Adicionar à lista se o tempo zero (iniciado no construtor)
                        if (participant.getTime() != 0) {
                            participantsWithTimes.add(participant);
                        }
                    }
                    break;
                }
            }
        }

        return participantsWithTimes;
    }

    public List<EventParticipants> getAthleteTimes(int event_id, List<EventParticipants> participants) {
        String sql = "SELECT " +
                "  participant_username, " +
                "  CASE " +
                "    WHEN race_local = 'finish' THEN 'finish' " +
                "    WHEN race_local = 'P1' OR race_local = 'P2' OR race_local = 'P3' THEN race_local " +
                "    WHEN race_local = 'start' THEN 'start' " +
                "  END AS situation, " +
                "  CASE " +
                "    WHEN race_local = 'finish' THEN ROUND((EXTRACT(EPOCH FROM time_timestamp) - EXTRACT(EPOCH FROM (SELECT time_timestamp FROM times WHERE race_local = 'start' AND event_id = ? AND participant_username = ? LIMIT 1))) / 60, 2) " +
                "    WHEN race_local = 'P1' OR race_local = 'P2' OR race_local = 'P3' THEN ROUND((EXTRACT(EPOCH FROM time_timestamp) - EXTRACT(EPOCH FROM (SELECT time_timestamp FROM times WHERE race_local = 'start' AND event_id = ? AND participant_username = ? LIMIT 1))) / 60, 2) " +
                "    WHEN race_local = 'start' THEN NULL " +
                "  END AS elapsed_time " +
                "FROM times " +
                "WHERE event_id = ? AND participant_username = ? " +
                "ORDER BY time_timestamp DESC " +
                "LIMIT 1";

        List<EventParticipants> participantsLocalTimes = new ArrayList<>();

        for (EventParticipants participant : participants) {
            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql,
                    event_id, participant.getParticipantUsername(),
                    event_id, participant.getParticipantUsername(),
                    event_id, participant.getParticipantUsername());

            if (!result.isEmpty()) {
                Map<String, Object> row = result.get(0);
                Object elapsed_time = row.get("elapsed_time");
                Object situation = row.get("situation");

                //Se o tempo a ser registado nao for no ponto 'start'
                if (elapsed_time != null) {
                    BigDecimal elapsedTimeBigDecimal = (BigDecimal) elapsed_time;
                    double elapsedTimeDouble = elapsedTimeBigDecimal.doubleValue();
                    participant.setTime((long) elapsedTimeDouble);
                    participant.setParticipantSituation((String) situation);
                } else {
                    String sql1 = "SELECT race_local, time_timestamp FROM times WHERE event_id = ? AND participant_username = ? AND race_local = 'start'";
                    List<Map<String, Object>> result1 = jdbcTemplate.queryForList(sql1, event_id, participant.getParticipantUsername());
                    java.sql.Time startTime = (java.sql.Time) result1.get(0).get("time_timestamp");
                    participant.setTimestamp(startTime);
                    participant.setParticipantSituation((String) result1.get(0).get("race_local"));
                }
            }

            participantsLocalTimes.add(participant);
        }

        return participantsLocalTimes;

    }

}
