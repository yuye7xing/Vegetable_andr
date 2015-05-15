package com.example.android.navigationdrawerexample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import com.example.android.navigationdrawerexample.convertStreamToString;







import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class login extends Fragment{

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_login, container, false);
        super.onCreate(savedInstanceState);   
        Button btnOk = (Button)rootView.findViewById(R.id.signin_button);

        final TextView contentName = (TextView)rootView.findViewById(R.id.username_edit);

        final TextView contentPassword = (TextView)rootView.findViewById(R.id.password_edit);
        btnOk.setOnClickListener(new View.OnClickListener() {



        	@Override

        	public void onClick(View v) {

        	String name = contentName.getText().toString();

        	String password = contentPassword.getText().toString();

        	login(name, password);

        	}

        	});



        return rootView;}
        public void login(String name, String password){

        	boolean error = false;



        	if(name.equalsIgnoreCase("")) {

        	//msgError.setText("用户名不能为空!");
        		Toast.makeText(getActivity(), "name fail", Toast.LENGTH_SHORT).show();

        	error = true;

        	}



        	if(error == false) {

        	if(password.equalsIgnoreCase("")) {

        	//msgError.setText("密码不能为空!");
        		Toast.makeText(getActivity(), "pw fail", Toast.LENGTH_SHORT).show();
        	error = true;

        	}

        	}



        	if(error == false) {
                 //     Log.i("system.out", name+password);
        		   Post_PHP(name,password);
        		   SharedPreferences.Editor shaEditor=getActivity().getSharedPreferences("data", 0).edit();
                      shaEditor.putString("name", name);
                      shaEditor.putBoolean("login", true);
                      shaEditor.putString("passw", password);
                      shaEditor.commit();
                      SharedPreferences readPreferences=getActivity().getSharedPreferences("data", 0);
//                      Log.v("TAG", readPreferences.getString("name",null));
        	}





        	}
		private void Post_PHP(final String name, final String password) {
		
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
						Map<String, String> parmas = new HashMap<String, String>();
			parmas.put("name", name);
			DefaultHttpClient client = new DefaultHttpClient();//http客户端
			HttpPost httpPost = new HttpPost("http://10.0.3.2/test/post.php");
			ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
			pairs.add(new BasicNameValuePair("name", name));
			pairs.add(new BasicNameValuePair("password", password));
			//Log.i("system.out", name+password+"post_PHP");
			try {
				UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(pairs, "utf8");
				httpPost.setEntity(p_entity);
				HttpResponse response = client.execute(httpPost);
				// i=response.getStatusLine().getStatusCode();
		        HttpEntity entity = response.getEntity();
		        InputStream content = entity.getContent();
		       convertStreamToString cStreamToString=new convertStreamToString();
		  String returnConnection = cStreamToString.convertStreamToString(content); 

	
		      if (returnConnection.equals("Yes")) {
				Log.i("System.out", name+password);
			}else {
				Log.i("System.out","fail");
			}
			} catch (Exception e) {
				e.printStackTrace();
			}
				}
			}).start();

		}


        	


    

	}


