package com.jz.pdf.bean;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.jz.pdf.aws.Constants;

import java.io.Serializable;

/**
 * Created by and2long on 16-8-28.
 */
@DynamoDBTable(tableName = Constants.PDF)
public class PDFInfos implements Serializable{

    private String HashKey;
    private long RangeKey;
    private String PDFAudioName;
    private String PDFDate;
    private String PDFDescription;
    private String PDFFileName;
    private String PDFImageName;

    @DynamoDBRangeKey(attributeName = "PDFRangekey")
    public long getRangeKey() {
        return RangeKey;
    }

    public void setRangeKey(long rangeKey) {
        RangeKey = rangeKey;
    }

    @DynamoDBAttribute(attributeName = "PDFAudioName")
    public String getPDFAudioName() {
        return PDFAudioName;
    }

    public void setPDFAudioName(String PDFAudioName) {
        this.PDFAudioName = PDFAudioName;
    }

    @DynamoDBHashKey(attributeName = "HashKey")
    public String getHashKey() {
        return HashKey;
    }

    public void setHashKey(String hashKey) {
        HashKey = hashKey;
    }

    @DynamoDBAttribute(attributeName = "PDFDate")
    public String getPDFDate() {
        return PDFDate;
    }

    public void setPDFDate(String PDFDate) {
        this.PDFDate = PDFDate;
    }

    @DynamoDBAttribute(attributeName = "PDFDescription")
    public String getPDFDescription() {
        return PDFDescription;
    }

    public void setPDFDescription(String PDFDescription) {
        this.PDFDescription = PDFDescription;
    }

    @DynamoDBAttribute(attributeName = "PDFFileName")
    public String getPDFFileName() {
        return PDFFileName;
    }

    public void setPDFFileName(String PDFFileName) {
        this.PDFFileName = PDFFileName;
    }

    @DynamoDBAttribute(attributeName = "PDFImageName")
    public String getPDFImageName() {
        return PDFImageName;
    }

    public void setPDFImageName(String PDFImageName) {
        this.PDFImageName = PDFImageName;
    }

    @Override
    public String toString() {
        return "PDFInfos{" +
                "HashKey='" + HashKey + '\'' +
                ", RangeKey=" + RangeKey +
                ", PDFAudioName='" + PDFAudioName + '\'' +
                ", PDFDate='" + PDFDate + '\'' +
                ", PDFDescription='" + PDFDescription + '\'' +
                ", PDFFileName='" + PDFFileName + '\'' +
                ", PDFImageName='" + PDFImageName + '\'' +
                '}';
    }
}
