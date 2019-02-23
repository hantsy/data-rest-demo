package com.example.demo.web;

import com.example.demo.PostNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.http.ResponseEntity.notFound;

@ControllerAdvice(basePackageClasses = WebExceptionHandler.class)
@Slf4j
public class WebExceptionHandler {

    @ExceptionHandler(PostNotFoundException.class)
    public ModelAndView postNotFound(PostNotFoundException ex, WebRequest request) {
        log.debug("handling PostNotFoundException:{}" + ex.getMessage());
        ModelAndView mav = new ModelAndView();
        mav.addObject("error", ex.getMessage());
        mav.setViewName("error");
        return mav;
    }
}
