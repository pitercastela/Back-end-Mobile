package com.example.demo.ItemsCatalog;

import com.example.demo.DaoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
public class ItemsCatalogController {

    private final ItemsCatalogDao itemsCatalogDao;

    @Autowired
    public ItemsCatalogController(DaoFactory daoFactory) {
        this.itemsCatalogDao = (ItemsCatalogDao) daoFactory.criarDao(ItemsCatalogDao.class);
    }

    @PostMapping
    public ResponseEntity<ItemsCatalog> criar(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String itemType,
            @RequestParam(defaultValue = "0") Integer costInSilver) {

        ItemsCatalog item = ItemsCatalog.builder()
                .name(name)
                .description(description)
                .itemType(itemType)
                .costInSilver(costInSilver)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(itemsCatalogDao.salvar(item));
    }

    @GetMapping
    public ResponseEntity<List<ItemsCatalog>> listar() {
        return ResponseEntity.ok(itemsCatalogDao.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemsCatalog> buscar(@PathVariable Integer id) {
        ItemsCatalog itemEncontrado = itemsCatalogDao.buscar(id);
        if (itemEncontrado != null) {
            return ResponseEntity.ok(itemEncontrado);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(
            @PathVariable Integer id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String itemType,
            @RequestParam Integer costInSilver) {

        ItemsCatalog item = ItemsCatalog.builder()
                .id(id)
                .name(name)
                .description(description)
                .itemType(itemType)
                .costInSilver(costInSilver)
                .build();

        boolean sucesso = itemsCatalogDao.atualizar(item);

        if (sucesso) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        boolean sucesso = itemsCatalogDao.deletar(id);

        if (sucesso) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}