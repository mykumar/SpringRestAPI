package app.jdbc;

import java.util.List;
import java.util.Map;

public class JdbcTemplate extends org.springframework.jdbc.core.JdbcTemplate {

    public Map<String, Object> queryForMap(String sql, Object... args) {
	List<Map<String, Object>> list = super.queryForList(sql, args);
	return list != null && !list.isEmpty() ? list.get(0) : null;
    }

}
	