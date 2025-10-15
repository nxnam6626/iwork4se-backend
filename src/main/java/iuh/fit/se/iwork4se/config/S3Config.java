package iuh.fit.se.iwork4se.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value("${aws.access-key-id}")
    private String accessKeyId;

    @Value("${aws.secret-access-key}")
    private String secretAccessKey;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.endpoint-url:}")
    private String endpointUrl;

    @Bean
    public AmazonS3 amazonS3() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
        
        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region);
        
        // If custom endpoint is provided (for local development or custom S3-compatible services)
        if (!endpointUrl.isEmpty()) {
            builder.withEndpointConfiguration(
                new com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration(
                    endpointUrl, region)
            );
        }
        
        return builder.build();
    }
}
