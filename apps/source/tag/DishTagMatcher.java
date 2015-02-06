package tag;

import java.util.Map;

public class DishTagMatcher {

	private static final String TAG_NAME_REGION = "地区";
	private static final String TAG_NAME_MATERIAL = "食材";

	public boolean match(String tagName, String tagValue,
			Map<String, Object> result) {

		if (TAG_NAME_REGION.equals(tagName)) {
			Object value = result.get(Table.TABLE_DISH.COLUMN_DISHTYPE);
			if (value != null && value.toString().contains(tagValue)) {
				System.out.println("MATCH!, value = " + value + ", tag = "
						+ tagValue);
			}

		} else if (TAG_NAME_MATERIAL.equals(tagName)) {

			Object value = result.get(Table.TABLE_DISH.COLUMN_MATERIALS);
			if (value != null && value.toString().contains(tagValue)) {
				System.out.println("MATCH!, value = " + value + ", tag = "
						+ tagValue);
			}
		}

		return false;
	}
}
