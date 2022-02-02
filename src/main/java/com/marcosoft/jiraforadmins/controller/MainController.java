package com.marcosoft.jiraforadmins.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcosoft.jiraforadmins.model.JiraIssue;
import com.marcosoft.jiraforadmins.model.OperationResult;
import com.marcosoft.jiraforadmins.model.OperationStatus;
import com.marcosoft.jiraforadmins.model.ResponseObject;
import com.marcosoft.jiraforadmins.service.JiraService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class MainController {

    @Autowired
    JiraService jiraService;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/apiStatus")
    public ResponseEntity<ResponseObject> apiStatus(){
        return ResponseEntity.ok(new ResponseObject(OperationStatus.SUCCESS,"API is UP and Running.",null));
    }


    @PostMapping("/run")
    public ResponseEntity<ResponseObject> run(@RequestParam String fieldToUpdate, @RequestParam String newValue, @RequestBody String issuesToUpdate){
        ResponseObject responseObject = new ResponseObject();

        List<JiraIssue> issueKeyList = new ArrayList<>();

        try {
            issueKeyList = objectMapper.readValue(issuesToUpdate, new TypeReference<List<JiraIssue>>(){});
            List<OperationResult> operationResultList = jiraService.update(fieldToUpdate,newValue,issueKeyList);

            if(operationResultList.stream().anyMatch(x -> x.getStatus() == OperationStatus.FAILURE)){
                responseObject.setStatus(OperationStatus.FAILURE);
                responseObject.setMessage("At least one error were found.");
            }else{
                responseObject.setStatus(OperationStatus.SUCCESS);
                responseObject.setMessage("Operation succeed!");
            }

            responseObject.setOperationResultList(operationResultList);

        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }

        return ResponseEntity.ok(responseObject);

    }





}
