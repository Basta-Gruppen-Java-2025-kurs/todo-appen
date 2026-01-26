package se.bastagruppen.todo_appen.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDoListEntryRequestDto {
    @NotNull(message = "Summary can not be null")
    @NotBlank(message = "Summary can not be blank")
    private String summary;

    private String details;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;

    private Long parentId;
}
