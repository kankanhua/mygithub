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

import org.jsoup.nodes.Document;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import filter.entity.DishDetail;
import filter.entity.DishType;

public class DishTypeCrawler extends DeepCrawler {
	// private JdbcTemplate mTemplate = null;

	private CrawlerDocParser mParser = new CrawlerDocParser();

	private DbUpdater mUpdater = new DbUpdater();

	public DishTypeCrawler(String crawlPath) {
		super(crawlPath);

		mUpdater = new DbUpdater();
	}

	private List<DishType> mDishTypeList = new ArrayList<DishType>();

	private List<DishType> mSubDishTypeUrlList = new ArrayList<DishType>();

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
					parentType.id = id++;
					mUpdater.updateType(parentType);
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

							child.parentType = parentType;

							synchronized (mSubDishTypeUrlList) {
								mSubDishTypeUrlList.add(child);
							}
							mUpdater.updateType(child);
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

					for (DishType type : mSubDishTypeUrlList) {
						if (url.contains(type.url + "&page=")
								|| url.contains(type.url + "?&page=")
								|| url.equals(type.url)) {
							List<String> urls = mParser
									.getDishTypeDetailPageUrls(doc, url,
											url.equals(type.url));
							isSubDishTypeUrl = true;
							nextLinks.addAll(urls);
							break;
						}
					}
				}

				if (!isSubDishTypeUrl) {
					// 菜谱详情
					DishDetail detail = mParser.getDishDetail(doc, url);

					mUpdater.updateDishDetail(detail);
				}
			}
		}

		return nextLinks;
	}

	private static final boolean TEST = false;

	public static void main(String[] args) throws Exception {
		if (!TEST) {

			final DishTypeCrawler crawler = new DishTypeCrawler("F:\\cache1");
			crawler.setThreads(50);
			crawler.addSeed("http://www.meishij.net/");
			crawler.setResumable(false);
			crawler.start(5);
		} else {

			final DishTypeCrawler crawler = new DishTypeCrawler("F:\\cache1");
			crawler.setThreads(1);
			crawler.addSeed("http://www.meishij.net/china-food/xiaochi/qinghai/18194.html");
			crawler.setResumable(false);
			crawler.start(1);
		}

	}
}