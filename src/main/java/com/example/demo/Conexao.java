package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static Connection instanciaConexao;

    // Credenciais do Clever Cloud
    private static final String URL = "jdbc:mysql://up4vyw7tvfazxwt0:eAhS6nnuRxmvnXn1QUDp@badhbeczpe2jfwpa7d5k-mysql.services.clever-cloud.com:3306/badhbeczpe2jfwpa7d5k";
    private static final String USUARIO = "up4vyw7tvfazxwt0";
    private static final String SENHA = "eAhS6nnuRxmvnXn1QUDp";

    private Conexao() {
    }

    // O "synchronized" impede colisão se duas requisições chegarem no mesmo milissegundo
    public static synchronized Connection getInstanciaConexao() {
        try {

            if (instanciaConexao == null || instanciaConexao.isClosed() || !instanciaConexao.isValid(2)) {

                instanciaConexao = DriverManager.getConnection(URL, USUARIO, SENHA);
                System.out.println("[DATABASE] Conexão com o Clever Cloud (MySQL) estabelecida.");
            }
        } catch (SQLException e) {
            System.err.println("[DATABASE ERRO] O abismo recusou a conexão.");
            throw new RuntimeException("Erro ao gerenciar conexão com o banco", e);
        }
        return instanciaConexao;
    }
}