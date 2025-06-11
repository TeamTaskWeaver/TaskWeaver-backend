package taskweaver.taskweaver_backend.config;



import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class SwaggerConfig {
    private static final String SECURITY_SCHEME_NAME = "authorization";


    @Bean
    public OpenAPI swaggerApi() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))
                        .addRequestBodies("MultipartFile", new RequestBody()
                                .content(new Content()
                                        .addMediaType("multipart/form-data", new MediaType()
                                                .schema(new Schema<>()
                                                        .type("object")
                                                        .addProperties("file", new Schema<>()
                                                                .type("string")
                                                                .format("binary")))))))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .info(new Info()
                        .title("TaskWeaver API 명세서")
                        .description("TaskWeaver API 명세서입니다.")
                        .version("1.0.0"));
    }
}
