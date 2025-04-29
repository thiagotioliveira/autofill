package dev.thiagooliveira.poc_autofill_processor.infrastructure.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.thiagooliveira.poc_autofill_processor.config.props.FieldProperties;
import dev.thiagooliveira.poc_autofill_processor.config.props.FormProperties;
import dev.thiagooliveira.poc_autofill_processor.domain.exception.GenericErrorException;
import dev.thiagooliveira.poc_autofill_processor.domain.model.Form;
import dev.thiagooliveira.poc_autofill_processor.domain.model.RawDocument;
import dev.thiagooliveira.poc_autofill_processor.domain.processor.Processor;
import dev.thiagooliveira.poc_autofill_processor.domain.user.User;
import dev.thiagooliveira.poc_autofill_processor.infrastructure.processor.model.ChatRequest;
import dev.thiagooliveira.poc_autofill_processor.infrastructure.processor.model.ChatResponse;
import dev.thiagooliveira.poc_autofill_processor.infrastructure.processor.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenAIProcessor implements Processor {

    private static final String PROMPT_1 = "Ola, nao precisa me responda formalmente, gostaria apenas como resposta o json que siga o " +
            "seguinte schema: " +
            "customerName(String), " +
            "nif(String), " +
            "supplyAddress(String), " +
            "billingAddress(String), " +
            "cui(String), " +
            "cpe(String)" +
            "region(String)" +
            "geographicCoordinates(String, calcule a cordenada geografica com base campo 'supplyAddress')";

    private static Logger logger = LoggerFactory.getLogger(OpenAIProcessor.class);

    private final String model;
    private final int maxCompletions;
    private final int temperature;
    private final FormProperties formProperties;
    private final ObjectMapper objectMapper;
    private final OpenAIClient client;

    public OpenAIProcessor(String model,
                           String apiUrl,
                           String apiKey,
                           int maxCompletions,
                           int temperature,
                           FormProperties formProperties) {
        this.model = model;
        this.maxCompletions = maxCompletions;
        this.temperature = temperature;
        this.formProperties = formProperties;
        this.objectMapper = new ObjectMapper();
        this.client = new OpenAIClient(apiUrl, apiKey);
    }

    @Override
    public Map<String, String> process(User user, RawDocument document) {
        //logger.debug("raw pdf content: {}", document.value());
        List<Message> messages = new ArrayList<>();
        String prompt = PROMPT_1 +
                "\n\nUtilize a seguinte fonte de dado abaixo:" +
                "\n\n" + document.value();
        messages.add(new Message("user", prompt));
        ChatRequest chatRequest = new ChatRequest(model, messages, maxCompletions, temperature);
        try {
            ChatResponse response = this.client.send(chatRequest);
            String content = response.getChoices().get(0).getMessage().content();
            Form form = this.objectMapper.readValue(content, Form.class);
            logger.debug("form data: {}", form);

            Field[] fields = Form.class.getDeclaredFields();
            Map<String, String> result = new HashMap<>();
            for (Field field : fields) {
                FieldProperties fieldProperties = formProperties.getFields().get(field.getName());
                if(fieldProperties != null) {
                    Object value = Form.class.getMethod("get" + field.getName().substring(0, 1).toUpperCase().concat(field.getName().substring(1))).invoke(form);
                    result.put("entry."+fieldProperties.id(), String.valueOf(value));
                }
            }
            formProperties.getFields()
                    .entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().useFromUser())
                    .forEach(entry -> {
                        try {
                            Object value = User.class.getMethod("get" + entry.getKey().substring(0, 1).toUpperCase().concat(entry.getKey().substring(1))).invoke(user);
                            result.put("entry."+entry.getValue().id(), String.valueOf(value));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException(e);
                        } catch (NoSuchMethodException e) {
                            throw new RuntimeException(e);
                        }
                    });

            formProperties.getFields()
                    .entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().defaultValue() != null)
                    .forEach(entry -> {
                        result.put("entry."+entry.getValue().id(), entry.getValue().defaultValue());
                    });
            return result;
        } catch (Exception e) {
            throw new GenericErrorException(e);
        }
    }
}
