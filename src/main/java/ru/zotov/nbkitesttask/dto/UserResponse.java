package ru.zotov.nbkitesttask.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Schema(description = "DTO передачи данных о пользователе")
public class UserResponse {
    @Schema(description = "ИД пользователя")
    private Long id;
    @Schema(description = "Имя пользователя")
    private String firstName;
    @Schema(description = "Фамилия пользователя")
    private String surname;
    @Schema(description = "Эл.почта пользователя")
    private String email;
    @Schema(description = "Возраст пользователя")
    private Integer age;
}
