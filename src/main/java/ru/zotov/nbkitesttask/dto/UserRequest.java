package ru.zotov.nbkitesttask.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
//    @NotBlank
//    @Size(min = 2, max = 50)
    private String firstName;

//    @NotBlank
//    @Size(min = 2, max = 50)
    private String surname;

//    @NotBlank
//    @Size(min = 2, max = 256)
    @Getter
    private String email;

//    @NotNull
    private Integer age;
}
