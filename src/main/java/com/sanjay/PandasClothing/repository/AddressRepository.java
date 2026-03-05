package com.sanjay.PandasClothing.repository;

import com.sanjay.PandasClothing.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUserId(Long userId);

    void deleteByUserId(Long userId);

}