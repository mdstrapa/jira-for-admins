package com.marcosoft.jiraforadmins.service;

import com.marcosoft.jiraforadmins.client.JiraClient;
import com.marcosoft.jiraforadmins.model.JiraIssue;
import com.marcosoft.jiraforadmins.model.OperationResult;
import com.marcosoft.jiraforadmins.model.OperationStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class JiraService {

    @Autowired
    JiraClient jiraClient;

    private void showOperationSummary(String fieldToUpdate, String newValue, List<JiraIssue> jiraIssues){

        log.info("Operation Summary ====================================================");
        log.info("The field '{}' is going to receive the value '{}'.",fieldToUpdate,newValue );
        log.info("{} issues will be affected:",jiraIssues.size());
        jiraIssues.forEach(i -> log.info("Issue Key: {}",i.getKey()));
        log.info("End ==================================================================");

    }

    public List<OperationResult> update(String fieldToUpdate, String newValue, List<JiraIssue> jiraIssues){
        showOperationSummary(fieldToUpdate,newValue,jiraIssues);

        List<OperationResult> operationResultList = new ArrayList<>();

        String requestBody = "{ \"fields\" : { \"" + fieldToUpdate + "\" : \"" + newValue + "\" } } ";

        HttpResponse httpResponse;

        for (JiraIssue jiraIssue:jiraIssues) {

            httpResponse = jiraClient.sendJiraRequest(jiraClient.buildJiraRequest("PUT","issue/" + jiraIssue.getKey(),requestBody));

            OperationResult operationResult = new OperationResult();

            operationResult.setJiraIssue(jiraIssue);

            if(httpResponse.statusCode() == 204) operationResult.setStatus(OperationStatus.SUCCESS);
            else{
                operationResult.setStatus(OperationStatus.FAILURE);
                operationResult.setMessage(httpResponse.body().toString());
            }

            operationResultList.add(operationResult);

        }

        return operationResultList;
    }


    public boolean create(){
        return true;
    }

}
