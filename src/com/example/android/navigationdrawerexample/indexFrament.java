package com.example.android.navigationdrawerexample;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import MyAdapter.indexAdapter;
import MyAdapter.sellerAdapter;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class indexFrament extends Fragment {
	protected ListView listniew;
	protected TextView name_text;
	protected TextView evaluate;
	protected File cache;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.index, container,false); 
		listniew = (ListView)view.findViewById(R.id.indexList); 
		final Handler handler=new Handler(){
	    	public void handleMessage(Message msg){
         switch (msg.what) {       
	    		 case 0:
	    			 Log.v("TAG", "消息传回");
//	    	     listniew.setAdapter((ListAdapter) msg.obj);
	    			 List<Map<String, Object>> contacts = (List<Map<String, Object>>) msg.obj;
	                 Log.v("count", contacts.size()+"index");
					indexAdapter mAdapter = new indexAdapter(getActivity(),contacts,cache);
	                // mListView.setAdapter(mAdapter);
	    			 listniew.setAdapter(mAdapter);
                    break;
                    default:
                    	Toast.makeText(getActivity(), "handler fali", 1).show();
} }  };
new Thread(new Runnable() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		@Override
		public void run() {
		    HttpClient client = new DefaultHttpClient();
		    StringBuilder builder = new StringBuilder();
		    HttpPost httpPost = new HttpPost("http://10.0.3.2/test/index.php");
			
			try {
				HttpResponse response = client.execute(httpPost);
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(
				        response.getEntity().getContent()));
				        for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				            builder.append(s);
				        }
				        Log.v("index", "--"+builder.toString());
				      JSONArray json=new JSONArray(builder.toString());
				       
				      for(int i=0;i<json.length();i++){ 
				    	 
				    	  Map<String, Object> map = new HashMap<String, Object>();
				    	  
				    	  map.put("image","http://10.0.3.2/test/picture/index/"+json.getJSONObject(i).getString("S_URL"));
				    	  Log.v("TAG", "到这步"+list.size());
				    	  map.put("name", json.getJSONObject(i).getString("S_name"));
				       
				        map.put("evaluate","起送价："+json.getJSONObject(i).getString("S_ID")+"元");
				        Log.v("index", "--"+json.getJSONObject(i).getString("S_name"));
				        list.add(map);}
				      
				       // SimpleAdapter adapter= new SimpleAdapter(getActivity(), list, R.layout.index_litm, new String[]{"name","evaluate"}, new int[]{R.id.index_name,R.id.index_evaluate});
					   handler.obtainMessage(0,list).sendToTarget();}
//				      handler.handleMessage(builder.toString());  				        }
				        catch(Exception e) {
							e.printStackTrace();
						
							}
		       
		}
	}).start();
              listniew.setOnItemClickListener(new OnItemClickListener(){
            	  public void onItemClick(AdapterView<?> parent, View view, int position,
        					long id) {
        		     TextView textView=(TextView) view.findViewById(R.id.index_name);
        		     String seller_name=textView.getText().toString();
        		     Fragment sellFragment=new sellerFrament();
        		     Bundle bundle=new Bundle();
        		     bundle.putString("seller_name", seller_name);
        		     Log.v("index","seller_name--"+seller_name);
        		     sellFragment.setArguments(bundle);
        		     FragmentTransaction transaction = getFragmentManager().beginTransaction();
        	  		 transaction.replace(R.id.content_frame, sellFragment);
        	         transaction.addToBackStack(null);
        	         transaction.commit();}
              });
		return view;
	}

}
