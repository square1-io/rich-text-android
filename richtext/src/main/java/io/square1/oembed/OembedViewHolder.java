package io.square1.oembed;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import io.square1.richtextlib.R;
import io.square1.richtextlib.spans.UrlBitmapDownloader;


/**
 * Created by roberto on 01/10/15.
 */
public class OembedViewHolder  {

    public interface ImageDownloader {

         void load(String url,
                   int placeHolder,
                   ImageView imageView);

    }

    private Oembed mOembed;

    private ImageView mThumbNail;
    private ImageView mActionButton;
    private ImageView mProviderLogo;
    private ImageDownloader mImageDownloader;


    public OembedViewHolder(View view, ImageDownloader downloader){
        mThumbNail = (ImageView)view.findViewById(R.id.thumbNail);
        mActionButton = (ImageView)view.findViewById(R.id.actionButton);
        mProviderLogo = (ImageView)view.findViewById(R.id.providerLogo);
        mImageDownloader = downloader;
    }



    public void setOembed(Context context, String data) {

        Oembed current = OembedCache.getInstance().loadOembed(context, data);


        if( Oembed.equals(mOembed, current) == false  ) {

            mOembed = current;

            mThumbNail.setImageResource(R.drawable.placeholder);
            mProviderLogo.setImageDrawable(null);
            mActionButton.setImageDrawable(null);

            if (mOembed != null) {
                //set data
                if(mImageDownloader != null){
                    mImageDownloader.load(mOembed.getThumbnailUrl(),
                            R.drawable.placeholder,
                            mThumbNail );
                }

                String provider = mOembed.getProviderName();
                if ("Vimeo".equalsIgnoreCase(provider)) {
                    mActionButton.setImageResource(R.drawable.vimeo_play);
                    mProviderLogo.setImageDrawable(null);
                } else if ("Vine".equalsIgnoreCase(provider)) {
                    mActionButton.setImageResource(R.drawable.vimeo_play);
                    mProviderLogo.setImageResource(R.drawable.vine_logo);
                }
                if ("Instagram".equalsIgnoreCase(provider)) {
                    mActionButton.setImageDrawable(null);
                    mProviderLogo.setImageResource(R.drawable.instagram_logo);
                }
            }
        }

    }
}
