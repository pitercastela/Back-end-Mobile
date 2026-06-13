package com.example.demo.Inventory;

import com.example.demo.DaoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryDao inventoryDao;

    @Autowired
    public InventoryController(DaoFactory daoFactory) {
        this.inventoryDao = (InventoryDao) daoFactory.criarDao(InventoryDao.class);
    }

    // ADICIONAR ITEM AO INVENTÁRIO (Usado na ShopActivity do Android)
    @PostMapping
    public ResponseEntity<Inventory> criar(
            @RequestParam Integer characterId,
            @RequestParam Integer itemCatalogId,
            @RequestParam(defaultValue = "false") Boolean isEquipped) {

        Inventory inventory = Inventory.builder()
                .characterId(characterId)
                .itemCatalogId(itemCatalogId)
                .isEquipped(isEquipped)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryDao.salvar(inventory));
    }

    // LISTAR TUDO NO BANCO (Apenas para debug)
    @GetMapping
    public ResponseEntity<List<Inventory>> listar() {
        return ResponseEntity.ok(inventoryDao.listarTodos());
    }

    // BUSCAR UM REGISTRO ESPECÍFICO
    @GetMapping("/{id}")
    public ResponseEntity<Inventory> buscar(@PathVariable Integer id) {
        Inventory invEncontrado = inventoryDao.buscar(id);
        if (invEncontrado != null) {
            return ResponseEntity.ok(invEncontrado);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // ATUALIZAR INVENTÁRIO (Usado quando o jogador aperta o Switch de "Equipar/Equipado")
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(
            @PathVariable Integer id,
            @RequestParam Integer characterId,
            @RequestParam Integer itemCatalogId,
            @RequestParam Boolean isEquipped) {

        Inventory inventory = Inventory.builder()
                .id(id)
                .characterId(characterId)
                .itemCatalogId(itemCatalogId)
                .isEquipped(isEquipped)
                .build();

        boolean sucesso = inventoryDao.atualizar(inventory);

        if (sucesso) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // REMOVER ITEM (Usado quando o jogador clica na lixeira)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        boolean sucesso = inventoryDao.deletar(id);

        if (sucesso) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Rota: GET /api/inventory/character/{characterId}
    @GetMapping("/character/{characterId}")
    public ResponseEntity<List<InventoryItemDTO>> buscarPorPersonagem(@PathVariable Integer characterId) {
        // Agora chamamos o método que traz os dados completos!
        List<InventoryItemDTO> itens = inventoryDao.buscarInventarioCompleto(characterId);
        return ResponseEntity.ok(itens);
    }

    @PutMapping("/{inventoryId}/equip")
    public ResponseEntity<Void> toggleEquip(
            @PathVariable Integer inventoryId,
            @RequestParam Boolean isEquipped) {

        boolean atualizado = inventoryDao.atualizarStatusEquipado(inventoryId, isEquipped);

        if (atualizado) {
            return ResponseEntity.ok().build(); // Retorna 200 OK sem corpo (Void)
        } else {
            return ResponseEntity.notFound().build(); // Retorna 404 se o item não existir
        }
    }
}