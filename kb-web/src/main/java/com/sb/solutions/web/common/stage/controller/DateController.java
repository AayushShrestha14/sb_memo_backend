package com.sb.solutions.web.common.stage.controller;

import java.text.ParseException;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.date.DateConverter;

/**
 * @author Rujan Maharjan on 6/9/2019
 */

@RestController
@AllArgsConstructor
@RequestMapping(value = DateController.URL)
public class DateController {

    static final String URL = "/v1/date";


    private DateConverter dateConverter;

    @GetMapping(path = "/nepali-date")
    public ResponseEntity<?> getNepDate(@RequestParam String date) throws ParseException {
        return new RestResponseDto().successModel(dateConverter.convertDate(date, "BS"));
    }
}
