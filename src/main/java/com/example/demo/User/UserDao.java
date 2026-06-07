package com.example.demo.User;

import com.example.demo.Conexao;
import com.example.demo.CrudDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements CrudDao<User> {

    @Override
    public User salvar(User user) {
        String sql = "INSERT INTO users (username, email, password_hash) VALUES (?, ?, ?)";
        Connection conn = Conexao.getInstanciaConexao();

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPasswordHash());

            stmt.executeUpdate();

            // Pega o ID gerado pelo banco e injeta no objeto
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar usuário", e);
        }
        return user;
    }

    @Override
    public User buscar(Integer id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        User user = null;
        Connection conn = Conexao.getInstanciaConexao();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = User.builder()
                            .id(rs.getInt("id"))
                            .username(rs.getString("username"))
                            .email(rs.getString("email"))
                            .passwordHash(rs.getString("password_hash"))
                            .createdAt(rs.getTimestamp("created_at"))
                            .build();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário", e);
        }
        return user;
    }

    @Override
    public List<User> listarTodos() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        Connection conn = Conexao.getInstanciaConexao();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(User.builder()
                        .id(rs.getInt("id"))
                        .username(rs.getString("username"))
                        .email(rs.getString("email"))
                        .passwordHash(rs.getString("password_hash"))
                        .createdAt(rs.getTimestamp("created_at"))
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários", e);
        }
        return users;
    }

    @Override
    public boolean atualizar(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, password_hash = ? WHERE id = ?";
        Connection conn = Conexao.getInstanciaConexao();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPasswordHash());

            stmt.setInt(4, user.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário", e);
        }
    }

    @Override
    public boolean deletar(Integer id) {
        String sql = "DELETE FROM users WHERE id = ?";
        Connection conn = Conexao.getInstanciaConexao();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar usuário", e);
        }
    }

    // Método para autenticar o login
    public User autenticar(String email, String passwordHash) {
        String sql = "SELECT * FROM users WHERE email = ? AND password_hash = ?";
        Connection conn = Conexao.getInstanciaConexao();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, passwordHash);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return User.builder()
                            .id(rs.getInt("id"))
                            .username(rs.getString("username"))
                            .email(rs.getString("email"))
                            .passwordHash(rs.getString("password_hash"))
                            .build();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao autenticar usuário", e);
        }
        return null; // Retorna nulo se o email/senha estiverem errados
    }
}