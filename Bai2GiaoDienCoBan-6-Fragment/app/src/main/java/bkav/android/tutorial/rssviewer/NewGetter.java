
package bkav.android.tutorial.rssviewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by quanglh on 29/08/2016.
 */
public class NewGetter {
    private static final String TAG = NewGetter.class.getSimpleName();
    private static final String MAXIM_URL = "https://www.maxim.com/.rss/full/";
    private static final String RSS_URL = MAXIM_URL;

    private static final String ITEM_TAG = "item";
    private static final String TITLE_TAG = "title";
    private static final String DESCRIPTION_TAG = "description";
    private static final String CONTENT_ENCODED_TAG = "content:encoded";
    private static final String LINK_TAG = "link";
    private static final String ENCLOSURE_TAG = "enclosure";
    private static final String PUB_DATE_TAG = "pubDate";
    

    private static final String URL_ATT = "url";

    private static final int SOCKET_TIMEOUT = 15000;

    private ArrayList<NewItem> mItems;
    private int mCurrentItemIndex;

    public NewGetter() {
        mItems = new ArrayList<NewItem>();
        mCurrentItemIndex = 0;

        try {
            URL url = new URL(RSS_URL);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            // set Timeout and method
            conn.setReadTimeout(SOCKET_TIMEOUT);
            conn.setConnectTimeout(SOCKET_TIMEOUT);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            conn.setSSLSocketFactory((SSLSocketFactory) SSLSocketFactory.getDefault());
            conn.connect();

            InputStream is = conn.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String inputLine;
            String response = "";
            while ((inputLine = in.readLine()) != null) {
                response += inputLine;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(response)));

            doc.getDocumentElement().normalize();

            NodeList allItems = doc.getElementsByTagName(ITEM_TAG);
            for (int i = 0; i < allItems.getLength(); i++) {
                Element xmlItem = (Element) allItems.item(i);
                NewItem imageItem = new NewItem();

                // Lấy ra thông tin của thẻ <title> đầu tiên trong thẻ <item>. Tương tự với
                // description, date.
                Node node = xmlItem.getElementsByTagName(TITLE_TAG).item(0);
                if (node != null) {
                    imageItem.title = node.getTextContent();
                }

                node = xmlItem.getElementsByTagName(DESCRIPTION_TAG).item(0);
                if (node != null) {
                    imageItem.description = node.getTextContent();
                }
                
                node = xmlItem.getElementsByTagName(CONTENT_ENCODED_TAG).item(0);
                if (node != null) {
                    imageItem.contentEncoded = node.getTextContent();
                }

                node = xmlItem.getElementsByTagName(LINK_TAG).item(0);
                if (node != null) {
                    imageItem.link = node.getTextContent();
                }

                node = xmlItem.getElementsByTagName(PUB_DATE_TAG).item(0);
                if (node != null) {
                    imageItem.date = node.getTextContent();
                }

                // Lấy ra thông tin của thẻ <enclosure> rồi đọc tiếp thuộc tính "url" của nó.
                Element enclosureItem = (Element) xmlItem.getElementsByTagName(ENCLOSURE_TAG).item(
                        0);
                if (enclosureItem != null) {
                    imageItem.imageUrl = enclosureItem.getAttributeNode(URL_ATT).getTextContent();
                }

                mItems.add(imageItem);
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, e.toString());
        } catch (SAXException e) {
            Log.e(TAG, e.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } catch (ParserConfigurationException e) {
            Log.e(TAG, e.toString());
        }
    }

    public NewItem getNextItem() {
        mCurrentItemIndex++;
        if (mCurrentItemIndex >= mItems.size()) {
            mCurrentItemIndex = mItems.size() - 1;
        }
        return mItems.get(mCurrentItemIndex);
    }

    public NewItem getPreviousItem() {
        mCurrentItemIndex--;
        if (mCurrentItemIndex < 0) {
            mCurrentItemIndex = 0;
        }
        return mItems.get(mCurrentItemIndex);
    }

    public int getCurrentItemIndex() {
        return mCurrentItemIndex;
    }
    
    public NewItem getCurrentItem() {
        return mItems.get(mCurrentItemIndex);
    }

    public boolean hasNext() {
        return mCurrentItemIndex < mItems.size() - 1;
    }

    public boolean hasPrevious() {
        return mCurrentItemIndex > 0;
    }
    
    public int getCount() {
        return mItems.size();
    }
    
    public NewItem getItemAt(int position) {
        return mItems.get(position);
    }

    public class NewItem {
        public String imageUrl;
        public String title;
        public String description;
        public String contentEncoded;
        public String date;
        public String link;

        public void loadData(final TextView titleView, final TextView descriptionView,
                final TextView contentEncodedView, final TextView dateView, final ImageView imageView) {
            new ImageLoader(imageUrl, imageView) {

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);
                    
                    titleView.setText(title);
                    descriptionView.setText(description);
                    contentEncodedView.setText(contentEncoded);
                    dateView.setText(date);
                }
            }.execute();
        }
    }
    
    public void setCurrentItemIndex(int position) {
        if (position < 0) position = 0;
        if (position >= getCount()) position = getCount() - 1;
        mCurrentItemIndex = position;
    }
    
    public void setCurrentItemByTitle(String title) {
        for (int i = 0; i < mItems.size(); i++) {
            NewItem item = mItems.get(i);
            if (title.equals(item.title)) {
                mCurrentItemIndex = i;
                return;
            }
        }
    }
}
