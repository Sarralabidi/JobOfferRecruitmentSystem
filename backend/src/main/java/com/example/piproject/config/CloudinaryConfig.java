package com.example.piproject.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dstpmi4px",
                "api_key", "394795138359129",
                "api_secret", "DcvmHRpbajDXjDCIB8zlEnvzKOc"
        ));
    }
}