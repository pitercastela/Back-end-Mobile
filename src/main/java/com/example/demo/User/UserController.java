package com.example.demo.User;

import com.example.demo.DaoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserDao userDao;

    @Autowired
    public UserController(DaoFactory daoFactory) {
        this.userDao = (UserDao) daoFactory.criarDao(UserDao.class);
    }

    @PostMapping
    public ResponseEntity<User> criar(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String passwordHash) {

        User user = User.builder()
                .username(username)
                .email(email)
                .passwordHash(passwordHash)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(userDao.salvar(user));
    }

    @GetMapping
    public ResponseEntity<List<User>> listar() {
        return ResponseEntity.ok(userDao.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> buscar(@PathVariable Integer id) {
        User userEncontrado = userDao.buscar(id);
        if (userEncontrado != null) {
            return ResponseEntity.ok(userEncontrado);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(
            @PathVariable Integer id,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String passwordHash) {

        User user = User.builder()
                .id(id)
                .username(username)
                .email(email)
                .passwordHash(passwordHash)
                .build();

        boolean sucesso = userDao.atualizar(user);

        if (sucesso) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        boolean sucesso = userDao.deletar(id);

        if (sucesso) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // ROTA DE LOGIN
    @PostMapping("/login")
    public ResponseEntity<User> login(
            @RequestParam String email,
            @RequestParam String passwordHash) {

        User user = userDao.autenticar(email, passwordHash);

        if (user != null) {
            return ResponseEntity.ok(user); // 200 OK -> Login aprovado
        }
        // 401 UNAUTHORIZED -> Credenciais inválidas
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}