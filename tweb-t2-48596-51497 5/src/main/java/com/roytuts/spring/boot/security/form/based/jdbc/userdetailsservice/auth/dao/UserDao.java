package com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth.dao;

import com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth.model.PaymentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth.model.User;
import com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth.rowmapper.UserRowMapper;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Transactional
    public User getUser(final String username) {

        return jdbcTemplate.queryForObject(
                "SELECT u.user_name, u.user_pass, ur.user_role " +
                        "FROM users u " +
                        "JOIN user_role ur ON u.user_name = ur.user_name " +
                        "WHERE u.user_name = ?",
        new String[]{username}, new UserRowMapper());
    }

    @Transactional
    public String getUserRole(final String username) {

        return jdbcTemplate.queryForObject(
                "SELECT ur.user_role " +
                        "FROM users u " +
                        "JOIN user_role ur ON u.user_name = ur.user_name " +
                        "WHERE u.user_name = ?",
                new String[]{username}, String.class);
    }


    public void saveUser(final User u) {

        String insertUserSql = "INSERT INTO users (user_name, user_pass) VALUES (?, ?)";
        jdbcTemplate.update(insertUserSql, u.getUsername(), u.getPassword());

        String insertUserRoleSql = "INSERT INTO user_role (user_name, user_role) VALUES (?, ?)";
        jdbcTemplate.update(insertUserRoleSql, u.getUsername(), u.getRole());

    }

    @Transactional
    public void updateIsPaid(final String username, final int event_id) {
        String sql = "UPDATE event_participants SET is_paid = TRUE WHERE participant_username = ? and event_id = ?";

        jdbcTemplate.update(sql, username, event_id);
    }

    public List<String> getUsernameList() {
        return jdbcTemplate.queryForList("SELECT user_name FROM users", String.class);
    }

    @Transactional
    public PaymentInfo getPaymentInfo(final String username, final int event_id) {

        String sql = "SELECT p.entity, p.reference, p.value " +
                        "FROM payment_info p " +
                        "WHERE p.user_name = ? AND p.event_id = ?";

        List<Map<String, Object>> pagamento = jdbcTemplate.queryForList(sql, username, event_id);
        PaymentInfo info = new PaymentInfo();
        for (Map<String, Object> row : pagamento) {
            String entity = (String) row.get("entity");
            String reference = (String) row.get("reference");
            BigDecimal value = (BigDecimal) row.get("value");
            info.setEntity(entity);
            info.setReference(reference);
            info.setValue(value);
        }
        return info;
    }


}
