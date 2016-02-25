package com.supernet.api.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.testng.Assert;

import com.google.common.base.Preconditions;

@SuppressWarnings("unused")
public class HTTPUtil extends HttpEntityEnclosingRequestBase {

	// private static final String USER_AGENT = "Mozilla/5.0";
	private static HttpResponse response;
	private static HttpRequest request;

	public static HttpResponse getResponse() {
		return response;
	}

	public static void setResponse(HttpResponse response) {
		HTTPUtil.response = response;
	}

	/**
	 * writeResponseToFile writing file with the response we get.
	 * 
	 * @param absFilePath
	 *            sending the absolute file to the method.
	 */

	public static void writeResponseToFile(String absFilePath) {
		Preconditions.checkArgument(!absFilePath.equals(null), "String absolutePath must not be null");
		BufferedReader rd;
		try {
			InputStream responseValue = response.getEntity().getContent();
			rd = new BufferedReader(new InputStreamReader(responseValue));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			if (result.toString().equals("Bad Request")) {
				System.out.println("No json response is return by server");

			} else {
				System.out.println(result.toString());
				File file = new File(absFilePath);

				if (!file.exists()) {
					file.createNewFile();
				}

				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(result.toString());
				bw.flush();
				bw.close();
				System.out.println("Done");
			}
		} catch (UnsupportedOperationException | IOException e) {
			e.printStackTrace();
			Assert.fail("Caught Exception ...");
		}

	}

	/**
	 * 
	 * @param url
	 * @param headerParameters
	 * @return
	 */
	public static int sendSimplePost(String url, Map<String, String> headerParameters) {
		Preconditions.checkArgument(!url.equals(null), "String URL must not be null");
		Preconditions.checkArgument(headerParameters != null, "headerParameters shouldn't be passed as null");
		
		connectionManager();
		
		HttpClient client = HttpClientBuilder.create().build();
		request = new HttpPost(url);

		// add request header
		// request.addHeader("User-Agent", USER_AGENT);
		for (Map.Entry<String, String> parameter : headerParameters.entrySet()) {

			request.addHeader(parameter.getKey(), parameter.getValue());
		}
		try {
			response = client.execute((HttpPost) request);
		} catch (IOException e) {
			System.out.println("Exception occured in sendSimplePost() method: " + e.getMessage());
			System.out.println("===================================================");
			e.printStackTrace();
			return -1;
		}
		return response.getStatusLine().getStatusCode();
	}

	/**
	 * sendPost sends the post operation to an URL using different parameters
	 * 
	 * @param url
	 *            the rest URL which is getting used for posting the request
	 * @param parameters
	 *            set of parameters to that needs to be added as basic name pair
	 *            value
	 * @return returns the response code. It returns -1 in case of
	 *         failure/exception.
	 */

	public static int sendPost(String url, Map<String, String> parameters) {

		Preconditions.checkArgument(!url.equals(null), "String URL must not be null");
		Preconditions.checkArgument(parameters != null, "Parameters shouldn't be passed as null");

		connectionManager();
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);

