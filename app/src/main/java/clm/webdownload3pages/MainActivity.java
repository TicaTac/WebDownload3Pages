package clm.webdownload3pages;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView htmlTV;
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView htmlTV = (TextView) findViewById(R.id.htmlTV);

        myDownloadFromWeb download=new myDownloadFromWeb();
        download.execute("http://www.ynet.co.il/","ynet");
//        download.execute("http://www.ynet.co.il/","ynet");



    }

    public void writeToFile(String fileName,String content)
    {
        String mySdcardFolder=  Environment.getExternalStorageDirectory().getAbsolutePath();

        Toast.makeText(this, mySdcardFolder , Toast.LENGTH_SHORT).show();
        //mnt/sdcard/testing.txt

        final String filetosave= mySdcardFolder+"/"+fileName+".txt";

      //  String content= "testing write file...";

        try {
            File file = new File(filetosave);
            FileOutputStream fop = new FileOutputStream(file);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // get the content in bytes
            byte[] contentInBytes = content.getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();
        }
        catch (IOException ee)
        {
            ee.printStackTrace();
        }
        Toast.makeText(this, "finish", Toast.LENGTH_SHORT).show();

    }

    public class myDownloadFromWeb extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            //start download....

            BufferedReader input = null;
            HttpURLConnection connection = null;
            StringBuilder response = new StringBuilder();
            try {
                //create a url:
                URL url = new URL(params[0]);
                //create a connection and open it:
                connection = (HttpURLConnection) url.openConnection();

                //status check:
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                    //connection not good - return.
                }

                //get a buffer reader to read the data stream as characters(letters)
                //in a buffered way.
                input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                //go over the input, line by line
                String line="";
                while ((line=input.readLine())!=null){
                    //append it to a StringBuilder to hold the
                    //resulting string
                    response.append(line+"\n");
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                if (input!=null){
                    try {
                        //must close the reader
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(connection!=null){
                    //must disconnect the connection
                    connection.disconnect();
                }
            }

            writeToFile(params[1], response.toString());


            return response.toString();
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            htmlTV.setText(s);

        }
    }
}
