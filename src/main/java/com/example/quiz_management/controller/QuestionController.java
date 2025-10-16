package com.example.quiz_management.controller;
import com.example.quiz_management.entity.*;
import com.example.quiz_management.repository.*;
import com.example.quiz_management.service.QuestionService;
import com.example.quiz_management.service.QuizService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
//@RequestMapping("/admin/questions")
public class QuestionController {

    private QuestionService questionService;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final ChoiceRepository choiceRepository;


    @GetMapping("/admin/questions")
    public String listQuestions(Model model) {
        List<Question> questions = questionService.getAllQuestions();
        model.addAttribute("questions", questions);
        return "questions";
    }

    // Show Create Question Form
    @GetMapping("/admin/questions/new")
    public String showCreateForm(Model model) {
        Question question = new Question();
        question.setChoices(new ArrayList<>());

        // Add 4 empty choices by default
        for (int i = 0; i < 4; i++) {
            question.getChoices().add(new Choice());
        }

        model.addAttribute("question", question);
        model.addAttribute("quizzes", quizRepository.findAll());
        return "question-form";
    }

    // Save Question and Choices
    @PostMapping("/admin/questions/save")
    public String saveQuestion(@ModelAttribute("question") Question question, Model model,
                               BindingResult result) {
        if(result.hasErrors()){
            return "question-form";
        }
        try {
            questionService.save(question);
        } catch (DataIntegrityViolationException e) {
            return "question-form";
        }
        model.addAttribute("question", question);
        model.addAttribute("quizzes", quizRepository.findAll());
        return "redirect:/admin/questions";

    }

    @GetMapping("/admin/questions/delete/{id}")
    public String deleteQuiz(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return "redirect:/admin/questions";
    }

}
