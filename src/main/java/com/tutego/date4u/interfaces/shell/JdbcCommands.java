package com.tutego.date4u.interfaces.shell;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.shell.standard.ShellComponent;

import java.util.List;

@ShellComponent
public class JdbcCommands {
    private final JdbcTemplate jdbcTemplate;

    public JdbcCommands(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String manelength(String nickname) {
        String sql = "SELECT manelength FROM Profile WHERE nickname = ?";
        List<Integer> lengths = jdbcTemplate.queryForList(sql, Integer.class, nickname);
        return lengths.isEmpty() ? "No such nickname" : lengths.get(0).toString();
    }
}
