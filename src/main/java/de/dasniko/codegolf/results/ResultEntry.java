package de.dasniko.codegolf.results;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Niko Köbler, http://www.n-k.de, @dasniko
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "codegolf")
public class ResultEntry {
    @DynamoDBHashKey
    private String username;
    @DynamoDBAttribute
    private String email;
    @DynamoDBAttribute
    private String firstName;
    @DynamoDBAttribute
    private String lastName;
    @DynamoDBAttribute
    private int countChars;
    @DynamoDBAttribute
    private Date timestamp;
    @DynamoDBAttribute
    private String sourceCode;
}
