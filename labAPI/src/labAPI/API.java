package labAPI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class API {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					API window = new API();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public API() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setBounds(10, 178, 192, 72);
		frame.getContentPane().add(textArea);
		
		textField = new JTextField();
		textField.setBounds(10, 11, 192, 25);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblTitle = new JLabel("Title");
		lblTitle.setBounds(20, 58, 46, 14);
		frame.getContentPane().add(lblTitle);
		
		JLabel lblCompleted = new JLabel("Completed:");
		lblCompleted.setBounds(20, 90, 86, 14);
		frame.getContentPane().add(lblCompleted);
		
		textField_1 = new JTextField();
		textField_1.setBounds(116, 55, 246, 20);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(116, 87, 246, 20);
		frame.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		JButton btnNewButton = new JButton("search");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String strUrl = "https://jsonplaceholder.typicode.com/todos/";
				String getID = textField.getText();
				
				strUrl = strUrl+getID;
				JSONObject jsonObj = makeHttpRequest(strUrl, "GET");
				
				String title;
				try {
					title = jsonObj.getString("title");
					boolean completed = jsonObj.getBoolean("completed");
					String comp = Boolean.toString(completed);
					textField_1.setText(title);
					textField_2.setText(comp);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});	
		btnNewButton.setBounds(233, 12, 89, 23);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("List");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				String strUrl = "https://jsonplaceholder.typicode.com/todos/";
				JSONObject jsonObj = makeHttpRequest(strUrl, "GET");
				
				String allData = jsonObj.toString();
				textArea.setText(allData);
				
			}
		});
		btnNewButton_1.setBounds(233, 198, 89, 23);
		frame.getContentPane().add(btnNewButton_1);
		

	}
	
	public JSONObject makeHttpRequest(String url, String method) {
		InputStream is = null;
		String json = "";
		JSONObject jobj = null;
		
		try {
			if(method == "POST") {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				//httpPost.setEntity(new UrlEncodedFormEntity(params));
				
				HttpResponse httpResponse = httpClient.execute(httpPost);
				System.out.println("Response Code : "+ httpResponse.getStatusLine().getStatusCode());
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			}else if(method == "GET") {
				DefaultHttpClient httpClient = new DefaultHttpClient();
//				String paramString = URLEncodedUtils.format(params,"utf-8");
//				url += "?"+paramString;
				HttpGet httpGet = new HttpGet(url);
				
				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuffer sb = new StringBuffer();
			String line = null;
			
			while((line = br.readLine())!=null) {
				sb.append(line+"\n");
//				System.out.println(sb);
			}
			is.close();
			json = sb.toString();
			jobj = new JSONObject(json);
		}catch(JSONException e) {
			try {
				JSONArray jsonArray = new JSONArray(json);
				for(int i=0;i<jsonArray.length();i++) {
					jobj = jsonArray.getJSONObject(i);
					System.out.println(jobj);
				}
			}catch(JSONException el) {
				el.printStackTrace();
			}
		}catch(Exception ee) {
			ee.printStackTrace();
		}
		return jobj;
	}
}
