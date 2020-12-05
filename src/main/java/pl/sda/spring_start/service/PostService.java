package pl.sda.spring_start.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import pl.sda.spring_start.model.Category;
import pl.sda.spring_start.model.Post;
import pl.sda.spring_start.model.PostDto;
import pl.sda.spring_start.model.User;
import pl.sda.spring_start.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;

    public boolean addDislike(int postId, User hater){
        Optional<Post> postToDislikeOptional = getPostById(postId);
        if(postToDislikeOptional.isPresent()){                     // sprawdzam czy post istnieje
            Post postToDislike = postToDislikeOptional.get();
            // gdy jestem autorem lub już likeowałem tego posta to nie nie mogę hejtować
            if(postToDislike.getAuthor().equals(hater) || postToDislike.getLikes().contains(hater)){
                return false;
            }
            Set<User> currentDislikes = postToDislike.getDislikes();     // pobieram aktualne dislike-i
            if(!currentDislikes.add(hater)) {                             // gdy dodawanie zwraca true tzn, że nie byłem hejterem
                currentDislikes.remove(hater);                          // aktualizuję zbiór dislike-ów
            }                                                           // gdy byłem hejterem to usuwam dislike ze zbioru
            postToDislike.setDislikes(currentDislikes);
            postRepository.save(postToDislike);                    // UPDATE post SET ....
            return true;
        }
        return false;
    }
    public boolean addLike(int postId, User follower){
        Optional<Post> postToLikeOptional = getPostById(postId);
        if(postToLikeOptional.isPresent()){                     // sprawdzam czy post istnieje
            Post postToLike = postToLikeOptional.get();
            // gdy jestem autorem lub już hejtowałem tego posta to nie nie mogę like-ować
            if(postToLike.getAuthor().equals(follower) || postToLike.getDislikes().contains(follower)){
                return false;
            }
            Set<User> currentLikes = postToLike.getLikes();     // pobieram aktualne like-i
            if(!currentLikes.add(follower)) {                    // dodaje like-a
                currentLikes.remove(follower);
            }
            postToLike.setLikes(currentLikes);
            postRepository.save(postToLike);                    // UPDATE post SET ....
            return true;
        }
        return false;
    }

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

    public void deletePostById(int postId) {
        postRepository.deleteById(postId);
    }

    public void addPost(String title, String content, Category category, User author) {
        postRepository.save(new Post(title, content, LocalDateTime.now(), category, author));
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "dateAdded"));
    }
    public List<Integer> generatePagesIndexes(List<Post> posts){
        int noOfPages = (getAllPosts().size() / 5) + 1;
        List<Integer> pagesIndexes = new ArrayList<>();
        for (int i = 0; i < noOfPages; i++){
            pagesIndexes.add(i + 1);
        }
        return pagesIndexes;
    }

    public List<Post> getAllPosts(int pageIndex, Sort.Direction sortDirection, String sortFieldName) {
        Pageable pageable = PageRequest.of(pageIndex, 5, Sort.by(sortDirection, sortFieldName));
        Page<Post> postPage = postRepository.findAll(pageable);
        return postPage.getContent();
    }

    public List<Post> getPostsByCategory(Category category) {
        return postRepository.findAllByCategory(category, Sort.by(Sort.Direction.DESC, "dateAdded"));
    }

    public List<Post> getPostsByCategoryAndAuthor(Category category, User author) {
        return postRepository.findAllByCategoryAndAuthor(category, author, Sort.by(Sort.Direction.DESC, "dateAdded"));
    }

    public List<Post> getPostsByTitleLikeOrContentLike(String keyWord) {
        return postRepository.findAllByTitleLikeOrContentLike("%" + keyWord + "%", "%" + keyWord + "%");
    }

    public String getPostsStats() {
        String output = "{\n";
        // IT : 3
        // DS : 1
        for (Object[] row : postRepository.postStatistics()) {
            output += "\"" + Category.values()[(int) row[0]].getCategoryName() + "\" : " + row[1] + "\n";
        }
        output += "}";
        return output;
    }

    public Optional<Post> getPostById(int postId) {
        return postRepository.findById(postId);
    }

    public List<Post> getAllPostsOrderByResult(String sortDirection, int pageIndex){
        Pageable pageable = PageRequest.of(pageIndex, 5);
        if(sortDirection.equals("ASC")){
            return postRepository.findAllSortedByResultASC();
        } else {
            return postRepository.findAllSortedByResultDESC();
        }
    }

}