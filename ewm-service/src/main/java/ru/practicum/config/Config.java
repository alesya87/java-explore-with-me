package ru.practicum.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(JacksonConfig.class)
public class Config {
}