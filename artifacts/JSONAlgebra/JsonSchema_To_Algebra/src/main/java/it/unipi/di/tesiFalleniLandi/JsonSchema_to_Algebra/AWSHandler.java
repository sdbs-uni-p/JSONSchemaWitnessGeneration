package it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.Commons.Utils;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Assertion;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.FullAlgebra.Utils_FullAlgebra;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.JSONSchema;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.JSONSchema.Utils_JSONSchema;
import it.unipi.di.tesiFalleniLandi.JsonSchema_to_Algebra.WitnessAlgebra.WitnessEnv;

import java.util.*;

public class AWSHandler implements RequestHandler<LinkedHashMap<String, ?>, Object> {
	/*@Override
	public Object handleRequest(LinkedHashMap<String, ?> stringLinkedHashMap, Context context) {
		return null;
	}*/

	public Object handleRequest(LinkedHashMap<String, ?> input, Context context) {
		
		try {
			System.out.println("\tQUERY: "+input.get("queryStringParameters"));
			System.out.println("\tHEADERS: "+input.get("headers"));
			
			
			LinkedHashMap<String, String> action = (LinkedHashMap<String, String>) input.get("queryStringParameters");
			String response;
			int code = 200;
			switch(action.get("action")) {
                case "toJSON":
                    response = toJSON((String) input.get("body"));
                    break;

                case "normalize":
                    response = normalize((String) input.get("body"));
                    break;

                case "assertionSeparation":
                    response = assertionSeparation((String) input.get("body"));
                    break;

                case "referenceNormalization":
                    response = referenceNormalization((String) input.get("body"));
                    break;

                case "toGrammarString":
                    response = toGrammarString((String) input.get("body"));
                    break;

                case "grammarToJSON":
                    response = grammarToJSON((String) input.get("body"));
                    break;

                case "notEliminationFull":
                    response = notEliminationFull((String) input.get("body"));
                    break;

                case "andMerging":
                    response = andMerging((String) input.get("body"));
                    break;

                case "notEliminationWitness":
                    response = notEliminationWitness((String) input.get("body"));
                    break;

                case "variableSeparation":
                    response = variableSeparation((String) input.get("body"));
                    break;

                case "Canonicalization":
                    response = canonicalization((String) input.get("body"));
                    break;

                case "variableExpansion":
                    response = variableExpansion((String) input.get("body"));
                    break;

                case "DNF":
                    response = DNF((String) input.get("body"));
                    break;

                case "objPrepare":
                    response = objPrepare((String) input.get("body"));
                    break;

                case "arrPrepare":
                    response = arrPrepare((String) input.get("body"));
                    break;

                case "objArrPrepare":
                    response = objArrPrepare((String) input.get("body"));
                    break;

                default:
                    response = "Unsupported " + action.get("action");
                    code = 400;
            }
			
			
			return new GatewayResponse(response,
					code,
					"Content-Type", "text/plain",
					false);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new GatewayResponse(e.getMessage(),
					400,
					"Content-Type", "text/plain",
					false);
		}
	}



    private String toJSON(String body) {
        Gson gson = new Gson();
        JsonObject object;
        try{
            object = gson.fromJson(body, JsonObject.class);
        }catch (JsonSyntaxException ex){
            throw new JsonSyntaxException("Syntax Error");
        }
        JSONSchema schema = new JSONSchema(object);

        return schema.toJSON().toString();
    }

    private String normalize(String body) {
        Gson gson = new Gson();
        JsonObject object;
        try{
            object = gson.fromJson(body, JsonObject.class);
        }catch (JsonSyntaxException ex){
            throw new JsonSyntaxException("Syntax Error");
        }
        JSONSchema schema = new JSONSchema(object);

        return Utils_JSONSchema.normalize(schema).toJSON().toString();
    }

    private String assertionSeparation(String body) {
        Gson gson = new Gson();
        JsonObject object;
        try{
            object = gson.fromJson(body, JsonObject.class);
        }catch (JsonSyntaxException ex){
            throw new JsonSyntaxException("Syntax Error");
        }

        JSONSchema schema = new JSONSchema(object);

        return schema.assertionSeparation().toJSON().toString();
    }

