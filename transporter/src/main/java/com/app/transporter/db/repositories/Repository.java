package com.app.transporter.db.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.dalesbred.Database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * General class for database queries.
 * @author Victor
 *
 * @param <T> Entity from DB.
 */
public abstract class Repository<T> implements Transfer<T> {
	
	private Database dataBase;
	
	private String entityName;

	public ObjectMapper objectMapper = new ObjectMapper();

	public Repository(Database db, String entityName) {
		this.dataBase = db;
		this.entityName = entityName;
	}

	public Database getDataBase() {
		return dataBase;
	}
	
	public String getEntityName() {
		return entityName;
	}
	
	//Create a list of JSON rows from table.
	public List<String> pullAllData() {
		return this.findAll().stream().map(this::toJSON).collect(Collectors.toList());
	}
	
	//Push all JSON rows of data into table.
	public void pushAllData(List<String> stringList) {
		List<T> list = stringList.stream().map(it -> fromJSON(it)).collect(Collectors.toList());
		insertAll(list);
	}
	
	public String extractAllRowsToJSON() {
		return listToJSON(findAll());
	}
	
	@Override
	public String toJSON(T object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return EMPTY_JSON;
		}
	}
	
	@Override
	public String listToJSON(List<? extends T> list) {
		try {
			return objectMapper.writeValueAsString(list);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return EMPTY_JSON;
		}
	}
	
	abstract void insertAll(List<T> insertAll);
	
	abstract List<? extends T> findAll();
	
	abstract Optional<? extends T> searchById(Integer id);
	
	abstract String save(T t);
}
