
package io.square1.richtext.io.square1.richtext.sample;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.square1.richtext.R;
import io.square1.richtextlib.ui.video.RichVideoView;


public class VideoTestFragment extends Fragment {


    private RichVideoView mVideoView;

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

        View view =  inflater.inflate(R.layout.fragment_video_test, container, false);
        mVideoView = (RichVideoView)view.findViewById(R.id.video_view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        mVideoView.setData("http://www.w3schools.com/html/mov_bbb.mp4");

    }

}
