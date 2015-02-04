//package filter;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import cn.edu.hfut.dmic.webcollector.model.Page;
//
//public class FilterEntry {
//
//
//	public void doFilter(Page page) {
//		Document doc = page.getDoc();
//		String title = doc.title();
//		String html = page.getHtml();
//		String url = page.getUrl();
//
//		if (BASE_URL.equals(url)) {
//			List<Type> holders = getPrimaryTypes(doc);
//
//			// int i = 0;
//			// for (Type holder : holders) {
//			// System.out.println("----------------------------------");
//			// System.out.println("名称：" + holder.name + ", url：" + holder.url
//			// + ", 子分类：");
//			//
//			// String update =
//			// "insert into dishtype (id, name,parentid) value(?,?,?)";
//			//
//			// int id = ++i;
//			// dest.update(update, id, holder.name, 0);
//			//
//			// Map<String, Object> typeDetail = source
//			// .queryForMap("select * from tb_content where url = '"
//			// + holder.url + "' limit 1");
//			//
//			// List<Type> children = getChildrenTypes(typeDetail.get("html")
//			// .toString(), typeDetail.get("url").toString());
//			//
//			// for (Type child : children) {
//			// System.out.println("        名称：" + child.name + ", url："
//			// + child.url);
//			// dest.update(update, ++i, child.name, id);
//			// }
//			// System.out.println("----------------------------------");
//			// }
//		}
//	}
//
//	private List<Type> getPrimaryTypes(Document doc) {
//		List<Type> list = new ArrayList<Type>();
//
//		Elements titles = doc
//				.select("#main_nav > li:nth-child(2) > div > div > div.dwitem > dl");
//
//		for (int i = 0; i < titles.size(); i++) {
//			Element titlesElem = titles.get(i);
//
//			Elements titleElem = titlesElem.select("dt > a");
//			String title = titleElem.text();
//			String titleUrl = titleElem.attr("href");
//
//			Type titleHolder = new Type();
//			titleHolder.name = title;
//			titleHolder.url = titleUrl;
//			list.add(titleHolder);
//		}
//		return list;
//	}
//
//	private List<Type> getChildrenTypes(String html, String url) {
//
//		List<Type> list = new ArrayList<Type>();
//
//		Document doc = Jsoup.parse(html, url);
//		Elements titles = doc.select("#listnav_con_c > dl.clearfix > dd > a");
//		// #listnav_con_c > dl.listnav_dl_style1.w300.br1.clearfix >
//		// dd:nth-child(2) > a
//
//		if (titles.size() == 0) {
//			titles = doc.select("#listnav > div > dl > dd > a");
//		}
//		for (int i = 0; i < titles.size(); i++) {
//			Element titlesElem = titles.get(i);
//
//			String title = titlesElem.text();
//			String titleUrl = titlesElem.attr("href");
//
//			Type titleHolder = new Type();
//			titleHolder.name = title;
//			titleHolder.url = titleUrl;
//			list.add(titleHolder);
//		}
//		return list;
//	}
//
//	private class Type {
//		String name;
//		String url;
//		List<Type> children;
//	}
//}
