package home.boottest1.config;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.codec.Decoder;
import org.springframework.core.codec.Encoder;

@Configuration
public class FeignResponseDecoderConfig {

    private ObjectFactory<HttpMessageConverters> messageConverters = HttpMessageConverters::new;

}
