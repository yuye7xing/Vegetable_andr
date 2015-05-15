package MyAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;







import org.apache.http.message.BasicNameValuePair;

import com.example.android.navigationdrawerexample.R;
import com.example.android.navigationdrawerexample.collect;
import com.example.android.navigationdrawerexample.login;

import android.R.raw;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class collectAdapter extends BaseAdapter {
	protected static final int SUCCESS_GET_IMAGE = 0;
    private Context context;
    private List<Map<String, Object>> contacts;
    private File cache;
    private LayoutInflater mInflater;
    public collectAdapter(Context context, List<Map<String, Object>> contacts, File cache) {
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
            view = mInflater.inflate(com.example.android.navigationdrawerexample.R.layout.collect_item, null);
        }
        SharedPreferences readpPreferences=view.getContext().getSharedPreferences("data", 0);
		final String username=readpPreferences.getString("name", null);
       Log.v("123","123--"+username);
        ImageView iv_header = (ImageView) view.findViewById(com.example.android.navigationdrawerexample.R.id.col_image);
        final TextView tv_name = (TextView) view.findViewById(com.example.android.navigationdrawerexample.R.id.col_name);
        TextView tv_money = (TextView) view.findViewById(com.example.android.navigationdrawerexample.R.id.col_evaluate);
       // TextView num_order=(TextView)view.findViewById(com.example.android.navigationdrawerexample.R.id.col_num);
        final Button button=(Button)view.findViewById(com.example.android.navigationdrawerexample.R.id.col_button);
        Map<String, Object> contact = contacts.get(position);
        Log.v("path",(String)contact.get("image") );
        // �첽�ļ���ͼƬ (�̳߳� + Handler ) ---> AsyncTask
        asyncloadImage(iv_header, (String)contact.get("image"));
        tv_name.setText((CharSequence) contact.get("name"));
        tv_money.setText((CharSequence) contact.get("evaluate"));
        button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new Thread(new Runnable() {
					public void run() {
						//SharedPreferences readpPreferences=getActivity().getSharedPreferences("data", 0);
						//String username=readpPreferences.getString("name", null);
						//Log.v("selllerFrament", "�û�����"+username);	
					   String seller_name=tv_name.getText().toString();
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
				
button.setText("��ȡ���ղ�");
			}
		});
        return view;
	}
	 private void asyncloadImage(ImageView iv_header, String path) {
	       // ContactService service = new ContactService();
	        AsyncImageTask task = new AsyncImageTask(iv_header);
	        task.execute(path);
	    }
	 private final class AsyncImageTask extends AsyncTask<String, Integer,Bitmap> {
		 
	       //private ContactService service;
	        private ImageView iv_header;
	 
	        public AsyncImageTask(ImageView iv_header) {
	     
	            this.iv_header = iv_header;
	        }
	 
	        // ��̨���е����߳����߳�
	
	        protected Bitmap doInBackground(String... params) {
	        	HttpClient client = new DefaultHttpClient();
			       // publishProgress(30);
			      //  HttpGet hg = new HttpGet(params[0]);//��ȡcsdn��logo
			        HttpPost httpPost = new HttpPost(params[0]);
			        final Bitmap bm;
			        try {
			        	Log.v("image","ͼƬ��ȡ---"+params[0]);
			            HttpResponse hr = client.execute(httpPost);
			            Log.v("image", "HTTP");
			            bm = BitmapFactory.decodeStream(hr.getEntity().getContent());
			        } catch (Exception e) {
			        	Log.v("image","ͼƬ��ȡʧ��");
			            return null;
			        }
			        Log.v("image","ͼƬ��ȡ����");
			       
			        return bm;
	        }
	 
	        // ���������ui�߳���ִ��
	        protected void onPostExecute(Bitmap result) {
	            //super.onPostExecute(result); 
	            // ���ͼƬ�İ�
	            if (iv_header != null && result != null) {
	                iv_header.setImageBitmap(result);
	            }
	        }
	    }
}
