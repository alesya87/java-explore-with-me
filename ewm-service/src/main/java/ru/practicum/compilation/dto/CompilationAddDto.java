package ru.practicum.compilation.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CompilationAddDto {
    private Set<Long> events;
    private Boolean pinned;
    @NotBlank
    @Size(max = 50)
    private String title;
}