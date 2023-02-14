package com.xxl.job.admin.notify;

import com.xxl.job.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

/**
 * @author sweeter
 * @date 2022/11/24
 */
@Slf4j
public class FeiShuNotifier extends AbstractEventNotifier {

    private static final String DEFAULT_MESSAGE = "任务：#{event.task.description} \n客户端实例地址：#{event.log.instanceUrl} \n调度状态：#{event.log.triggerStatus} 执行状态：#{event.log.handleStatus}";

    private final SpelExpressionParser parser = new SpelExpressionParser();

    private RestTemplate restTemplate;

    private Expression message;

    /**
     * Webhook URL for the FeiShu(飞书) chat group API (i.e. https://open.feishu.cn/open-apis/bot/v2/hook/xxx).
     */
    private URI webhookUrl;

    /**
     * @ all.
     */
    private boolean atAll = true;

    /**
     * The secret of the chat group robot from the FeiShu setup.
     */
    private String secret;

    /**
     * FeiShu message type: text(文本) interactive(消息卡片)
     */
    private MessageType messageType = MessageType.interactive;

    /**
     * Card theme message
     */
    private Card card = new Card();

    public FeiShuNotifier(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.message = this.parser.parseExpression(DEFAULT_MESSAGE, ParserContext.TEMPLATE_EXPRESSION);
    }

    @Override
    public void doNotify(Event event) {
        if (event instanceof TaskEvent) {
            this.restTemplate.postForEntity(this.webhookUrl, this.createNotification((TaskEvent) event), Void.class);
        }

    }


    private String generateSign(String secret, long timestamp) {
        try {
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(stringToSign.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(new byte[] {});
            return new String(Base64.getEncoder().encode(signData));
        }
        catch (Exception ex) {
            log.error("Description Failed to generate the Webhook signature of the FeiShu：{}", ex.getMessage());
        }
        return null;
    }

    protected HttpEntity<Map<String, Object>> createNotification(TaskEvent event) {
        Map<String, Object> body = new HashMap<>();
        body.put("receive_id", event.getId());
        if (StringUtils.hasText(this.secret)) {
            long timestamp = Optional.ofNullable(event.getTimestamp()).map(Instant::getEpochSecond).orElse(Instant.now().getEpochSecond());
            body.put("timestamp", timestamp);
            body.put("sign", this.generateSign(this.secret, timestamp));
        }
        body.put("msg_type", this.messageType);
        switch (this.messageType) {
            case interactive:
                body.put("card", this.createCardContent(event));
                break;
            case text:
                body.put("content", this.createTextContent(event));
                break;

            default:
                body.put("content", this.createTextContent(event));
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("User-Agent", "XXL-JOB Pro Admin FeiShuNotifier");
        return new HttpEntity<>(body, headers);
    }

    private String createContent(TaskEvent event) {
        Map<String, Object> root = new HashMap<>();
        root.put("event", event);
        StandardEvaluationContext context = new StandardEvaluationContext(root);
        context.addPropertyAccessor(new MapAccessor());
        return this.message.getValue(context, String.class);
    }

    private String createTextContent(TaskEvent event) {
        Map<String, Object> textContent = new HashMap<>();
        String content = this.createContent(event);
        if (this.atAll) {
            content += "\n<at user_id=\"all\">@all</at>";
        }
        textContent.put("text", content);
        return JsonUtils.obj2Json(textContent);
    }

    private String createCardContent(TaskEvent event) {
        String content = this.createContent(event);
        Card card = this.card;

        Map<String, Object> header = new HashMap<>();
        header.put("template", StringUtils.hasText(card.getThemeColor()) ? "red" : card.getThemeColor());
        Map<String, String> titleContent = new HashMap<>();
        titleContent.put("tag", "plain_text");
        titleContent.put("content", card.getTitle());
        header.put("title", titleContent);

        List<Map<String, Object>> elements = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("tag", "div");

        Map<String, String> text = new HashMap<>();
        text.put("tag", "plain_text");
        text.put("content", content);
        item.put("text", text);
        elements.add(item);

        if (this.atAll) {
            Map<String, Object> atItem = new HashMap<>();
            atItem.put("tag", "div");
            Map<String, String> atText = new HashMap<>();
            atText.put("tag", "lark_md");
            atText.put("content", "<at id=all></at>");
            atItem.put("text", atText);
            elements.add(atItem);
        }
        Map<String, Object> cardContent = new HashMap<>();
        cardContent.put("header", header);
        cardContent.put("elements", elements);
        return JsonUtils.obj2Json(cardContent);
    }

    public URI getWebhookUrl() {
        return this.webhookUrl;
    }

    public void setWebhookUrl(URI webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public String getMessage() {
        return this.message.getExpressionString();
    }

    public void setMessage(String message) {
        this.message = this.parser.parseExpression(message, ParserContext.TEMPLATE_EXPRESSION);
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isAtAll() {
        return atAll;
    }

    public void setAtAll(boolean atAll) {
        this.atAll = atAll;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public enum MessageType {

        text, interactive;

    }

    public class Card {

        /**
         * This is header title.
         */
        private String title = "XXL-JOB Pro Admin FeiShuNotifier";

        private String themeColor = "red";

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getThemeColor() {
            return themeColor;
        }

        public void setThemeColor(String themeColor) {
            this.themeColor = themeColor;
        }

    }
}
