package ru.zotov.nbkitesttask.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class UserResponse {
    private Long id;
    private String firstName;
    private String surname;
    private String email;
    private Integer age;
}
