package org.psc.web;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import org.psc.web.domain.Recommendation;
import org.psc.web.json.RecommendationDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import tcall.Processor;

@Slf4j
@Api(value = "/", description = "worker test app")
@OpenAPIDefinition(info = @Info(title = "workerapp", version = "1.0", description = "works"))
@RestController
@RequestMapping("/workerapp")
public class WorkerRestController {

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Recommendation.class, new RecommendationDeserializer());
        objectMapper.registerModule(module);
    }

    @ApiOperation(value = "worker resource", notes = "more work")
    @Operation(summary = "worker resource", description = "more work", responses = {
            @ApiResponse(description = "the work status", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "an error") })
    @RequestMapping(path = "/worker", method = RequestMethod.GET)
    public String getWorker(
            @ApiParam(value = "the param", required = false) @Parameter(description = "the param", required = false) @RequestParam(required = false) String param)
            throws IOException {
        Path path = Paths.get("src/main/resources/test.json");
        String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        Recommendation rec = objectMapper.readValue(content, Recommendation.class);

        log.info(objectMapper.writeValueAsString(rec));

        return objectMapper.writeValueAsString(rec);
    }

    @ApiOperation(value = "test resource", notes = "a test")
    @RequestMapping(path = "/tresource", method = RequestMethod.GET)
    public String getTResource() {
        Processor processor = new Processor();
        String result = processor.invoke();
        log.info(result);

        String result2 = processor.invoke2();
        log.info(result2);

        return new StringBuilder(20).append(result).append(" - ").append(result2).toString();
    }

    @ApiOperation(value = "lazyworker resource", notes = "a lazy worker")
    @RequestMapping(path = "/lazyworker", method = RequestMethod.GET)
    public String getLazyWorker() throws IOException {
        return "OK";
    }

}
