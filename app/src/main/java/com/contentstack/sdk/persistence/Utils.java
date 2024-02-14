package com.contentstack.sdk.persistence;

import org.bson.types.ObjectId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

public class Utils {

    static String generateSeqId(String timestamp) {
        Objects.requireNonNull(timestamp, "Timestamp Should Not Be Null");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date eventAtDate = null;
        try {
            eventAtDate = sdf.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Convert the Date to a Unix timestamp in milliseconds
        long unixTimeInMillis = eventAtDate.getTime();

        // Create a BSON ObjectId using the Unix timestamp
        ObjectId objectId = new ObjectId((int) (unixTimeInMillis / 1000), 0);
        String seqId = objectId.toHexString();
        System.out.println(seqId);
        return seqId;
    }
}
