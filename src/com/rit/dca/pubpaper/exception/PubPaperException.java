package com.rit.dca.pubpaper.exception;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PubPaperException extends Exception {
    String message;
    String[] messageInformation;

    public PubPaperException(Exception exception){
        super(exception.getMessage());
    }

    public PubPaperException(Exception exception, String errorMessage, String... values){
        super(exception.getMessage());
        message = errorMessage;
        messageInformation = values;
        writeLog();
    }

    private void writeLog(){
        try {
            FileWriter fw = new FileWriter("errors.log", true);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            if(fw!=null) {
                fw.write("\n["+dtf.format(now)+"] " +
                            message + ". " + getMessage() +
                            " - Class Name - "  + messageInformation[0] +
                            " - Method Name - " + messageInformation[1] +
                            " - Line Number - " + messageInformation[2]);
            }
            fw.close();
        }
        catch(Exception exception) {}
    }

}
