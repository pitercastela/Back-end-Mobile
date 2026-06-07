package com.example.demo.ItemsCatalog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ItemsCatalog {

    private Integer id;
    private String name;
    private String description;
    private String itemType; // Armas, Armaduras, Pergaminhos, Diversos
    private Integer costInSilver;
}