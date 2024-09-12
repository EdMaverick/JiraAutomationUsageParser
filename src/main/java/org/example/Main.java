package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Main {

    public static void main(String[] args) {

        PropertiesLoader propertiesLoader = new PropertiesLoader();
        String login = propertiesLoader.getLogin();
        String password = propertiesLoader.getPassword();

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String actualDate = currentDate.format(formatter);

        JiraParser jiraParser = new JiraParser();
        jiraParser.parse(login, password);

        String jira = MessageBuilder.build("Jira",
                jiraParser.getJiraLimit(),
                jiraParser.getJiraCurrentUsage(),
                jiraParser.getJiraMonthlyPercentage(),
                currentDate.getDayOfMonth());

        String jsm = MessageBuilder.build("JSM",
                jiraParser.getJsmLimit(),
                jiraParser.getJsmCurrentUsage(),
                jiraParser.getJsmMonthlyPercentage(),
                currentDate.getDayOfMonth());

        MailSender.send(
                login,
                password,
                "itsupport@solbeg.com",
                "Jira Automation Usage Report (" + actualDate + ")",
                 jira + jsm);
    }
}
