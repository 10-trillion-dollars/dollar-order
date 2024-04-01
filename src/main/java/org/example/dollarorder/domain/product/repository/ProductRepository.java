package org.example.dollarorder.domain.product.repository;


import org.example.dollarorder.domain.product.entity.Product;
import org.example.dollarorder.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {



    Page<Product> findAllByUserAndStateTrue(User user, Pageable pageable);

    Page<Product> findAllByStateTrue(Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseAndStateTrue(String search, Pageable pageable);

}
