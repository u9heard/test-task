package ru.zotov.nbkitesttask.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String firstName;
    private String surname;
    private String email;
    private Integer age;
}
