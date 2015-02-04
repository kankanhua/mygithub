package filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.edu.hfut.dmic.webcollector.util.JDBCHelper;

public class ZuofaFilter {

	public void run() {
		JdbcTemplate srouce = JDBCHelper
				.createMysqlTemplate(
						"mysql1",
						"jdbc:mysql://localhost/crawler?useUnicode=true&characterEncoding=utf8",
						"root", "409569210", 5, 30);

		JdbcTemplate dest = JDBCHelper
				.createMysqlTemplate(
						"mysql1",
						"jdbc:mysql://localhost/food?useUnicode=true&characterEncoding=utf8",
						"root", "409569210", 5, 30);

		int start = 0;
		int limit = 100;
		List<Map<String, Object>> result = srouce.queryForList(getQueryString(
				start, limit));

		do {

			for (Map<String, Object> data : result) {

				dest.update("insert into data (title,url,html) value(?,?,?)",
						data.get("title"), data.get("url"), data.get("html"));
			}
			start += limit;

			result = srouce.queryForList(getQueryString(start, limit));
		} while (result != null && result.size() > 0);

	}

	public void parseHtml() {
		JdbcTemplate source = JDBCHelper
				.createMysqlTemplate(
						"mysql1",
						"jdbc:mysql://localhost/food?useUnicode=true&characterEncoding=utf8",
						"root", "409569210", 5, 30);
		JdbcTemplate dest = JDBCHelper
				.createMysqlTemplate(
						"mysql1",
						"jdbc:mysql://localhost/food?useUnicode=true&characterEncoding=utf8",
						"root", "409569210", 5, 30);
		int start = 0;
		int limit = 5000;
		List<Map<String, Object>> result = source.queryForList(getQueryString(
				start, limit));

		do {
			List<Holder> list = new ArrayList<ZuofaFilter.Holder>();
			parseDishes(result, list);
			saveToDB(dest, list);

			start += limit;
			result = dest.queryForList(getQueryString(start, limit));
		} while (result != null && result.size() > 0);
	}

	protected void saveToDB(JdbcTemplate dest, List<Holder> list) {
		for (Holder holder : list) {
			System.out.println("------------------------");
			System.out.println("名称 :" + holder.title + ", 方法:" + holder.method
					+ ", 主料:" + holder.mainMaterial + "分类：" + holder.dishType
					+ ", 链接地址:" + holder.url);

			dest.update(
					"insert into dish (name,materials,steps,dishType) value(?,?,?,?)",
					holder.title, holder.mainMaterial, holder.steps.toString(),
					holder.dishType);
		}
	}

	protected void parseDishes(List<Map<String, Object>> result,
			List<Holder> list) {
		for (Map<String, Object> data : result) {
			list.add(doParse(data.get("html").toString(), data.get("url")
					.toString()));
		}
	}

	public Holder doParse(String html, String url) {
		Holder holder = new Holder();
		holder.url = url;

		Document doc = Jsoup.parse(html, url);
		Elements typeElement = doc
				.select("body > div.main_w.clearfix > div.main.clearfix > ul > li:nth-child(5) > a");
		holder.dishType = typeElement.text();

		Elements titleElement = doc
				.select("body > div.main_w.clearfix > div.main.clearfix > div.cp_header.clearfix > div.cp_main_info_w > div.info1 > h1 > a");

		holder.title = titleElement.text();

		Elements methodElement = doc
				.select("body > div.main_w.clearfix > div.main.clearfix > div.cp_header.clearfix > div.cp_main_info_w > div.info2 > ul > li:nth-child(1) > a");
		holder.method = methodElement.text();

		Elements materialElement = doc
				.select("body > div.main_w.clearfix > div.main.clearfix > div.cp_body.clearfix > div.cp_body_left > div.materials > div > div.yl.zl.clearfix > ul > li > div > h4 > a");

		holder.mainMaterial = materialElement.text();

		Elements stepE = doc
				.select("body > div.main_w.clearfix > div.main.clearfix > div.cp_body.clearfix > div.cp_body_left > div.measure > div.editnew.edit > div.content.clearfix");
		//
		// body > div.main_w.clearfix > div.main.clearfix > div.cp_body.clearfix
		// > div.cp_body_left > div.measure > div.editnew.edit >
		// div.content.clearfix
		// body > div.main_w.clearfix > div.main.clearfix > div.cp_body.clearfix
		// > div.cp_body_left > div.measure > div.edit > p:nth-child(1) > em
		//

		if (stepE.size() == 0) {
			stepE = doc
					.select("body > div.main_w.clearfix > div.main.clearfix > div.cp_body.clearfix > div.cp_body_left > div.measure > div.edit > p");
		}

		for (int i = 0; i < stepE.size(); i++) {
			Element e = stepE.get(i);

			if (e.children().hasClass("step")) {
				String step = e.text();
				if (!"".equals(step)) {
					holder.steps.add(step);
				}
			}

		}
		// body > div.main_w.clearfix > div.main.clearfix > div.cp_body.clearfix
		// > div.cp_body_left > div.measure > div.editnew.edit >
		// div:nth-child(1)

		return holder;
	}

	private class Holder {
		String url;
		String title;
		String dishType;
		String method;
		String mainMaterial;
		List<String> steps = new ArrayList<String>();
	}

	private String getQueryString(int start, int limit) {
		return "select * from source where url like '%meishij.net/zuofa%' limit "
				+ start + "," + limit;
	}

	public static void main(String[] args) {
		new ZuofaFilter().parseHtml();
	}
}
