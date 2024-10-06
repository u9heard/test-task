package ru.zotov.nbkitesttask.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table("users")
public class User {
    @Id
    @Column("id")
    private Long id;

    @Column("first_name")
    private String firstName;

    @Column("surname")
    private String surname;

    @Column("email")
    private String email;

    @Column("age")
    private Integer age;
}
