package com.marcosoft.jiraforadmins.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationResult {
    private JiraIssue jiraIssue;
    private OperationStatus status;
    private String message;
}
