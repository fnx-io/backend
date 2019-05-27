package io.fnx.backend.guice;

import com.google.inject.servlet.ServletModule;
import com.googlecode.objectify.ObjectifyService;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Configures swagger model server
 */
public class SwaggerModule extends ServletModule {

    @Override
    protected void configureServlets() {
        bind(OpenApiResource.class);

        OpenAPI oas = new OpenAPI();
        Info info = new Info()
                .title("Swagger Sample App bootstrap code")
                .description("This is a sample server Petstore server.  You can find out more about Swagger " +
                        "at [http://swagger.io](http://swagger.io) or on [irc.freenode.net, #swagger](http://swagger.io/irc/).  For this sample, " +
                        "you can use the api key `special-key` to test the authorization filters.")
                .termsOfService("http://swagger.io/terms/")
                .contact(new Contact()
                        .email("apiteam@swagger.io"))
                .license(new License()
                        .name("Apache 2.0")
                        .url("http://www.apache.org/licenses/LICENSE-2.0.html"));

        oas.info(info);
        Server server = new Server().url("/api");
        List<Server> servers = new ArrayList<Server>();
        servers.add(server);
        oas.servers(servers);

        Set<String> restPackages = new HashSet<>();
        restPackages.add("io.fnx.backend.rest");
        restPackages.add("io.fnx.backend.rest.secure");

        SwaggerConfiguration oasConfig = new SwaggerConfiguration()
                .openAPI(oas)
                .prettyPrint(true)
                .resourcePackages(restPackages);

        try {
            new JaxrsOpenApiContextBuilder()
                    .openApiConfiguration(oasConfig)
                    .buildContext(true);
        } catch (OpenApiConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
