package filter.crawler;

import org.springframework.jdbc.core.JdbcTemplate;

import cn.edu.hfut.dmic.webcollector.util.JDBCHelper;
import filter.entity.DishDetail;
import filter.entity.DishType;

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

		/**
		 * 创建与准备数据库表格
		 */

		mTemplate.execute("DROP TABLE IF EXISTS `dish`;");
		mTemplate.execute("DROP TABLE IF EXISTS `dishmenu`;");
		mTemplate.execute("DROP TABLE IF EXISTS `dishtype`;");
		mTemplate.execute("DROP TABLE IF EXISTS `material`;");
		mTemplate.execute("DROP TABLE IF EXISTS `materialtype`;");

		mTemplate.execute("CREATE TABLE IF NOT EXISTS `dish` ("
				+ "`Id` int(11) NOT NULL AUTO_INCREMENT,"
				+ " `name` varchar(255) DEFAULT NULL,"
				+ " `materials` longtext DEFAULT '',"
				+ " `steps` longtext,"
				+ "`dishType` varchar(255) DEFAULT NULL,"
				+ "`submaterials` longtext DEFAULT NULL,"
				+ " `level` varchar(255) DEFAULT NULL,"
				+ "  `quantity` varchar(255) DEFAULT NULL,"
				+ " `method` varchar(255) DEFAULT NULL,"
				+ "`url` varchar(255) DEFAULT NULL," + "PRIMARY KEY (`Id`)"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");

		mTemplate.execute("CREATE TABLE IF NOT EXISTS `dishmenu` ("
				+ "`Id` int(11) NOT NULL AUTO_INCREMENT,"
				+ "`name` varchar(255) DEFAULT NULL,"
				+ "`type` varchar(255) DEFAULT NULL,"
				+ "`dishes` varchar(255) DEFAULT NULL," + "PRIMARY KEY (`Id`)"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");

		mTemplate.execute("CREATE TABLE `dishtype` ("
				+ "`id` int(11) NOT NULL AUTO_INCREMENT,"
				+ "`name` varchar(50) DEFAULT NULL,"
				+ "`parentid` int(11) DEFAULT NULL,"
				+ "`url` varchar(255) DEFAULT NULL," + "PRIMARY KEY (`id`)"
				+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8;");

		mTemplate.execute("CREATE TABLE `material` ("
				+ "`Id` int(11) NOT NULL AUTO_INCREMENT,"
				+ "`name` varchar(255) DEFAULT NULL,"
				+ "`materialtype` varchar(255) DEFAULT NULL,"
				+ "`url` varchar(255) DEFAULT NULL," + "PRIMARY KEY (`Id`)"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;");

		mTemplate.execute("CREATE TABLE `materialtype` ("
				+ "`Id` int(11) NOT NULL AUTO_INCREMENT,"
				+ "`name` varchar(255) DEFAULT NULL,"
				+ "`parentid` varchar(255) DEFAULT NULL,"
				+ "`url` varchar(255) DEFAULT NULL," + "PRIMARY KEY (`Id`)"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
	}

	public void updateType(DishType type) {

		if (type == null) {
			return;
		}

		DishType parentType = type.parentType;

		String name = type.name;
		String url = type.url;
		int id = type.id;
		int parentId = 0;

		String typeName = type.name;
		if (parentType != null) {
			typeName = parentType.name;
			parentId = parentType.id;
		}

		String update = "";
		Object[] values = null;
		if (id == 0) {

			if (!"食材百科".equals(typeName)) {
				update = "insert into dishtype (name,parentid,url) value(?,?,?)";
			} else {
				update = "insert into materialtype (name,parentid,url) value(?,?,?)";
			}

			values = new Object[] { name, parentId, url };
		} else {
			if (!"食材百科".equals(typeName)) {
				update = "insert into dishtype (id,name,parentid,url) value(?,?,?,?)";
			} else {
				update = "insert into materialtype (id,name,parentid,url) value(?,?,?,?)";
			}

			values = new Object[] { id, name, parentId, url };
		}

		mTemplate.update(update, values);
	}

	public void updateDishDetail(DishDetail detail) {
		if (!"食材百科".equals(detail.dishType)) {
			mTemplate
					.update("insert into dish (name,materials,steps,dishType,submaterials,level,quantity,method,url) value(?,?,?,?,?,?,?,?,?)",
							detail.title, detail.mainMaterial,
							detail.steps.toString(), detail.dishType,
							detail.subMaterial, detail.level, detail.quantity,
							detail.method, detail.url);
		} else {
			mTemplate
					.update("insert into material (name,materialtype,url) value(?,?,?)",
							detail.title, detail.dishType, detail.url);
		}
	}
}
