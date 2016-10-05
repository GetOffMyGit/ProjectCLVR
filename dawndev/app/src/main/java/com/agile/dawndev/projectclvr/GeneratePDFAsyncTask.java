package com.agile.dawndev.projectclvr;

import android.content.Context;
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
 * Asynctask which generates the PDF containing the results that is sent to the company and the transcript which is sent to the user.
 */
public class GeneratePDFAsyncTask extends AsyncTask<Void, Integer, Long> {
    private String TAG = "GeneratePDFAsyncTask";
    private boolean haveImage;
    private String fileName;
    private int counter = 1;
    private Document document;

    public GeneratePDFAsyncTask(boolean haveImage, String fileName, Context context){
        this.haveImage = haveImage;
        this.fileName = fileName;
        this.document = new Document();
    }

    /*
    Performed asynchronously and creates the PDF file containing informtation regarding the questions, answwers, and the resultant graphs
     */
    @Override
    protected Long doInBackground(Void... params) {

        boolean success = false;

        //Create a directory for your PDF
        //make a new clvr directory if it doesnt already exist
        File pdfDir = new File(Environment.getExternalStorageDirectory() + "/CLVR");

        if (!pdfDir.exists()) {
            pdfDir.mkdirs();
        }

        OutputStream foutPdf = null;

        try {
            //create the PDF file
            foutPdf = new FileOutputStream(new File(pdfDir + "/" + fileName + ".pdf"));
            PdfWriter.getInstance(document, foutPdf);
            document.open();

            document.add(new Chunk(""));

            CLVRResults results = CLVRResults.getInstance();
            HashMap<Integer, CLVRQuestion> testResult = results.getClvrQuestionHashMap();

            //add the company and candidate details
            document.add(new Paragraph("Company Name: "+results.getmCompanyName()));
            document.add(new Paragraph("Candidate Name: "+results.getmUsername()));
            document.add(new Paragraph("Candidate Email: "+results.getmUserEmail()));
            document.add(new Paragraph("Date: " + new Date()));

            document.add(new Paragraph("-------------------------------"));

            //retrieve the image of the graphs
            if (this.haveImage) {
                File imageFile = new File(pdfDir + "/graphScreenShot" + -1 + ".png");
                document.add(new Paragraph("Overall Personality Result"));

                addGraph(document, imageFile);

                imageFile = new File(pdfDir + "/graphScreenShot" + -2 + ".png");
                document.add(new Paragraph("Overall Tone Analyser Result"));

                addGraph(document, imageFile);
            }

            // iterate through all bitmaps or file location and call this
            for(int question : testResult.keySet()){
                File imageFile = new File(pdfDir + "/graphScreenShot" + question + ".png");
                CLVRQuestion clvrQuestion = testResult.get(question);

                addQuestionAnswerAndGraph( imageFile, clvrQuestion, this.haveImage);
            }
            //close the PDF, which generates it and makes it readble by the user
            document.close();
            foutPdf.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "PDF generated");

        return null;
    }

    //Used to add additional graphs to the PDF. Called for each quesiton.
    private void addGraph(Document document, File imageFile) throws DocumentException, IOException {
        Image graph = Image.getInstance(imageFile.getAbsolutePath());
        graph.scaleAbsolute(500, 500);
        document.add(graph);
        Log.d(TAG, "new page added");
        document.newPage();
    }

    public void addQuestionAnswerAndGraph(File imageFile, CLVRQuestion clvrQuestion,
                                          boolean addImage) throws DocumentException, IOException {

        this.document.add(new Paragraph("Question "+ this.counter));
        this.counter++;

        //add question and answer from db
        document.add(new Paragraph("Question: " + clvrQuestion.getmQuestion()));
        document.add(new Paragraph("Answer: " + clvrQuestion.getmAnswer()));
        document.add(new Paragraph("Voice Note: " + clvrQuestion.getmMediaURL()));
        Log.d(TAG, "before");

        if(addImage){
            addGraph(document, imageFile);
        }
    }
}
