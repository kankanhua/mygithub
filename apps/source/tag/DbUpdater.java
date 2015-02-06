package tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import cn.edu.hfut.dmic.webcollector.util.JDBCHelper;

public class DbUpdater {

	private JdbcTemplate mTemplate = null;

	private static String DB = "";
	private static final String UN = "root";
	private static final String PW = "409569210";

	public DbUpdater() {
		mTemplate = JDBCHelper
				.createMysqlTemplate(
						"mysql1",
						String.format(
								"jdbc:mysql://%s:3306/food?useUnicode=true&characterEncoding=utf8",
								DB), UN, PW, 5, 30);
	}

	private List<Tag> getTags() {

		String updateFormat = "select tagname.name, tagvalue.value from tagname, tagvalue where tagname.id = tagvalue.nameid";
		List<Map<String, Object>> nameList = mTemplate
				.queryForList(updateFormat);

		List<Tag> tags = new ArrayList<Tag>();
		for (Map<String, Object> valueMap : nameList) {

			String name = (String) valueMap.get("name");
			String value = (String) valueMap.get("value");
			Tag tag = new Tag();
			tag.name = name;
			tag.value = value;

			tags.add(tag);
		}
		return tags;
	}

	private class Tag {
		String name;
		String value;
	}

	public void updateDishTags() {

		List<Tag> tags = getTags();
		// System.out.println(getTags());

		// 从 dish里读取

		DishTagMatcher matcher = new DishTagMatcher();
		int start = 0;
		int limit = 100;
		String updateFormat = "select * from dish limit %d,%d";

		List<Map<String, Object>> values = mTemplate.queryForList(String
				.format(updateFormat, start, limit));
		do {

			for (Map<String, Object> valueMap : values) {

				for (Tag tag : tags) {
					matcher.match(tag.name, tag.value, valueMap);
				}
			}

			start += limit;

			values = mTemplate.queryForList(String.format(updateFormat, start,
					limit));
		} while (values.size() > 0);
	}

	public static void main(String[] args) {
		new DbUpdater().updateDishTags();

	}

}
