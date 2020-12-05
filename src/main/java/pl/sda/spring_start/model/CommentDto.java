package pl.sda.spring_start.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Data
public class CommentDto {
    @NotBlank(message="Content can not be empty")
    @Size(min=10, max=5000, message = "Size must be in {min} and {max}")
    private String message;
}
