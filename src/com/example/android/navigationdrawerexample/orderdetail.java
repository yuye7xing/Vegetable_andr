package com.example.android.navigationdrawerexample;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import MyAdapter.sellerAdapter;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class orderdetail extends Fragment {
    private TextView textView;
    private ImageView image;
    private ListView listview;
    private Button button;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v("listen","name"+"-------");
		View view = inflater.inflate(R.layout.orderdetail, container,false);
		textView=(TextView)view.findViewById(R.id.textView1);
		image=(ImageView)view.findViewById(R.id.imageView1);
		listview=(ListView)view.findViewById(R.id.listView1);
		button=(Button)view.findViewById(R.id.second_pay);
		Button button2=(Button)view.findViewById(R.id.delete);
		String name=getArguments().getString("name");
		final String TAG=getArguments().getString("TAG");
		final ListView listView2=(ListView)view.findViewById(R.id.listView1);
		textView.setText(name);
		final Handler handler=new Handler(){
			public void handleMessage(Message msg){
		         switch (msg.what){
		         case 0:
	    			 List<Map<String, Object>> contacts = (List<Map<String, Object>>) msg.obj;
	                 Log.v("count_od==", contacts.size()+"");
					SimpleAdapter mAdapter = new SimpleAdapter(getActivity(),contacts,R.layout.order_detail_item,new String[]{"name","num"},new int[]{R.id.od_name,R.id.od_num});
	                // mListView.setAdapter(mAdapter);
	    			 listView2.setAdapter(mAdapter);
		        	 break;
		         }
		}};
		new Thread(new Runnable() {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			
			@Override
			public void run() {
			    HttpClient client = new DefaultHttpClient();
			    StringBuilder builder = new StringBuilder();
			    HttpPost httpPost = new HttpPost("http://10.0.3.2/test/order_detail.php");
				ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
				pairs.add(new BasicNameValuePair("ID",TAG));
				Log.v("count_od==", TAG+"ID");
			//	pairs.add(new BasicNameValuePair("username", username));
			    try {	
			    	UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(pairs, "UTF8");
	  				httpPost.setEntity(p_entity);
	  				HttpResponse response = client.execute(httpPost);
	  				
	  				BufferedReader reader = new BufferedReader(new InputStreamReader(
	  				        response.getEntity().getContent()));
	  				        for (String s = reader.readLine(); s != null; s = reader.readLine()) {
	  				            builder.append(s);
	  				        }
	  				     // Log.v("count_od", list.size()+"----"+builder.toString());
	  				      JSONArray jsonArray=new JSONArray(builder.toString());
	  				    
	  				    JSONObject json=jsonArray.getJSONObject(0);
	  				  Log.v("count_od", list.size()+"----");
	  				  for (int i = 1; i < 6; i++) {
	  					//Log.v("count_od", i+"----"+TAG+"--");
	  					Log.v("count_od", i+"----"+TAG+"--"+json.getString("ve_name_"+i));
	  					Log.v("count_od", i+"----"+TAG+"-----"+json.getString("ve_num_"+i));
						if (!json.getString("ve_num_"+i).equals("0")) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("name", json.getString("ve_name_"+i));
							map.put("num", json.getString("ve_num_"+i));
							list.add(map);
						}
					}
	  				Log.v("count_od", list.size()+"");
	  				  
	  				 handler.obtainMessage(0,list).sendToTarget();
	  				      
					} catch (Exception e) {
						e.printStackTrace();
					}
						}
					}).start();
		
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String seller_name=textView.getText().toString();	
				Fragment sellFragment=new sellerFrament();
   		     Bundle bundle=new Bundle();
   		     bundle.putString("seller_name", seller_name);
   		     Log.v("index","seller_name--"+seller_name);
   		     sellFragment.setArguments(bundle);
   		     FragmentTransaction transaction = getFragmentManager().beginTransaction();
   	  		 transaction.replace(R.id.content_frame, sellFragment);
   	         transaction.addToBackStack(null);
   	         transaction.commit();
				
			}
		});
		
		button2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("确定删除！！").setPositiveButton("确定",
	        			 new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					new Thread(new Runnable() {
						public void run() {
						    HttpClient client = new DefaultHttpClient();
						    HttpPost httpPost = new HttpPost("http://10.0.3.2/test/order_delete.php");
						    ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
							pairs.add(new BasicNameValuePair("ID",TAG));
							Log.v("delete", TAG);
							 try {	
							    	UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(pairs, "UTF8");
					  				httpPost.setEntity(p_entity);
					  				HttpResponse response = client.execute(httpPost);
							 } catch (Exception e) {
									e.printStackTrace();
								}
							
						}}).start();
					Fragment indexFragment=new order();
					FragmentManager fManager = getFragmentManager();
			        fManager.beginTransaction().replace(R.id.content_frame, indexFragment).commit();
				}
			}
				).setNegativeButton("取消", null).show();	     
				
			}
		});
		
		return view;
	}
	

}
