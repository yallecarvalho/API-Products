package com.example.springboot.repositories;

import com.example.springboot.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
//Ao usar o JpaRepository, por default o Spring já reconhece que essa interface vai ser um bean do tipo repository
public interface ProductRepository extends JpaRepository<ProductModel, UUID> {

}
