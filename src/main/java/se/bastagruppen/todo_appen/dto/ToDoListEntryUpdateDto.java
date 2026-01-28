package se.bastagruppen.todo_appen.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
public class ToDoListEntryUpdateDto {
    @NotNull(message = "Summary can not be null")
    @NotBlank(message = "Summary can not be blank")
    private String summary;

    private String details;

    @NotNull(message = "Done can not be null")
    private Boolean done;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;
}
