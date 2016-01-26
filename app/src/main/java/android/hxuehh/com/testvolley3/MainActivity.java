package android.hxuehh.com.testvolley3;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String imageURL="http://ss.bdimg.com/static/superman/img/logo/bd_logo1_31bdc765.png";
    ImageView image;
    NetworkImageView network_image_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        image= (ImageView) findViewById(R.id.image);
        network_image_view= (NetworkImageView) findViewById(R.id.network_image_view);
        testLoadHtml();
//        testLoadjson();
//        testLoadImage();
//        testImageLoader();
//        testNetworkImageView();
    }


    private void testNetworkImageView() {
        RequestQueue mQueue = Volley.newRequestQueue(this);

        class BitmapCache implements ImageLoader.ImageCache {
            private LruCache<String, Bitmap> mCache;
            public BitmapCache() {
                int maxSize = 10 * 1024 * 1024;
                mCache = new LruCache<String, Bitmap>(maxSize) {
                    @Override
                    protected int sizeOf(String key, Bitmap bitmap) {
                        return bitmap.getRowBytes() * bitmap.getHeight();
                    }
                };
            }
            @Override
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
        }
        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
        network_image_view.setDefaultImageResId(R.mipmap.ic_launcher);
        network_image_view.setErrorImageResId(R.mipmap.ic_launcher);
        network_image_view.setImageUrl(imageURL,imageLoader);

    }


    private void testImageLoader() {
        RequestQueue mQueue = Volley.newRequestQueue(this);

         class BitmapCache implements ImageLoader.ImageCache {
            private LruCache<String, Bitmap> mCache;
            public BitmapCache() {
                int maxSize = 10 * 1024 * 1024;
                mCache = new LruCache<String, Bitmap>(maxSize) {
                    @Override
                    protected int sizeOf(String key, Bitmap bitmap) {
                        return bitmap.getRowBytes() * bitmap.getHeight();
                    }
                };
            }
            @Override
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }

        }

        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());

        ImageLoader.ImageListener listener = ImageLoader.getImageListener(image,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);

        imageLoader.get(imageURL,listener,100,100);
        imageLoader.get(imageURL, listener,100,100);
//        imageLoader.get("http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg", listener,200,200);

    }

    private void testLoadImage() {
        RequestQueue mQueue = Volley.newRequestQueue(this);

        ImageRequest imageRequest = new ImageRequest(
                imageURL,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        Log.e(this.toString()," Bitmap response OK");
                        image.setImageBitmap(response);
                    }
                }, 100, 100, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(this.toString(),"err");
//                image.setImageResource(R.drawable.default_image);
            }
        });

        mQueue.add(imageRequest);
    }

    private void testLoadjson() {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://m.weather.com.cn/data/101010100.html", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });

        mQueue.add(jsonObjectRequest);
    }



    private void testLoadHtml() {
        RequestQueue mQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                "http://www.baidu.com",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("params1", "value1");
                map.put("params2", "value2");
                return map;
            }
        };
        mQueue.add(stringRequest);
    }


}
