package com.example.android.navigationdrawerexample;

import image.add_sub;

import java.io.BufferedReader;
import java.io.File;
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

import com.example.android.navigationdrawerexample.R.id;
import com.example.android.navigationdrawerexample.R.string;


import MyAdapter.sellerAdapter;
import android.R.integer;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class sellerFrament extends Fragment {
	Double sum=0.0;
	private File cache;
   protected ListView listview2;
	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.inde_detail, container,false);
		final String seller_name=getArguments().getString("seller_name");
		//将卖家放入SharedPreferences暂时存储
//		SharedPreferences.Editor shaEditor=getActivity().getSharedPreferences("data", 0).edit();
//		shaEditor.putString("seller_name", seller_name);
//		shaEditor.commit();
		TextView textView=(TextView) view.findViewById(R.id.sellername);
		textView.setText(seller_name);
		final Button collect=(Button)view.findViewById(R.id.collect);
		
		final Handler handler=new Handler(){
			public void handleMessage(Message msg){
		         switch (msg.what){
		         case 0:
		        	 collect.setText("取消收藏");
		        	 break;
		         }
		}};
		new Thread(new Runnable() {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			
			@Override
			public void run() {
				SharedPreferences readpPreferences=getActivity().getSharedPreferences("data", 0);
				String username=readpPreferences.getString("name", null);
				Log.v("selllerFrament", "用户名："+username);	
			    HttpClient client = new DefaultHttpClient();
			    StringBuilder builder = new StringBuilder();
			    HttpPost httpPost = new HttpPost("http://10.0.3.2/test/is_col.php");
				ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
				pairs.add(new BasicNameValuePair("seller_name", seller_name));
				pairs.add(new BasicNameValuePair("username", username));
			    try {	UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(pairs, "utf8");
				httpPost.setEntity(p_entity);
					HttpResponse response = client.execute(httpPost); 
					HttpEntity entity = response.getEntity();
			        InputStream content = entity.getContent();
				       convertStreamToString cStreamToString=new convertStreamToString();
				  String returncollect = cStreamToString.convertStreamToString(content); 

			
				      if (returncollect.equals("YES")) {
						handler.obtainMessage(0).sendToTarget();
					}else {
						Log.i("collect","收藏查询："+"fail");
					}
					} catch (Exception e) {
						e.printStackTrace();
					}
						}
					}).start();
		
//显示可买的蔬菜品种及价格-------------------------------------------------------------------
		   listview2=(ListView)view.findViewById(R.id.buy_order);
		   final Handler handler2=new Handler(){
			   public void handleMessage(Message msg){

		    		 switch (msg.what) {       
		    		 case 0:
		    			 List<Map<String, Object>> contacts = (List<Map<String, Object>>) msg.obj;
		                 Log.v("count", contacts.size()+"");
						sellerAdapter mAdapter = new sellerAdapter(getActivity(),contacts,cache);
		                // mListView.setAdapter(mAdapter);
		    			 listview2.setAdapter(mAdapter);
		    			 listview2.post(new Runnable(){
		    				    public void run(){
		    				    Log.v("num", listview2.getChildCount()+"");
//		    				 num=listview2.getChildCount();
		    				    TextView textView2=(TextView)view.findViewById(R.id.sum);
		    				    textView2.setText(sum+"");
		    				    }});
		    			 break;
		    		 default:

		    			 Toast.makeText(getActivity(), "handler fali", 1).show();

		    			 break;
		   }}};
		    		 new Thread(new Runnable() {
		    		  		List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
		    		  		public void run() {
		    		  		    HttpClient client = new DefaultHttpClient();
		    		  		    StringBuilder builder = new StringBuilder();
		    		  		    HttpPost httpPost = new HttpPost("http://10.0.3.2/test/vegetable_seller.php");
		    		  		  ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		    		  			pairs.add(new BasicNameValuePair("seller_name", seller_name));
		    		  			try {
		    		  				UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(pairs, "UTF8");
		    		  				httpPost.setEntity(p_entity);
		    		  				HttpResponse response = client.execute(httpPost);
		    		  				
		    		  				BufferedReader reader = new BufferedReader(new InputStreamReader(
		    		  				        response.getEntity().getContent()));
		    		  				        for (String s = reader.readLine(); s != null; s = reader.readLine()) {
		    		  				            builder.append(s);
		    		  				        }
//		    		  				        Log.v("index_detail",builder.toString());
		    		  				      JSONArray json=new JSONArray(builder.toString());
		    		  				    for(int i=0;i<json.length();i++){ 
		    		  				    	  Map<String, Object> map = new HashMap<String, Object>();
		    		  				    	  map.put("v_URL","http://10.0.3.2/test/picture/"+seller_name+"/"+json.getJSONObject(i).getString("URL"));
		    		  				    	  Log.v("URL", "http://10.0.3.2/test/picture/"+seller_name+json.getJSONObject(i).getString("URL"));
		    		  				    	  map.put("v_name",json.getJSONObject(i).getString("v_name"));
		    		  				    	  Log.v("seller_name", json.getJSONObject(i).getString("v_name"));
		    		  				    	  map.put("v_money", json.getJSONObject(i).getString("v_money"));
		    		  				    	  list2.add(map);}
//		    		  				  SimpleAdapter inadapter=new SimpleAdapter(getActivity(), list2, R.layout.index_detail_item, new String[]{"v_name","v_money"}, new int[]{R.id.seller_name,R.id.seller_money});
		    		  				Log.v("index_detail","simpleAdater");  
		    		  				  handler2.obtainMessage(0,list2).sendToTarget();}
		  				        catch(Exception e) {
		  							e.printStackTrace();
		  						
		  							}
		  		       
		  		}
		  	}).start(); 
