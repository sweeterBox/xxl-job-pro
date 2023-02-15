package com.xxl.job.admin.notify;

import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sweeter
 * @date 2022/11/24
 */
public class MailNotifier extends AbstractEventNotifier {

    private static final String DEFAULT_MESSAGE = String.join(System.lineSeparator(),
            "<h5>任务：#{event.task.description} </h5>",
            "<h5>客户端实例地址：#{event.log.instanceUrl}</h5>",
            "<h5>调度状态：#{event.log.triggerStatus} </h5>",
            "<h5>执行状态：#{event.log.handleStatus}</h5>"
    );

    private final SpelExpressionParser parser = new SpelExpressionParser();

    private Expression message;

    private final JavaMailSender javaMailSender;

    /**
     * recipients of the mail
     */
    private String[] to = { "admin@localhost" };

    /**
     * cc-recipients of the mail
     */
    private String[] cc = {};

    private String title = "XXL-JOB Pro Admin MailNotifier";

    /**
     * sender of the change
     */
    private String from = "XXL-JOB Pro Admin <admin@localhost>";


   // private String template = "classpath:/META-INF/notify/mail/template.html";

    public MailNotifier(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
        this.message = this.parser.parseExpression(DEFAULT_MESSAGE, ParserContext.TEMPLATE_EXPRESSION);
    }
    @Override
    public void doNotify(Event event) {

        if (event instanceof TaskEvent) {
            try {
                String content = this.createContent((TaskEvent)event);
                MimeMessage message = this.javaMailSender.createMimeMessage();
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
                mimeMessageHelper.setFrom(this.from);
                mimeMessageHelper.setTo(this.to);
                mimeMessageHelper.setSubject(this.title);
                mimeMessageHelper.setText(content, true);
                javaMailSender.send(message);
            } catch (MessagingException ex) {
                throw new RuntimeException("Error sending mail notification", ex);
            }
        }

    }

    private String createContent(TaskEvent event) {
        Map<String, Object> root = new HashMap<>();
        root.put("event", event);
        StandardEvaluationContext context = new StandardEvaluationContext(root);
        context.addPropertyAccessor(new MapAccessor());
        return this.message.getValue(context, String.class);
    }

    public String getMessage() {
        return this.message.getExpressionString();
    }

    public void setMessage(String message) {
        this.message = this.parser.parseExpression(message, ParserContext.TEMPLATE_EXPRESSION);
    }


    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String[] getCc() {
        return cc;
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
