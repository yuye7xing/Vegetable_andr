package com.example.android.navigationdrawerexample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.example.android.navigationdrawerexample.R.id;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class jiesuan extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			Log.v("jiesuan", "进入结算");
			final View view = inflater.inflate(R.layout.jiesuan, container,false);
			final ArrayList orderArrayList=getArguments().getParcelableArrayList("order");
			final String seller_name=getArguments().getString("seller_name");
			final float sum=getArguments().getFloat("sum");
			TextView sumTextView=(TextView)view.findViewById(R.id.sumview);
			sumTextView.setText("总计："+sum+"元");
			//(List<? extends NameValuePair>) weborderlist=(List<? extends NameValuePair>)orderArrayList;
			ArrayList<Map<String,Object>> orderlist=(ArrayList<Map<String,Object>>)orderArrayList;
			final ListView orderListView=(ListView)view.findViewById(R.id.orderlist);
			SimpleAdapter adapter= new SimpleAdapter(getActivity(), orderArrayList, R.layout.jieuan_litm, new String[]{"name","money","number","sum"}, new int[]{R.id.orderlist_name,R.id.orderlist_money,R.id.orderlist_number,R.id.orderlist_sum});
			orderListView.setAdapter(adapter);
			final Handler handler=new Handler(){
				public void handleMessage(Message msg){
			         switch (msg.what){
			         case 0:
			        	 Toast.makeText(getActivity().getApplicationContext(),"支付成功", Toast.LENGTH_SHORT).show();
		        	  Log.v("pay", "开始建立AlertDialogAlertDialog");
			        	 new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("支付成功,继续选菜").setPositiveButton("确定",
			        			 new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							Fragment indexFragment=new indexFrament();
	 						FragmentManager fManager = getFragmentManager();
	 				        fManager.beginTransaction().replace(R.id.content_frame, indexFragment).commit();
						}
					}
						).setNegativeButton("取消", null).show();	     
			        	 break;
			          }
			}};
			Button payButton=(Button)view.findViewById(R.id.pay);
			
			payButton.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.v("pay",seller_name);
					SharedPreferences readpPreferences=getActivity().getSharedPreferences("data", 0);
					String username=readpPreferences.getString("name", null);
					final ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
					pairs.add(new BasicNameValuePair("seller_name", seller_name));
					pairs.add(new BasicNameValuePair("username", username));
					pairs.add(new BasicNameValuePair("count",""+ orderListView.getChildCount()));
					for (int i = 0; i < orderListView.getChildCount(); i++) {
					     LinearLayout layout = (LinearLayout)orderListView.getChildAt(i);// 获得子item的layout
					     TextView et = (TextView) layout.findViewById(R.id.orderlist_name);// 从layout中获得控件,根据其id
					    pairs.add(new BasicNameValuePair("V_name"+i, et.getText().toString()));
					    TextView etnum=(TextView)layout.findViewById(R.id.orderlist_number);
					    pairs.add(new BasicNameValuePair("V_num"+i, etnum.getText().toString().substring(etnum.getText().toString().length()-1)));
					   TextView sumView=(TextView)layout.findViewById(R.id.orderlist_sum);
					  // String sumString=sumView.getText().toString().substring();
					  // float sum=Float.parseFloat(sumString);
					}
					pairs.add(new BasicNameValuePair("sum", sum+""));
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					pairs.add(new BasicNameValuePair("time",df.format(new Date())));
					//进行网络操作
				new Thread(new Runnable() {
					public void run() {
						HttpClient client = new DefaultHttpClient();
						 StringBuilder builder = new StringBuilder();
					HttpPost httpPost = new HttpPost("http://10.0.3.2/test/order_add.php");
	//				ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
	//	  			pairs.add(new BasicNameValuePair("name1", seller_name));
				try { 
	 				UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(pairs, "UTF8");
			Log.v("pay", "结算完毕");
					httpPost.setEntity(p_entity);
	  				HttpResponse response = client.execute(httpPost);
	  				BufferedReader reader = new BufferedReader(new InputStreamReader(
	 				        response.getEntity().getContent()));
	  				        for (String s = reader.readLine(); s != null; s = reader.readLine()) {
	 				            builder.append(s);
					        }
	 				        Log.v("pay", builder.toString());
	 				      if (builder.toString().equalsIgnoreCase("sucesssucess")) {
	 				    	 
	 				    	 Log.v("pay", "toast");
	 				    	handler.obtainMessage(0).sendToTarget();
//	 				    	 Fragment indexFragment=new indexFrament();
//	 						FragmentManager fManager = getFragmentManager();
//	 				        fManager.beginTransaction().replace(R.id.content_frame, indexFragment).commit();
//							
						}
			}				        catch(Exception e) {
					e.printStackTrace();
						
						}
	     
	}
	}).start(); 
			}});
			return view;
		}

}
