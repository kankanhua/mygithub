package filter;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import cn.edu.hfut.dmic.webcollector.util.JDBCHelper;

public class DishFilter {

	private void run() {
		JdbcTemplate source = null;
		JdbcTemplate dest = null;
		try {
			source = JDBCHelper
					.createMysqlTemplate(
							"mysql1",
							"jdbc:mysql://localhost:3306/food?useUnicode=true&characterEncoding=utf8",
							"root", "409569210", 5, 30);
			dest = JDBCHelper
					.createMysqlTemplate(
							"mysql1",
							"jdbc:mysql://localhost:3306/food?useUnicode=true&characterEncoding=utf8",
							"root", "409569210", 5, 30);

			int limit = 100;
			int start = 0;
			List<Map<String, Object>> result = source.queryForList(getQuery(
					start, limit));

			do {

				for (Map<String, Object> data : result) {

					System.out.println(data.get("title"));
					// String update =
					// "insert into source (title,url,html) value(?,?,?)";
					// // for (String s : keys) {
					// // Object value = data.get(s);
					// // }
					// dist.update(update, data.get("title"), data.get("url"),
					// data.get("html"));
				}

				start += limit;

				result = source.queryForList(getQuery(start, limit));
			} while (result != null && result.size() > 0);

		} catch (Exception ex) {
			source = null;
		}
	}

	private String getQuery(int start, int limit) {
		return "select * from source limit " + start + "," + limit;
	}

	public static void main(String args[]) {
		new DishFilter().run();
	}
}
