package com.example.quiz_management.controller;

import com.example.quiz_management.entity.Quiz;
import com.example.quiz_management.service.QuizService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@AllArgsConstructor
public class QuizController {

    private QuizService quizService;

    @GetMapping("/admin/quiz")
    public String showAllQuizzes(Model model) {
        List<Quiz> quizzes = quizService.getAllQuizzes();
        model.addAttribute("quizzes", quizzes);
        return "quizzes"; // refers to templates
    }


    @GetMapping("/admin/quiz/create")
    public String showCreateForm(Model model) {
        model.addAttribute("quiz", new Quiz());
        return "quiz-form";
    }

    @PostMapping("/admin/quiz/save")
    public String saveQuiz(@Valid @ModelAttribute("quiz") Quiz quiz,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            // Return to form if validation fails
            return "quiz-form";
        }
        quiz.setCreatedAt(LocalDateTime.now());
        quizService.saveQuiz(quiz);
        return "redirect:/admin/quiz";
    }

    // Admin: edit movie form
    @GetMapping("/admin/quiz/edit/{id}")
    public String editQuizForm(@PathVariable Long id, Model model) {
        model.addAttribute("quiz", quizService.getQuizById(id));
        return "quiz-form";
    }

    // Admin: update movie
    @PostMapping("/admin/quiz/edit/{id}")
    public String updateQuiz(@PathVariable Long id, @ModelAttribute("quiz") Quiz quiz)
            throws IOException {
        quizService.updateQuiz(id,quiz);
        return "redirect:/admin/quiz";
    }

    // Admin: delete movie
    @GetMapping("/admin/quiz/delete/{id}")
    public String deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return "redirect:/admin/quiz";
    }

}
