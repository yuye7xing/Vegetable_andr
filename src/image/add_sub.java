package image;
import com.example.android.navigationdrawerexample.R;

import android.content.Context;  
import android.util.AttributeSet;  
import android.view.LayoutInflater;  
import android.view.View;  
import android.widget.Button;  
import android.widget.EditText;  
import android.widget.LinearLayout;
import android.widget.TextView;
public class add_sub extends LinearLayout{
	private EditText mEditText;  
    private Button bAdd;  
    private Button bReduce; 
    private TextView sumTextView;
     Double sum=0.0; 
    //这里的构造一定是两个参数。  
    public add_sub(final Context ctxt, AttributeSet attrs) {  
        super(ctxt,attrs);  
    }  
  
  
    protected void onFinishInflate() {  
        super.onFinishInflate();  
          
        LayoutInflater.from(getContext()).inflate(R.layout.add_sub, this);  
        
        init_widget();  
        addListener();  
          
    }  
      
    public void init_widget(){  
          
          
        mEditText = (EditText)findViewById(R.id.num); 
       
        bAdd = (Button)findViewById(R.id.add);  
        bReduce = (Button)findViewById(R.id.sub);  
        mEditText.setText("0");  
    }  
      
    public void addListener(){  
        bAdd.setOnClickListener(new OnClickListener() {  
              
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
                  
                int num = Integer.valueOf(mEditText.getText().toString());  
                num++;  
                mEditText.setText(Integer.toString(num)); 
              
          
            }  
        });  
          
        bReduce.setOnClickListener(new OnClickListener() {  
              
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
                int num = Integer.valueOf(mEditText.getText().toString());  
                num--;  
                mEditText.setText(Integer.toString(num));
                
            }  
        });  
    }  
}
