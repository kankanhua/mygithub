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
				for (int i = 2; i <= size; i++) {
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
		DishDetail detail = getDishDetail(doc);

		if (detail != null) {
			detail.url = url;
		}

		return detail;
	}

	//
	// protected DishDetail getOtherDishDetail(Document doc) {
	// DishDetail holder = new DishDetail();
	// Elements typeElement = doc
	// .select("body > div.main_w.clearfix > div.main > ul > li:nth-child(5) > a");
	// holder.dishType = typeElement.text();
	//
	// Elements titleElement = doc
	// .select("body > div.main_w.clearfix > div.main.clearfix > div.cp_header.clearfix > div.cp_main_info_w > div.info1 > h1 > a");
	//
	// holder.title = titleElement.text();
	//
	// Elements methodElement = doc
	// .select("body > div.main_w.clearfix > div.main.clearfix > div.cp_header.clearfix > div.cp_main_info_w > div.info2 > ul > li:nth-child(1) > a");
	// holder.method = methodElement.text();
	//
	// Elements detailElem = doc
	// .select("body > div.main_w.clearfix > div.main.clearfix > div.cp_body.clearfix > div.cp_body_left > div.measure > div.edit > ul > li > p");
	//
	// if (detailElem.size() == 0) {
	// detailElem = doc
	// .select("body > div.main_w.clearfix > div.main.clearfix > div.cp_body.clearfix > div.cp_body_left > div.measure > div.edit > p");
	// }
	// boolean isMaterial = false;
	// boolean isStep = false;
	// StringBuilder materialSb = new StringBuilder();
	// for (int i = 0; i < detailElem.size(); i++) {
	// Element e = detailElem.get(i);
	//
	// String text = e.text().replaceAll("　", "").trim();
	//
	// if (text.contains("配料:") || text.contains("配料：")) {
	// isMaterial = true;
	// isStep = false;
	//
	// continue;
	// } else if (text.contains("操作:") || text.contains("操作：")
	// || text.contains("制作方法")) {
	// isMaterial = false;
	// isStep = true;
	// continue;
	// }
	//
	// if (isMaterial) {
	// materialSb.append(e.text());
	// }
	//
	// if (isStep) {
	// holder.steps.add(e.text());
	// }
	// }
	// holder.mainMaterial = materialSb.toString();
	//
	// // holder.mainMaterial = materialElement.text();
	//
	// // Elements subMaterialElement = doc
	// //
	// .select("body > div.main_w.clearfix > div.main.clearfix > div.cp_body.clearfix > div.cp_body_left > div.materials > div > div.yl.fuliao.clearfix > ul > li");
	// //
	// // holder.subMaterial = subMaterialElement.text();
	//
	// Elements quantityElement = doc
	// .select("body > div.main_w.clearfix > div.main.clearfix > div.cp_header.clearfix > div.cp_main_info_w > div.info2 > ul > li:nth-child(3) > div > a");
	// holder.quantity = quantityElement.text();
	//
	// Elements levelElement = doc
	// .select("body > div.main_w.clearfix > div.main.clearfix > div.cp_header.clearfix > div.cp_main_info_w > div.info2 > ul > li:nth-child(2) > div > a");
	// holder.level = levelElement.text();
	//
	// // Elements stepE = doc
	// //
	// .select("body > div.main_w.clearfix > div.main.clearfix > div.cp_body.clearfix > div.cp_body_left > div.measure > div.edit > ul > li:nth-child(2) > p:nth-child(8)");
	// //
	// // for (int i = 0; i < stepE.size(); i++) {
	// // Element e = stepE.get(i);
	// //
	// // if (e.children().hasClass("step")) {
	// // String step = e.text();
	// // if (!"".equals(step)) {
	// // holder.steps.add(step);
	// // }
	// // }
	// //
	// // }
	//
	// return holder;
	// }

	private String getTypeText(Document doc) {
		String text = "";
		Elements typeElement = doc
				.select("body > div.main_w.clearfix > div.main.clearfix > ul > li:nth-child(5) > a");
		if (typeElement.size() == 0) {
			typeElement = doc
					.select("body > div.main_w.clearfix > div.main > ul > li:nth-child(5) > a");
		}

		text = typeElement.text();

		return text;
	}

	private String getTitleText(Document doc) {
		String text = "";
		Elements titleElement = doc
				.select("body > div.main_w.clearfix > div.main.clearfix > div.cp_header.clearfix > div.cp_main_info_w > div.info1 > h1 > a");

		if (titleElement.size() == 0) {
			// titleElement = doc
			// .select("body > div.main_w.clearfix > div.main.clearfix > div.cp_header.clearfix > div.cp_main_info_w > div.info1 > h1 > a");
		}
		text = titleElement.text();
		return text;
	}

	private String getMethodText(Document doc) {
		Elements methodElement = doc
				.select("body > div.main_w.clearfix > div.main.clearfix > div.cp_header.clearfix > div.cp_main_info_w > div.info2 > ul > li:nth-child(1) > a");

		if (methodElement.size() == 0) {
			// methodElement = doc
			// .select("body > div.main_w.clearfix > div.main.clearfix > div.cp_header.clearfix > div.cp_main_info_w > div.info2 > ul > li:nth-child(1) > a");
		}
		return methodElement.text();
	}

	private String getMaterialText(Document doc) {
		Elements materialElement = doc
				.select("body > div.main_w.clearfix > div.main.clearfix > div.cp_body.clearfix > div.cp_body_left > div.materials > div > div.yl.zl.clearfix > ul > li > div > h4");

		if (materialElement.size() == 0) {
			Elements detailElem = doc
					.select("body > div.main_w.clearfix > div.main.clearfix > div.cp_body.clearfix > div.cp_body_left > div.measure > div.edit > ul > li > p");

			if (detailElem.size() == 0) {
				detailElem = doc
						.select("body > div.main_w.clearfix > div.main.clearfix > div.cp_body.clearfix > div.cp_body_left > div.measure > div.edit > p");
			}

			boolean isMaterial = false;
			StringBuilder materialSb = new StringBuilder();
			for (int i = 0; i < detailElem.size(); i++) {
				Element e = detailElem.get(i);

				String text = e.text().replaceAll("　", "").trim();

				if (isMaterialString(text)) {
					isMaterial = true;

					continue;
				} else if (isStepString(text)) {
					return materialSb.toString();
				}

				if (isMaterial) {
					materialSb.append(e.text());
				}

			}
		}

		return materialElement.text();
	}

	private String getSubMaterialText(Document doc) {
		Elements subMaterialElement = doc
				.select("body > div.main_w.clearfix > div.main.clearfix > div.cp_body.clearfix > div.cp_body_left > div.materials > div > div.yl.fuliao.clearfix > ul > li");

		return subMaterialElement.text();
	}

	private String getQuantityText(Document doc) {
		Elements quantityElement = doc
				.select("body > div.main_w.clearfix > div.main.clearfix > div.cp_header.clearfix > div.cp_main_info_w > div.info2 > ul > li:nth-child(3) > div > a");

		if (quantityElement.size() == 0) {
			// quantityElement = doc
			// .select("body > div.main_w.clearfix > div.main.clearfix > div.cp_header.clearfix > div.cp_main_info_w > div.info2 > ul > li:nth-child(3) > div > a");
		}
		return quantityElement.text();
	}

	private String getLevelElement(Document doc) {
		Elements levelElement = doc
				.select("body > div.main_w.clearfix > div.main.clearfix > div.cp_header.clearfix > div.cp_main_info_w > div.info2 > ul > li:nth-child(2) > div > a");

		return levelElement.text();
	}

	private ArrayList<String> getSteps(Document doc) {
		ArrayList<String> steps = new ArrayList<String>();

		Elements stepE = doc
				.select("body > div.main_w.clearfix > div.main.clearfix > div.cp_body.clearfix > div.cp_body_left > div.measure > div.editnew.edit > div.content.clearfix");

		if (stepE.size() == 0) {
			stepE = doc
					.select("body > div.main_w.clearfix > div.main.clearfix > div.cp_body.clearfix > div.cp_body_left > div.measure > div.edit");
		}
		boolean isStep = false;

		Element first = stepE.first();
		if (first != null) {
			Element node = first.child(0);
			if ("p".equals(node.nodeName())) {
				stepE = stepE.select("p");
			} else if ("ul".equals(node.nodeName())) {
				stepE = stepE.select("ul > li > p");
			}
		}

		for (int i = 0; i < stepE.size(); i++) {
			Element e = stepE.get(i);
			if (e.children().hasClass("step")) {
				String step = e.text();
				if (!"".equals(step)) {
					steps.add(step);
				}
			} else {
				String text = e.text().replaceAll("　", "").trim();

				if (isStepString(text)) {
					isStep = true;
					continue;
				}

				if (isStep) {
					steps.add(e.text());
				}
			}
		}
		return steps;
	}

	static String[] STEP_STRING = { "操作:", "做法:", "操作：", "做法：", "制作方法" };
	static String[] MATERIAL_STRING = { "配料:", "配料：" };

	private boolean isStepString(String text) {
		for (String s : STEP_STRING) {
			if (text.contains(s)) {
				return true;
			}
		}

		return false;
	}

	private boolean isMaterialString(String text) {
		for (String s : MATERIAL_STRING) {
			if (text.contains(s)) {
				return true;
			}
		}

		return false;
	}

	protected DishDetail getDishDetail(Document doc) {
		DishDetail holder = new DishDetail();
		holder.dishType = getTypeText(doc);
		holder.title = getTitleText(doc);
		holder.method = getMethodText(doc);
		holder.mainMaterial = getMaterialText(doc);
		holder.subMaterial = getSubMaterialText(doc);
		holder.quantity = getQuantityText(doc);
		holder.level = getLevelElement(doc);
		holder.steps.addAll(getSteps(doc));

		return holder;
	}

	// public static void main(String[] args) {
	// String a = "共2400页，                  fjdklsj35jlj";
	// String regEx = "[^共0-9页]";
	//
	// System.out.println(a.replaceAll(" ", ""));
	// Pattern p = Pattern.compile(regEx);
	// Matcher m = p.matcher(a);
	//
	// System.out.println(m.replaceAll(""));
	//
	// // String input = "sdfsdfjava";
	// // Pattern p = Pattern.compile("((?java))");
	// // Matcher m = p.matcher(input);
	// // System.out.println(m.replaceAll("").trim());
	// }
}
