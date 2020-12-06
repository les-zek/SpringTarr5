package pl.sda.spring_start.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentDto {
    @NotBlank(message = "Comment must be not empty")
    @Size(min= 0, max = 100, message = "Comment must be no longer than {max}")
    private String message;
}