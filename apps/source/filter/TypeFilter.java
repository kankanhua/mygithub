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
						"root", "409569210", 5, 30);

		JdbcTemplate dest = JDBCHelper
				.createMysqlTemplate(
						"mysql1",
						"jdbc:mysql://localhost/food?useUnicode=true&characterEncoding=utf8",
						"root", "409569210", 5, 30);

		Map<String, Object> result = source
				.queryForMap("select * from tb_content where url = 'http://www.meishij.net/' limit 1");

		String html = (String) result.get("html");

		List<DishType> holders = getPrimaryTypes(html, (String) result.get("url"));

		int i = 0;
		for (DishType holder : holders) {
			System.out.println("----------------------------------");
			System.out.println("名称：" + holder.name + ", url：" + holder.url
					+ ", 子分类：");

			String update = "insert into dishtype (id, name,parentid) value(?,?,?)";

			int id = ++i;
			dest.update(update, id, holder.name, 0);

			Map<String, Object> typeDetail = source
					.queryForMap("select * from tb_content where url = '"
							+ holder.url + "' limit 1");

			List<DishType> children = getChildrenTypes(typeDetail.get("html")
					.toString(), typeDetail.get("url").toString());

			for (DishType child : children) {
				System.out.println("        名称：" + child.name + ", url："
						+ child.url);
				dest.update(update, ++i, child.name, id);
			}
			System.out.println("----------------------------------");
		}

	}

	private class DishType {
		String name;
		String url;
		List<DishType> children;
	}

	private List<DishType> getChildrenTypes(String html, String url) {

		List<DishType> list = new ArrayList<TypeFilter.DishType>();

		Document doc = Jsoup.parse(html, url);
		Elements titles = doc.select("#listnav_con_c > dl.clearfix > dd > a");
		// #listnav_con_c > dl.listnav_dl_style1.w300.br1.clearfix >
		// dd:nth-child(2) > a

		if (titles.size() == 0) {
			titles = doc.select("#listnav > div > dl > dd > a");
		}
		for (int i = 0; i < titles.size(); i++) {
			Element titlesElem = titles.get(i);

			String title = titlesElem.text();
			String titleUrl = titlesElem.attr("href");

			DishType titleHolder = new DishType();
			titleHolder.name = title;
			titleHolder.url = titleUrl;
			list.add(titleHolder);
		}
		return list;
	}

	private List<DishType> getPrimaryTypes(String html, String url) {
		List<DishType> list = new ArrayList<TypeFilter.DishType>();
		Document doc = Jsoup.parse(html, url);

		Elements titles = doc
				.select("#main_nav > li:nth-child(2) > div > div > div.dwitem > dl");

		for (int i = 0; i < titles.size(); i++) {
			Element titlesElem = titles.get(i);

			Elements titleElem = titlesElem.select("dt > a");
			String title = titleElem.text();
			String titleUrl = titleElem.attr("href");

			DishType titleHolder = new DishType();
			titleHolder.name = title;
			titleHolder.url = titleUrl;
			list.add(titleHolder);
		}
		return list;
	}

	public List<DishType> doParse(String html, String url) {

		List<DishType> list = new ArrayList<TypeFilter.DishType>();
		Document doc = Jsoup.parse(html, url);

		Elements titles = doc
				.select("#main_nav > li:nth-child(2) > div > div > div.dwitem > dl");

		for (int i = 0; i < titles.size(); i++) {
			Element titlesElem = titles.get(i);

			Elements titleElem = titlesElem.select("dt > a");
			String title = titleElem.text();
			String titleUrl = titleElem.attr("href");

			DishType titleHolder = new DishType();
			titleHolder.name = title;
			titleHolder.url = titleUrl;

			titleHolder.children = new ArrayList<TypeFilter.DishType>();

			Elements node = titlesElem.select("dd > a");

			for (int j = 0; j < node.size(); j++) {
				Element e = node.get(j);
				String name = e.text();
				String eUrl = e.attr("href");
				DishType holder = new DishType();
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
