package com.example.springboot.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;


/* Serializable é uma interface que mostra pra JVM que a classe está habilitada a passar por serializações. 
É uma inteface de marcação, não tem nenhum método pra implementar 
@Entity e @Table são anotações para mapear a classe Java pro BD.

Extensão de representationModel -> Hateoas , quando for utilizar o productModel, agora ele tem alguns metodos pra utilizar pra construir os links pra navegabilidade
*/
@Entity
@Table(name = "TB PRODUCTS")
public class ProductModel extends RepresentationModel<ProductModel> implements Serializable{
    /* É o numero de controle de versão de cada uma dessas classes que 
    forem serializadas quando houver necessidade */
    private static final long serialVersionUID = 1L; 

    /* Atributos do nosso Model, que são as colunas da tabela. 
    Identificador - Id da tabela @Id e @GeneratedValue demonstra como vão ser gerados os identificadores.
    Eles vao sendo gerados e salvos */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    /* Identificador do tipo UUID, muito utilizado em arquiteturas distribuidas, como arquitetura
     de microservices. UUIDs são universais e podem ser gerados em qualquer local */
    private UUID idProduct; 
    private String name;
    private BigDecimal value;
    
    //Geração automatica dos Gets and setters
    public UUID getIdProduct() {
        return idProduct;
    }
    public void setIdProduct(UUID idProduct) {
        this.idProduct = idProduct;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }
    public void setValue(BigDecimal value) {
        this.value = value;
    }
    
    
    
    
    
    

    
    
}
