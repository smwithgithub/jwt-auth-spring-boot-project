package com.codewithsm.springsecuritywithjwtauth.repository;

import com.codewithsm.springsecuritywithjwtauth.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product,Integer> {


}
