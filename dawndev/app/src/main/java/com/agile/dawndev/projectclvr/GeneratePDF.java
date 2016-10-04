package com.agile.dawndev.projectclvr;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.agile.dawndev.projectclvr.Models.CLVRQuestion;
import com.agile.dawndev.projectclvr.Models.CLVRResults;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by farida on 5/10/16.
 */
public class GeneratePDF extends AsyncTask<Void, Integer, Long> {

    private boolean haveImage;
    private String fileName;
    private int counter = 1;
    private Document document;

    public GeneratePDF(boolean haveImage, String fileName){
        this.haveImage = haveImage;
        this.fileName = fileName;
        this.document = new Document();
    }
    @Override
    protected Long doInBackground(Void... params) {

        boolean success = false;


        //Create a directory for your PDF
        //make a new clvr directory if it doesnt already exist
        File pdfDir = new File(Environment.getExternalStorageDirectory() + "/CLVR");

        if (!pdfDir.exists()) {
            success = pdfDir.mkdirs();
        }

        if (!success) {
            Log.d("screesnhot", "folder not created");
        } else {
            Log.d("screenshot", "folder created");
        }

        OutputStream foutPdf = null;

        try {
            foutPdf = new FileOutputStream(new File(pdfDir + "/" + fileName + ".pdf"));


            PdfWriter.getInstance(document, foutPdf);
            document.open();

            document.add(new Chunk(""));

            CLVRResults results = CLVRResults.getInstance();
            HashMap<Integer, CLVRQuestion> testResult = results.getClvrQuestionHashMap();

            document.add(new Paragraph("Company Name: "+results.getmCompanyName()));
            document.add(new Paragraph("Candidate Name: "+results.getmUsername()));
            document.add(new Paragraph("Candidate Email: "+results.getmUserEmail()));

            document.add(new Paragraph("-------------------------------"));


            //final graphs

            // iterate through all bitmaps or file location and call this
            for(int question : testResult.keySet()){
                File imageFile = new File(pdfDir + "/graphScreenShot" + question + ".png");
                CLVRQuestion clvrQuestion = testResult.get(question);

                addQuestionAnswerAndGraph( imageFile, clvrQuestion, this.haveImage);
               // addQuestionAnswerAndGraph(document, imageFile, clvrQuestion, false);
            }

            document.close();
            foutPdf.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        Log.d("zoe-chan", "PDF generated");

        return null;
    }

    public void addQuestionAnswerAndGraph(File imageFile, CLVRQuestion clvrQuestion,
                                          boolean addImage) throws DocumentException, IOException {

        this.document.add(new Paragraph("Question "+ this.counter));
        this.counter++;

        //add question and answer from db
        document.add(new Paragraph("Question: " + clvrQuestion.getmQuestion()));
        document.add(new Paragraph("Answer: " + clvrQuestion.getmAnswer()));
        document.add(new Paragraph("Voice Note: " + clvrQuestion.getmMediaURL()));
        Log.d("zoe-chan", "before");

        if(addImage){
            Image graph = Image.getInstance(imageFile.getAbsolutePath());
            graph.scaleAbsolute(500, 500);
            document.add(graph);
            Log.d("zoe-chan", "new page added");
            document.newPage();
        }

    }
}
