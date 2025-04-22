package itu.p16.crypto.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "duvfwe8zh",
            "api_key", "483429138178156",
            "api_secret", "iTlfIZDiU5LCeQBH4pekFsidIkc"
        ));
    }
}