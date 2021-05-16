package com.itproger.blog.controllers;

import com.itproger.blog.models.Post;
import com.itproger.blog.repo.PostRepository;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/blog") //Обрабатывает запросы начинающиеся с /blog
public class BlogController {
    @Autowired
    private PostRepository postRepository;

    @GetMapping("")
    public String blogMain(Model model) {
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "blog-main";
    }

    // страница добавления статьи
    @GetMapping("/add")
    public String blogAdd(Model model) {
        return "blog-add";
    }

    //Добавление статьи
    @PostMapping("/add")
    public String blogPostAdd(@RequestParam String title, @RequestParam String anons, @RequestParam String fullText) {
        Post post = new Post(title, anons, fullText);
        postRepository.save(post);
        return "redirect:/blog";
    }

    //Вывод статьи по id
    @GetMapping("/{id}")
    public String articleDetails(@PathVariable(value = "id") long postId, Model model) {
        if (!postRepository.existsById(postId)) {
            return "redirect:/blog";
        }

        Optional<Post> post = postRepository.findById(postId);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);
        return "blog-details";
    }

    //Вывод view о изменении статьи
    @GetMapping("/{id}/edit")
    public String blogEdit(@PathVariable(value = "id") long id, Model model) {
        if (!postRepository.existsById(id)) {
            return "redirect:/blog";
        }

        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);
        return "blog-edit";
    }

    //Редактирование статьи по id
    @PostMapping("/{id}/edit")
    public String blogPostUpdate(@PathVariable(value = "id") long id,
                                 @RequestParam String title, @RequestParam String anons, @RequestParam String full_text, // Изменяемые переменный
                                 Model model) {

        Post post = postRepository.findById(id).orElseThrow(); // Находим нужную запись по ID

        post.setTitle(title);
        post.setAnons(anons);
        post.setFullText(full_text); // Устанавливаем новое значение

        postRepository.save(post); // Сохраняем (обновляем) запись

        return "redirect:/blog"; // Возвращаем на главную страницу
    }

    //Вывод view об удалении
    @GetMapping("/{id}/remove")
    public String blogRemove(@PathVariable(value = "id") long id, Model model) {
        if (!postRepository.existsById(id)) {
            return "redirect:/blog";
        }

        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);
        return "blog-edit";
    }

    // Отслеживаем POST данные по определенному URL адресу
    @PostMapping("/{id}/remove")
    public String blogPostDelete(@PathVariable(value = "id") long id, Model model) {
        // Находим нужную запись по ID
        Post post = postRepository.findById(id).orElseThrow();
        postRepository.delete(post); // Выполняем удаление

        // Возвращаем на главную страницу
        return "redirect:/blog/";
    }
}
