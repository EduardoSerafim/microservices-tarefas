package br.com.fiap;

public enum EmailTemplates {

    TASK_CREATED("task-created", "Task Created"),
    ;

    private final String templateName;
    private final String subject;

    EmailTemplates(String templateName, String subject) {
        this.templateName = templateName;
        this.subject = subject;
    }

    public String getTemplateName() {
        return templateName;
    }

    public String getSubject() {
        return subject;
    }
}
