package com.example.demo.PlayerCharacter;

import com.example.demo.DaoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/characters")
public class PlayerCharacterController {

    private final PlayerCharacterDao characterDao;

    @Autowired
    public PlayerCharacterController(DaoFactory daoFactory) {
        this.characterDao = (PlayerCharacterDao) daoFactory.criarDao(PlayerCharacterDao.class);
    }

    @PostMapping
    public ResponseEntity<PlayerCharacter> criar(
            @RequestParam Integer userId,
            @RequestParam(defaultValue = "Jogador Padrão") String name,
            @RequestParam(defaultValue = "Sem Classe") String className,
            @RequestParam(defaultValue = "10") Integer currentHp,
            @RequestParam(defaultValue = "10") Integer maxHp,
            @RequestParam(defaultValue = "2") Integer currentOmens,
            @RequestParam(defaultValue = "2") Integer maxOmens,
            @RequestParam(defaultValue = "0") Integer silver,
            @RequestParam(defaultValue = "0") Integer strength,
            @RequestParam(defaultValue = "0") Integer agility,
            @RequestParam(defaultValue = "0") Integer presence,
            @RequestParam(defaultValue = "0") Integer toughness,
            @RequestParam(required = false) String notes,
            @RequestParam(required = false) String conditions) {

        PlayerCharacter character = PlayerCharacter.builder()
                .userId(userId)
                .name(name)
                .className(className)
                .currentHp(currentHp)
                .maxHp(maxHp)
                .currentOmens(currentOmens)
                .maxOmens(maxOmens)
                .silver(silver)
                .strength(strength)
                .agility(agility)
                .presence(presence)
                .toughness(toughness)
                .notes(notes)
                .conditions(conditions)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(characterDao.salvar(character));
    }

    @GetMapping
    public ResponseEntity<List<PlayerCharacter>> listar() {
        return ResponseEntity.ok(characterDao.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerCharacter> buscar(@PathVariable Integer id) {
        PlayerCharacter characterEncontrado = characterDao.buscar(id);
        if (characterEncontrado != null) {
            return ResponseEntity.ok(characterEncontrado);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(
            @PathVariable Integer id,
            @RequestParam String name,
            @RequestParam String className,
            @RequestParam Integer currentHp,
            @RequestParam Integer maxHp,
            @RequestParam(defaultValue = "2") Integer currentOmens,
            @RequestParam(defaultValue = "2") Integer maxOmens,
            @RequestParam Integer silver,
            @RequestParam Integer strength,
            @RequestParam Integer agility,
            @RequestParam Integer presence,
            @RequestParam Integer toughness,
            @RequestParam(required = false) String notes,
            @RequestParam(required = false) String conditions) {

        // Como a atualização requer todos os dados para montar o objeto,
        // você pode buscar o original antes ou apenas confiar que o Front-end mandará tudo
        PlayerCharacter character = PlayerCharacter.builder()
                .id(id)
                .name(name)
                .className(className)
                .currentHp(currentHp)
                .maxHp(maxHp)
                .currentOmens(currentOmens)
                .maxOmens(maxOmens)
                .silver(silver)
                .strength(strength)
                .agility(agility)
                .presence(presence)
                .toughness(toughness)
                .notes(notes)
                .conditions(conditions)
                .build();

        boolean sucesso = characterDao.atualizar(character);

        if (sucesso) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        boolean sucesso = characterDao.deletar(id);

        if (sucesso) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // ROTA NOVA: Expor a lista de personagens pelo ID do usuário
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlayerCharacter>> listarPorUsuario(@PathVariable Integer userId) {
        List<PlayerCharacter> characters = characterDao.buscarPorUsuario(userId);
        return ResponseEntity.ok(characters);
    }
}