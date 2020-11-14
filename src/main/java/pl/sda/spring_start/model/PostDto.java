package pl.sda.spring_start.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostDto {
@NotBlank(message="Field can not be empty")
@Size(min=0, max=100, message = "Title must be shorter then {valuee}")
    private String title;
@NotBlank(message="Content can not be empty")
@Size(min=10, max=5000, message = "Size must be in {min} and {max}")
    private String content;
// @NotBlank(message = "Category can not be empty")
    private Category category;
}
