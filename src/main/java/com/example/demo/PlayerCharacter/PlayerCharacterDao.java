package com.example.demo.PlayerCharacter;

import com.example.demo.Conexao;
import com.example.demo.CrudDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerCharacterDao implements CrudDao<PlayerCharacter> {

    @Override
    public PlayerCharacter salvar(PlayerCharacter character) {
        String sql = "INSERT INTO characters (user_id, name, class_name, current_hp, max_hp, omens, silver, strength, agility, presence, toughness, notes, conditions) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = Conexao.getInstanciaConexao();

        // O Statement.RETURN_GENERATED_KEYS nos permite pegar o ID gerado automaticamente (AUTO_INCREMENT)
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, character.getUserId());
            stmt.setString(2, character.getName());
            stmt.setString(3, character.getClassName());
            stmt.setInt(4, character.getCurrentHp());
            stmt.setInt(5, character.getMaxHp());
            stmt.setInt(6, character.getOmens());
            stmt.setInt(7, character.getSilver());
            stmt.setInt(8, character.getStrength());
            stmt.setInt(9, character.getAgility());
            stmt.setInt(10, character.getPresence());
            stmt.setInt(11, character.getToughness());
            stmt.setString(12, character.getNotes());
            stmt.setString(13, character.getConditions());

            stmt.executeUpdate();

            // Pega o ID que o MySQL gerou e define no objeto antes de retorná-lo
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    character.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar personagem", e);
        }
        return character;
    }

    @Override
    public PlayerCharacter buscar(Integer id) {
        String sql = "SELECT * FROM characters WHERE id = ?";
        PlayerCharacter character = null;
        Connection conn = Conexao.getInstanciaConexao();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    character = PlayerCharacter.builder()
                            .id(rs.getInt("id"))
                            .userId(rs.getInt("user_id"))
                            .name(rs.getString("name"))
                            .className(rs.getString("class_name"))
                            .currentHp(rs.getInt("current_hp"))
                            .maxHp(rs.getInt("max_hp"))
                            .omens(rs.getInt("omens"))
                            .silver(rs.getInt("silver"))
                            .strength(rs.getInt("strength"))
                            .agility(rs.getInt("agility"))
                            .presence(rs.getInt("presence"))
                            .toughness(rs.getInt("toughness"))
                            .notes(rs.getString("notes"))
                            .conditions(rs.getString("conditions"))
                            .createdAt(rs.getTimestamp("created_at"))
                            .build();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar personagem", e);
        }
        return character;
    }

    @Override
    public List<PlayerCharacter> listarTodos() {
        List<PlayerCharacter> characters = new ArrayList<>();
        String sql = "SELECT * FROM characters";
        Connection conn = Conexao.getInstanciaConexao();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                characters.add(PlayerCharacter.builder()
                        .id(rs.getInt("id"))
                        .userId(rs.getInt("user_id"))
                        .name(rs.getString("name"))
                        .className(rs.getString("class_name"))
                        .currentHp(rs.getInt("current_hp"))
                        .maxHp(rs.getInt("max_hp"))
                        .omens(rs.getInt("omens"))
                        .silver(rs.getInt("silver"))
                        .strength(rs.getInt("strength"))
                        .agility(rs.getInt("agility"))
                        .presence(rs.getInt("presence"))
                        .toughness(rs.getInt("toughness"))
                        .notes(rs.getString("notes"))
                        .conditions(rs.getString("conditions"))
                        .createdAt(rs.getTimestamp("created_at"))
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar personagens", e);
        }
        return characters;
    }

    @Override
    public boolean atualizar(PlayerCharacter character) {
        String sql = "UPDATE characters SET name = ?, class_name = ?, current_hp = ?, max_hp = ?, omens = ?, silver = ?, strength = ?, agility = ?, presence = ?, toughness = ?, notes = ?, conditions = ? WHERE id = ?";
        Connection conn = Conexao.getInstanciaConexao();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, character.getName());
            stmt.setString(2, character.getClassName());
            stmt.setInt(3, character.getCurrentHp());
            stmt.setInt(4, character.getMaxHp());
            stmt.setInt(5, character.getOmens());
            stmt.setInt(6, character.getSilver());
            stmt.setInt(7, character.getStrength());
            stmt.setInt(8, character.getAgility());
            stmt.setInt(9, character.getPresence());
            stmt.setInt(10, character.getToughness());
            stmt.setString(11, character.getNotes());
            stmt.setString(12, character.getConditions());

            stmt.setInt(13, character.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar personagem", e);
        }
    }

    @Override
    public boolean deletar(Integer id) {
        String sql = "DELETE FROM characters WHERE id = ?";
        Connection conn = Conexao.getInstanciaConexao();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar personagem", e);
        }
    }

    // MÉTODO NOVO: Buscar todos os personagens de um usuário específico
    public List<PlayerCharacter> buscarPorUsuario(Integer userId) {
        List<PlayerCharacter> characters = new ArrayList<>();
        String sql = "SELECT * FROM characters WHERE user_id = ?";
        Connection conn = Conexao.getInstanciaConexao();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    characters.add(PlayerCharacter.builder()
                            .id(rs.getInt("id"))
                            .userId(rs.getInt("user_id"))
                            .name(rs.getString("name"))
                            .className(rs.getString("class_name"))
                            .currentHp(rs.getInt("current_hp"))
                            .maxHp(rs.getInt("max_hp"))
                            .omens(rs.getInt("omens"))
                            .silver(rs.getInt("silver"))
                            .strength(rs.getInt("strength"))
                            .agility(rs.getInt("agility"))
                            .presence(rs.getInt("presence"))
                            .toughness(rs.getInt("toughness"))
                            .notes(rs.getString("notes"))
                            .conditions(rs.getString("conditions"))
                            .createdAt(rs.getTimestamp("created_at"))
                            .build());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar personagens por usuário", e);
        }
        return characters;
    }
}