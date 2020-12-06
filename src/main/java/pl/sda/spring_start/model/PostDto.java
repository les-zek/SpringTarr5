package pl.sda.spring_start.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostDto {  // DTO - data transfer object - obiekt wykorzystywany w formularzach do przekyzawania parametrów
    @NotBlank(message = "Title must be not empty")
    @Size(min= 0, max = 100, message = "Title must be no longer than {value}")
    private String title;
    @NotBlank(message = "Content must be not empty")
    @Size(min = 10, max = 5000, message = "Message must have number of characters between {min} and {max}")
    private String content;
    //    @NotBlank(message = "Category must be not empty")
    private Category category;
    private MultipartFile imagePath;
}