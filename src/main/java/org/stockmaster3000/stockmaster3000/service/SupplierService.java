package org.stockmaster3000.stockmaster3000.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stockmaster3000.stockmaster3000.model.Supplier;
import org.stockmaster3000.stockmaster3000.repository.SupplierRepository;

import java.util.Optional;

@Service
public class SupplierService {
    private final SupplierRepository supplierRepository;

    // Constructor for dependency injection
    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public Optional<Supplier> findByName(String name) {
        return supplierRepository.findByName(name);
    }

    public Supplier save(Supplier supplier) {
        return supplierRepository.save(supplier);
    }
}
