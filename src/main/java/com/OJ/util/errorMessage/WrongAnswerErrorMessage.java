package com.OJ.util.errorMessage;

import com.OJ.domain.InFile;
import com.OJ.domain.OutFile;
import com.OJ.domain.UserOutFile;

import java.util.List;

public class WrongAnswerErrorMessage{

    public static String createErrorMessage(List<InFile> inFiles, List<OutFile> outFiles, List<UserOutFile> userOutFiles){
      StringBuilder sb = new StringBuilder();

      for(int i = 0; i< inFiles.size(); i++){
          // Header
          sb.append("========[Wrong Answer : ")
                  .append(inFiles.get(i).getNo())
                  .append("]========")
                  .append(System.getProperty("line.separator"));

          // input value
          sb.append("[input]")
                  .append(System.getProperty("line.separator"))
                  .append(inFiles.get(i).getInput())
                  .append(System.getProperty("line.separator"))
                  .append("-------------------------------------")
                  .append(System.getProperty("line.separator"));

          // correct output
          sb.append("[correct output]")
                  .append(System.getProperty("line.separator"))
                  .append(outFiles.get(i).getAnswer())
                  .append(System.getProperty("line.separator"))
                  .append(System.getProperty("line.separator"))
                  .append("-------------------------------------")
                  .append(System.getProperty("line.separator"));

          //user output
          sb.append("[user output]")
                  .append(System.getProperty("line.separator"))
                  .append(userOutFiles.get(i).getResult())
                  .append(System.getProperty("line.separator"))
                  .append("=====================================")
                  .append(System.getProperty("line.separator"));
      }

        return sb.toString();
    }
}
