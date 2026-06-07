package com.example.demo;

import java.util.List;

public interface CrudDao<Tipo> {
    Tipo salvar(Tipo entidade);
    Tipo buscar(Integer id);
    List<Tipo> listarTodos();
    boolean atualizar(Tipo entidade);
    boolean deletar(Integer id);
}
