package filter.crawler;

/*
 * Copyright (C) 2014 hu
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.util.JDBCHelper;
import cn.edu.hfut.dmic.webcollector.util.RegexRule;
import filter.entity.DishDetail;
import filter.entity.DishType;

public class DishTypeCrawler extends DeepCrawler {
	private JdbcTemplate mTemplate = null;

	private CrawlerDocParser mParser = new CrawlerDocParser();

	public DishTypeCrawler(String crawlPath) {
		super(crawlPath);

		mTemplate = JDBCHelper
				.createMysqlTemplate(
						"mysql1",
						"jdbc:mysql://localhost:3306/food?useUnicode=true&characterEncoding=utf8",
						"root", "409569210", 5, 30);

		mTemplate.execute("CREATE TABLE IF NOT EXISTS dishtype ("
				+ "id int(11) NOT NULL AUTO_INCREMENT,"
				+ "name varchar(50),parentid int(11)," + "PRIMARY KEY (id)"
				+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8;");
	}

	private List<DishType> mDishTypeList = new ArrayList<DishType>();

	private List<String> mSubDishTypeUrlList = new ArrayList<String>();

	public static final String BASE_URL = "http://www.meishij.net/";

	@Override
	public Links visitAndGetNextLinks(Page page) {
		Document doc = page.getDoc();

		Links nextLinks = new Links();
		String url = page.getUrl();

		// 根分类，（家常菜谱 中华菜系 各地小吃 外国菜谱...）
		if (BASE_URL.equals(url)) {
			synchronized (mDishTypeList) {
				mDishTypeList.addAll(mParser.getPrimaryTypes(doc));

				int id = 1;
				for (DishType parentType : mDishTypeList) {
					nextLinks.add(parentType.url);

					String update = "insert into dishtype (id, name,parentid) value(?,?,?)";
					parentType.id = id++;
					mTemplate.update(update, parentType.id, parentType.name, 0);
//					break;
				}
			}
		} else {
			// 子分类 （家常菜私家菜凉菜....）
			boolean isChildTypeUrl = false;
			synchronized (mDishTypeList) {
				for (DishType parentType : mDishTypeList) {

					if (url.equals(parentType.url)) {
						parentType.children = mParser.getChildrenTypes(doc);

						for (DishType child : parentType.children) {
							nextLinks.add(child.url);

							synchronized (mSubDishTypeUrlList) {
								mSubDishTypeUrlList.add(child.url);
							}

							String update = "insert into dishtype (name,parentid) value(?,?)";
							mTemplate.update(update, child.name, parentType.id);
//							break;
						}
						isChildTypeUrl = true;
						break;
					}
				}
			}

			if (!isChildTypeUrl) {

				// 子分类详情（为菜谱列表），用于获取菜谱详情
				boolean isSubDishTypeUrl = false;
				synchronized (mSubDishTypeUrlList) {

					for (String typeUrl : mSubDishTypeUrlList) {
						if (url.contains(typeUrl)) {
							List<String> urls = mParser
									.getDishTypeDetailPageUrls(doc, url,
											url.equals(typeUrl));
							isSubDishTypeUrl = true;
							nextLinks.addAll(urls);
							break;
						}
					}
				}

				if (!isSubDishTypeUrl) {

					// 菜谱详情
					DishDetail detail = mParser.getDishDetail(doc, url);
					mTemplate
							.update("insert into dish (name,materials,steps,dishType,submaterials,level,quantity,method,url) value(?,?,?,?,?,?,?,?,?)",
									detail.title, detail.mainMaterial,
									detail.steps.toString(), detail.dishType,
									detail.subMaterial, detail.level, detail.quantity, detail.method,
									url);
				}
			}
		}

		return nextLinks;
	}

	public static void main(String[] args) throws Exception {

		//
		// String reg =
		// "http://www.meishij.net/chufang/diy/jiangchangcaipu/.*page=.*";
		// String str =
		// "http://www.meishij.net/chufang/diy/jiangchangcaipu/?&page=21";
		// System.out.println(str.matches(reg));
		//

		/*
		 * 构造函数中的string,是爬虫的crawlPath，爬虫的爬取信息都存在crawlPath文件夹中,
		 * 不同的爬虫请使用不同的crawlPath
		 */
		final DishTypeCrawler crawler = new DishTypeCrawler("F:\\cache");
		crawler.setThreads(80);
//		crawler.addSeed("http://www.meishij.net/chufang/diy/gaodianxiaochi/19547.html");
//		crawler.addSeed("http://www.meishij.net/zuofa/baocaisichaomuer.html");
		crawler.addSeed("http://www.meishij.net/");
		crawler.setResumable(false);

		/* 设置是否断点爬取 */
		crawler.setResumable(false);

		crawler.start(5);

		// new Thread() {
		// public void run() {
		// try {
		// Thread.sleep(10000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// for (DishType holder : crawler.mDishTypeList) {
		// System.out.println("----------------------------------");
		// System.out.println("名称：" + holder.name + ", url："
		// + holder.url + ", 子分类：");
		//
		// // String update =
		// // "insert into dishtype (id, name,parentid) value(?,?,?)";
		//
		// // dest.update(update, id, holder.name, 0);
		//
		// for (DishType child : holder.children) {
		// System.out.println("        名称：" + child.name
		// + ", url：" + child.url);
		// }
		// System.out.println("----------------------------------");
		// }
		// };
		// }.start();
	}
}