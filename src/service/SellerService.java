package service;

import java.time.LocalDate;
import java.util.List;

import model.entities.Seller;
import repository.SellerRepository;

public class SellerService {

	private SellerRepository repository;

	public SellerService(SellerRepository repository) {
		super();
		this.repository = repository;
	}

	public void insert(Seller seller) {
		if (seller == null) {
			throw new IllegalArgumentException("Seller cannot be null.");
		}
		if (seller.getName() == null || seller.getName().trim().isEmpty()) {
			throw new IllegalArgumentException("Seller name cannot be empty.");
		}
		if (seller.getEmail() == null || seller.getEmail().trim().isEmpty()) {
			throw new IllegalArgumentException("Seller email cannot be empty.");
		}
		if (!seller.getEmail().matches("^[\\w.+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
			throw new IllegalArgumentException("Seller email is invalid.");
		}
		if (seller.getBirthDate() == null) {
			throw new IllegalArgumentException("Seller birth date cannot be null.");
		}
		if (seller.getBirthDate().isAfter(LocalDate.now())) {
			throw new IllegalArgumentException("Seller birth date cannot be in the future.");
		}
		if (seller.getBirthDate().isAfter(LocalDate.now().minusYears(18))) {
			throw new IllegalArgumentException("Seller must be at least 18 years old.");
		}
		if (seller.getBaseSalary() == null || seller.getBaseSalary() < 1621) {
			throw new IllegalArgumentException(
					"Seller base salary must be at least equal to the national minimum salary.");
		}
		if (seller.getDepartment() == null || seller.getDepartment().getId() == null) {
			throw new IllegalArgumentException("Seller must be associated with a valid department.");
		}
		repository.insert(seller);
	}
	
	public List<Seller> findAll() {
		return repository.findAll();
	}

	public Seller findById(int id) {
		return repository.findById(id);
	}
	
	public void delete(int id) {
		repository.delete(id);
	}
}
