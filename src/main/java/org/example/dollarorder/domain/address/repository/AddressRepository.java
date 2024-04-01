package org.example.dollarorder.domain.address.repository;


import java.util.List;
import org.example.dollarorder.domain.address.entity.Address;
import org.example.dollarorder.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAllByUser(User user);
}