    private String referenceNormalization(String body) {
        Gson gson = new Gson();
        JsonObject object;
        try{
            object = gson.fromJson(body, JsonObject.class);
        }catch (JsonSyntaxException ex){
            throw new JsonSyntaxException("Syntax Error");
        }
        JSONSchema schema = new JSONSchema(object);
        schema = Utils_JSONSchema.referenceNormalization(schema);

        return schema.toJSON().toString();
    }

    private String toGrammarString(String body) {
        Gson gson = new Gson();
        JsonObject object;
        try{
            object = gson.fromJson(body, JsonObject.class);
        }catch (JsonSyntaxException ex){
            throw new JsonSyntaxException("Syntax Error");
        }

        JSONSchema schema = new JSONSchema(object);
        return Utils_JSONSchema.toGrammarString(Utils_JSONSchema.normalize(schema));
    }

    private String grammarToJSON(String body){
        Assertion schema = Utils_FullAlgebra.parseString(body);

        JsonObject JSON = (JsonObject) schema.toJSONSchema(null);

        return JSON.toString();
    }

    private String notEliminationFull(String body) throws Exception {
        Assertion schema = Utils_FullAlgebra.parseString(body);

        return Utils.beauty(schema.notElimination().toGrammarString());
    }

    private String andMerging(String body) throws Exception {
        WitnessEnv schema = Utils_FullAlgebra.getWitnessAlgebra(Utils_FullAlgebra.parseString(body));

        schema.buildOBDD_notElimination();

        return Utils.beauty(schema.merge(null, null).getFullAlgebra().toGrammarString());

    }

    private String notEliminationWitness(String body) throws Exception {
        Assertion schema = Utils_FullAlgebra.parseString(body);

        WitnessEnv witnessSchema = Utils_FullAlgebra.getWitnessAlgebra(schema);
        witnessSchema.buildOBDD_notElimination();

        return Utils.beauty(witnessSchema.getFullAlgebra().toGrammarString());
    }

    private String variableSeparation(String body) throws Exception {
        WitnessEnv schema =  Utils_FullAlgebra.getWitnessAlgebra(Utils_FullAlgebra.parseString(body));

        schema.buildOBDD_notElimination();
        schema.varNormalization_separation(null, null);
        schema = (WitnessEnv) schema.merge(null, null);

        return Utils.beauty(schema.getFullAlgebra().toGrammarString());
    }

    private String canonicalization(String body) throws Exception {
        WitnessEnv schema = Utils_FullAlgebra.getWitnessAlgebra(Utils_FullAlgebra.parseString(body));

        schema.buildOBDD_notElimination();
        schema.varNormalization_separation(null, null);
        schema = (WitnessEnv) schema.merge(null, null).groupize();

        return Utils.beauty(schema.getFullAlgebra().toGrammarString());
    }

    private String variableExpansion(String body) throws Exception {
        WitnessEnv schema =  Utils_FullAlgebra.getWitnessAlgebra(Utils_FullAlgebra.parseString(body));

        schema.buildOBDD_notElimination();
        schema.varNormalization_separation(null, null);
        schema = (WitnessEnv) schema.merge(null, null).groupize();
        schema = schema.varNormalization_expansion(null);

        return Utils.beauty(schema.getFullAlgebra().toGrammarString());
    }

    private String DNF(String body) throws Exception {
        WitnessEnv schema = Utils_FullAlgebra.getWitnessAlgebra(Utils_FullAlgebra.parseString(body));

        schema.buildOBDD_notElimination();
        schema.varNormalization_separation(null, null);
        schema = (WitnessEnv) schema.merge(null, null).groupize();
        schema = schema.varNormalization_expansion(null);

        return Utils.beauty(schema.DNF().getFullAlgebra().toGrammarString());
    }

