package com.example.springboot.controllers;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController  //Bean do Spring que tem a implementaçao de uma API REST
public class ProductController {

    @Autowired
    ProductRepository productRepository; //ponto de injeçao pra essa interface (pra ter acesso a todos os métodos JPA).
    /* Na hora que recebermos um POST de um client, esse método vai ser acionado pra responder
     a essa requisição. /products é a URI.
     */
    @PostMapping("/products")
    //retorna um ResponseEntity do tipo ProductModel. Recebe como corpo da solicitação HTTP via POST o ProductRecordDto (nome e valor)
    // @Valid pra fazer a validação entrar em vigor (validação feita no Dto - not null e not blank)
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
        ProductModel productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel); //conversão de Dto para Model - recebe o que vai ser convertido e em que tipo
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }

    /** Método de leitura. também vai ser acionado com a URI /products /
     * @return Lista de produtos com o status Ok, e o corpo dessa resposta vai
     * ter a lista de todos os recursos salovs da base de dados
     */
    @GetMapping("/products")
    public ResponseEntity<List<ProductModel>> getAllProducts(){
        List<ProductModel> productsList = productRepository.findAll();
        if(!productsList.isEmpty()){
            for(ProductModel product : productsList){
                UUID id = product.getIdProduct();
                //linkTo -> pra qual endpoint, pra qual metodo eu vou redirecionar meu cliente quando ele clicar neste link
                //methodOn -> qual controler que está esse metodo
                //withSelfRel -> direcionar pra cada um dos seus produtos
                product.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(productsList);
    }

    /**
     * Método que retorna um produto de acordo com o id recebido
     * URI composta de /products/{id} do produto que quero visualizar. ResponseEntity é Object pois vai
     * ter dois tipos de retorno. Dentro do método, @PathVariable usado pra passar o valor correto id
     * @param id (do tipo UUID - de ProductModel)
     * @return
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value="id") UUID id){
        //método JPA (findById), que faz select na base de dados com o id recebido. Clausúla WHERE
        // se ele achar, vai retornar como um productO do tipo Optional.
        Optional<ProductModel> productO = productRepository.findById(id);
        // se achar, faz checagem. caso esteja vazio, ele retorna a mensagem que o produto não foi encontrado
        if(productO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        //se não está vazio, o status vai ser OK, e no corpo vamos passar o produto, com o get por ser Optional.
        productO.get().add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("Products List"));
        return ResponseEntity.status(HttpStatus.OK).body(productO.get());
    }
    /*
    Parecido com o o getOne, precisa também passar o id. Recebe o id (pathVaraiable, e também vai receber o corpo da requisição
    (nome e valor a ser atualizados)
     */
    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value="id") UUID id, @RequestBody @Valid ProductRecordDto productRecordDto) {
        Optional <ProductModel> productO = productRepository.findById(id); //antes de atualizar, ve se o produto existe na base de dados
        if(productO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found"); // se não existe, retorna not found
        }
        var productModel = productO.get(); //se ele existe, vem pra essa linha. vai iniciar o productModel, atribuindo o valor que recebeu da base de dados
        BeanUtils.copyProperties(productRecordDto, productModel); //conversao do dto pra productModel
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel)); //resposta com ok e salvando o produto (com o mesmo ID).
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value="id") UUID id) {
        Optional <ProductModel> productO = productRepository.findById(id); //pesquisa na base se ele existe pra ser deletado
        if(productO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found"); // se não existe, retorna not found
        }
        productRepository.delete(productO.get());
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted sucessfully.");

    }
}

