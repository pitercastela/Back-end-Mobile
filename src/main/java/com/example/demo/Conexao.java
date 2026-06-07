package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static Connection instanciaConexao;

    private static final String URL = "jdbc:mysql://up4vyw7tvfazxwt0:eAhS6nnuRxmvnXn1QUDp@badhbeczpe2jfwpa7d5k-mysql.services.clever-cloud.com:3306/badhbeczpe2jfwpa7d5k";
    private static final String USUARIO = "up4vyw7tvfazxwt0";
    private static final String SENHA = "eAhS6nnuRxmvnXn1QUDp";

    private Conexao() {
    }

    public static Connection getInstanciaConexao(){
        try{
        if (instanciaConexao == null) {
            new Conexao();
            instanciaConexao = DriverManager.getConnection(URL, USUARIO, SENHA);
        }
    } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return instanciaConexao;
    }
}