    private String objPrepare(String body) throws Exception {
        WitnessEnv schema = Utils_FullAlgebra.getWitnessAlgebra(Utils_FullAlgebra.parseString(body));

        schema.buildOBDD_notElimination();
        schema.varNormalization_separation(null, null);
        schema = (WitnessEnv) schema.merge(null, null).groupize();
        schema = schema.varNormalization_expansion(null);

        schema = schema.DNF();
        schema.toOrPattReq();
        schema.objectPrepare();

        return Utils.beauty(schema.getFullAlgebra().toGrammarString());
    }

    private String arrPrepare(String body) throws Exception {
        WitnessEnv schema = Utils_FullAlgebra.getWitnessAlgebra(Utils_FullAlgebra.parseString(body));

        schema.buildOBDD_notElimination();
        schema.varNormalization_separation(null, null);
        schema = (WitnessEnv) schema.merge(null, null).groupize();
        schema = schema.varNormalization_expansion(null);

        schema = schema.DNF();
        schema.toOrPattReq();
        schema.arrayPreparation();

        return Utils.beauty(schema.getFullAlgebra().toGrammarString());
    }

    private String objArrPrepare(String body) throws Exception {
        WitnessEnv schema = Utils_FullAlgebra.getWitnessAlgebra(Utils_FullAlgebra.parseString(body));

        schema.buildOBDD_notElimination();
        schema.varNormalization_separation(null, null);
        schema = (WitnessEnv) schema.merge(null, null).groupize();
        schema = schema.varNormalization_expansion(null);

        schema = schema.DNF();
        schema.toOrPattReq();
        schema.objectPrepare();
        schema.arrayPreparation();

        return Utils.beauty(schema.getFullAlgebra().toGrammarString());
    }






	/*private GatewayResponse toJSON(String body) throws ParseException {
		JsonObject object;
		object = (JSONObject) new JSONParser().parse(body.replace('\n', ' '));
		JSONSchema schema = new JSONSchema(object);

		return new GatewayResponse(schema.toJSON().toString(),
				200,
				"type", "application/schema+json",
				false);

	}

	private GatewayResponse normalize(String body) throws ParseException {
		JSONObject object = (JSONObject) new JSONParser().parse(body);
		JSONSchema schema = new JSONSchema(object);

		return new GatewayResponse(Utils_JSONSchema.normalize(schema).toJSON().toString(),
				200,
				"type", "application/schema+json",
				false);

	}

	private GatewayResponse assertionSeparation(String body) throws ParseException {
		JSONObject object;
		object = (JSONObject) new JSONParser().parse(body);

		JSONSchema schema = new JSONSchema(object);

		return new GatewayResponse(schema.assertionSeparation().toJSON().toString(),
				200,
				"type", "application/schema+json",
					false);
	}

	private GatewayResponse referenceNormalization(String body) throws ParseException {
		JSONObject object = (JSONObject) new JSONParser().parse(body);
		JSONSchema schema = new JSONSchema(object);
		schema = Utils_JSONSchema.referenceNormalization(schema);

		return new GatewayResponse(schema.toJSON().toString(),
				200,
				"type", "application/schema+json",
				false);
	}

	private GatewayResponse toGrammarString(String body) throws ParseException {
		JSONObject object = (JSONObject) new JSONParser().parse(body);

		JSONSchema schema = new JSONSchema(object);
		return new GatewayResponse(Utils_JSONSchema.toGrammarString(Utils_JSONSchema.normalize(schema)),
				200,
				"type", "application/json+schema",
				false);
	}

	private GatewayResponse grammarToJSON(String body) {
		Assertion schema = Utils_FullAlgebra.parseString(body);

		JSONObject JSON = (JSONObject)schema.toJSONSchema();

		return new GatewayResponse(JSON.toJSONString(),
				200,
				"type", "application/json+schema",
				false);
	}

	private GatewayResponse notEliminationFull(String body) {
		Assertion schema = Utils_FullAlgebra.parseString(body);

		return new GatewayResponse(Utils.beauty(schema.notElimination().toGrammarString()),
				200,
				"type", "application/json+schema",
				false);
	}

	private GatewayResponse andMerging(String body) throws REException {
		Assertion schema = Utils_FullAlgebra.parseString(body);

		return new GatewayResponse(Utils.beauty(Utils_FullAlgebra.getWitnessAlgebra(schema.notElimination()).mergeElement(null).getFullAlgebra().toGrammarString()),
				200,
				"type", "application/json+schema",
				false);
	}

	private GatewayResponse notEliminationWitness(String body) throws WitnessException, REException {
		Assertion schema = Utils_FullAlgebra.parseString(body).notElimination();

		return new GatewayResponse(Utils.beauty(Utils_FullAlgebra.getWitnessAlgebra(schema).groupize().getFullAlgebra().toGrammarString()),
				200,
				"type", "application/json+schema",
				false);
	}

	private GatewayResponse canonicalization(String body) throws WitnessException, REException {
		Assertion schema = Utils_FullAlgebra.parseString(body).notElimination();

		return new GatewayResponse(Utils.beauty(Utils_FullAlgebra.getWitnessAlgebra(schema).mergeElement(null).groupize().getFullAlgebra().toGrammarString()),
				200,
				"type", "application/json+schema",
				false);
	}*/
}






