package com.app.transporter.utility;

import org.dalesbred.Database;

import com.app.transporter.db.repositories.CourierRepository;
import com.app.transporter.db.repositories.PackageRepository;
import com.app.transporter.db.repositories.UniversalRepository;
import com.google.inject.AbstractModule;

public class DBBinder extends AbstractModule {
	
	private final Database db;
	
	public DBBinder(final Database db) {
		this.db = db;
	}
	
	@Override
	protected void configure() {
		UniversalRepository universalRepo = new UniversalRepository();
		CourierRepository courierRepo = new CourierRepository(db,"courier");
		PackageRepository packageRepo = new PackageRepository(db,"package");
		universalRepo.addRepository(courierRepo);
		universalRepo.addRepository(packageRepo);
		bind(CourierRepository.class).toInstance(courierRepo);
		bind(PackageRepository.class).toInstance(packageRepo);
		bind(UniversalRepository.class).toInstance(universalRepo);
		
	}

}
