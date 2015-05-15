package MyAdapter;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;




import org.apache.http.message.BasicNameValuePair;

import com.example.android.navigationdrawerexample.convertStreamToString;

import android.R.raw;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class orderlist extends BaseAdapter {
	protected static final int SUCCESS_GET_IMAGE = 0;
    private Context context;
    private List<Map<String, Object>> contacts;
    private File cache;
    private LayoutInflater mInflater;
    public orderlist(Context context, List<Map<String, Object>> contacts, File cache) {
        this.context = context;
        this.contacts = contacts;
        this.cache = cache;
 
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contacts.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		 return contacts.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
        if (convertView != null) {
            view = convertView;
        } else {
            view = mInflater.inflate(com.example.android.navigationdrawerexample.R.layout.list_litm, null);
        }
        
      // Log.v("TAG", "准备适配器");
       //Log.v("TAG", "准备适配器"+position);
       // ImageView iv_header = (ImageView) view.findViewById(com.example.android.navigationdrawerexample.R.id.imageView1);
        TextView tv_name = (TextView) view.findViewById(com.example.android.navigationdrawerexample.R.id.textView1);
        TextView tv_money = (TextView) view.findViewById(com.example.android.navigationdrawerexample.R.id.order_money);
        TextView tv_time = (TextView) view.findViewById(com.example.android.navigationdrawerexample.R.id.order_time);
        TextView tv_TAG = (TextView) view.findViewById(com.example.android.navigationdrawerexample.R.id.TAG);
        Map<String, Object> contact = contacts.get(position);
       // Log.v("path",(String)contact.get("image") );
        // 异步的加载图片 (线程池 + Handler ) ---> AsyncTask
        //asyncloadImage(iv_header, (String)contact.get("image"));
        //asyncloadImage(iv_header, "http://10.0.3.2/test/picture/菜市场1号1.gif");
        tv_TAG.setText((String)contact.get("TAG"));
        tv_TAG.setVisibility(View.GONE);
       asyncloadImage(tv_name,(String)contact.get("name")); 
       Log.v("hand11", "0"+"----"+position+"name:"+(String)contact.get("TAG")); 
      //tv_name.setText((CharSequence) contact.get("name"));
        tv_money.setText("总计:"+"  "+(CharSequence) contact.get("money"));
        tv_time.setText((CharSequence) contact.get("time"));
        return view;
	}
	 private void asyncloadImage(TextView iv_header, String path) {
	       // ContactService service = new ContactService();
	        AsyncImageTask task = new AsyncImageTask(iv_header);
	        task.execute(path);
	    }
	 private final class AsyncImageTask extends AsyncTask<String, Integer,String> {
		 
	       //private ContactService service;
	        private TextView iv_header;
	 
	        public AsyncImageTask(TextView iv_header) {
	     
	            this.iv_header = iv_header;
	        }
	 
	        // 后台运行的子线程子线程
	
	        protected String doInBackground(String... params) {
	        	HttpClient client = new DefaultHttpClient();
	        	String returncollect;
			       // publishProgress(30);
			      //  HttpGet hg = new HttpGet(params[0]);//获取csdn的logo
			        HttpPost httpPost = new HttpPost("http://10.0.3.2/test/search_seller.php");
			        ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		  			pairs.add(new BasicNameValuePair("id", params[0]));
			        try {
			        	UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(pairs, "utf8");
						httpPost.setEntity(p_entity);
							HttpResponse response = client.execute(httpPost); 
							HttpEntity entity = response.getEntity();
					        InputStream content = entity.getContent();
						       convertStreamToString cStreamToString=new convertStreamToString();
						   returncollect = cStreamToString.convertStreamToString(content); 

			        } catch (Exception e) {
			        	Log.v("image","图片获取失败");
			            return null;
			        }
			        Log.v("image","图片获取结束");
			       
			        return returncollect;
	        }
	 
	        // 这个放在在ui线程中执行
	        protected void onPostExecute(String result) {
	            //super.onPostExecute(result); 
	            // 完成图片的绑定
	            if (iv_header != null && result != null) {
	                iv_header.setText(result);
	            }
	        }
	    }
}