class GatewayResponse {
    private Boolean isBase64Encoded;
    private Integer statusCode;
    private Map<String, String> headers;
    private Map<String, List<String>> multiValueHeaders;
    private String body;

    public GatewayResponse() {
    }

    public GatewayResponse(
    		final String body,
            final Integer statusCode,
            final String key,
            final String value,
            final Boolean isBase64Encoded
        ) {
            this.isBase64Encoded = isBase64Encoded;
            this.statusCode = statusCode;
            this.headers = new HashMap<>();

            headers.put("Access-Control-Allow-Origin", "*");
            headers.put(key, value);

            this.body = body;
        }

    public GatewayResponse(
        final Boolean isBase64Encoded,
        final Integer statusCode,
        final Map<String, String> headers,
        final Map<String, List<String>> multiValueHeaders,
        final String body
    ) {
        this.isBase64Encoded = isBase64Encoded;
        this.statusCode = statusCode;
        this.headers = headers;
        this.multiValueHeaders = multiValueHeaders;
        this.body = body;
    }

    public GatewayResponse(
        final Integer statusCode,
        final Map<String, String> headers,
        final Map<String, List<String>> multiValueHeaders,
        final String body
    ) {
        this(false, statusCode, headers, multiValueHeaders, body);
    }

    public GatewayResponse(
        final Integer statusCode,
        final Map<String, String> headers,
        final String body
    ) {
        this(false, statusCode, headers, null, body);
    }

    public GatewayResponse(
        final Integer statusCode,
        final Map<String, String> headers,
        final Map<String, List<String>> multiValueHeaders
    ) {
        this(false, statusCode, headers, multiValueHeaders, null);
    }

    public GatewayResponse(
        final Integer statusCode,
        final Map<String, String> headers
    ) {
        this(false, statusCode, headers, null, null);
    }

    public Boolean getBase64Encoded() {
        return isBase64Encoded;
    }

    public void setBase64Encoded(final Boolean base64Encoded) {
        isBase64Encoded = base64Encoded;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(final Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(final Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, List<String>> getMultiValueHeaders() {
        return multiValueHeaders;
    }

    public void setMultiValueHeaders(Map<String, List<String>> multiValueHeaders) {
        this.multiValueHeaders = multiValueHeaders;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GatewayResponse that = (GatewayResponse) o;
        return Objects.equals(isBase64Encoded, that.isBase64Encoded) &&
               Objects.equals(statusCode, that.statusCode) &&
               Objects.equals(headers, that.headers) &&
               Objects.equals(multiValueHeaders, that.multiValueHeaders) &&
               Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isBase64Encoded, statusCode, headers, multiValueHeaders, body);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GatewayResponse.class.getSimpleName() + "[", "]")
            .add("isBase64Encoded=" + isBase64Encoded)
            .add("statusCode=" + statusCode)
            .add("headers=" + headers)
            .add("multiValueHeaders=" + multiValueHeaders)
            .add("body='" + body + "'")
            .toString();
    }
}