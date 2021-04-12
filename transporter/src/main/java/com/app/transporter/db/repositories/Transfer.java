package com.app.transporter.db.repositories;

import java.util.List;

public interface Transfer<T> {

	public final static String EMPTY_JSON = "{ }";

	String toJSON(T t);
	
	String listToJSON(List<? extends T> list);

	T fromJSON(String s);
}