		// Removing the url agent. Commented the below line for time being
		// post.setHeader("User-Agent", USER_AGENT);

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> parameter : parameters.entrySet()) {
			urlParameters.add(new BasicNameValuePair(parameter.getKey(), parameter.getValue()));
		}

		try {
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
		} catch (UnsupportedEncodingException e) {
			System.out.println("Exception occured in sendPost() method: " + e.getMessage());
			System.out.println("===================================================");
			e.printStackTrace();
			return -1;
		}
		try {
			response = client.execute(post);
		} catch (IOException e) {
			System.out.println("Exception occured in sendPost() method: " + e.getMessage());
			System.out.println("====================================================");
			e.printStackTrace();
			return -1;
		}
		return response.getStatusLine().getStatusCode();

	}

	/**
	 * sendGet sends the get operation to an URL using different parameters
	 * 
	 * @param url
	 *            URL to which the get operation has to be performed.
	 * @return response code. It returns -1 in case of failure/exception.
	 */
	public static int sendGet(String url) {
		Preconditions.checkArgument(!url.equals(null), "String URL must not be null");
		
		connectionManager();
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		try {
			response = client.execute(request);
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
		return response.getStatusLine().getStatusCode();
	}

	/**
	 * sendGet sends the get operation to an URL using different parameters
	 * 
	 * @param url
	 *            URL to which the get operation has to be performed.
	 * @param headerParameters
	 *            list of header parameters
	 * @return response code. It returns -1 in case of failure/exception.
	 */
	public static int sendGet(String url, Map<String, String> headerParameters) {
		Preconditions.checkArgument(!url.equals(null), "String URL must not be null");
		Preconditions.checkArgument(headerParameters != null, "headerParameters shouldn't be passed as null");

		connectionManager();

		HttpClient client = HttpClientBuilder.create().build();

		HttpGet request = new HttpGet(url);

		// add request header
		// request.addHeader("User-Agent", USER_AGENT);
		for (Map.Entry<String, String> parameter : headerParameters.entrySet()) {

			request.addHeader(parameter.getKey(), parameter.getValue());
			// System.out.println(parameter.getKey() +"-" +
			// parameter.getValue());
		}
		try {
			System.out.println(request.toString());
			response = client.execute(request);
		} catch (IOException e) {
			System.out.println("Exception occured in sendGet() method: " + e.getMessage());
			System.out.println("====================================================");
			e.printStackTrace();
			return -1;
		}
		return response.getStatusLine().getStatusCode();
	}

	/**
	 * sendPostWithFileAttachment send a post request with file as attachment
	 * 
	 * @param url
	 *            URL to which the get operation has to be performed.
	 * @param headerParameters
	 *            list of header parameters
	 * @param formParameters
	 *            list of form Parameters
	 * @param filesData
	 *            list of file field name and filePath
	 * 
	 * @return response code. It returns -1 in case of failure/exception.
	 */
	public static int sendPostWithFileAttachment(String url, Map<String, String> headerParameters,
			Map<String, String> formParameters, Map<String, String> filesData) {

		Preconditions.checkArgument(!url.equals(null), "String URL must not be null");
		Preconditions.checkArgument(headerParameters != null, "headerParameters shouldn't be passed as null");
		Preconditions.checkArgument(formParameters != null, "formParameters shouldn't be passed as null");

		connectionManager();

		HttpClient client = HttpClients.createDefault();
		HttpPost request = new HttpPost(url);

		// add request header
		for (Map.Entry<String, String> headerParameter : headerParameters.entrySet()) {
			request.addHeader(headerParameter.getKey(), headerParameter.getValue());

		}

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();

		// add upload file(s) in the form parameter
		for (Map.Entry<String, String> fileData : filesData.entrySet()) {

			if ((!(fileData.getValue()).equals(null)) && (!(fileData.getValue()).isEmpty())) {
				builder.addBinaryBody(fileData.getKey(), new File(fileData.getValue()));
			}
		}

		// add request form parameters
		for (Map.Entry<String, String> formParameter : formParameters.entrySet()) {
			builder.addTextBody(formParameter.getKey(), formParameter.getValue(), ContentType.MULTIPART_FORM_DATA);
		}

		HttpEntity multipart = builder.build();
		request.setEntity(multipart);

		try {
			response = client.execute(request);
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found ... ");

		} catch (IOException e) {
			System.out.println("Exception occured in sendPostWithFileAttachment() method: " + e.getMessage());
			System.out.println("====================================================");
			e.printStackTrace();
			return -1;
		}
		return response.getStatusLine().getStatusCode();
	}

	/**
	 * sendPostWithFileAttachment send a post request with file as attachment
	 * 
	 * @param url
	 *            URL to which the get operation has to be performed.
	 * @param headerParameters
	 *            list of header parameters
	 * @param filesData
	 *            list of file field name and filePath
	 * 
	 * @return response code. It returns -1 in case of failure/exception.
	 */
	public static int sendPostWithFileAttachment(String url, Map<String, String> headerParameters,
			Map<String, String> filesData) {

		Preconditions.checkArgument(!url.equals(null), "String URL must not be null");
		Preconditions.checkArgument(headerParameters != null, "headerParameters shouldn't be passed as null");

		connectionManager();

		HttpClient client = HttpClients.createDefault();
		HttpPost request = new HttpPost(url);

		// add request header
		for (Map.Entry<String, String> headerParameter : headerParameters.entrySet()) {
			request.addHeader(headerParameter.getKey(), headerParameter.getValue());

		}

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();

		// add upload file(s) in the form parameter
		for (Map.Entry<String, String> fileData : filesData.entrySet()) {

			if ((!(fileData.getValue()).equals(null)) && (!(fileData.getValue()).isEmpty())) {
				builder.addBinaryBody(fileData.getKey(), new File(fileData.getValue()));
			}
		}

		HttpEntity multipart = builder.build();
		request.setEntity(multipart);

		try {
			response = client.execute(request);
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found ... ");

		} catch (IOException e) {
			System.out.println("Exception occured in sendPostWithFileAttachment() method: " + e.getMessage());
			System.out.println("====================================================");
			e.printStackTrace();
			return -1;
		}
		return response.getStatusLine().getStatusCode();
	}

	/**
	 * sendPostWithPayload send a post request with raw data/payload as request
	 * parameter
	 * 
	 * @param url
	 * @param headerParameters
	 * @param payloads
	 * @return
	 */
	public static int sendPostWithPayload(String url, Map<String, String> headerParameters, Map<String, String> payloads) {

		Preconditions.checkArgument(!url.equals(null), "String URL must not be null");
		Preconditions.checkArgument(headerParameters != null, "headerParameters shouldn't be passed as null");
		Preconditions.checkArgument(payloads != null, "formParameters shouldn't be passed as null");

		connectionManager();

		HttpClient client = HttpClients.createDefault();
		HttpPost request = new HttpPost(url);

		// add request header
		for (Map.Entry<String, String> headerParameter : headerParameters.entrySet()) {
			request.addHeader(headerParameter.getKey(), headerParameter.getValue());

		}

		// add raw data
		for (Map.Entry<String, String> payload : payloads.entrySet()) {
			try {
				request.setEntity(new StringEntity(payload.getValue()));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			response = client.execute(request);
		} catch (IOException e) {
			System.out.println("Exception occured in sendPostWithPayload() method: " + e.getMessage());
			System.out.println("====================================================");
			e.printStackTrace();
			return -1;
		}
		return response.getStatusLine().getStatusCode();
	}

	/**
	 * 
	 * @param url
	 * @param headerParameters
	 * @param formParameters
	 * @param payloads
	 * @return
	 */
	public static int sendPostWithFormDataAndPayload(String url, Map<String, String> headerParameters,
			Map<String, String> formParameters, Map<String, String> payloads) {

		Preconditions.checkArgument(!url.equals(null), "String URL must not be null");
		Preconditions.checkArgument(headerParameters != null, "headerParameters shouldn't be passed as null");
		Preconditions.checkArgument(payloads != null, "formParameters shouldn't be passed as null");

		connectionManager();

		HttpClient client = HttpClients.createDefault();
		HttpPost request = new HttpPost(url);

		// add request header
		for (Map.Entry<String, String> headerParameter : headerParameters.entrySet()) {
			request.addHeader(headerParameter.getKey(), headerParameter.getValue());

		}

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();

		// add request form parameters
		for (Map.Entry<String, String> formParameter : formParameters.entrySet()) {
			builder.addTextBody(formParameter.getKey(), formParameter.getValue(), ContentType.MULTIPART_FORM_DATA);
		}

		// add raw data
		for (Map.Entry<String, String> payload : payloads.entrySet()) {
			try {
				request.setEntity(new StringEntity(payload.getValue()));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			response = client.execute(request);
		} catch (IOException e) {
			System.out.println("Exception occured in sendPostWithFormDataAndPayload() method: " + e.getMessage());
			System.out.println("====================================================");
			e.printStackTrace();
			return -1;
		}
		return response.getStatusLine().getStatusCode();
	}

	/**
	 * sendPostWithFormData send a post request with form data as request
	 * parameter
	 * 
	 * @param url
	 * @param headerParameters
	 * @param formData
	 * @return
	 */
	public static int sendPostWithFormData(String url, Map<String, String> headerParameters,
			Map<String, String> formParameters) {

		Preconditions.checkArgument(!url.equals(null), "String URL must not be null");
		Preconditions.checkArgument(headerParameters != null, "headerParameters shouldn't be passed as null");
		Preconditions.checkArgument(formParameters != null, "formParameters shouldn't be passed as null");

		connectionManager();

		HttpClient client = HttpClients.createDefault();
		HttpPost request = new HttpPost(url);

		// add request header
		for (Map.Entry<String, String> headerParameter : headerParameters.entrySet()) {
			request.addHeader(headerParameter.getKey(), headerParameter.getValue());

		}

		// add request form parameters
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		for (Map.Entry<String, String> formParameter : formParameters.entrySet()) {
			builder.addTextBody(formParameter.getKey(), formParameter.getValue(), ContentType.MULTIPART_FORM_DATA);
		}

		HttpEntity multipart = builder.build();
		request.setEntity(multipart);

		try {
			response = client.execute(request);
		} catch (IOException e) {
			System.out.println("Exception occured in sendPostWithFormData() method: " + e.getMessage());
			System.out.println("====================================================");
			e.printStackTrace();
			return -1;
		}
		return response.getStatusLine().getStatusCode();
	}

	/**
	 * 
	 * @param url
	 * @param headerParameters
	 * @param formParameters
	 * @return
	 */
	public static int sendPut(String url, Map<String, String> headerParameters, Map<String, String> formParameters) {

		Preconditions.checkArgument(!url.equals(null), "String URL must not be null");
		Preconditions.checkArgument(headerParameters != null, "headerParameters shouldn't be passed as null");
		Preconditions.checkArgument(formParameters != null, "formParameters shouldn't be passed as null");

		connectionManager();
		
		HttpClient client = HttpClients.createDefault();
		HttpPut request = new HttpPut(url);

		// add request header
		for (Map.Entry<String, String> headerParameter : headerParameters.entrySet()) {
			request.addHeader(headerParameter.getKey(), headerParameter.getValue());
		}

		// add request form parameters
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		for (Map.Entry<String, String> formParameter : formParameters.entrySet()) {
			builder.addTextBody(formParameter.getKey(), formParameter.getValue(), ContentType.MULTIPART_FORM_DATA);
		}

		HttpEntity multipart = builder.build();
		request.setEntity(multipart);

		try {
			response = client.execute(request);
		} catch (IOException e) {
			System.out.println("Exception occured in sendPut() method: " + e.getMessage());
			System.out.println("====================================================");
			e.printStackTrace();
			return -1;
		}
		return response.getStatusLine().getStatusCode();
	}

	/**
	 * 
	 * @param url
	 * @param headerParameters
	 * @param rawData
	 * @return
	 */
	public static int sendPut(String url, Map<String, String> headerParameters, String rawData) {

		Preconditions.checkArgument(!url.equals(null), "String URL must not be null");
		Preconditions.checkArgument(headerParameters != null, "headerParameters shouldn't be passed as null");

		connectionManager();

		HttpClient client = HttpClients.createDefault();
		HttpPut request = new HttpPut(url);

		// add request header
		for (Map.Entry<String, String> headerParameter : headerParameters.entrySet()) {
			request.addHeader(headerParameter.getKey(), headerParameter.getValue());
		}

		// add raw data

		try {
			request.setEntity(new StringEntity(rawData));

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			response = client.execute(request);
		} catch (IOException e) {
			System.out.println("Exception occured in sendPut() method: " + e.getMessage());
			System.out.println("====================================================");
			e.printStackTrace();
			return -1;
		}
		return response.getStatusLine().getStatusCode();
	}

	/**
	 * 
	 * @param url
	 * @param headerParameters
	 * @return response code
	 */
	public static int sendPut(String url, Map<String, String> headerParameters) {

		Preconditions.checkArgument(!url.equals(null), "String URL must not be null");
		Preconditions.checkArgument(headerParameters != null, "headerParameters shouldn't be passed as null");

		connectionManager();
		
		HttpClient client = HttpClients.createDefault();
		HttpPut request = new HttpPut(url);

		// add request header
		for (Map.Entry<String, String> headerParameter : headerParameters.entrySet()) {
			request.addHeader(headerParameter.getKey(), headerParameter.getValue());
		}

		try {
			response = client.execute(request);
		} catch (IOException e) {
			System.out.println("Exception occured in sendPut() method: " + e.getMessage());
			System.out.println("====================================================");
			e.printStackTrace();
			return -1;
		}
		return response.getStatusLine().getStatusCode();
	}

	/**
	 * 
	 * @param url
	 * @param headerParameters
	 * @return
	 */
	public static int sendDelete(String url, Map<String, String> headerParameters) {

		Preconditions.checkArgument(!url.equals(null), "String URL must not be null");
		Preconditions.checkArgument(headerParameters != null, "headerParameters shouldn't be passed as null");

		connectionManager();
		
		HttpClient client = HttpClients.createDefault();
		HttpDelete request = new HttpDelete(url);

		// add request header
		for (Map.Entry<String, String> headerParameter : headerParameters.entrySet()) {
			request.addHeader(headerParameter.getKey(), headerParameter.getValue());

		}

		try {
			response = client.execute(request);
		} catch (IOException e) {
			System.out.println("Exception occured in sendDelete() method: " + e.getMessage());
			System.out.println("====================================================");
			e.printStackTrace();
			return -1;
		}
		return response.getStatusLine().getStatusCode();
	}

	/**
	 * 
	 * @param url
	 * @param headerParameters
	 * @param rawData
	 * @return
	 */
	public static int sendDelete(String url, Map<String, String> headerParameters, String rawData) {

		Preconditions.checkArgument(!url.equals(null), "String URL must not be null");
		Preconditions.checkArgument(headerParameters != null, "headerParameters shouldn't be passed as null");

		connectionManager();
		
		try {
			HttpClient client = HttpClients.createDefault();
			HttpEntity entity = new StringEntity(rawData);
			HttpDeleteWithBody request = new HttpDeleteWithBody(url);

			// add request header
			for (Map.Entry<String, String> headerParameter : headerParameters.entrySet()) {
				request.addHeader(headerParameter.getKey(), headerParameter.getValue());

			}

			request.addHeader("Content-Type", "application/json");

			if (!rawData.isEmpty()) {
				request.setEntity(entity);
			}

			response = client.execute(request);

			// System.out.println("R:" + response.getStatusLine());

		} catch (UnsupportedOperationException e) {
			System.out.println("Exception occured in sendDeleteWithPayload() method: " + e.getMessage());
			System.out.println("====================================================");
			e.printStackTrace();
			return -1;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response.getStatusLine().getStatusCode();
	}

	@SuppressWarnings("resource")
	public static void connectionManager() {

		HttpClientContext context = HttpClientContext.create();
		
		HttpClientConnectionManager connMrg = new BasicHttpClientConnectionManager();
		HttpRoute route = new HttpRoute(new HttpHost("localhost", 7778));
		// Request new connection. This can be a long process
		ConnectionRequest connRequest = connMrg.requestConnection(route, null);

		try {
			// Wait for connection up to 20 sec
			HttpClientConnection conn = connRequest.get(20, TimeUnit.SECONDS);
			try {
				// If not open
				if (!conn.isOpen()) {
					// establish connection based on its route info
					connMrg.connect(conn, route, 1000, context);
					// and mark it as route complete
					connMrg.routeComplete(conn, route, context);
				}
				// Do useful things with the connection.
			} finally {
				connMrg.releaseConnection(conn, null, 1, TimeUnit.MINUTES);
			}
		} catch (InterruptedException | ExecutionException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return null;
	}

}
