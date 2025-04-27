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
    private static final String PROMPT_2 = "Olá, nao precisa me responder formalmente, poderia extrair informações desta fonte de dado e retornar apenas o json seguindo o schema de componentes abaixo:" +
            "components:\n" +
            "  schemas:\n" +
            "    Document:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        document_number:\n" +
            "          type: string\n" +
            "        issue_date:\n" +
            "          type: string\n" +
            "          format: date\n" +
            "        billing_period_start:\n" +
            "          type: string\n" +
            "          format: date\n" +
            "        billing_period_end:\n" +
            "          type: string\n" +
            "          format: date\n" +
            "        due_date:\n" +
            "          type: string\n" +
            "          format: date\n" +
            "        total_amount_due:\n" +
            "          type: number\n" +
            "          format: float\n" +
            "        components:\n" +
            "          type: object\n" +
            "          properties:\n" +
            "            electricity:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "            taxes_and_fees:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "            others:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "\n" +
            "    Customer:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        name:\n" +
            "          type: string\n" +
            "        tax_id:\n" +
            "          type: string\n" +
            "        supply_address:\n" +
            "          type: string\n" +
            "        billing_address:\n" +
            "          type: string\n" +
            "        friend_plan_code:\n" +
            "          type: string\n" +
            "        supply_point_code:\n" +
            "          type: string\n" +
            "        contract_end_date:\n" +
            "          type: string\n" +
            "          format: date\n" +
            "        plan:\n" +
            "          type: string\n" +
            "        power:\n" +
            "          type: string\n" +
            "        schedule_cycle:\n" +
            "          type: string\n" +
            "\n" +
            "    PaymentInfo:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        payment_method:\n" +
            "          type: string\n" +
            "        bank:\n" +
            "          type: string\n" +
            "        iban:\n" +
            "          type: string\n" +
            "        mandate_reference:\n" +
            "          type: string\n" +
            "\n" +
            "    ElectricityDetails:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        energy_term:\n" +
            "          type: object\n" +
            "          properties:\n" +
            "            quantity_kwh:\n" +
            "              type: integer\n" +
            "            price_per_kwh:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "            gross_value:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "            discount:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "            net_value:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "            vat_rate:\n" +
            "              type: string\n" +
            "        power_term:\n" +
            "          type: object\n" +
            "          properties:\n" +
            "            days:\n" +
            "              type: integer\n" +
            "            price_per_day:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "            gross_value:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "            discount:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "            net_value:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "            vat_rate:\n" +
            "              type: string\n" +
            "        network_access_fixed_term:\n" +
            "          type: object\n" +
            "          properties:\n" +
            "            days:\n" +
            "              type: integer\n" +
            "            price_per_day:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "            gross_value:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "            discount:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "            net_value:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "            vat_rate:\n" +
            "              type: string\n" +
            "\n" +
            "    OtherCredits:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        interruption_compensation:\n" +
            "          type: object\n" +
            "          properties:\n" +
            "            quantity:\n" +
            "              type: integer\n" +
            "            unit_price:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "            total:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "            vat_rate:\n" +
            "              type: string\n" +
            "\n" +
            "    TaxesAndFees:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        audiovisual_contribution:\n" +
            "          type: object\n" +
            "          properties:\n" +
            "            months:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "            unit_price:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "            total:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "            vat_rate:\n" +
            "              type: string\n" +
            "        dgeg_fee:\n" +
            "          type: object\n" +
            "          properties:\n" +
            "            months:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "            unit_price:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "            total:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "            vat_rate:\n" +
            "              type: string\n" +
            "        special_consumption_tax:\n" +
            "          type: object\n" +
            "          properties:\n" +
            "            quantity_kwh:\n" +
            "              type: integer\n" +
            "            price_per_kwh:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "            total:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "            vat_rate:\n" +
            "              type: string\n" +
            "        vat:\n" +
            "          type: object\n" +
            "          properties:\n" +
            "            vat_23_percent:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "            vat_6_percent:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "            vat_exempt:\n" +
            "              type: number\n" +
            "              format: float\n" +
            "\n" +
            "    AccountMovements:\n" +
            "      type: array\n" +
            "      items:\n" +
            "        type: object\n" +
            "        properties:\n" +
            "          description:\n" +
            "            type: string\n" +
            "          date:\n" +
            "            type: string\n" +
            "            format: date\n" +
            "          amount:\n" +
            "            type: number\n" +
            "            format: float\n" +
            "          number:\n" +
            "            type: string\n" +
            "\n" +
            "    EnvironmentalInfo:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        co2_footprint_kg:\n" +
            "          type: number\n" +
            "          format: float\n" +
            "\n" +
            "    AdditionalInfo:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        network_tariffs_value_without_vat:\n" +
            "          type: number\n" +
            "          format: float\n" +
            "        cieg_value:\n" +
            "          type: number\n" +
            "          format: float\n" +
            "        savings_vs_regulated_tariff:\n" +
            "          type: number\n" +
            "          format: float\n" +
            "        energy_tariffs_and_commercialization_without_vat:\n" +
            "          type: number\n" +
            "          format: float\n";

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
            return result;
        } catch (Exception e) {
            throw new GenericErrorException(e);
        }
    }
}
