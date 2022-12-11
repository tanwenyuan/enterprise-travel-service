package com.thoughtworks.enterprise.travel.configuration;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TruncateDatabaseService {

    public static final String SHOPPING_CART_ITEM_GROUP = "shopping_cart_item_group";
    public static final String TRUNCATE_TABLE = "truncate table";
    public static final String ALTER_TABLE = "alter table";
    public static final String AUTO_INCREMENT = "auto_increment =";
    public static final String SET_FOREIGN_KEY_CHECKS_1 = "SET FOREIGN_KEY_CHECKS = 1";
    public static final String SET_FOREIGN_KEY_CHECKS_0 = "set FOREIGN_KEY_CHECKS = 0";
    public static final String SHOW_TABLES = "show tables";
    private final JdbcTemplate jdbcTemplate;

    public TruncateDatabaseService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Transactional(rollbackOn = Exception.class)
    public void restartIdWith(int startId) {
        List<String> tableNames = jdbcTemplate.queryForList(SHOW_TABLES, String.class);
        tableNames.remove("labour_rate");

        jdbcTemplate.execute(SET_FOREIGN_KEY_CHECKS_0);
        tableNames.stream()
                .filter(tableName -> !tableName.endsWith("_view"))
                .collect(Collectors.toList())
                .forEach(
                        tableName -> {
                            jdbcTemplate.execute(TRUNCATE_TABLE + " " + tableName);
                            if (!SHOPPING_CART_ITEM_GROUP.equals(tableName)) {
                                jdbcTemplate.execute(ALTER_TABLE + " " + tableName + " " + AUTO_INCREMENT + " " + startId);
                            }
                        }
                );
        jdbcTemplate.execute(SET_FOREIGN_KEY_CHECKS_1);
    }
}