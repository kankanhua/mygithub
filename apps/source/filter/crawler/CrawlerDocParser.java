package filter.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import filter.entity.DishDetail;
import filter.entity.DishType;

public class CrawlerDocParser {

	/**
	 * 获取子分类
	 * 
	 * @param doc
	 * @return
	 */
	public List<DishType> getChildrenTypes(Document doc) {

		List<DishType> list = new ArrayList<DishType>();

		Elements titles = doc.select("#listnav_con_c > dl.clearfix > dd > a");

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

	/**
	 * 获取主分类
	 * 
	 * @param doc
	 * @return
	 */
	public List<DishType> getPrimaryTypes(Document doc) {
		List<DishType> list = new ArrayList<DishType>();

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

	protected List<String> getDishTypeDetailPageUrls(Document doc, String url,
			boolean parsePages) {
		List<String> urls = new ArrayList<String>();

		Elements dishDetail = doc.select("#listtyle1_list > div.listtyle1 > a");

		for (int i = 0; i < dishDetail.size(); i++) {
			String detailUrl = dishDetail.get(i).attr("href");
			urls.add(detailUrl);
		}

		if (parsePages) {
			Elements pageSumElem = doc
					.select("#listtyle1_w > div.listtyle1_page > div > span.gopage > form");

			String text = pageSumElem.text();
			if (pageSumElem.size() == 0) {
				pageSumElem = doc
						.select("#listtyle1_w > div.listtyle1_page > div > a");

				if (pageSumElem.size() > 2) {
					text = pageSumElem.get(pageSumElem.size() - 2).text(); // 下一页之前一个
				} else {
					text = pageSumElem.text();
				}
			}

			if (!"".equals(text)) {
				String regEx = "[^0-9]";
				Pattern p = Pattern.compile(regEx);
				Matcher m = p.matcher(text);
				int size = 0;
				try {
					size = Integer.parseInt(m.replaceAll("").trim());
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("pageSumElem = " + pageSumElem
							+ ", url = " + url);
				}
				for (int i = 0; i < size; i++) {
					String pageUrl = "";
					if (url.contains("?")) {
						pageUrl = url + "&page=" + i;
					} else {
						pageUrl = url + "?&page=" + i;
					}

					urls.add(pageUrl);
				}
			}
		}
		return urls;
	}

	public DishDetail getDishDetail(Document doc, String url) {

		if (url.contains("/zuofa/")) {
			return getZuofaDishDetail(doc);
		} else {
			return getOtherDishDetail(doc);
		}
	}

	protected DishDetail getOtherDishDetail(Document doc) {
		DishDetail holder = new DishDetail();
		Elements typeElement = doc
				.select("body > div.main_w.clearfix > div.main.clearfix > ul > li:nth-child(5) > a");
		holder.dishType = typeElement.text();

		Elements titleElement = doc
				.select("body > div.main_w.clearfix > div.main.clearfix > div.cp_header.clearfix > div.cp_main_info_w > div.info1 > h1 > a");

		holder.title = titleElement.text();

		Elements methodElement = doc
				.select("body > div.main_w.clearfix > div.main.clearfix > div.cp_header.clearfix > div.cp_main_info_w > div.info2 > ul > li:nth-child(1) > a");
		holder.method = methodElement.text();

		Elements detailElem = doc
				.select("body > div.main_w.clearfix > div.main.clearfix > div.cp_body.clearfix > div.cp_body_left > div.measure > div.edit > ul > li > p");

		boolean isMaterial = false;
		boolean isStep = false;
		StringBuilder materialSb = new StringBuilder();
		for (int i = 0; i < detailElem.size(); i++) {
			Element e = detailElem.get(i);

			if (e.text().replaceAll("　", "").trim().contains("配料:")) {
				isMaterial = true;
				isStep = false;

				continue;
			} else if (e.text().replaceAll("　", "").trim().contains("操作:")) {
				isMaterial = false;
				isStep = true;
				continue;
			}

			if (isMaterial) {
				materialSb.append(e.text());
			}

			if (isStep) {
				holder.steps.add(e.text());
			}
		}
		holder.mainMaterial = materialSb.toString();

		// holder.mainMaterial = materialElement.text();

		// Elements subMaterialElement = doc
		// .select("body > div.main_w.clearfix > div.main.clearfix > div.cp_body.clearfix > div.cp_body_left > div.materials > div > div.yl.fuliao.clearfix > ul > li");
		//
		// holder.subMaterial = subMaterialElement.text();

		Elements quantityElement = doc
				.select("body > div.main_w.clearfix > div.main.clearfix > div.cp_header.clearfix > div.cp_main_info_w > div.info2 > ul > li:nth-child(3) > div > a");
		holder.quantity = quantityElement.text();

		Elements levelElement = doc
				.select("body > div.main_w.clearfix > div.main.clearfix > div.cp_header.clearfix > div.cp_main_info_w > div.info2 > ul > li:nth-child(2) > div > a");
		holder.level = levelElement.text();

		// Elements stepE = doc
		// .select("body > div.main_w.clearfix > div.main.clearfix > div.cp_body.clearfix > div.cp_body_left > div.measure > div.edit > ul > li:nth-child(2) > p:nth-child(8)");
		//
		// for (int i = 0; i < stepE.size(); i++) {
		// Element e = stepE.get(i);
		//
		// if (e.children().hasClass("step")) {
		// String step = e.text();
		// if (!"".equals(step)) {
		// holder.steps.add(step);
		// }
		// }
		//
		// }

		return holder;
	}

	protected DishDetail getZuofaDishDetail(Document doc) {
		DishDetail holder = new DishDetail();
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
				.select("body > div.main_w.clearfix > div.main.clearfix > div.cp_body.clearfix > div.cp_body_left > div.materials > div > div.yl.zl.clearfix > ul > li > div > h4");

		holder.mainMaterial = materialElement.text();

		Elements subMaterialElement = doc
				.select("body > div.main_w.clearfix > div.main.clearfix > div.cp_body.clearfix > div.cp_body_left > div.materials > div > div.yl.fuliao.clearfix > ul > li");

		holder.subMaterial = subMaterialElement.text();

		Elements quantityElement = doc
				.select("body > div.main_w.clearfix > div.main.clearfix > div.cp_header.clearfix > div.cp_main_info_w > div.info2 > ul > li:nth-child(3) > div > a");
		holder.quantity = quantityElement.text();

		Elements levelElement = doc
				.select("body > div.main_w.clearfix > div.main.clearfix > div.cp_header.clearfix > div.cp_main_info_w > div.info2 > ul > li:nth-child(2) > div > a");
		holder.level = levelElement.text();

		Elements stepE = doc
				.select("body > div.main_w.clearfix > div.main.clearfix > div.cp_body.clearfix > div.cp_body_left > div.measure > div.editnew.edit > div.content.clearfix");

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

		return holder;
	}

	public static void main(String[] args) {
		String a = "共2400页，                  fjdklsj35jlj";
		String regEx = "[^共0-9页]";

		System.out.println(a.replaceAll(" ", ""));
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(a);

		System.out.println(m.replaceAll(""));

		// String input = "sdfsdfjava";
		// Pattern p = Pattern.compile("((?java))");
		// Matcher m = p.matcher(input);
		// System.out.println(m.replaceAll("").trim());
	}
}
