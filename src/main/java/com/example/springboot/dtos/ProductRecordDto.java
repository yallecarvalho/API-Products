package com.example.springboot.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/* Adiciona os parametros que vao ser recebidos do cliente na entrada dos dados.
O Id não entra pois é gerado automaticamente. Usa-se as anotações @NotBlank e @NotNull pra definir
que o nome não poderá ser recebido em branco e o valor nulo. Ou seja, restrições no BD.
 */
public record ProductRecordDto(@NotBlank String name, @NotNull BigDecimal value) {
}
