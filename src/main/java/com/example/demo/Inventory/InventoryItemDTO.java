package com.example.demo.Inventory; // Ajuste o pacote se necessário

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryItemDTO {
    private Integer inventoryId;
    private Integer itemCatalogId;
    private String name;
    private String description;
    private String itemType;
    private Boolean isEquipped;
}