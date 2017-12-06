package config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan({"model","services","dao","config"})
@EnableAspectJAutoProxy
public class NetworkConfig {
}
