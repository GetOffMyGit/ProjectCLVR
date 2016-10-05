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

public class GeneratePDFAsyncTask extends AsyncTask<Void, Integer, Long> {

    private boolean haveImage;
    private String fileName;
    private int counter = 1;
    private Document document;
    private Context context;

    public GeneratePDFAsyncTask(boolean haveImage, String fileName, Context context){
        this.haveImage = haveImage;
        this.fileName = fileName;
        this.document = new Document();
        this.context = context;
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
            Log.d("screenshot", "folder not created");
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
            document.add(new Paragraph("Date: " + new Date()));

            document.add(new Paragraph("-------------------------------"));


            if (this.haveImage) {
                File imageFile = new File(pdfDir + "/graphScreenShot" + -1 + ".png");
                document.add(new Paragraph("Overall Personality Result"));

                addGraph(document, imageFile);

                imageFile = new File(pdfDir + "/graphScreenShot" + -2 + ".png");
                document.add(new Paragraph("Overall Tone Analyser Result"));

                addGraph(document, imageFile);
            }


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

    private void addGraph(Document document, File imageFile) throws DocumentException, IOException {
        Image graph = Image.getInstance(imageFile.getAbsolutePath());
        graph.scaleAbsolute(500, 500);
        document.add(graph);
        Log.d("zoe-chan", "new page added");
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
        Log.d("zoe-chan", "before");

        if(addImage){
            addGraph(document, imageFile);
        }

    }

    protected void onPostExecute() {
//Start the async task for personality insights

        //Create SendGridSendEmail object for company pdf. Send context and email content.
        SendGridSendEmail task = new SendGridSendEmail(context);
        //Execute async task.
        task.execute();

        //Create SendGridSendEmail object for user pdf. Send context and email content.
        TranscribeAnswerEmail task2 = new TranscribeAnswerEmail(context);
        //Execute async task.
        task2.execute();

    }
}
