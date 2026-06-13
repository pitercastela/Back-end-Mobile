package com.example.demo.PlayerCharacter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class PlayerCharacter {

    private Integer id;
    private Integer userId;
    private String name;
    private String className;
    private Integer currentHp;
    private Integer maxHp;
    private Integer currentOmens = 2;
    private Integer maxOmens = 2;
    private Integer silver;

    private Integer strength;
    private Integer agility;
    private Integer presence;
    private Integer toughness;

    private String notes;
    private String conditions;
    private Timestamp createdAt;
}