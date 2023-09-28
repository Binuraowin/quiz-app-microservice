package com.example.questionservive.service;

import com.example.questionservive.dao.QuestionDao;
import com.example.questionservive.model.QuestionWrapper;
import com.example.questionservive.model.Questions;
import com.example.questionservive.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;
    public ResponseEntity<List<Questions>> getAllQuestions() {
        try {
            return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Questions>> getQuestionsByCategory(String category) {
        try {
            return new ResponseEntity<>(questionDao.findByCategory(category), HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> addQuestion(Questions question) {
        try {
            return new ResponseEntity<>(String.valueOf(questionDao.save(question)), HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Errpr", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Integer>> getQuestionForQuiz(String categoryName, Integer numQuestion) {

        List<Integer> questions = questionDao.findRandomQuestionsByCategory(categoryName,numQuestion);
        return new ResponseEntity<>(questions,HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionIds) {
        List<QuestionWrapper> wrappers = new ArrayList<>();
        List<Questions> questions = new ArrayList<>();

        for(Integer id: questionIds){
            questions.add(questionDao.findById(id).get());
        }

        for (Questions question: questions){
            QuestionWrapper questionWrapper = new QuestionWrapper();
            questionWrapper.setId(question.getId());
            questionWrapper.setQuestionTitle(question.getTitle());
            questionWrapper.setOption1(question.getOption1());
            questionWrapper.setOption2(question.getOption2());
            questionWrapper.setOption3(question.getOption3());
            questionWrapper.setOption4(question.getOption4());
            wrappers.add(questionWrapper);
        }
        return new ResponseEntity<>(wrappers,HttpStatus.OK);

    }

    public ResponseEntity<Integer> getScore(List<Response> responses) {

        int right = 0;
        for(Response response : responses){
            Questions questions = questionDao.findById(response.getId()).get();
            if(response.getResponse().equals(questions.getRightAnswer()))
                right++;
        }
        return new ResponseEntity<>(right, HttpStatus.OK);
    }
}
