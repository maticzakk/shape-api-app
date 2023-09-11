package pl.kurs.shapeapiapp.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import java.util.Set;

@Configuration
@EnableJpaAuditing
public class BeansConfig {

    @Bean
    public ModelMapper modelMapper(Set<Converter> converters) {
        ModelMapper modelMapper = new ModelMapper();
        converters.forEach(modelMapper::addConverter);
        return modelMapper;
    }

//    @Bean(name = "usernameAuditorProvider")
//    public AuditorAware<String> usernameAuditorProvider() {
//        return () -> Optional.of(((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
//    }
}
