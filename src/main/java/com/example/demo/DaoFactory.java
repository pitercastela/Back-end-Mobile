package com.example.demo;

import org.springframework.stereotype.Component;

@Component
public class DaoFactory {
    public <Tipo> Tipo criarDao(Class<Tipo> classeDao){
        try{
            return classeDao.getDeclaredConstructor().newInstance();
        } catch (Exception e){
            throw new RuntimeException("Erro ao criar a instância do DAO: " + classeDao, e);
        }
    }
}
