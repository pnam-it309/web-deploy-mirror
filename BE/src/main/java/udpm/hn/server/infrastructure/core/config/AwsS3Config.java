package udpm.hn.server.infrastructure.core.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test") // Exclude from test profile
public class AwsS3Config {

    @Bean
    @ConditionalOnProperty(prefix = "aws", name = "enabled", havingValue = "false", matchIfMissing = true)
    public AmazonS3 amazonS3Disabled() {
        return null; // Return null when AWS is explicitly disabled
    }

    @Bean
    @ConditionalOnProperty(prefix = "aws", name = "enabled", havingValue = "true", matchIfMissing = true)
    public AmazonS3 s3Client(
            @Value("${aws.region:ap-southeast-1}") String region,
            @Value("${aws.access-key:}") String accessKey,
            @Value("${aws.secret-key:}") String secretKey) {
        
        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
        
        // Use IAM role if no credentials are provided (recommended for EC2, ECS, Lambda)
        if (StringUtils.isBlank(accessKey) || StringUtils.isBlank(secretKey)) {
            builder.withCredentials(new DefaultAWSCredentialsProviderChain());
        } else {
            // Use provided credentials (for local development only)
            AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(accessKey, secretKey)
            );
            builder.withCredentials(credentialsProvider);
        }
        
        // Set region, default to ap-southeast-1 if not specified
        try {
            Regions awsRegion = Regions.fromName(region);
            return builder.withRegion(awsRegion).build();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid AWS region: " + region + 
                ". Valid regions are: " + java.util.Arrays.toString(Regions.values()), e);
        }
    }
}
