package de.dasniko.codegolf.play;

import com.amazonaws.services.s3.AmazonS3;
import de.dasniko.codegolf.CodegolfConfig;
import de.dasniko.codegolf.User;
import de.dasniko.codegolf.results.ResultService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static de.dasniko.codegolf.CodegolfApplication.S3_BUCKET;

/**
 * @author Niko Köbler, http://www.n-k.de, @dasniko
 */
@Service
@RequiredArgsConstructor
public class PlayService {

    private static final String CLASSNAME = "Codegolf";

    private static final File ROOT = new File(System.getProperty("java.io.tmpdir"));

    private final ResultService resultService;
    private final AmazonS3 s3;
    private final CodegolfConfig config;

    PlayResult play(PlayRequest playRequest) {
        User user = playRequest.getUser();
        String sourceCode = playRequest.getSourcecode();
        String packageName = user.getPackagename();

        String check = checkForForbiddenExpressions(sourceCode);
        if (!"".equals(check)) {
            return PlayResult.builder().result(check).build();
        }

        String source = buildSource(packageName, sourceCode);
        File sourceFile = saveSource(packageName, source);

        try {
            compile(sourceFile);
        } catch (RuntimeException e) {
            return PlayResult.builder()
                    .result(e.getMessage())
                    .build();
        }

        File classFile = new File(ROOT, packageName + "/" + CLASSNAME + ".class");
        saveSourceAndClass(packageName, source, classFile);

        String result = callTest(packageName);
        boolean success = "OK".equals(result);

        int numChars = countChars(sourceCode);

        if (success) {
            resultService.updateResultlist(user, numChars, source);
        }

        return PlayResult.builder()
                .success(success)
                .result(result)
                .count(numChars)
                .build();
    }

    private String checkForForbiddenExpressions(String source) {
        String ret = "";
        if (source.contains("java.util.Date") ||source.contains("util.Date") || source.contains(".Date")
                || source.contains("java.time") ||source.contains(".time") || source.contains("time.")
                || source.contains("LocalDate")
                || source.contains("SimpleDateFormat")) {
            ret = "You used forbidden expressions, that's not allowed. Stay fair!";
        }
        return ret;
    }

    private String buildSource(String packageName, String sourceCode) {
        return "package " + packageName + ";\npublic class " + CLASSNAME
                + " {\npublic int play(String s) {\n" + sourceCode + "\n} }";
    }

    @SneakyThrows
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File saveSource(String packageName, String source) {
        File sourceFile = new File(ROOT, packageName + "/" + CLASSNAME + ".java");
        sourceFile.getParentFile().mkdirs();
        Files.write(sourceFile.toPath(), source.getBytes(StandardCharsets.UTF_8));
        return sourceFile;
    }

    private void compile(File sourceFile) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        OutputStream outputStream = new ByteArrayOutputStream();
        int compileResult = compiler.run(null, outputStream, outputStream, sourceFile.getPath());
        if (compileResult != 0) {
            throw new RuntimeException(outputStream.toString());
        }
    }

    private String callTest(String packageName) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(config.getLambdaUrl(), packageName, String.class);
        return responseEntity.getBody();
    }

    private int countChars(String sourceCode) {
        return sourceCode.replaceAll("\\s", "").length();
    }

    private void saveSourceAndClass(String packageName, String sourceCode, File classFile) {
        s3.putObject(S3_BUCKET, packageName + ".java", sourceCode);
        s3.putObject(S3_BUCKET, packageName + ".class", classFile);
    }
}
