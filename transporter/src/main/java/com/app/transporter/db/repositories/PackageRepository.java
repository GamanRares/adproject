package com.app.transporter.db.repositories;

import java.util.List;
import java.util.Optional;

import org.dalesbred.Database;

import com.app.transporter.db.entities.Package;

import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpEntity;
import akka.http.javadsl.unmarshalling.Unmarshaller;


public class PackageRepository extends Repository<Package>{

	public static final Unmarshaller<HttpEntity, Package> unmarshaller = Jackson.unmarshaller(Package.class);
	
	public PackageRepository(Database db, String entityName) {
		super(db, entityName);
	}

	@Override
	public Package fromJSON(String s) {
		try {
			return objectMapper.readValue(s, Package.class);
		} catch (Exception e) {
			return new Package();
		}
	}

	@Override
	public void insertAll(List<Package> packages) {
		for (var pack : packages) {
			getDataBase().update("insert into package (id, title) values (?, ?)", pack.id, pack.title);
		}
	}

	@Override
	public List<? extends Package> findAll() {
		return getDataBase().findAll(Package.class, "select * from package");
	}

	@Override
	public Optional<? extends Package> searchById(Integer id) {
		return getDataBase().findOptional(Package.class, "select * from package where id = ?", id);
	}

	@Override
	public String save(Package pack) {
		getDataBase().update("insert into package (title) values (?, ?)", pack.title);
		return "Succes";
	}

}
