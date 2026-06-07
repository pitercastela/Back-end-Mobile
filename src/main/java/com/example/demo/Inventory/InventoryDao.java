package com.example.demo.Inventory;

import com.example.demo.Conexao;
import com.example.demo.CrudDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDao implements CrudDao<Inventory> {

    @Override
    public Inventory salvar(Inventory inventory) {
        String sql = "INSERT INTO inventory (character_id, item_catalog_id, is_equipped) VALUES (?, ?, ?)";
        Connection conn = Conexao.getInstanciaConexao();

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, inventory.getCharacterId());
            stmt.setInt(2, inventory.getItemCatalogId());
            stmt.setBoolean(3, inventory.getIsEquipped());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    inventory.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar item ao inventário", e);
        }
        return inventory;
    }

    @Override
    public Inventory buscar(Integer id) {
        String sql = "SELECT * FROM inventory WHERE id = ?";
        Inventory inventory = null;
        Connection conn = Conexao.getInstanciaConexao();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    inventory = Inventory.builder()
                            .id(rs.getInt("id"))
                            .characterId(rs.getInt("character_id"))
                            .itemCatalogId(rs.getInt("item_catalog_id"))
                            .isEquipped(rs.getBoolean("is_equipped"))
                            .build();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar registro no inventário", e);
        }
        return inventory;
    }

    @Override
    public List<Inventory> listarTodos() {
        List<Inventory> inventories = new ArrayList<>();
        String sql = "SELECT * FROM inventory";
        Connection conn = Conexao.getInstanciaConexao();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                inventories.add(Inventory.builder()
                        .id(rs.getInt("id"))
                        .characterId(rs.getInt("character_id"))
                        .itemCatalogId(rs.getInt("item_catalog_id"))
                        .isEquipped(rs.getBoolean("is_equipped"))
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar inventário geral", e);
        }
        return inventories;
    }

    // MÉTODO EXTRA: Buscar apenas os itens de um personagem específico!
    public List<Inventory> listarPorPersonagem(Integer characterId) {
        List<Inventory> inventories = new ArrayList<>();
        String sql = "SELECT * FROM inventory WHERE character_id = ?";
        Connection conn = Conexao.getInstanciaConexao();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, characterId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    inventories.add(Inventory.builder()
                            .id(rs.getInt("id"))
                            .characterId(rs.getInt("character_id"))
                            .itemCatalogId(rs.getInt("item_catalog_id"))
                            .isEquipped(rs.getBoolean("is_equipped"))
                            .build());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar inventário do personagem", e);
        }
        return inventories;
    }

    @Override
    public boolean atualizar(Inventory inventory) {
        String sql = "UPDATE inventory SET character_id = ?, item_catalog_id = ?, is_equipped = ? WHERE id = ?";
        Connection conn = Conexao.getInstanciaConexao();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inventory.getCharacterId());
            stmt.setInt(2, inventory.getItemCatalogId());
            stmt.setBoolean(3, inventory.getIsEquipped());

            stmt.setInt(4, inventory.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar inventário", e);
        }
    }

    @Override
    public boolean deletar(Integer id) {
        String sql = "DELETE FROM inventory WHERE id = ?";
        Connection conn = Conexao.getInstanciaConexao();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar item do inventário", e);
        }
    }
}