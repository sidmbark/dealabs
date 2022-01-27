package scrapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Test {

	public static void main(String[] args) throws IOException {
		final URL url = new URL("http://www.example.com");
		HttpURLConnection huc = (HttpURLConnection) url.openConnection();
		int responseCode = huc.getResponseCode();

		if (responseCode != 404) {
		System.out.println("GOOD");
		} else {
		System.out.println("BAD");
		}
	}

}
