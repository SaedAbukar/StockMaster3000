package org.stockmaster3000.stockmaster3000.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stockmaster3000.stockmaster3000.model.Nutritions;

@Repository
public interface NutritionsRepository extends JpaRepository<Nutritions, Long> {

}