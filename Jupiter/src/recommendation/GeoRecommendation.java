package recommendation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Collections;
import java.util.Comparator;


import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;

public class GeoRecommendation {
	 public List<Item> recommendItems(String userId, double lat, double lon) {
		 // get its favorite lists
		 System.out.println("search recommed items in database");
		 List<Item> recommendedItems = new ArrayList<>();
		 DBConnection connection = DBConnectionFactory.getConnection();
		 // getFavoriteItemIds
		 Set<String> favoriteItemIds = connection.getFavoriteItemIds(userId);
		 // get all of the favorite one's category		
		 Map<String, Integer> allCategories = new HashMap<>();
		 System.out.println("favoriteItems : " + favoriteItemIds.size());
		 // Set<String> getCategories
		 
		 for(String id : favoriteItemIds) {
			 Set<String> categories = connection.getCategories(id);
			 for(String category : categories) {
				 allCategories.put(category, allCategories.getOrDefault(id, 0) + 1);
			 } 
		 }		 
		 // sort the map and get the most liked
		 List<Entry<String, Integer>> categoryList = new ArrayList<>(allCategories.entrySet());
		 Collections.sort(categoryList, new Comparator<Entry<String, Integer>>() {
			 @Override 
			 public int compare(Entry<String, Integer> one, Entry<String, Integer> two) {
				 if(one.getValue() == two.getValue()) {
					 return 0;
				 }
				 return one.getValue() < two.getValue() ? -1 : 1;
			 }
			 
		 });
		 System.out.println("categorylist: " + categoryList.size());
		 // make sure to get the unique items
		 Set<String> visitedItemIds = new HashSet<>();
		 // get the category and the items and recommend it(if it not be recommended before)
		 for (Entry<String, Integer> category : categoryList) {
			 System.out.println("category de KV : " + category.getKey() + " " + category.getValue());
				List<Item> items = connection.searchItems(lat, lon, category.getKey());
				for (Item item : items) {
					if (!favoriteItemIds.contains(item.getItemId()) && !visitedItemIds.contains(item.getItemId())) {
//						System.out.println("tmp search item: " + items + "    " + item.getItemId());
						recommendedItems.add(item);
						visitedItemIds.add(item.getItemId());
					}
				}
			}
			
			connection.close();
			return recommendedItems;


		 

		 
	 }

}
