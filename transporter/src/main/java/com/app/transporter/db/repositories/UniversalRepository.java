package com.app.transporter.db.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;

/**
 * Container for all the available repositories. Create DB scheme with all the existing data.
 * <br> Structure: [ tableName,[data] ]. The entire structure is converted to JSON format.
 * <br>E.g: 
 * <br>table["courier",[{name = "firstCourier", password = "something"}, {name = "secondCourier", password = "something"}]
 * <br>table["package",[{name = "firstPackage", }, {name = "secondPackage}]
 * @author Victor
 *
 */
public class UniversalRepository {
	
	private List<Repository<?>> repositories = new ArrayList<>();
	
	private ObjectMapper objectMapper = new ObjectMapper();

	public UniversalRepository() { }

	public UniversalRepository(List<Repository<?>> repositories) {
		this.repositories = repositories;
	}

	public void addRepository(final Repository<?> repo) {
		repositories.add(repo);
	}
	
	public String createDBSchema() {
		var entities = new ArrayList<Map<String, List<String>>>();
		for (var repo : repositories) {
			List<String> tableData = repo.pullAllData();
			var entity = Map.of(repo.getEntityName(), tableData);
			entities.add(entity);
		}
		try {
			String dbSchema = objectMapper.writeValueAsString(entities);
			return dbSchema;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{ }";
	}
	
	public void insertAllDBSchema(String dbSchema) {
		MapType mapType = objectMapper.getTypeFactory().constructMapType(Map.class, String.class, List.class);
		CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, mapType);
		try {
			List<Map<String, List<String>>> entries = objectMapper.readValue(dbSchema, collectionType);
			for(var entry : entries) {
				for(var entity : entry.entrySet()) {
					var tableName = entity.getKey();
					var tableData = entity.getValue();
					var repository = repositories.stream()
							.filter(it -> it.getEntityName().equals(tableName))
							.findFirst().get(); //TODO: ifPresent maybe
					repository.pushAllData(tableData);
					
				}
			}
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
}
