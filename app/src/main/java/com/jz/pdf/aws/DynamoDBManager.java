package com.jz.pdf.aws;

import android.content.Context;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.jz.pdf.bean.PDFInfos;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/5/4.
 */
public class DynamoDBManager {
    private static final String TAG = "DynamoDBManager";
    public Context context;
    private static AmazonDynamoDBClient ddb;

    public static void init() {
        if (ddb == null) {
            ClientConfiguration clientConfiguration=new ClientConfiguration();
            clientConfiguration.setConnectionTimeout(3000 * 1000); // 60 sec
            clientConfiguration.setSocketTimeout(3000 * 1000); // 60 sec
                    ddb = new AmazonDynamoDBClient(
                    CognitoClientManager.getCredentials(),clientConfiguration);
        }
    }
    public DynamoDBManager(Context context) {
        this.context=context;
    }



    private static void checkDynamoDBClientAvailability() {
        if (ddb == null) {
            throw new IllegalStateException(
                    "DynamoDB client not initialized yet");
        }
    }

    /*public static ArrayList<PDFBaseBean> getPdfList() {
        checkDynamoDBClientAvailability();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        try {
            PaginatedScanList<PDFBaseBean> result = mapper.scan(PDFBaseBean.class, scanExpression);
            ArrayList<PDFBaseBean> resultList = new ArrayList();
            for (PDFBaseBean up : result) {
                resultList.add(up);
            }
            return resultList;
        } catch (AmazonServiceException ex) {
        }
        return  null;
    }*/


    public static ArrayList<PDFInfos> getPdfList() {
        checkDynamoDBClientAvailability();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        try {
            PaginatedScanList<PDFInfos> result = mapper.scan(PDFInfos.class, scanExpression);
            ArrayList<PDFInfos> resultList = new ArrayList();
            for (PDFInfos info : result) {
                resultList.add(info);
            }
            return resultList;
        } catch (AmazonServiceException ex) {
        }
        return  null;
    }


}
