package scrapper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScrapperEbay {
	
	public static ArrayList<String> getPageLinks(String pageRacine){
  	  
		  
	      ArrayList<String> tabLinks = new ArrayList<String>();

	    	  try {
	      Document docHome = Jsoup.connect(pageRacine).get();
	      Elements div = docHome.select("div.container");
	      Elements links = div.select("[href*=/itm/]");
	      for (Element element : links) {
		    	
	    	  String link = element.attr("abs:href");
	    	   if(!tabLinks.contains(link) && link.contains("?hash")) {
	          	  tabLinks.add(link);
	    	   }
	      }
	      
	      } catch (IOException e) {
	    	    e.printStackTrace();
	      }
	      
	      return tabLinks;
	    }
	
	public static void getDetails (String  pageRacine) throws IOException {
    		
    	String name = null;
    	String screenSize = null;
    	String cpu = null;
    	String gpu = null;
    	String ram = null;
    	String wheight = "Null";
    	String storage = null;
    	String os = null;
    	String price = null;
    	String source = "Ebay";
    	String newline = System.getProperty("line.separator");
    	String USER_AGENT = "Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Mobile/7B405";
    	ArrayList<String> listLinksParPage = getPageLinks(pageRacine);
    
    	for(int i = 0; i< listLinksParPage.size(); i++) {
    		
            Document doc = Jsoup.connect(listLinksParPage.get(i)).userAgent(USER_AGENT).timeout(12000).get();
            String url = doc.baseUri();
            Elements divPrix = doc.select("span#prcIsum_bidPrice");
            Elements divPrix1 = doc.select("span#prcIsum");
            
            if(divPrix.isEmpty()) {
            System.out.println(divPrix1.text());
            price = divPrix1.text();
            }else {
            System.out.println(divPrix.text());
            price = divPrix.text();
            }
		         
            Elements divDescription = doc.select("div.ux-labels-values__labels");
            System.out.println(url);
            for (Element element : divDescription) {
				
            	if(element.text().contains("Capacité du disque dur") || element.text().contains("Festplattenkapazität") || element.text().contains("Stockage")){
            		Element etat = element.nextElementSibling();
            		storage = etat.text();
            		System.out.println("disque dure :" + etat.text());
            	}
            	
            	if(element.text().contains("RAM")){
            		Element childRam = element.nextElementSibling();
            		ram = childRam.text();
            		System.out.println("Ram de pc :" + childRam.text());
            	}
            	
            	if(element.text().contains("écran") || element.text().contains("Screen")){
            		Element childRam = element.nextElementSibling();
            		screenSize = childRam.text();
            		System.out.println("ecran de pc" + childRam.text());
            	}
            	
            	if(element.text().contains("Processeur graphique")){
            		Element childRam = element.nextElementSibling();
            		gpu = childRam.text();
            		System.out.println("processeur de pc" + childRam.text());
            	}
            	
             	if(element.text().contains("Marque") || element.text().contains("Model") || element.text().contains("Modèle")){
            		Element childRam = element.nextElementSibling();
            		name = childRam.text();
            		System.out.println("modele de pc :" + childRam.text());
            	}
             	
            	if(element.text().contains("Système d'exploitation") || element.text().contains("Operating System")){
            		Element childRam = element.nextElementSibling();
            		os = childRam.text();
            		System.out.println("systeme d'exploitation" + childRam.text());
            	}
            	
             	if(element.text().contains("Vitesse de processeur")){
            		Element childRam = element.nextElementSibling();
            		cpu = childRam.text();
            		System.out.println("Vitesse de processeur" + childRam.text());
            	}
 
			
            }
            
            System.out.println("****************************************************");
            
            try {
          	  
                FileWriter myWriter = new FileWriter("dataEbay.txt",true);
        	    BufferedWriter out = new BufferedWriter(myWriter);
        	      
                out.write("source :"+source+newline+"name :"+name+newline+"url :"+url+newline+"Screen size :"+
                screenSize+newline+"Screen resolution :"+screenSize+newline+"Processeur :"+cpu
                +newline+"Carte graphique :"+gpu+newline+"ram :"+ram+newline+"stoackage :"+storage+newline
                +"systeme d'exploitation :"+os+newline+"poids :"+wheight+newline+"prix :"+price+newline+pageRacine+newline);
                out.write("###########################################################"+newline);
                out.close();
                //System.out.println("Successfully wrote to the file.");
              } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
              }
    	}
	}
	
	public static Boolean scrapperEbay(String page) throws IOException {
	final URL url = new URL(page);
	HttpURLConnection huc = (HttpURLConnection) url.openConnection();
	int responseCode = huc.getResponseCode();
	
	if (responseCode != 404) {
		return true;
		} else {
		return false;
		}
	

	}
	
	public static void main(String[] args) throws IOException {
	  	
        String pageRacine = "https://www.ebay.fr/b/Portables-et-netbooks/175672/bn_16550150?rt=nc&_pgn=";
    	
    	int i = 1;
    	while (scrapperEbay(pageRacine+i)) {
    		getDetails(pageRacine+i);
    		i++;
    	}
    	
 
}
  
}

