package com.example.demo.ItemsCatalog;

import com.example.demo.Conexao;
import com.example.demo.CrudDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemsCatalogDao implements CrudDao<ItemsCatalog> {

    @Override
    public ItemsCatalog salvar(ItemsCatalog item) {
        String sql = "INSERT INTO items_catalog (name, description, item_type, cost_in_silver) VALUES (?, ?, ?, ?)";
        Connection conn = Conexao.getInstanciaConexao();

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setString(3, item.getItemType());
            stmt.setInt(4, item.getCostInSilver());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    item.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar item no catálogo", e);
        }
        return item;
    }

    @Override
    public ItemsCatalog buscar(Integer id) {
        String sql = "SELECT * FROM items_catalog WHERE id = ?";
        ItemsCatalog item = null;
        Connection conn = Conexao.getInstanciaConexao();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    item = ItemsCatalog.builder()
                            .id(rs.getInt("id"))
                            .name(rs.getString("name"))
                            .description(rs.getString("description"))
                            .itemType(rs.getString("item_type"))
                            .costInSilver(rs.getInt("cost_in_silver"))
                            .build();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar item no catálogo", e);
        }
        return item;
    }

    @Override
    public List<ItemsCatalog> listarTodos() {
        List<ItemsCatalog> items = new ArrayList<>();
        String sql = "SELECT * FROM items_catalog";
        Connection conn = Conexao.getInstanciaConexao();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                items.add(ItemsCatalog.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .itemType(rs.getString("item_type"))
                        .costInSilver(rs.getInt("cost_in_silver"))
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar catálogo de itens", e);
        }
        return items;
    }

    @Override
    public boolean atualizar(ItemsCatalog item) {
        String sql = "UPDATE items_catalog SET name = ?, description = ?, item_type = ?, cost_in_silver = ? WHERE id = ?";
        Connection conn = Conexao.getInstanciaConexao();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setString(3, item.getItemType());
            stmt.setInt(4, item.getCostInSilver());

            stmt.setInt(5, item.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar item no catálogo", e);
        }
    }

    @Override
    public boolean deletar(Integer id) {
        String sql = "DELETE FROM items_catalog WHERE id = ?";
        Connection conn = Conexao.getInstanciaConexao();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar item do catálogo", e);
        }
    }
}