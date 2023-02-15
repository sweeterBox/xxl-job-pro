package com.xxl.job.admin.notify;

import com.xxl.job.utils.JsonUtils;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Nullable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sweeter
 * @date 2022/11/24
 */
public class WebhookNotifier extends AbstractEventNotifier {

    private static final String DEFAULT_MESSAGE = "*#{instance.registration.name}* (#{instance.id}) is *#{event.statusInfo.status}*";

    private final SpelExpressionParser parser = new SpelExpressionParser();

    private RestTemplate restTemplate;

    private Expression message;

    /**
     * Webhook URI
     */
    @Nullable
    private URI webhookUrl;

    /**
     * If the message is a text to speech message. False by default.
     */
    private boolean tts = false;


    public WebhookNotifier(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.message = parser.parseExpression(DEFAULT_MESSAGE, ParserContext.TEMPLATE_EXPRESSION);
    }

    @Override
    public void doNotify(Event event) {
        if (webhookUrl == null) {
            throw new IllegalStateException("'webhookUrl' must not be null.");
        }
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(webhookUrl, createDiscordNotification((TaskEvent) event), String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Object createDiscordNotification(TaskEvent event) {
        Map<String, Object> body = new HashMap<>();
        body.put("content", event);
        body.put("tts", tts);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.USER_AGENT, "XXL-JOB Pro RestTemplate");
        return new HttpEntity<>(body, headers);
    }

    @Nullable
    protected String createContent(TaskEvent event) {
        Map<String, Object> root = new HashMap<>();
        root.put("event", event);
        StandardEvaluationContext context = new StandardEvaluationContext(root);
        context.addPropertyAccessor(new MapAccessor());
        return message.getValue(context, String.class);
    }

    @Nullable
    public URI getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(@Nullable URI webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public boolean isTts() {
        return tts;
    }

    public void setTts(boolean tts) {
        this.tts = tts;
    }


    public String getMessage() {
        return message.getExpressionString();
    }

    public void setMessage(String message) {
        this.message = parser.parseExpression(message, ParserContext.TEMPLATE_EXPRESSION);
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
