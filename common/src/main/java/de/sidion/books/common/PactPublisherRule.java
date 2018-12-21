package de.sidion.books.common;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
public class PactPublisherRule implements TestRule {

    private static final String HOST_AND_SCHEME = "http://localhost";

    private String groupName;
    private String projectVersion;
    private String projectName;

    private String packagePath;
    private String contractsBasePath;
    private String baseLocalContractPath;

    private String producerName;

    /**
     *  Sets the properties projectName, projectGroup, projectVersion from gradle processed application environment
     *  required to build the path to the downloaded contract files.
     *
     * @param env
     */
     public void configure(Environment env) {
        try {
            this.projectName = env.getProperty("projectName");
            this.groupName = env.getProperty("projectGroup");
            this.projectVersion = env.getProperty("projectVersion");

            log.debug("projectName: {}", projectName);
            log.debug("groupName: {}", groupName);
            log.debug("projectVersion: {}", projectVersion);

            this.packagePath = groupName.replace(".", "/") + "/";
            this.contractsBasePath = "/contracts/";
            this.baseLocalContractPath = "./build/stubs/META-INF/" + groupName + "/";


        } catch (IllegalArgumentException e) {
            log.info("Gradle properties not available");
        }
    }


    @Override
    public Statement apply(Statement base, Description description) {

        this.producerName = providerNameFromGeneratedSccTestName(description.getMethodName());

        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                boolean verified = true;
                try {
                    base.evaluate();
                } catch (Exception e) {
                    verified = false;
                    throw e;
                } finally {
                    if (projectVersion == null || !projectVersion.contains(".")) {
                        log.info("Skipping publishing, because it seems that gradle information is missing. " +
                                "Probably it's not a test executed by gradle. ProjectVersion was: {} ", projectVersion);
                    } else {
                        publishVerificationResult(description.getMethodName(), verified);
                    }
                }

            }
        };
    }

    private String providerNameFromGeneratedSccTestName(String testName) {
        String[] parts = testName.split("_");
        int endIndex = parts.length - 1;
        //because scc will append running numbers if there is more than one generated test
        if (isNumber(parts[endIndex]) && endIndex > 1) {
            endIndex --;
        }
        return String.join("-", new ArrayList<>(Arrays.asList(parts)).subList(2, endIndex));
    }

    private boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    private void publishVerificationResult(String testName, boolean verified) throws Exception {

        String providerName = providerNameFromGeneratedSccTestName(testName);

        String[] parts = testName.split("_");
        String pactFileName = parts[1] + "_" + providerName +"_pact.json";
        String consumerName = getConsumerPactName(pactFileName);

        String version = getPactVersion(consumerName);

        String contractUrl = HOST_AND_SCHEME + "/pacts/provider/" + providerName +"/consumer/"
                + consumerName + "/version/" + version;

        postVerificationResult(contractUrl, verified);

    }

    private String getPactVersion(String consumerName)  {

        RestTemplate template = new RestTemplate();

        String pacts = template.exchange
                (HOST_AND_SCHEME + "/pacts/latest",
                        HttpMethod.GET, new HttpEntity(createHeaders()), String.class).getBody();

        ReadContext ctx = JsonPath.parse(pacts);

        return (String)((List<?>)ctx.read("$.pacts[*]._embedded.consumer[?(@.name==='"+consumerName+"')]._embedded.version.number")).get(0);

    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        //put any auth stuff here if required
        return headers;
    }

    private JSONObject getContractFile(String fileName) throws Exception {
        String pathToContract = baseLocalContractPath + projectName + "/" + projectVersion + contractsBasePath
                + packagePath + producerName + contractsBasePath +  fileName;
        byte[] contract = Files.readAllBytes(Paths.get(pathToContract));
        return new JSONObject(new String(contract));
    }

    private String getConsumerPactName(String fileName) throws Exception {
        JSONObject jsonObject = getContractFile(fileName);
        JSONObject consumerObject = (JSONObject)jsonObject.get("consumer");
        return (String)consumerObject.get("name");
    }

    private void postVerificationResult(String url, boolean success) throws Exception {

        VerificationResult result = new VerificationResult(success, projectVersion);

        Traverson traverson = new Traverson(new URI(url), MediaTypes.HAL_JSON);

        Link link = traverson.follow("$._links.pb:publish-verification-results.href")
                .withHeaders(createHeaders())
                .asLink();

        RestTemplate template = new RestTemplate();

        template.exchange(link.getHref(), HttpMethod.POST, new HttpEntity(result, createHeaders()), String.class);

    }

    public static class VerificationResult {

        private boolean success;
        private String providerApplicationVersion;

        public VerificationResult(boolean success, String providerApplicationVersion) {
            this.success = success;
            this.providerApplicationVersion = providerApplicationVersion;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getProviderApplicationVersion() {
            return providerApplicationVersion;
        }
    }

}