//进入结算----------------------------------------------------------------------------------
		    		 Button jiesuan=(Button)view.findViewById(R.id.jiesuan);
		    		 Log.i("jisuan","即将进入结算");
		    		
		    		 jiesuan.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
				    Log.v("jisuan","按钮一点开始");
	
						int num=listview2.getChildCount();
//							Log.v("jisuan",num+"");
						Log.v("jiesuan", "马上进获取");
				List<Map<String,Object>> orderlist = new ArrayList<Map<String,Object>>();
				float zonghe=0;
					for (int i = 0; i < num; i++) {
						
					     LinearLayout layout = (LinearLayout)listview2.getChildAt(i);// 获得子item的layout	 
						 TextView et = (TextView) layout.findViewById(R.id.seller_money);// 从layout中获得控件,根据其id
						 //获取自定义控件中的子控件
						 final View layout2=inflater.inflate(R.layout.index_detail_item,(ViewGroup) layout.findViewById(R.id.seller_number));
						 final EditText numEditText=(EditText)layout2.findViewById(R.id.num);
						 final TextView nametTextView=(TextView)layout.findViewById(R.id.seller_name);
						 Log.i("jisuan", et.getText().toString()+"数量----》"+numEditText.getText().toString());
						 Map<String, Object> ordermap = new HashMap<String, Object>();
						 Float moneyFloat=Float.parseFloat(et.getText().toString());
						 Float numFloat=Float.parseFloat(numEditText.getText().toString());
						Float sum=moneyFloat*numFloat;
						zonghe=zonghe+sum;
						if (numFloat!=0) {
		
							ordermap.put("name", nametTextView.getText().toString());
							 ordermap.put("money","单价："+et.getText().toString());
							 ordermap.put("number","数量："+ numEditText.getText().toString());
							 ordermap.put("sum", "总价："+sum);
							 orderlist.add(ordermap);
						}
					
						 
					}
					Log.v("pay", "总和"+zonghe);
					Fragment jiesuanFragment=new jiesuan();
					Bundle bundle=new Bundle();
					//bundle.putInt("test", 1);
					bundle.putFloat("sum", zonghe);
					bundle.putParcelableArrayList("order", (ArrayList<? extends Parcelable>) orderlist);
					bundle.putString("seller_name", seller_name);
							jiesuanFragment.setArguments(bundle);
							 FragmentTransaction transaction = getFragmentManager().beginTransaction();
					  		 transaction.replace(R.id.content_frame, jiesuanFragment);
					         transaction.addToBackStack(null);
					         transaction.commit();
						}
		    			 
		    		 });
	//给收藏按钮添加处理事件
		    		 collect.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							if (collect.getText().equals("收藏")) {
								collect.setText("取消收藏");
								new Thread(new Runnable() {
									public void run() {
										SharedPreferences readpPreferences=getActivity().getSharedPreferences("data", 0);
										String username=readpPreferences.getString("name", null);
										Log.v("selllerFrament", "用户名："+username);	
									    HttpClient client = new DefaultHttpClient();
									  //  StringBuilder builder = new StringBuilder();
									    HttpPost httpPost = new HttpPost("http://10.0.3.2/test/add_col.php");
										ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
										pairs.add(new BasicNameValuePair("seller_name", seller_name));
										pairs.add(new BasicNameValuePair("username", username));
									    try {	
									    	UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(pairs, "utf8");
										httpPost.setEntity(p_entity);
											HttpResponse response = client.execute(httpPost); 
											} catch (Exception e) {
												e.printStackTrace();
											}
									}	}).start();
						
								
							}else {
								collect.setText("收藏");
								new Thread(new Runnable() {
									public void run() {
										SharedPreferences readpPreferences=getActivity().getSharedPreferences("data", 0);
										String username=readpPreferences.getString("name", null);
										Log.v("selllerFrament", "用户名："+username);	
									    HttpClient client = new DefaultHttpClient();
									    StringBuilder builder = new StringBuilder();
									    HttpPost httpPost = new HttpPost("http://10.0.3.2/test/del_col.php");
										ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
										pairs.add(new BasicNameValuePair("seller_name", seller_name));
										pairs.add(new BasicNameValuePair("username", username));
									    try {	
									    	UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(pairs, "utf8");
										httpPost.setEntity(p_entity);
											HttpResponse response = client.execute(httpPost); 
											} catch (Exception e) {
												e.printStackTrace();
											}
												}
								}).start();
							}
							
						}
					});
		    		  				    return view;
	}

}
