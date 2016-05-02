package mon.singlebutton;

import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SingleActivity extends AppCompatActivity implements View.OnTouchListener {
    ImageView iw;
    ImageView shadow;
    boolean touched = false;
    TextView pointV;
    TextView totalPointsView;
    Button resetButton;
    int ballsLeft;
    Random r;
    Handler h = new Handler();
    RelativeLayout rl;
    long start = 0L;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        r = new Random();
        rl = (RelativeLayout) findViewById(R.id.space);
        pointV = (TextView) findViewById(R.id.point);
        totalPointsView = (TextView) findViewById(R.id.totalPointsView);
        iw = (ImageView) findViewById(R.id.imageView);
        shadow = (ImageView) findViewById(R.id.shadow);
        resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetStage();
            }
        });
        resetStage();
        //iw.setOnTouchListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void resetStage() {
        ballsLeft = 13;
        totalPointsView.setTextSize(40);
        rl.setOnTouchListener(this);
        pointV.setText("0");
        totalPointsView.setText("0");
    }


    @Override
    public boolean onTouch(View v, MotionEvent m) {
        if(m.getActionMasked()!=0&&m.getActionMasked()!=5)
            return true;
        int p = calculateTouchPoint(m, Arrays.asList(iw));
        long timePoint = 0;
        if(start != 0){
//            p = (int) (p-(start-System.nanoTime()));
            timePoint =(System.currentTimeMillis()-start);
            start = System.currentTimeMillis();
        }
        start = System.currentTimeMillis();
        long timePlusAccuracyPoint = (p - (timePoint * 3))>0?(p - (timePoint * 3)):0;
        pointV.setText(timePlusAccuracyPoint +"\n");
        if(--ballsLeft<11){
            long stagePoint = Integer.parseInt(totalPointsView.getText().toString());
            stagePoint += timePlusAccuracyPoint;
            totalPointsView.setText(stagePoint+"");
        }
        if(ballsLeft<1) {
            totalPointsView.setTextSize(100);
            rl.setOnTouchListener(null);
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rl.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            resetStage();
                            return false;
                        }
                    });
                }
            },1000);

        }
//        pointV.append("--"+timePoint);
        final float nextX = shadow.getX();
        final float nextY = shadow.getY();
//        h.postDelayed(new Runnable() {
//            @Override
//            public void run() {
        iw.setX(nextX);
        iw.setY(nextY);
//            }
//        },200);
        shadow.setX(r.nextInt(1100)+50);
        shadow.setY(r.nextInt(1000)+50);
        return false;
    }

    private int calculateTouchPoint(MotionEvent m, List<ImageView> imgL) {
        int p = 0;

//        System.out.println(m.getActionMasked()+"-"+m.getPointerCount());
        for (ImageView img :imgL){
            int temp = 0;
            for (int i = 0; i < m.getPointerCount(); i++) {
                if(m.getActionMasked() == MotionEvent.ACTION_DOWN || m.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN ) {
                    float imgCenterX = img.getX() + img.getWidth()  / 2;
                    float imgCenterY = img.getY() + img.getHeight()  / 2;
                    int x = Math.round((Math.abs(imgCenterX - m.getX(i)) + Math.abs(imgCenterY - m.getY(i)))*10);
                    if(x>temp)
                        temp = x;
                }
            }
            if(temp < 10000000)
                p += temp;
        }
            return 3000>p? (int) Math.floor(100000 / Math.floor(Math.sqrt(p))) :0;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Single Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://mon.singlebutton/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Single Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://mon.singlebutton/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
