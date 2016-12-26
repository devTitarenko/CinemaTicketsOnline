package com.netcracker.cinema.dao.impl;

import com.netcracker.cinema.dao.HallDao;
import com.netcracker.cinema.model.Hall;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.netcracker.cinema.dao.impl.queries.HallDaoQuery.*;

public class HallDaoImpl implements HallDao {

    private static final Logger LOGGER = Logger.getLogger(HallDaoImpl.class);
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public HallDaoImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Hall> findAll() {
        List<Hall> halls = jdbcTemplate.query(FIND_ALL_SQL, new HallMapper());
        LOGGER.info("Find all seances: found " + halls.size() + " seance objects");
        return halls;
    }

    @Override
    public Hall getById(long id) {
        Hall hall = jdbcTemplate.queryForObject(FIND_HALL_BY_ID, new Object[]{id}, new HallMapper());
        return hall;
    }

    @Override
    public void save(Hall hall) {
        jdbcTemplate.update(MERGE_HALL_OBJECT, hall.getId(), hall.getName());
        LOGGER.info("");
    }

    @Override
    public void delete(Hall hall) {
        jdbcTemplate.update(DELETE_HALL, hall.getId());
    }

    private class HallMapper implements RowMapper<Hall> {
        @Override
        public Hall mapRow(ResultSet resultSet, int i) throws SQLException {
            Hall hall = new Hall();
            hall.setId(resultSet.getLong("id"));
            hall.setName(resultSet.getString("name"));
            return hall;
        }
    }
}
