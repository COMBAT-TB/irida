package ca.corefacility.bioinformatics.irida.config.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.servlet.view.xml.MarshallingView;

import com.fasterxml.jackson.datatype.jdk7.Jdk7Module;
import com.google.common.collect.ImmutableMap;

import ca.corefacility.bioinformatics.irida.config.services.scheduled.IridaScheduledTasksConfig;
import ca.corefacility.bioinformatics.irida.web.spring.view.CSVView;
import ca.corefacility.bioinformatics.irida.web.spring.view.FastaView;
import ca.corefacility.bioinformatics.irida.web.spring.view.FastqView;
import ca.corefacility.bioinformatics.irida.web.spring.view.GenbankView;
import ca.corefacility.bioinformatics.irida.web.spring.view.NewickFileView;

/**
 * Configuration for IRIDA REST API.
 * 
 * 
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "ca.corefacility.bioinformatics.irida.web.controller.api" })
@Import(IridaScheduledTasksConfig.class)
public class IridaRestApiWebConfig extends WebMvcConfigurerAdapter {

	/** named constant for allowing unlimited upload sizes. */
	public static final Long UNLIMITED_UPLOAD_SIZE = -1L;

	@Value("${file.upload.max_size}")
	private Long MAX_UPLOAD_SIZE = UNLIMITED_UPLOAD_SIZE;

	public static final int MAX_IN_MEMORY_SIZE = 1048576; // 1MB

	private static final Logger logger = LoggerFactory.getLogger(IridaRestApiWebConfig.class);

	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();

		resolver.setMaxInMemorySize(MAX_IN_MEMORY_SIZE);
		resolver.setMaxUploadSize(MAX_UPLOAD_SIZE);

		return resolver;
	}

	@Bean
	public ViewResolver apiViewResolver(ContentNegotiationManager contentNegotiationManager) {
		logger.debug("Configuring REST API view resolver.");
		ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
		resolver.setDefaultViews(defaultViews());
		resolver.setContentNegotiationManager(contentNegotiationManager);
		resolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return resolver;
	}

	private List<View> defaultViews() {
		List<View> views = new ArrayList<>();
		MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
		jsonView.setPrettyPrint(true);
		
		// add support for serializing Path data
		jsonView.getObjectMapper().registerModule(new Jdk7Module());
		
		views.add(jsonView);
		Jaxb2Marshaller jaxb2marshaller = new Jaxb2Marshaller();
		jaxb2marshaller
				.setPackagesToScan(new String[] { "ca.corefacility.bioinformatics.irida.web.assembler.resource" });
		MarshallingView marshallingView = new MarshallingView(jaxb2marshaller);
		views.add(marshallingView);
		views.add(new FastaView());
		views.add(new FastqView());
		views.add(new GenbankView());
		views.add(new NewickFileView());
		views.add(new CSVView());
		return views;
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		Map<String, MediaType> mediaTypes = ImmutableMap.of("json", MediaType.APPLICATION_JSON, "xml",
				MediaType.APPLICATION_XML, "fasta", MediaType.valueOf("application/fasta"), "fastq",
				MediaType.valueOf("application/fastq"), "gbk", MediaType.valueOf("application/genbank"));
		configurer.ignoreAcceptHeader(false).defaultContentType(MediaType.APPLICATION_JSON).favorPathExtension(true)
				.mediaTypes(mediaTypes);
	}

	@Bean
	public MessageSource messageSource() {
		String[] resources = { "classpath:/i18n/oauth" };

		ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
		source.setBasenames(resources);
		source.setDefaultEncoding("UTF-8");
		return source;
	}
}
