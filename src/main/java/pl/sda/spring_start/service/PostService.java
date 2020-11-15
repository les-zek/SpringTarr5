package pl.sda.spring_start.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.sda.spring_start.model.Category;
import pl.sda.spring_start.model.Post;
import pl.sda.spring_start.model.PostDto;
import pl.sda.spring_start.model.User;
import pl.sda.spring_start.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;

    public boolean editPost(int postId, PostDto postDto) {
        if (getPostById(postId).isPresent()) {
            Post post = getPostById(postId).get();
            post.setTitle(postDto.getTitle());
            post.setContent(postDto.getContent());
            post.setCategory(postDto.getCategory());
            // do rozważenia co z autorem posta ???
            postRepository.save(post);      // update
            return true;
        }
        return false;
    }
    public void deletePostById(int postId){
        postRepository.deleteById(postId);
    }
    public void addPost(String title, String content, Category category, User author){
        postRepository.save(new Post(title,content, LocalDateTime.now(), category, author));
    }
    public List<Post> getAllPosts(){
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "dateAdded"));
    }
    public List<Post> getPostsByCategory(Category category){
        return postRepository.findAllByCategory(category, Sort.by(Sort.Direction.DESC, "dateAdded"));
    }
    public List<Post> getPostsByCategoryAndAuthor(Category category, User author){
        return postRepository.findAllByCategoryAndAuthor(category,author,Sort.by(Sort.Direction.DESC, "dateAdded"));
    }
    public List<Post> getPostsByTitleLikeOrContentLike(String keyWord){
        return postRepository.findAllByTitleLikeOrContentLike("%"+keyWord+"%", "%"+keyWord+"%");
    }
    public String getPostsStats(){
        String output = "{\n";
        // IT : 3
        // DS : 1
        for(Object[] row : postRepository.postStatistics()){
            output += "\"" + Category.values()[(int) row[0]].getCategoryName() + "\" : " + row[1] + "\n";
        }
        output += "}";
        return output;
    }
    public Optional<Post> getPostById(int postId){
        return postRepository.findById(postId);
    }
}
