package filter.entity;

import java.util.List;

public class DishType {
	public int id;
	public String name;
	public String url;
	public DishType parentType;
	public List<DishType> children;
}
