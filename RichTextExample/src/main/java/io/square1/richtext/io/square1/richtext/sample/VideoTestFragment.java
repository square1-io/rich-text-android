
package io.square1.richtext.io.square1.richtext.sample;


import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.square1.richtext.R;
import io.square1.richtextlib.spans.RemoteBitmapSpan;
import io.square1.richtextlib.spans.UrlBitmapDownloader;
import io.square1.richtextlib.ui.RichContentView;
import io.square1.richtextlib.ui.video.RichVideoView;
import io.square1.richtextlib.v2.RichTextV2;
import io.square1.richtextlib.v2.content.RichDocument;


public class VideoTestFragment extends Fragment implements UrlBitmapDownloader {


    private RichVideoView mVideoView;
    private RichContentView mContentView;

    public VideoTestFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static VideoTestFragment newInstance() {
        VideoTestFragment fragment = new VideoTestFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       // View  contentView = inflater.inflate(R.layout.fragment_video_test, container, false);
       // mContentView =  (RichContentView)contentView.findViewById(R.id.content);
       // mContentView.setUrlBitmapDownloader(this);

        mVideoView = new RichVideoView(getActivity());

        return mVideoView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        String html = Utils.readFromfile(getActivity(),
                NavigationDrawerFragment.SAMPLES_FOLDER +"/single_video.html");

        if(mContentView != null) {
            RichDocument result = RichTextV2.fromHtml(getActivity(), html);
            mContentView.setText(result);
        }


         mVideoView.setRichVideoViewListener(new RichVideoView.RichVideoViewListener() {
             @Override
             public void onVideoReady(RichVideoView videoView) {


             }

             @Override
             public void onVideoSizeAvailable(RichVideoView videoView) {

                 Log.d("VIDEO", "size " +
                         videoView.getVideoWidth() +
                         " - " +
                         videoView.getVideoHeight());
             }
         });
         mVideoView.setData("http://www.w3schools.com/html/mov_bbb.mp4");
       // mVideoView.setLayoutParams(mContentView.generateDefaultLayoutParams(new Point(0,0),100,100));

    }

    @Override
    public void downloadImage(RemoteBitmapSpan urlBitmapSpan, Uri image) {
        Glide.with(getActivity())
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(new GlideTarget(getActivity(),urlBitmapSpan));
    }

}
