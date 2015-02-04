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

public class TypeFilter {

	public void filterType() {
		JdbcTemplate source = JDBCHelper
				.createMysqlTemplate(
						"mysql1",
						"jdbc:mysql://localhost/crawler?useUnicode=true&characterEncoding=utf8",
						"root", "root", 5, 30);

		Map<String, Object> result = source
				.queryForMap("select * from tb_content where url = 'http://www.meishij.net/'");

		String html = (String) result.get("html");

		List<Holder> holders = doParse(html, (String) result.get("url"));

		for (Holder holder : holders) {
			System.out.println("----------------------------------");
			System.out.println("名称：" + holder.name + ", url：" + holder.url
					+ ", 子分类：");

			for (Holder child : holder.children) {
				System.out.println("        名称：" + child.name + ", url："
						+ child.url);
			}
			System.out.println("----------------------------------");
		}

	}

	private class Holder {
		String name;
		String url;
		List<Holder> children;
	}

	public List<Holder> doParse(String html, String url) {

		List<Holder> list = new ArrayList<TypeFilter.Holder>();
		Document doc = Jsoup.parse(html, url);

		Elements titles = doc
				.select("#main_nav > li:nth-child(2) > div > div > div.dwitem > dl");

		for (int i = 0; i < titles.size(); i++) {
			Element titlesElem = titles.get(i);

			Elements titleElem = titlesElem.select("dt > a");
			String title = titleElem.text();
			String titleUrl = titleElem.attr("href");

			Holder titleHolder = new Holder();
			titleHolder.name = title;
			titleHolder.url = titleUrl;

			titleHolder.children = new ArrayList<TypeFilter.Holder>();

			Elements node = titlesElem.select("dd > a");

			for (int j = 0; j < node.size(); j++) {
				Element e = node.get(j);
				String name = e.text();
				String eUrl = e.attr("href");
				Holder holder = new Holder();
				holder.name = name;
				holder.url = eUrl;
				titleHolder.children.add(holder);
			}
			list.add(titleHolder);
		}

		return list;
	}

	public static void main(String[] args) {
		new TypeFilter().filterType();
	}
}
