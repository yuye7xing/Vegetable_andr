package MyAdapter;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;




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

public class sellerAdapter extends BaseAdapter {
	protected static final int SUCCESS_GET_IMAGE = 0;
    private Context context;
    private List<Map<String, Object>> contacts;
    private File cache;
    private LayoutInflater mInflater;
    public sellerAdapter(Context context, List<Map<String, Object>> contacts, File cache) {
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
            view = mInflater.inflate(com.example.android.navigationdrawerexample.R.layout.index_detail_item, null);
        }
      // Log.v("TAG", "׼��������");
       Log.v("TAG", "׼��������"+position);
        ImageView iv_header = (ImageView) view.findViewById(com.example.android.navigationdrawerexample.R.id.seller_image);
        TextView tv_name = (TextView) view.findViewById(com.example.android.navigationdrawerexample.R.id.seller_name);
        TextView tv_money = (TextView) view.findViewById(com.example.android.navigationdrawerexample.R.id.seller_money);
        Map<String, Object> contact = contacts.get(position);
 
        // �첽�ļ���ͼƬ (�̳߳� + Handler ) ---> AsyncTask
        asyncloadImage(iv_header, (String)contact.get("v_URL"));
        //asyncloadImage(iv_header, "http://10.0.3.2/test/picture/���г�1��1.gif");
        Log.v("path",(String)contact.get("v_URL") );
      tv_name.setText((CharSequence) contact.get("v_name"));
        tv_money.setText((CharSequence) contact.get("v_money"));
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
