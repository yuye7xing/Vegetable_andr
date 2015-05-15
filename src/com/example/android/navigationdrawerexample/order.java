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

import MyAdapter.orderlist;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.android.navigationdrawerexample.orderdetail;

public class order extends Fragment {
   private File cache;
    protected ListView listniew;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View view = inflater.inflate(R.layout.orders, container,false); 
        listniew = (ListView)view.findViewById(R.id.listView1); 
		  final Handler handler=new Handler(){
	    	public void handleMessage(Message msg){

	    		 switch (msg.what) {       
	    		 case 0:
	    	 
	    			 List<Map<String, Object>> contacts = (List<Map<String, Object>>) msg.obj;
	                 Log.v("count11", contacts.size()+"");
					orderlist mAdapter = new orderlist(getActivity(),contacts,cache);
	                // mListView.setAdapter(mAdapter);
	    			 listniew.setAdapter(mAdapter);
	    			  	
	    			 Log.v("hand", "0"); 

	    			 break;

	    			 default:

	    			 Toast.makeText(getActivity(), "handler fali", 1)

	    			 .show();

	    			 break;

	    			 }

	    			 }                                     
	    };
      new Thread(new Runnable() {
  		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			
  		@Override
  		public void run() {
  		    HttpClient client = new DefaultHttpClient();
  		    StringBuilder builder = new StringBuilder();
  		    HttpPost httpPost = new HttpPost("http://10.0.3.2/test/search_order.php");
  		    SharedPreferences readPreferences=getActivity().getSharedPreferences("data", 0);
  		    String name =readPreferences.getString("name", null);
  			ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
  			pairs.add(new BasicNameValuePair("name", name));
  			
  			try {
  				UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(pairs, "UTF8");
  				httpPost.setEntity(p_entity);
  				HttpResponse response = client.execute(httpPost);
  				
  				BufferedReader reader = new BufferedReader(new InputStreamReader(
  				        response.getEntity().getContent()));
  				        for (String s = reader.readLine(); s != null; s = reader.readLine()) {
  				            builder.append(s);
  				        }
  				        Log.v("TAG", "--"+builder.toString());
  				      JSONArray json=new JSONArray(builder.toString());
//  				        JSONObject jsonObject = new JSONObject(builder.toString());
//  				        Log.v("TAG", "+++"+jsonObject.getString("[1]"));
  				       
  				      for(int i=0;i<json.length();i++){ 
  				    	  Map<String, Object> map = new HashMap<String, Object>();
  				    	map.put("TAG", json.getJSONObject(i).getString("o_id"));
  				        map.put("name", json.getJSONObject(i).getString("S_ID"));
  				        map.put("time", json.getJSONObject(i).getString("o_time"));
  				        map.put("money","$"+json.getJSONObject(i).getString("o_money"));
//  				        Log.v("TAG", jsonOb.getString("o_time")+jsonObject.getString("o_money"));
  				        list.add(map);}
  				        //SimpleAdapter adapter= new SimpleAdapter(getActivity(), list, R.layout.list_litm, new String[]{"name","time","money"}, new int[]{R.id.textView1,R.id.order_time,R.id.order_money});
  				    Log.v("hand11", "0"+"----"+list.size()); 
  				    handler.obtainMessage(0,list).sendToTarget();}
//  				      handler.handleMessage(builder.toString());  				        }
  				        catch(Exception e) {
  							e.printStackTrace();
  						
  							}
  		       
  		}
  	}).start();
  	  listniew.setOnItemClickListener(new OnItemClickListener(){
  			public void onItemClick(AdapterView<?> parent, View view, int position,
  					long id) {
  		     TextView textView=(TextView) view.findViewById(R.id.textView1);
  		     TextView TAGview=(TextView)view.findViewById(R.id.TAG);
  		     String name=textView.getText().toString();
  		     String TAG=TAGview.getText().toString();
  		     Fragment orFragment=new orderdetail();
  		    Bundle bundle=new Bundle();
  		    bundle.putString("name", name);
  		  bundle.putString("TAG", TAG);
  		   Log.v("listen==",name+"-------"+TAG);
  		   orFragment.setArguments(bundle);
  		 FragmentTransaction transaction = getFragmentManager().beginTransaction();
  		 transaction.replace(R.id.content_frame, orFragment);
         transaction.addToBackStack(null);
         transaction.commit();
  			}});
         return view;  
    }



	



  
   
}