package com.courzelo.courzelo_core.auth.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Document(collection = "database_sequences")
@Getter
@Setter
@NoArgsConstructor
public class DatabaseSequence {

    @Id
    private String id;

    private long seq;

  
}