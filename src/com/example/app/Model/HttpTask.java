package com.example.app.Model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.widget.Toast;

public class HttpTask extends AsyncTask<String, Void, String> {
	
	public String mJson;
	
	@Override
	public String doInBackground(String... params)
    {

		HttpURLConnection con = null ;
        InputStream is = null;
        String url = "https://dl.dropboxusercontent.com/u/8606210/trivia.json";//params[0];
        

        try {
            con = (HttpURLConnection) ( new URL(url)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while (  (line = br.readLine()) != null )
                buffer.append(line + "\r\n");

            is.close();
            con.disconnect();
            mJson = buffer.toString();
            return buffer.toString();
        }
        catch(Throwable t) {
            t.printStackTrace();
            return null;
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }
	}
	
	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

}
