package com.example.demo.Inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class Inventory {

    private Integer id;
    private Integer characterId;
    private Integer itemCatalogId;
    private Boolean isEquipped;
}